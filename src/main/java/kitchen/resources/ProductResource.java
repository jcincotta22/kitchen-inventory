package kitchen.resources;

import kitchen.models.Product;
import kitchen.services.ProductRequestBody;

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

    public static ProductResource from(ProductRequestBody product) {
        ProductResource productResource = new ProductResource();
        productResource.long_name = product.getLong_name();
        productResource.manufacturer = product.getManufacturer();

        return productResource;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public long getNDB_Number() {
        return NDB_Number;
    }

    public String getLong_name() {
        return long_name;
    }

    public void setNDB_Number(long NDB_Number) {
        this.NDB_Number = NDB_Number;
    }
}
