package l33tD33r.app.database.form;

import l33tD33r.app.database.FieldType;

/**
 * Created by Simon on 10/24/2014.
 */
public class ReferenceItem extends Item {

    private String table;

    public ItemType getType() {
        return ItemType.Reference;
    }

    public String getTable() {
        return table;
    }
    public void setTable(String table) {
        this.table = table;
    }

    public String getReferenceId() {
        return (String)getValue();
    }
    public void setReferenceId(String referenceId) {
        setValue(referenceId);
    }
}
