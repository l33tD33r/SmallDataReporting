package l33tD33r.app.database.form.view;

/**
 * Created by Simon on 2014-10-31.
 */
public abstract class ItemView extends View {
    private String itemId;

    private String label;

    public String getItemId() {
        return itemId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
}
