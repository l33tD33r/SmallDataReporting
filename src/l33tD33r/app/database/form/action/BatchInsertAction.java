package l33tD33r.app.database.form.action;

import l33tD33r.app.database.data.DataManager;
import l33tD33r.app.database.data.DataRecord;
import l33tD33r.app.database.data.DataRecordExistsException;
import l33tD33r.app.database.form.data.Collection;
import l33tD33r.app.database.form.data.CollectionRefSource;
import l33tD33r.app.database.form.data.Element;

import java.util.ArrayList;

/**
 * Created by Simon on 2014-11-29.
 */
public class BatchInsertAction extends Action {

    private String table;

    private CollectionRefSource sourceCollection;

    private String updatePropertyId;

    private ArrayList<Field> fields;

    public BatchInsertAction() {
        fields = new ArrayList<>();
    }

    public String getTable() {
        return table;
    }
    public void setTable(String table) {
        this.table = table;
    }

    public void setSourceCollection(CollectionRefSource sourceCollection) {
        this.sourceCollection = sourceCollection;
    }

    public ArrayList<Field> getFields() {
        return new ArrayList<>(fields);
    }
    public void addField(Field field) {
//        field.setParentAction(this);
        fields.add(field);
    }

    @Override
    public ActionType getType() {
        return ActionType.Insert;
    }

    @Override
    public void execute() {

        for (Element element : sourceCollection.getElements()) {
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
}
