package kitchen.resources;

import kitchen.models.Product;

public class ProductResource {
    private long NDB_Number;
    private String long_name;
    private String manufacturer;

    private ProductResource() {

    }

    public static ProductResource from(Product product) {
        ProductResource productResource = new ProductResource();
        productResource.NDB_Number = product.getNDB_Number();
        productResource.long_name = product.getLong_name();
        productResource.manufacturer = product.getManufacturer();

        return productResource;
    }
}
