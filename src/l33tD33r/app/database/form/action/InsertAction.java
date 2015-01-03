package l33tD33r.app.database.form.action;

import l33tD33r.app.database.data.DataManager;
import l33tD33r.app.database.data.DataRecord;
import l33tD33r.app.database.data.DataRecordExistsException;
import l33tD33r.app.database.form.data.ItemSource;
import l33tD33r.app.ui.workspace.data.DataRecordReference;

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
        fields.add(field);
    }

    @Override
    public void execute() {
        DataRecord newRecord = DataManager.getSingleton().createNewRecord(getTable());

        for (Field field : getFields()) {
            Object value = field.getValueSource().getValue();
            if (value instanceof DataRecordReference) {
                value = ((DataRecordReference)value).getId();
            }
            newRecord.getField(field.getName()).setValue(value);
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
