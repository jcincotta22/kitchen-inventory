package kitchen.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import kitchen.models.KitchenInventory;
import kitchen.models.Spice;
import kitchen.mongo.KitchenInventoryMongoClient;
import kitchen.resources.KitchenInventoryResource;
import kitchen.utils.JsonUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.bson.Document;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class KitchenInventoryService {
    final private static Logger logger = LogManager.getLogger(KitchenInventoryService.class.getName());

    public KitchenInventoryService() { }

    public List<KitchenInventoryResource> findAll() throws UnknownHostException {
        List<KitchenInventoryResource> kitchenInventoryResourceList = new ArrayList<>();

        for(KitchenInventory inventory : getAllInventory()) {
            kitchenInventoryResourceList.add(KitchenInventoryResource.from(inventory));
        }
        logger.debug("Successfully Retrieved all kitchen inventory");

        return kitchenInventoryResourceList;
    }

    public KitchenInventory create(String name, List<Spice> spiceList) throws UnknownHostException {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        MongoCollection<Document> collection = KitchenInventoryMongoClient.getCollection("inventory", uuid1);
        MongoCollection<Document> counter = KitchenInventoryMongoClient.getCollection("counters", uuid2);
        Document inventorySequence = KitchenInventoryMongoClient.findOne("_id", "inventory", counter);
        int id  = inventorySequence.getInteger("sequence_value");
        KitchenInventory inventory = new KitchenInventory(id, name);
        inventory.setSpiceList(spiceList);

        collection.insertOne(KitchenInventoryMongoClient.toDocument(inventory));

        logger.debug("User created with name, {}, and email, {}");
        updateCounter(id + 1, counter);
        KitchenInventoryMongoClient.closeMongoConnection(uuid1);
        KitchenInventoryMongoClient.closeMongoConnection(uuid2);

        return inventory;
    }

    private static List<KitchenInventory> getAllInventory() throws UnknownHostException {
        UUID uuid = UUID.randomUUID();
        MongoCollection kitchenInventoryCollection = KitchenInventoryMongoClient.getCollection("Inventory", uuid);

        List<KitchenInventory> kitchenInventoryList = new ArrayList<>();

        try (MongoCursor cursor = kitchenInventoryCollection.find().iterator()) {
            while (cursor.hasNext()) {
                KitchenInventory document = JsonUtil.jsonToObject(JsonUtil.toJson(cursor.next()), KitchenInventory.class);
                kitchenInventoryList.add(document);
            }
        }

        KitchenInventoryMongoClient.closeMongoConnection(uuid);
        return kitchenInventoryList;
    }

    static void updateCounter(int postSequence, MongoCollection<Document> collectionToUpdate) {

        collectionToUpdate.updateOne(eq("_id", "inventory"), new Document("$set", new Document("sequence_value", postSequence)));

        logger.debug("Counter updated to " + postSequence);
    }

    static void updateCounter(long postSequence, MongoCollection<Document> collectionToUpdate, String id) {

        collectionToUpdate.updateOne(eq("_id", id), new Document("$set", new Document("sequence_value", postSequence)));

        logger.debug("Counter updated to " + postSequence);
    }
}
