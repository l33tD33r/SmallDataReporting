package l33tD33r.app.database.query;

import l33tD33r.app.database.FieldType;
import l33tD33r.app.database.data.DataField;
import l33tD33r.app.database.data.DataRecord;

import java.util.List;

/**
 * Created by Simon on 2/17/14.
 */
public class SetCountNode extends ExpressionNode {

    private String[] fieldPath;

    public SetCountNode(String[] fieldPath) {
        this.fieldPath = fieldPath;
    }
    @Override
    public ExpressionNodeType getType() {
        return ExpressionNodeType.SetCount;
    }

    @Override
    protected Object evaluateValue(IDataRow dataRow) {
        if (!(dataRow instanceof TableQuery.TableDataRow)) {
            throw new RuntimeException();
        }
        Object value = null;
        DataRecord currentRecord = ((TableQuery.TableDataRow)dataRow).getDataRecord();
        for (int i=0; i < fieldPath.length; i++) {
            String field = fieldPath[i];
            if (i < fieldPath.length - 1) {
                DataField dataField = currentRecord.getField(field);
                FieldType fieldType = dataField.getType();
                if (fieldType != FieldType.Reference) {
                    throw new RuntimeException("Field '" + field + "' must be a reference field");
                }
                currentRecord = dataField.getReferenceDataRecord();
            } else {
                List<DataRecord> dataRecordSet = currentRecord.getSetField(field);
                value = Integer.valueOf(dataRecordSet.size());
            }
        }
        return value;
    }
}
