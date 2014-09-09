package l33tD33r.app.database.query;

import java.util.Map;
import java.util.Map.Entry;

import l33tD33r.app.database.data.DataField;
import l33tD33r.app.database.data.DataManager;
import l33tD33r.app.database.data.DataRecord;
import l33tD33r.app.database.data.DataRecordDoesNotExistException;
import l33tD33r.app.database.data.DataRecordExistsException;


public class QueryManager {

    public static JoinQuery createJoinQuery(Query leftSide, Query rightSide, JoinRules rules, String name, ExpressionNode sourceFilterExpression, boolean group, String[] columnNames, GroupRule[] columnGroupRules, SortRule[] columnSortRules, ExpressionNode[] columnExpressions, DataType[] columnDataTypes, ExpressionNode resultFilterExpression) {
        Column[] columns = createColumns(columnNames, columnGroupRules, columnSortRules, columnExpressions, columnDataTypes);
        return new JoinQuery(leftSide, rightSide, rules, name, sourceFilterExpression, group, columns, resultFilterExpression);
    }

	public static TableQuery createTableQuery(String tableName, String name, ExpressionNode sourceFilterExpression, boolean group, String[] columnNames, GroupRule[] columnGroupRules, SortRule[] columnSortRules, ExpressionNode[] columnExpressions, DataType[] columnDataTypes, ExpressionNode resultFilterExpression) {
		Column[] columns = createColumns(columnNames, columnGroupRules, columnSortRules, columnExpressions, columnDataTypes);
		return new TableQuery(tableName, name, sourceFilterExpression, group, columns, resultFilterExpression);
	}

    private static Column[] createColumns(String[] names, GroupRule[] groupRules, SortRule[] sortRules, ExpressionNode[] expressions, DataType[] dataTypes) {
        Column[] columns = new Column[names.length];
        for (int i=0; i < columns.length; i++) {
            columns[i] = new Column(names[i], groupRules[i], sortRules[i], expressions[i], dataTypes[i]);
        }
        return columns;
    }
	
	public static void insertDataTableRecord(String tableName, Map<String,String> recordValues) {
		DataManager dataManager = DataManager.getSingleton();
		DataRecord newRecord = dataManager.createNewRecord(tableName);
		updateDataRecordValues(newRecord, recordValues);
		try {
			dataManager.insertRecord(tableName, newRecord);
		} catch (DataRecordExistsException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void modifyDataTableRecord(String tableName, String recordId, Map<String,String> recordValues) {
		DataManager dataManager = DataManager.getSingleton();
		DataRecord existingRecord = dataManager.createExistingRecord(tableName, recordId);
		updateDataRecordValues(existingRecord, recordValues);
		try {
			dataManager.updateRecord(tableName, existingRecord);
		} catch (DataRecordDoesNotExistException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void deleteDataTableRecord(String tableName, String recordId) {
		DataManager dataManager = DataManager.getSingleton();
		DataRecord existingRecord = dataManager.createExistingRecord(tableName, recordId);
		try {
			dataManager.deleteRecord(tableName, existingRecord);
		} catch (DataRecordDoesNotExistException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static void updateDataRecordValues(DataRecord dataRecord, Map<String,String> recordValues) {
		for (Entry<String,String> fieldValuePair : recordValues.entrySet()) {
			String fieldName = fieldValuePair.getKey();
			String fieldStringValue = fieldValuePair.getValue();
			DataField field = dataRecord.getField(fieldName);
			field.setValueString(fieldStringValue);
		}
	}
}
