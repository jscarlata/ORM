package at.tgm.jscarlata;

import at.tgm.jscarlata.warehouse.WarehouseData;
import at.tgm.jscarlata.warehouse.WarehouseSimulation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import at.tgm.jscarlata.warehouse.Product;

import java.util.Optional;

@Component
@RestController
@AllArgsConstructor
public class MainController {

    private final WarehouseRepository warehouseRepository;

    @PostMapping(path="/add")
    public @ResponseBody String addNewUser (@RequestParam int id) {
        WarehouseData wd = new WarehouseSimulation().getData(id);
        warehouseRepository.save(wd);
        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<WarehouseData> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    @GetMapping(path="/warehouse/{id}")
    public ResponseEntity<WarehouseData> getWarehouseById(@PathVariable int id) {
        Optional<WarehouseData> warehouseData = Optional.ofNullable(warehouseRepository.findById(id));
        return warehouseData.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(path="/warehouse/{warehouseId}/product/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable int warehouseId, @PathVariable int productId) {
        Optional<WarehouseData> warehouseData = Optional.ofNullable(warehouseRepository.findById(warehouseId));
        if (warehouseData.isPresent()) {
            for (Product product : warehouseData.get().getProductData()) {
                if (product.getId() == productId) {
                    return ResponseEntity.ok(product);
                }
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping(path="/warehouse/{id}")
    public ResponseEntity<WarehouseData> updateWarehouse(@PathVariable int id, @RequestBody WarehouseData newWarehouseData) {
        Optional<WarehouseData> optionalWarehouseData = Optional.ofNullable(warehouseRepository.findById(id));
        return optionalWarehouseData.map(warehouseData -> {
            warehouseData.setName(newWarehouseData.getName());
            warehouseData.setTimestamp(newWarehouseData.getTimestamp());
            warehouseData.setStreet(newWarehouseData.getStreet());
            warehouseData.setCity(newWarehouseData.getCity());
            warehouseData.setCountry(newWarehouseData.getCountry());
            warehouseData.setPlz(newWarehouseData.getPlz());
            warehouseData.setProductData(newWarehouseData.getProductData());
            warehouseRepository.save(warehouseData);
            return ResponseEntity.ok(warehouseData);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

}