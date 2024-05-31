# ORM

This project demonstrates the use of Object-Relational Mapping (ORM) in Java to interact with a warehouse database. ORM allows us to map Java objects to database tables and perform CRUD operations without writing SQL queries. Here, the ORM framework manages the database interaction for us.

## Add User to Warehouse Database

```{java}
@PostMapping(path="/add")
public @ResponseBody String addNewUser (@RequestParam int id) {
    WarehouseData wd = new WarehouseSimulation().getData(id);
    warehouseRepository.save(wd);
    return "Saved";
}
```

This endpoint adds a new warehouse user to the database. The @PostMapping annotation maps HTTP POST requests to this method. 
The method retrieves warehouse data using the provided id and saves it to the database using the warehouseRepository. 
The @ResponseBody annotation indicates that the returned String is the response body.

## Return all Warehouses

```{java}
@GetMapping(path="/all")
public @ResponseBody Iterable<WarehouseData> getAllWarehouses() {
    return warehouseRepository.findAll();
}
```

This endpoint returns all warehouse records from the database.
The @GetMapping annotation maps HTTP GET requests to this method.
The findAll() method of warehouseRepository fetches all records, and the @ResponseBody annotation ensures the returned data is in the response body.

## Return Warehouse by ID

```{java}
@GetMapping(path="/warehouse/{id}")
public ResponseEntity<WarehouseData> getWarehouseById(@PathVariable int id) {
    Optional<WarehouseData> warehouseData = Optional.ofNullable(warehouseRepository.findById(id));
    return warehouseData.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
}
```

This endpoint returns a warehouse record by its ID. The @GetMapping annotation maps HTTP GET requests with a variable path to this method. 
The @PathVariable annotation binds the method parameter to the path variable. The findById() method fetches the record by ID. 
If found, it returns the data with an HTTP 200 status; otherwise, it returns a 404 status.

## Return Product by Product-ID and Warehouse-ID

```{java}
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
```

This endpoint returns a product by its ID within a specific warehouse. The @GetMapping annotation maps HTTP GET requests to this method, with two path variables. 
It retrieves the warehouse by warehouseId and iterates through its products to find the one with productId. 
If found, it returns the product with an HTTP 200 status; otherwise, it returns a 404 status.

## Update a Warehouse

```{java}
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
```

This endpoint updates a warehouse record by its ID. The @PutMapping annotation maps HTTP PUT requests to this method. 
The @RequestBody annotation binds the request body to the method parameter. 
The method retrieves the warehouse by ID, updates its fields with the new data, and saves the updated record. 
If the warehouse is found, it returns the updated data with an HTTP 200 status; otherwise, it returns a 404 status.

## application.properties

I just need to update my data source config like that:
```
# spring.datasource.url=jdbc:mysql://localhost:3307/example
spring.datasource.url=jdbc:postgresql://localhost:5432/example
```

The same thing for the username:
```
# spring.datasource.username=root
spring.datasource.username=postgres
```

And the driver:
```
# spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.driver-class-name=org.postgresql.Driver
```

As well as the hibernate properties:
```
# spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```