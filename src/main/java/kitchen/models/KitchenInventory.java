package kitchen.models;

import java.util.List;

public class KitchenInventory {
    private int _id;
    private String name;
    private List<Spice> spiceList;

    public KitchenInventory(int _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public KitchenInventory(int _id, String name, List<Spice> spiceList) {
        this._id = _id;
        this.name = name;
        this.spiceList = spiceList;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Spice> getSpiceList() {
        return spiceList;
    }

    public void setSpiceList(List<Spice> spiceList) {
        this.spiceList = spiceList;
    }
}

