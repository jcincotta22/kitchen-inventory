package kitchen.controllers;

import kitchen.errors.ResponseError;
import kitchen.models.Product;
import kitchen.resources.ProductResource;
import kitchen.services.ProductRequestBody;
import kitchen.services.ProductService;
import kitchen.utils.JsonUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.elasticsearch.ElasticsearchException;

import java.io.IOException;

import static kitchen.utils.JsonUtil.json;
import static spark.Spark.get;
import static spark.Spark.post;

public class ProductController {

    final private static Logger logger = LogManager.getLogger(ProductController.class.getName());

    public ProductController(final ProductService productService) {
        get("/product/:name", (req, res) -> {
            String value = req.params(":name");
            logger.debug("Query params: " + value);
            return productService.find(value);
        }, json());

        get("/product/get/:id", (req, res) -> {
            String value = req.params(":id");
            logger.debug("Query params: " + value);
            return productService.get(Long.parseLong(value));
        }, json());

        post("/product", (req, res) -> {
            try {
                String body = req.body();

                ProductRequestBody productRequestBody = JsonUtil.jsonToObject(body, ProductRequestBody.class);
                logger.debug("creating new product");

                ProductResource product = ProductResource.from(productRequestBody);

                return productService.create(product);

            } catch (IOException e) {
                throw new ResponseError(e);
            }
        }, json());
    }

}
