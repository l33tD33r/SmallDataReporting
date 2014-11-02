package l33tD33r.app.database.form.data;

/**
 * Created by Simon on 2014-10-31.
 */
public class ItemTemplate {

    private String id;
    private DataType type;

    public ItemTemplate(String id, DataType type) {
        setId(id);
        setType(type);
    }

    public String getId() {
        return id;
    }
    private void setId(String id) {
        this.id = id;
    }

    public DataType getType() {
        return type;
    }
    private void setType(DataType type) {
        this.type = type;
    }
}
