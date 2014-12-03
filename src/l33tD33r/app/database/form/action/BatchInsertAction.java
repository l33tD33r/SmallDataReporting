package l33tD33r.app.database.form.action;

import l33tD33r.app.database.data.DataManager;
import l33tD33r.app.database.data.DataRecord;
import l33tD33r.app.database.data.DataRecordExistsException;
import l33tD33r.app.database.form.data.*;

import java.util.ArrayList;

/**
 * Created by Simon on 2014-11-29.
 */
public class BatchInsertAction extends Action {

    private String table;

    private CollectionRefSource sourceCollection;

    private String updatePropertyId;

    public BatchInsertAction() {
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

    public void setUpdatePropertyId(String updatePropertyId) {
        this.updatePropertyId = updatePropertyId;
    }

    @Override
    public ActionType getType() {
        return ActionType.Insert;
    }

    @Override
    public void execute() {

        for (ElementRefSource elementSource : sourceCollection.getElementSources()) {
            DataRecord newRecord = DataManager.getSingleton().createNewRecord(getTable());

            for (Field field : elementSource.getFields()) {
                newRecord.getField(field.getName()).setValue(field.getValueSource().getValue());
            }

            try {
                DataManager.getSingleton().insertRecord(getTable(), newRecord);

                if (updatePropertyId != null) {
                    ItemSource property = elementSource.getElement().getProperty(updatePropertyId);
                    property.setValue(newRecord.getId());
                }
            } catch (DataRecordExistsException e) {
                throw new RuntimeException("Insert record failed", e);
            }
        }
    }
}
