package l33tD33r.app.database.form.action;

import l33tD33r.app.database.data.DataField;
import l33tD33r.app.database.data.DataManager;
import l33tD33r.app.database.data.DataRecord;
import l33tD33r.app.database.data.DataRecordDoesNotExistException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Simon on 12/16/2014.
 */
public class BatchUpdateAction extends Action {

    private String table;

    private FieldValueSet filterFieldValues;

    private Map<String,FieldValueSet> updateFieldValues;

    private ArrayList<String> nonUpdatedIds;

    @Override
    public ActionType getType() {
        return ActionType.Update;
    }

    public String getTable() {
        return table;
    }
    public void setTable(String table) {
        this.table = table;
    }

    public FieldValueSet getFilterFieldValues() {
        return filterFieldValues;
    }
    public void setFilterFieldValues(FieldValueSet filterFieldValues) {
        this.filterFieldValues = filterFieldValues;
    }

    public Map<String,FieldValueSet> getUpdateFieldValues() {
        return updateFieldValues;
    }
    public void setUpdateFieldValues(Map<String,FieldValueSet> updateFieldValues) {
        this.updateFieldValues = updateFieldValues;
    }

    public FieldValueSet getUpdateValues(String id) {
        FieldValueSet updateValues = getUpdateFieldValues().get(id);
        if (updateValues == null) {
            throw new RuntimeException(MessageFormat.format("Update field values do not exist for id ''{0}''", id));
        }
        return updateValues;
    }

    @Override
    public void execute() {
        List<DataRecord> updateRecords = DataManager.getSingleton().retrieveRecords(getTable(), getFilterFieldValues().getFieldValues());

        for (DataRecord updateRecord : updateRecords) {
            updateRecordValues(updateRecord);
        }

        if (!nonUpdatedIds.isEmpty()) {
            throw new RuntimeException(MessageFormat.format("{0} expected records were not updated: {1}", nonUpdatedIds.size(), nonUpdatedIds));
        }

        try {
            DataManager.getSingleton().updateRecords(getTable(), updateRecords);
        } catch (DataRecordDoesNotExistException e) {
            throw new RuntimeException(MessageFormat.format("Record does not exist to be updated: {0}", e.getMessage()));
        }
    }

    private void updateRecordValues(DataRecord updateRecord) {

        FieldValueSet updateValues = getUpdateValues(updateRecord.getId());

        nonUpdatedIds.remove(updateRecord.getId());

        for (String fieldName : updateValues.getFieldNames()) {
            DataField field = updateRecord.getField(fieldName);
            if (field == null) {
                throw new RuntimeException(MessageFormat.format("The field {0} does not exist on table {1}.", fieldName, getTable()));
            }
            Object fieldValue = updateValues.getFieldValue(fieldName);
            field.setValue(fieldValue);
        }
    }
}
