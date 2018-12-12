package kitchen.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import kitchen.controllers.ProductController;
import kitchen.errors.ResponseError;
import kitchen.models.Product;
import kitchen.mongo.KitchenInventoryMongoClient;
import kitchen.resources.ProductResource;
import kitchen.utils.JsonUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.bson.Document;

import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

public class ProductService {
    final private static Logger logger = LogManager.getLogger(ProductController.class.getName());
    private static Queue<UUID> getQueue = new LinkedList<>();

    public ProductService() {

    }

    public List<ProductResource> find(String searchString) throws UnknownHostException {
        logger.debug("Searching with search string: " + searchString);

        UUID uuid = UUID.randomUUID();
        MongoCollection productCollection = KitchenInventoryMongoClient.getCollection("products", uuid);

        Document regexQuery = new Document();
        regexQuery.append("$regex", Pattern.compile(searchString, Pattern.CASE_INSENSITIVE));
        Document criteria = new Document();
        criteria.append("long_name", regexQuery);

        List<ProductResource> productList = new ArrayList<>();

        try (MongoCursor<Document> cursor = productCollection.find(criteria).iterator()) {
            while (cursor.hasNext()) {
                Product product = JsonUtil.jsonToObject(JsonUtil.toJson(cursor.next()), Product.class);

                productList.add(ProductResource.from(product));
            }
        }
        logger.debug("Returning search results");
        KitchenInventoryMongoClient.closeMongoConnection(uuid);
        return productList;

    }

    public ProductResource get(long id) throws UnknownHostException, ResponseError {
        UUID uuid = UUID.randomUUID();

        try {
            logger.debug("Getting product with id: " + id);

            MongoCollection productCollection = KitchenInventoryMongoClient.getCollection("products", uuid);

            Document query = new Document();
            query.append("NDB_Number", id);
            ProductResource product = JsonUtil.jsonToObject(JsonUtil.toJson(productCollection.find(query).first()), ProductResource.class);

            logger.debug("Returning result");


            KitchenInventoryMongoClient.closeMongoConnection(uuid);

            if(product == null) {
                throw new ResponseError("No products available with that id");
            }
            return product;

        } catch (Exception e) {
//            KitchenInventoryMongoClient.closeMongoConnection(uuid);
            throw new ResponseError(e);
        }


    }
}
