package kitchen.controllers;

import kitchen.elasticsearch.ElasticSearchClient;
import kitchen.errors.ResponseError;
import kitchen.models.Product;
import kitchen.resources.ProductResource;
import kitchen.resources.SearchResource;
import kitchen.services.ProductRequestBody;
import kitchen.services.ProductService;
import kitchen.utils.JsonUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.elasticsearch.ElasticsearchException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

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

        post("/product/search", (req, res) -> {
            System.out.println("search endpoint");
            try {
                String body = req.body();
                logger.debug("creating new product");

                SearchResource searchResource = JsonUtil.jsonToObject(body, SearchResource.class);

                return productService.search(searchResource);

            } catch (IOException e) {
                throw new ResponseError(e);
            }
        }, json());

        post("/products/es", (req, res) -> {
            try {

                List<Product> productList = productService.findAll();

                ForkJoinPool forkJoinPool = new ForkJoinPool(12);

                forkJoinPool.submit(() -> {
                    productList.parallelStream().forEach(product -> {
                        ElasticSearchClient.insertIndex(product, Long.toString(product.getNDB_Number()));
                    });
                });

                logger.debug("Syncing elasticsearch");


                return "Syncing elasticsearch";

            } catch (ElasticsearchException e) {
                throw new ResponseError(e);
            }
        }, json());
    }

}
