package l33tD33r.app.database.form.action;

import l33tD33r.app.database.data.DataManager;
import l33tD33r.app.database.data.DataRecord;
import l33tD33r.app.database.data.DataRecordExistsException;
import l33tD33r.app.database.form.data.ItemSource;

import java.util.ArrayList;

/**
 * Created by Simon on 2014-10-28.
 */
public class InsertAction extends Action {

    private String table;

    private ItemSource updateItemSource;

    private ArrayList<Field> fields;

    public InsertAction() {
        fields = new ArrayList<>();
    }

    @Override
    public ActionType getType() {
        return ActionType.Insert;
    }

    public String getTable() {
        return table;
    }
    public void setTable(String table) {
        this.table = table;
    }

    public void setUpdateItemSource(ItemSource updateItemSource) { this.updateItemSource = updateItemSource; }

    public ArrayList<Field> getFields() {
        return new ArrayList<>(fields);
    }
    public void addField(Field field) {
        field.setParentAction(this);
        fields.add(field);
    }

    @Override
    public void execute() {
        DataRecord newRecord = DataManager.getSingleton().createNewRecord(getTable());

        for (Field field : getFields()) {
            newRecord.getField(field.getName()).setValue(field.getValueSource().getValue());
        }

        try {
            DataManager.getSingleton().insertRecord(getTable(), newRecord);

            if (updateItemSource != null) {
                updateItemSource.setValue(newRecord.getId());
            }
        } catch (DataRecordExistsException e) {
            throw new RuntimeException("Insert record failed", e);
        }
    }
}
