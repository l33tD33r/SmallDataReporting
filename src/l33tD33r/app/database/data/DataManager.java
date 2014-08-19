package l33tD33r.app.database.data;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

import org.w3c.dom.Document;

import l33tD33r.app.database.FieldType;
import l33tD33r.app.database.data.storage.DataProvider;
import l33tD33r.app.database.schema.SchemaField;
import l33tD33r.app.database.schema.SchemaManager;
import l33tD33r.app.database.schema.SchemaTable;

public class DataManager implements DataRecordProvider, DataChangeListener {

	// Singleton
	private static DataManager singleton = null;
	
	public static boolean singletonCreated() {
		return singleton != null;
	}
	
	public static void createSingleton(DataProvider dataProvider) {
		if (singletonCreated()) {
			throw new RuntimeException("DataManager can only be created once");
		}
		singleton = new DataManager(dataProvider);
	}
	
	public static DataManager getSingleton() {
		return singleton;
	}
	
	private SchemaManager schemaManager = null;
	
	private LinkedHashMap<String,DataTable> dataTablesMap;
	
	private DataProvider dataProvider;
	
	private DataManager(DataProvider dataProvider) {
		this.schemaManager = SchemaManager.getSingleton();
		this.dataTablesMap = new LinkedHashMap<String, DataTable>();
		this.dataProvider = dataProvider;
		loadDataTables();
	}
	
	private void loadDataTables() {
		
		List<String> dataTableNames = schemaManager.getDataTableNames();

        Document dataDocument = this.dataProvider.getDataDocument();

        if (dataDocument == null) {
            return;
        }

		for (DataTable dataTable : DataSerialization.deserializeData(dataDocument)) {
			if (!dataTableNames.contains(dataTable.getName())) {
				throw new RuntimeException("Schema does not contain table: " + dataTable.getName());
			}
			loadTable(dataTable);
		}
		
		for (String dataTableName : dataTableNames) {
			if (!this.dataTablesMap.containsKey(dataTableName)) {
				loadTable(DataSerialization.createEmptyDataTable(dataTableName));
			}
		}
		resolveDataReferences();
	}
	
	public void reloadDataTables() {
		this.dataTablesMap.clear();
		loadDataTables();
	}
	
	public void loadTable(DataTable table) {
		this.dataTablesMap.put(table.getName(), table);
		table.addDataChangeListener(this);
	}
	
	public DataTable getTable(String name) {
		return this.dataTablesMap.get(name);
	}
	
	public void resolveDataReferences() {
		for (DataTable table : this.dataTablesMap.values()) {
			SchemaTable schemaTable = table.getSchema();
			
			ArrayList<SchemaField> schemaReferenceFields = new ArrayList<SchemaField>();
			for (SchemaField schemaField : schemaTable.getAllFields()) {
				if (schemaField.getType() == FieldType.Reference) {
					schemaReferenceFields.add(schemaField);
				}
			}
			
			for (DataRecord record : table) {
				for (SchemaField schemaReferenceField : schemaReferenceFields) {
					DataField referenceField = record.getField(schemaReferenceField.getName());
					String referenceRecordId = referenceField.getValueString();
					DataTable referenceTable = this.dataTablesMap.get(schemaReferenceField.getRelatedTableName());
					
					DataRecord referenceRecord = referenceTable.retrieveDataRecord(referenceRecordId);
					referenceField.setReferenceDataRecord(referenceRecord);
					
					DataField setField = referenceRecord.getField(schemaReferenceField.getInverseFieldName());
					setField.addInverseSetDataRecord(record);
				}
			}
		}
	}

	@Override
	public List<DataRecord> retrieveRecords(String tableName) {
		DataTable dataTable = this.getTable(tableName);
		return dataTable.getAllRecords();
	}

	@Override
	public List<DataRecord> retrieveRecords(String tableName, Map<String, Object> filterValues) {
		DataTable dataTable = this.getTable(tableName);
		return dataTable.getMatchingRecords(filterValues);
	}
	
	@Override
	public DataRecord createNewRecord(String tableName) {
		DataTable dataTable = this.getTable(tableName);
		return dataTable.createNewRecord();
	}
	
	public DataRecord createExistingRecord(String tableName, String id) {
		DataTable dataTable = this.getTable(tableName);
		return dataTable.createExistingRecord(id);
	}

	@Override
	public void insertRecord(String tableName, DataRecord newRecord) throws DataRecordExistsException {
		DataTable dataTable = this.getTable(tableName);
		dataTable.insertRecord(newRecord);
	}

	@Override
	public void insertRecords(String tableName, List<DataRecord> newRecords) throws DataRecordExistsException {
		DataTable dataTable = this.getTable(tableName);
		dataTable.insertRecords(newRecords);
	}

	@Override
	public void updateRecord(String tableName, DataRecord updatedRecord) throws DataRecordDoesNotExistException {
		DataTable dataTable = this.getTable(tableName);
		ArrayList<DataRecord> updatedRecords = new ArrayList<DataRecord>();
		updatedRecords.add(updatedRecord);
		dataTable.updateRecords(updatedRecords);
	}

	@Override
	public void updateRecords(String tableName, List<DataRecord> updatedRecords) throws DataRecordDoesNotExistException {
		DataTable dataTable = this.getTable(tableName);
		dataTable.updateRecords(updatedRecords);
	}

	@Override
	public void deleteRecord(String tableName, DataRecord deleteRecord) throws DataRecordDoesNotExistException {
		
	}

	@Override
	public void deleteRecords(String tableName, List<DataRecord> deleteRecords) throws DataRecordDoesNotExistException {

	}

	@Override
	public void deleteAllRecords(String tableName) {

	}

	@Override
	public void dataChange(DataChangeEvent event) {
		resolveDataReferences();
		
		Document dataDocument = DataSerialization.serializeDataTables(new ArrayList<DataTable>(this.dataTablesMap.values()));
		
		dataProvider.setDataDocument(dataDocument);
	}
}
