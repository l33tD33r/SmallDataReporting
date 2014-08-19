package l33tD33r.app.database.query;

import java.util.ArrayList;

import l33tD33r.app.database.Date;
import l33tD33r.app.database.FieldType;
import l33tD33r.app.database.Time;
import l33tD33r.app.database.data.DataManager;
import l33tD33r.app.database.data.DataRecord;

public class TableQuery extends Query {

	private static DataManager dataManager = DataManager.getSingleton();
	
	private String tableName;
	
	protected TableQuery(String tableName, ExpressionNode sourceFilterExpression, boolean group, Column[] columns, ExpressionNode resultFilterExpression) {
		super(sourceFilterExpression, group, columns, resultFilterExpression);
		this.tableName = tableName;
	}

	@Override
	public IDataSource getDataSource() {
		return new TableDataSource();
	}
	
	private class TableDataSource implements IDataSource {

		private ArrayList<DataRecord> dataRecords;
		private int currentIndex;
		
		private TableDataSource() {
			dataRecords = new ArrayList<DataRecord>(dataManager.retrieveRecords(tableName));
			currentIndex = 0;
		}
		@Override
		public boolean hasMoreElements() {
			return currentIndex < dataRecords.size();
		}

		@Override
		public IDataRow nextElement() {
			if (!hasMoreElements()) {
				throw new RuntimeException("no more elements");
			}
			return new TableDataRow(dataRecords.get(currentIndex++));
		}
	}
	
	protected static class TableDataRow implements IDataRow {

		private DataRecord dataRecord;
		
		public TableDataRow(DataRecord dataRecord) {
			this.dataRecord = dataRecord;
		}
		
		private static DataType getDataType(FieldType fieldType) {
			switch (fieldType) {
				case String:
					return DataType.String;
				case Integer:
					return DataType.Integer;
				case Number:
					return DataType.Number;
				case Boolean:
					return DataType.Boolean;
				case Date:
					return DataType.Date;
				case Time:
					return DataType.Time;
				default:
					throw new RuntimeException("FieldType has no matching DataType: " + fieldType.name());
			}
		}
		
		@Override
		public DataType getType(String name) {
			return getDataType(dataRecord.getFieldType(name));
		}

		@Override
		public Object getValue(String name) {
			return dataRecord.getFieldValue(name);
		}

		@Override
		public String getStringValue(String name) {
			return dataRecord.getFieldValueString(name);
		}

		@Override
		public Integer getIntegerValue(String name) {
			return dataRecord.getFieldValueInteger(name);
		}

		@Override
		public Double getNumberValue(String name) {
			return dataRecord.getFieldValueDouble(name);
		}

		@Override
		public Boolean getBooleanValue(String name) {
			return dataRecord.getFieldValueBoolean(name);
		}

		@Override
		public Date getDateValue(String name) {
			return dataRecord.getFieldValueDate(name);
		}

		@Override
		public Time getTimeValue(String name) {
			return dataRecord.getFieldValueTime(name);
		}
		
		public DataRecord getDataRecord() {
			return dataRecord;
		}
		
	}
}
