package at.tgm.jscarlata;

import at.tgm.jscarlata.warehouse.WarehouseData;
import at.tgm.jscarlata.warehouse.WarehouseSimulation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

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
}