package l33tD33r.app.database.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import l33tD33r.app.database.FieldType;
import l33tD33r.app.database.schema.SchemaField;
import l33tD33r.app.database.schema.SchemaManager;
import l33tD33r.app.database.schema.SchemaTable;

public class DataTable implements Iterable<DataRecord>{

	private String name;
	private SchemaTable schema;
	
	private HashMap<String,DataRecord> dataRecordIdCache;
	
	private DataChangeSupport dataChangeSupport;
	
	public DataTable(String name) {
		this.dataRecordIdCache = new HashMap<String,DataRecord>();
		this.dataChangeSupport = new DataChangeSupport(this);
		this.name = name;
		setSchema(SchemaManager.getSingleton().getTable(getName()));
	}
	
	public String getName() {
		return name;
	}

	private void setSchema(SchemaTable schema) {
		this.schema = schema;
	}
	
	public SchemaTable getSchema() {
		return schema;
	}
	
	public void loadDataRecord(DataRecord newDataRecord) {
		this.dataRecordIdCache.put(newDataRecord.getId(), newDataRecord);
	}
	
	public DataRecord retrieveDataRecord(String dataRecordId) {
		return this.dataRecordIdCache.get(dataRecordId);
	}
	
	public void resolveFieldValues(DataManager dataManager) {
		HashMap<String,String> referenceFieldTableMap = new HashMap<String, String>();
		for (SchemaField schemaField : getSchema().getAllFields()) {
			if (schemaField.getType() == FieldType.Reference) {
				referenceFieldTableMap.put(schemaField.getName(), schemaField.getRelatedTableName());
			}
		}
		if (referenceFieldTableMap.size() == 0) {
			return;
		}
		for (DataRecord dataRecord : this.dataRecordIdCache.values()) {
			for (Entry<String,String> referenceFieldTableEntry : referenceFieldTableMap.entrySet()) {
				DataField referenceField = dataRecord.getField(referenceFieldTableEntry.getKey());
				DataTable referencedDataTable = dataManager.getTable(referenceFieldTableEntry.getValue());
				
				String referencedDataRecordId = referenceField.getValueString();
				DataRecord referencedDataRecord = referencedDataTable.retrieveDataRecord(referencedDataRecordId);
				
				referenceField.setReferenceDataRecord(referencedDataRecord);
			}
		}
	}

	@Override
	public Iterator<DataRecord> iterator() {
		return this.dataRecordIdCache.values().iterator();
	}
	
	public List<DataRecord> getAllRecords() {
		return getMatchingRecords(null);
	}
	
	public List<DataRecord> getMatchingRecords(Map<String,Object> fieldFilterValues) {
		ArrayList<DataRecord> allRecords = new ArrayList<DataRecord>();
		for (DataRecord record : this) {
			if (recordMatches(record, fieldFilterValues)) {
				allRecords.add(record);
			}
		}
		return allRecords;
	}
	
	private boolean recordMatches(DataRecord record, Map<String,Object> fieldFilterValues) {
		if (fieldFilterValues == null) {
			return true;
		}
		for (Entry<String,Object> filterEntry : fieldFilterValues.entrySet()) {
			DataField field = record.getField(filterEntry.getKey());
			if (!field.getValue().equals(filterEntry.getValue())) {
				return false;
			}
		}
		return true;
	}
	
	public DataRecord createNewRecord() {
		DataRecord newRecord = new DataRecord();
		addDataFields(newRecord);
		return newRecord;
	}
	
	public DataRecord createExistingRecord(String id) {
		DataRecord newRecord = new DataRecord(id);
		addDataFields(newRecord);
		return newRecord;
	}
	
	private void addDataFields(DataRecord dataRecord) {
		for (SchemaField schemaField : getSchema().getAllFields()) {
			FieldType fieldType = schemaField.getType();
			DataField dataField = new DataField();
			dataField.setType(fieldType);
			dataField.setName(schemaField.getName());
			dataField.setValue(schemaField.getDefaultValue());
			dataRecord.addField(dataField);
		}
	}
	
	public void insertRecord(DataRecord newRecord) throws DataRecordExistsException {
		ArrayList<DataRecord> newRecords = new ArrayList<DataRecord>();
		newRecords.add(newRecord);
		insertRecords(newRecords);
	}
	
	public void insertRecords(List<DataRecord> newRecords) throws DataRecordExistsException {
		List<SchemaField> keyFields = getSchema().getKeyFields();
		
		if (keyFields.size() > 0) {
			for (DataRecord existingRecord : this) {
				for (DataRecord newRecord : newRecords) {
					if (recordsEqual(keyFields, existingRecord, newRecord)) {
						throw new DataRecordExistsException(existingRecord, newRecord);
					}	
				}
			}
		}
		
		for (DataRecord newRecord : newRecords) {
			this.dataRecordIdCache.put(newRecord.getId(), newRecord);
		}
		
		fireDataChange();
	}
	
	public void updateRecords(List<DataRecord> updateRecords) throws DataRecordDoesNotExistException {
		List<SchemaField> dataFields = getSchema().getAllFields();
		
		HashMap<String,DataRecord> existingRecords = new HashMap<String,DataRecord>();
		
		for (DataRecord updateRecord : updateRecords) {
			DataRecord existingRecord = this.dataRecordIdCache.get(updateRecord.getId());
			if (existingRecord == null) {
				throw new DataRecordDoesNotExistException();
			}
			existingRecords.put(existingRecord.getId(),existingRecord);
		}
		
		for (DataRecord updateRecord : updateRecords) {
			DataRecord existingRecord = existingRecords.get(updateRecord.getId());
			
			for (SchemaField dataField : dataFields) {
				if (dataField.getType() == FieldType.Set) {
					continue;
				}
				
				DataField existingField = existingRecord.getField(dataField.getName());
				DataField updateField = updateRecord.getField(dataField.getName());
				
				if (dataField.getType() == FieldType.Reference) {
					DataRecord existingReferenceRecord = existingField.getReferenceDataRecord();
					existingReferenceRecord.getField(dataField.getInverseFieldName());
					existingField.setValue(updateField.getValue());
					existingField.setReferenceDataRecord(updateField.getReferenceDataRecord());
				} else {
					existingField.setValue(updateField.getValue());
				}
			}
		}
		
		fireDataChange();
	}
	
	private boolean recordsEqual(List<SchemaField> keyFields, DataRecord a, DataRecord b) {
		for (SchemaField keyField : keyFields) {
			DataField keyFieldA = a.getField(keyField.getName());
			DataField keyfieldB = b.getField(keyField.getName());
			if (!keyFieldA.equals(keyfieldB)) {
				return false;
			}
		}
		return true;
	}
	
	// DataChange
	
	public void addDataChangeListener(DataChangeListener dataChangeListener) {
		this.dataChangeSupport.addDataChangeListener(dataChangeListener);
	}
	
	public void removeDataChangeListener(DataChangeListener dataChangeListener) {
		this.dataChangeSupport.removeDataChangeListener(dataChangeListener);
	}
	
	private void fireDataChange() {
		this.dataChangeSupport.fireDataChange();
	}
}
