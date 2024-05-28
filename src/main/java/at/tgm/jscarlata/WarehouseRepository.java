package at.tgm.jscarlata;

import at.tgm.jscarlata.warehouse.WarehouseData;
import org.springframework.data.repository.CrudRepository;

public interface WarehouseRepository extends CrudRepository<WarehouseData, Integer> {
    WarehouseData findById(int id);
}