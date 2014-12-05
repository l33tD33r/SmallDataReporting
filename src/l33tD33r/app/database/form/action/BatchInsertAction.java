package l33tD33r.app.database.form.action;

import l33tD33r.app.database.data.DataManager;
import l33tD33r.app.database.data.DataRecord;
import l33tD33r.app.database.data.DataRecordExistsException;
import l33tD33r.app.database.form.data.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 2014-11-29.
 */
public class BatchInsertAction extends Action {

    private String table;

    private String updatePropertyId;

    private Collection sourceCollection;

    private ArrayList<PropertyRefSource> propertySources;

    private ArrayList<Field> fields;

    public BatchInsertAction() {
        propertySources = new ArrayList<>();
        fields = new ArrayList<>();
    }

    public String getTable() {
        return table;
    }
    public void setTable(String table) {
        this.table = table;
    }

    public void setUpdatePropertyId(String updatePropertyId) {
        this.updatePropertyId = updatePropertyId;
    }

    public void setSourceCollection(Collection sourceCollection) {
        this.sourceCollection = sourceCollection;
    }

    private void updateCurrentElement(Element currentElement) {
        propertySources.forEach(s -> s.setCurrentElement(currentElement));
    }

    public ArrayList<Field> getFields() {
        return fields;
    }
    public void addFields(Field field) {
        fields.add(field);

        ValueSource fieldSource = field.getValueSource();
        if (fieldSource instanceof PropertyRefSource) {
            propertySources.add((PropertyRefSource)fieldSource);
        }
    }

    @Override
    public ActionType getType() {
        return ActionType.Insert;
    }

    @Override
    public void execute() {
        for (Element element : sourceCollection.getElements()) {
            updateCurrentElement(element);

            DataRecord newRecord = DataManager.getSingleton().createNewRecord(getTable());

            for (Field field : getFields()) {
                newRecord.getField(field.getName()).setValue(field.getValueSource().getValue());
            }

            try {
                DataManager.getSingleton().insertRecord(getTable(), newRecord);

                if (updatePropertyId != null) {
                    ItemSource property = element.getProperty(updatePropertyId);
                    property.setValue(newRecord.getId());
                }
            } catch (DataRecordExistsException e) {
                throw new RuntimeException("Insert record failed", e);
            }
        }
    }
}
