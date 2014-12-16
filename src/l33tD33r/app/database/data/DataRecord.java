package l33tD33r.app.database.data;

import java.text.MessageFormat;
import java.util.List;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.UUID;

import l33tD33r.app.database.Date;
import l33tD33r.app.database.FieldType;
import l33tD33r.app.database.Time;

public class DataRecord implements Iterable<DataField> {

	private String id;
	private LinkedHashMap<String,DataField> fieldsMap;
	
	public DataRecord() {
		this(UUID.randomUUID().toString());
	}
	public DataRecord(String id) {
		this.id = id;
		this.fieldsMap = new LinkedHashMap<String, DataField>();

		DataField idField = new DataField();
		idField.setType(FieldType.String);
		idField.setName("Id");
		idField.setValue(id);
		addField(idField);
	}
	
	public String getId() {
		return this.id;
	}
	
	void addField(DataField field) {
		if (this.fieldsMap.containsKey(field.getName())) {
			throw new RuntimeException("");
		}
		this.fieldsMap.put(field.getName(), field);
	}
	
	public DataField getField(String name) {
		if (!fieldsMap.containsKey(name)) {
			throw new RuntimeException(MessageFormat.format("Field ''{0}'' does not exist", name));
		}
		return this.fieldsMap.get(name);
	}
	
	public FieldType getFieldType(String name) {
		DataField dataField = getField(name);
		return dataField.getType();
	}
	
	public Object getFieldValue(String name) {
		DataField dataField = getField(name);
		return dataField.getValue();
	}
	
	public String getFieldValueString(String name) {
		DataField dataField = getField(name);
		return dataField.getValueString();
	}
	
	public void setFieldValueString(String name, String value) {
		DataField dataField = getField(name);
		dataField.setValueString(value);
	}
	
	public Double getFieldValueDouble(String name) {
		DataField dataField = getField(name);
		return dataField.getValueDouble();
	}
	
	public void setFieldValueDouble(String name, Double value) {
		DataField dataField = getField(name);
		dataField.setValue(value);
	}
	
	public Integer getFieldValueInteger(String name) {
		DataField dataField = getField(name);
		return dataField.getValueInteger();
	}
	
	public void setFieldValueInteger(String name, Integer value) {
		DataField dataField = getField(name);
		dataField.setValue(value);
	}
	
	public Date getFieldValueDate(String name) {
		DataField dataField = getField(name);
		return dataField.getValueDate();
	}
	
	public void setFieldValueDate(String name, Date value) {
		DataField dataField = getField(name);
		dataField.setValue(value);
	}
	
	public Time getFieldValueTime(String name) {
		DataField dataField = getField(name);
		return dataField.getValueTime();
	}
	
	public void setFieldValueTime(String name, Time value) {
		DataField dataField = getField(name);
		dataField.setValue(value);
	}
	
	public Boolean getFieldValueBoolean(String name) {
		DataField dataField = getField(name);
		return dataField.getValueBoolean();
	}
	
	public void setFieldValueBoolean(String name, Boolean value) {
		DataField dataField = getField(name);
		dataField.setValue(value);
	}
	
	public DataRecord getFieldReference(String name) {
		DataField dataField = getField(name);
		return dataField.getReferenceDataRecord();
	}
	
	public void setFieldReference(String name, DataRecord reference) {
		DataField dataField = getField(name);
		dataField.setValue(reference.getId());
		dataField.setReferenceDataRecord(reference);
	}
	
	public List<DataRecord> getSetField(String name) {
		DataField dataField = getField(name);
		return dataField.getInverseSetDataRecord();
	}
	
	@Override
	public Iterator<DataField> iterator() {
		return this.fieldsMap.values().iterator();
	}
}
