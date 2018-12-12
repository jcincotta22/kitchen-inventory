package kitchen.services;

import kitchen.models.KitchenInventory;
import kitchen.models.Spice;
import java.util.List;

public class KitchenInventoryRequestBody extends KitchenInventory {

    public KitchenInventoryRequestBody(int _id, String name, List<Spice> spiceList) {
        super(_id, name, spiceList);
    }

}
