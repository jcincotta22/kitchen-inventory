package kitchen.resources;

public class SearchResource {
    private String field;
    private String value;

    public SearchResource(String field, String value) {
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }
}
