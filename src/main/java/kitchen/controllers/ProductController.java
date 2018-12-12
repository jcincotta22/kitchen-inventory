package kitchen.controllers;

import kitchen.errors.ResponseError;
import kitchen.services.ProductService;
import kitchen.utils.JsonUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import static kitchen.utils.JsonUtil.json;
import static spark.Spark.get;

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
    }

}
