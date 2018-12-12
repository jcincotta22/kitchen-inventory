package kitchen.resources;

import kitchen.models.KitchenInventory;
import kitchen.models.Spice;

import java.util.ArrayList;
import java.util.List;

public class KitchenInventoryResource {
    private int _id;
    private String name;
    private List<Spice> spiceList;

    private KitchenInventoryResource() {

    }

    public static KitchenInventoryResource from(KitchenInventory kitchenInventory) {
        KitchenInventoryResource kitchenInventoryResource = new KitchenInventoryResource();
        kitchenInventoryResource._id = kitchenInventory.getId();
        kitchenInventoryResource.name = kitchenInventory.getName();
        kitchenInventoryResource.spiceList = kitchenInventory.getSpiceList();

        return kitchenInventoryResource;
    }
}
