package l33tD33r.app.database.form;

/**
 * Created by Simon on 10/24/2014.
 */
public abstract class Item {

    private String id;
    private Object value;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        this.value = value;
    }

    public abstract ItemType getType();
}
