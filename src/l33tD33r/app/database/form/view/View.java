package l33tD33r.app.database.form.view;

import l33tD33r.app.database.form.FormComponent;

/**
 * Created by Simon on 10/24/2014.
 */
public abstract class View extends FormComponent {

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

    public abstract ViewType getType();
}
