package kitchen;

import static kitchen.utils.JsonUtil.toJson;
import static spark.Spark.*;


import kitchen.controllers.KitchenController;
import kitchen.controllers.ProductController;
import kitchen.errors.ResponseError;
import kitchen.services.KitchenInventoryService;
import kitchen.services.ProductService;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

import java.nio.channels.ClosedChannelException;


public class Bootstrap {
    final private static Logger logger = LogManager.getLogger(Bootstrap.class.getName());

    public static void main(String[] args) {
        port(8080);

        before((q, a) -> logger.debug("Received api call"));


        new KitchenController(new KitchenInventoryService());
        new ProductController(new ProductService());

        get("/hello", (req, res) -> "Hello World");

        after((req, res) -> {
            res.type("application/json");
        });

        exception(IllegalArgumentException.class, (e, req, res) -> {
            res.status(400);
            logger.error(new ResponseError(e).getMessage(), e);
            res.body(toJson(new ResponseError(e).getMessage()));
        });

        exception(ResponseError.class, (e, req, res) -> {
            res.status(400);
            logger.error(e.getMessage(), e);
            res.body(toJson(e.getMessage()));
        });

        notFound((req, res) -> {
            res.status(404);
            res.type("application/json");
            return "{\"message\":\"No such route\"}";
        });

        exception(Exception.class, (e, req, res) -> {
            res.status(500);
            logger.error(new ResponseError(e).getMessage(), e);
            res.body(toJson(new ResponseError(e).getMessage()));
        });

        exception(ClosedChannelException.class, (e, req, res) -> {
            res.status(500);
            logger.error(new ResponseError(e).getMessage(), e);
            res.body(toJson(new ResponseError(e).getMessage()));
        });


    }
}
