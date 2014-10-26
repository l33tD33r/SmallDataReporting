package l33tD33r.app.database.form;

/**
 * Created by Simon on 10/25/2014.
 */
public class ItemOutputSource extends OutputSource {

    private String itemId;

    @Override
    public OutputSourceType getType() { return OutputSourceType.Item; }

    public String getItemId() {
        return itemId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
