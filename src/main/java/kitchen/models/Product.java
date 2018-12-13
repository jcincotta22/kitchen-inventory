package kitchen.models;

import org.bson.types.ObjectId;

public class Product {
    private long NDB_Number;
    private String long_name;
    private String manufacturer;
    public String data_source;
    public String date_modified;
    public String date_available;
    public String ingredients_english;

    public Product(long NDB_Number, String long_name, String manufacturer, String data_source, String getDate_modified, String ingredients_english, String date_available){
        this.NDB_Number = NDB_Number;
        this.long_name = long_name;
        this.manufacturer = manufacturer;
        this.data_source = data_source;
        this.date_modified = getDate_modified;
        this.date_available = date_available;
        this.ingredients_english = ingredients_english;
    }

    public Product(String long_name, String manufacturer){
        this.long_name = long_name;
        this.manufacturer = manufacturer;
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
