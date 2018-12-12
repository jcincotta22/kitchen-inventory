package kitchen.models;

import org.bson.types.ObjectId;

public class Product {
    private ObjectId _id;
    private long NDB_Number;
    private String long_name;
    private String manufacturer;
    public String data_source;
    public long gtin_upc;
    public String date_modified;
    public String date_available;
    public String ingredients_english;

    public Product(ObjectId _id, long NDB_Number, String long_name, String manufacturer, String data_source, long gtin_upc, String getDate_modified, String ingredients_english, String date_available){
        this._id = _id;
        this.NDB_Number = NDB_Number;
        this.long_name = long_name;
        this.manufacturer = manufacturer;
        this.data_source = data_source;
        this.gtin_upc = gtin_upc;
        this.date_modified = getDate_modified;
        this.date_available = date_available;
        this.ingredients_english = ingredients_english;
    }

    public Product(String long_name, String manufacturer){
        this.long_name = long_name;
        this.manufacturer = manufacturer;
    }

    public ObjectId get_id() {
        return _id;
    }

    public long getNDB_Number() {
        return NDB_Number;
    }

    public String getLong_name() {
        return long_name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setNDB_Number(long NDB_Number) {
        this.NDB_Number = NDB_Number;
    }

}
