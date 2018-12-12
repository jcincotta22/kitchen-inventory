package kitchen.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import kitchen.controllers.ProductController;
import kitchen.errors.ResponseError;
import kitchen.elasticsearch.ElasticSearchClient;
import kitchen.models.Product;
import kitchen.mongo.KitchenInventoryMongoClient;
import kitchen.resources.ProductResource;
import kitchen.utils.JsonUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.elasticsearch.ElasticsearchException;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.regex.Pattern;

import static kitchen.services.KitchenInventoryService.updateCounter;

public class ProductService {
    final private static Logger logger = LogManager.getLogger(ProductController.class.getName());

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
            throw new ResponseError(e);
        }

    }

    public ProductResource create(ProductResource productResource) throws UnknownHostException, ElasticsearchException {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        MongoCollection<Document> collection = KitchenInventoryMongoClient.getCollection("products", uuid1);
        MongoCollection<Document> counter = KitchenInventoryMongoClient.getCollection("counters", uuid2);
        Document productSequence = KitchenInventoryMongoClient.findOne("_id", "products", counter);

        long id  = productSequence.getLong("sequence_value");
        productResource.setNDB_Number(id);

        collection.insertOne(KitchenInventoryMongoClient.toDocument(productResource));

        logger.debug("Created product successfully");

        updateCounter(id + 1, counter, "products");
        KitchenInventoryMongoClient.closeMongoConnection(uuid1);
        KitchenInventoryMongoClient.closeMongoConnection(uuid2);

        ElasticSearchClient.insertIndex(productResource, Long.toString(productResource.getNDB_Number()));

        return productResource;
    }
}
