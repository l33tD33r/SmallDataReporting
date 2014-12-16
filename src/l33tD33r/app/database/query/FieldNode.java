package l33tD33r.app.database.query;

import l33tD33r.app.database.FieldType;
import l33tD33r.app.database.data.DataField;
import l33tD33r.app.database.data.DataRecord;
import l33tD33r.app.database.query.TableQuery.TableDataRow;

import java.text.MessageFormat;

public class FieldNode extends ExpressionNode {

	private String[] fieldPath;
	
	public FieldNode(String[] fieldPath) {
		this.fieldPath = fieldPath;
	}
	
	@Override
	public ExpressionNodeType getType() {
		return ExpressionNodeType.Field;
	}

	@Override
	protected Object evaluateValue(IDataRow dataRow) {
		if (!(dataRow instanceof TableDataRow)) {
			throw new RuntimeException();
		}
		Object value = null;
		DataRecord currentRecord = ((TableDataRow)dataRow).getDataRecord();
		if (currentRecord != null && fieldPath != null) {
			for (int i = 0; i < fieldPath.length; i++) {
				String field = fieldPath[i];
				DataField dataField = currentRecord.getField(field);
				if (dataField == null) {
					throw new RuntimeException(MessageFormat.format("Field ''{0}'' does not exist", field));
				}
				FieldType fieldType = dataField.getType();
				if (i < fieldPath.length - 1) {
					if (fieldType != FieldType.Reference) {
						throw new RuntimeException("Field '" + field + "' must be a reference field");
					}
					currentRecord = dataField.getReferenceDataRecord();

					if (currentRecord == null) {
						throw new RuntimeException("Reference field not found");
					}
				} else {
					if (fieldType == FieldType.Reference) {
						throw new RuntimeException("Field '" + field + "' cannot be a reference field");
					}
					value = dataField.getValue();
				}
			}
		}
		return value;
	}

}
