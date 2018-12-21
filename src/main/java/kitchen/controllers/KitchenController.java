package kitchen.controllers;

import kitchen.errors.ResponseError;
import kitchen.services.KitchenInventoryRequestBody;
import kitchen.services.KitchenInventoryService;
import kitchen.utils.JsonUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import static kitchen.utils.JsonUtil.json;
import static spark.Spark.get;
import static spark.Spark.post;

public class KitchenController {
    final private static Logger logger = LogManager.getLogger(KitchenController.class.getName());

    public KitchenController(final KitchenInventoryService kitchenInventoryService) {
        get("/kitchen", (req, res) -> kitchenInventoryService.findAll(), json());
        post("/inventory", (req, res) -> {
            try {
                String body = req.body();

                KitchenInventoryRequestBody kitchenInventoryRequestBody = JsonUtil.jsonToObject(body, KitchenInventoryRequestBody.class);
                logger.debug("create user with name " + kitchenInventoryRequestBody.getName());

                return kitchenInventoryService.create(kitchenInventoryRequestBody.getName(), kitchenInventoryRequestBody.getSpiceList());

            } catch (Exception e) {
                throw new ResponseError(e);
            }
        }, json());
    }


}

