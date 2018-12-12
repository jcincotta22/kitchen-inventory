package kitchen.services;

import kitchen.models.Product;

public class ProductRequestBody extends Product {
    ProductRequestBody(String long_name, String manufacturer){
        super(long_name, manufacturer);
    }
}
