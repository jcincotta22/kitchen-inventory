package kitchen.mongo;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import kitchen.models.KitchenInventory;
import kitchen.models.Product;
import kitchen.models.Spice;
import kitchen.resources.ProductResource;
import kitchen.services.ProductRequestBody;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.bson.Document;

import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import static com.mongodb.client.model.Filters.eq;

public class KitchenInventoryMongoClient {
    private static final String dbName = "kitchen_inventory";
    private static Map<UUID, MongoClient> connectionMap = new HashMap<>();
    final private static Logger logger = LogManager.getLogger(KitchenInventoryMongoClient.class.getName());

    private static void connect(UUID uuid) throws UnknownHostException {
        MongoClient conn = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        connectionMap.put(uuid, conn);
        logger.warn("connecting to mongo");
    }

    public static Document toDocument(KitchenInventory kitchenInventory) {
        List<Spice> spiceList = kitchenInventory.getSpiceList();
        List<Document> documentList = new ArrayList<>();
        for( Spice spice : spiceList) {
            documentList.add(new Document("name", spice.getName()));
        }
        return new Document("_id", kitchenInventory.getId())
                .append("name", kitchenInventory.getName())
                .append("spice_list", documentList);
    }

    public static void closeMongoConnection(UUID uuid) {
        if(connectionMap.containsKey(uuid)) {
            connectionMap.get(uuid).close();
            connectionMap.remove(uuid);
        }
    }

    public static Document toDocument(ProductResource product) {
        return new Document()
                .append("long_name", product.getLong_name())
                .append("NDB_Number", product.getNDB_Number())
                .append("manufacturer", product.getManufacturer());
    }

    private static MongoDatabase getDB(UUID uuid) throws UnknownHostException {
        KitchenInventoryMongoClient.connect(uuid);
        return connectionMap.get(uuid).getDatabase(dbName);
    }

    public static <T> MongoCollection<T> getCollection(String name, UUID uuid, Class<T> tClass) throws UnknownHostException {
        return getDB(uuid).getCollection(name, tClass);
    }

    public static MongoCollection<Document> getCollection(String name, UUID uuid) throws UnknownHostException {
        return getDB(uuid).getCollection(name);
    }

    public static Document findOne(String field, String value, MongoCollection<Document> collection) {
        return collection.find(eq(field, value)).first();
    }

    public static BasicDBObject findOne(String field, int value, DBCollection collection) {
        return (BasicDBObject) collection.find(new BasicDBObject(field, value)).next();
    }
}
