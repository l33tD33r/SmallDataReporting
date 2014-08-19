package l33tD33r.app.database.data;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import l33tD33r.app.database.Date;
import l33tD33r.app.database.FieldType;
import l33tD33r.app.database.Time;

public class DataField {

	private String name;
	private FieldType type;
	private Object value;
	
	private DataRecord referenceDataRecord;
	private HashMap<String,DataRecord> inverseSetDataRecords;
	
	public DataField() {
		this.referenceDataRecord = null;
		this.inverseSetDataRecords = new HashMap<String,DataRecord>();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public FieldType getType() {
		return this.type;
	}
	public void setType(FieldType type) {
		this.type = type;
	}
	
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	public String getValueString() {
		Object value = getValue();
		if (value == null) {
			return null;
		}
		switch (getType()) {
			case String:
			case Reference:
				return (String)value;
			case Integer:
				return ((Integer)value).toString();
			case Number:
				return ((Double)value).toString();
			case Date:
				return ((Date)value).toString();
			case Time:
				return ((Time)value).toString();
			case Boolean:
				return ((Boolean)value).toString();
			case Set:
				throw new RuntimeException("Cannot get string value for Set field");
			default:
				throw new RuntimeException("Cannot get string value for " + getType().name() + " field");
		}
	}
	
	public Double getValueDouble() {
		return (Double)getValue();
	}
	
	public Integer getValueInteger() {
		return (Integer)getValue();
	}
	
	public Date getValueDate() {
		return (Date)getValue();
	}
	
	public Time getValueTime() {
		return (Time)getValue();
	}
	
	public Boolean getValueBoolean() {
		return (Boolean)getValue();
	}
	
	public void setValueString(String valueString) {
		switch (getType()) {
			case String:
			case Reference:
				this.setValue(valueString);
				break;
			case Boolean:
				this.setValue(Boolean.valueOf(valueString));
				break;
			case Integer:
				this.setValue(Integer.valueOf(valueString));
				break;
			case Number:
				this.setValue(Double.valueOf(valueString));
				break;
			case Date:
				this.setValue(Date.valueOf(valueString));
				break;
			case Time:
				this.setValue(Time.valueOf(valueString));
				break;
			case Set:
				throw new RuntimeException("Cannot set string value for Set field");
			default:
				throw new RuntimeException("Cannot set string value for " + getType().name() + " field");
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof DataField) {
			return equals((DataField)o);
		}
		return false;
	}
	
	private boolean equals(DataField field) {
		return getName().equalsIgnoreCase(field.getName()) && getValue().equals(field.getValue());
	}
	
	public void setReferenceDataRecord(DataRecord referenceDataRecord) {
		this.referenceDataRecord = referenceDataRecord;
	}
	
	public DataRecord getReferenceDataRecord() {
		return this.referenceDataRecord;
	}
	
	public void addInverseSetDataRecord(DataRecord inverseSetDataRecord) {
		this.inverseSetDataRecords.put(inverseSetDataRecord.getId(), inverseSetDataRecord);
	}
	
	public void removeInverseSetDataRecord(DataRecord inverseReferenceDataRecord) {
		this.inverseSetDataRecords.remove(inverseReferenceDataRecord.getId());
	}
	
	public List<DataRecord> getInverseSetDataRecord() {
		return new ArrayList<DataRecord>(this.inverseSetDataRecords.values());
	}
}
