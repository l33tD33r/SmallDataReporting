package l33tD33r.app.database.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;

public class SchemaTable {
	protected String name;
	protected LinkedHashMap<String,SchemaField> keyFieldsMap;
	protected LinkedHashMap<String,SchemaField> dataFieldsMap;
	protected LinkedHashMap<String,SchemaField> allFieldsMap;
		
	public SchemaTable() {
		keyFieldsMap = new LinkedHashMap<String,SchemaField>();
		dataFieldsMap = new LinkedHashMap<String,SchemaField>();
		allFieldsMap = new LinkedHashMap<String, SchemaField>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addKeyField(SchemaField field) {
		this.keyFieldsMap.put(field.getName(), field);
		this.allFieldsMap.put(field.getName(), field);
	}
	
	public void removeKeyField(SchemaField field) {
		this.keyFieldsMap.remove(field.getName());
		this.allFieldsMap.remove(field.getName());
	}
	
	public void addDataField(SchemaField field) {
		this.dataFieldsMap.put(field.getName(),field);
		this.allFieldsMap.put(field.getName(), field);
	}
	
	public void removeDataField(SchemaField field) {
		this.dataFieldsMap.remove(field.getName());
		this.allFieldsMap.remove(field.getName());
	}
	
	public SchemaField getField(String name) {
		return this.allFieldsMap.get(name);
	}
	
	public List<String> getKeyFieldNames() {
		return new ArrayList<String>(this.keyFieldsMap.keySet());
	}
	
	public List<String> getDataFieldNames() {
		return new ArrayList<String>(this.dataFieldsMap.keySet());
	}
	
	public List<String> getAllFieldNames() {
		return new ArrayList<String>(this.allFieldsMap.keySet());
	}
	
	public List<SchemaField> getKeyFields() {
		return new ArrayList<SchemaField>(this.keyFieldsMap.values());
	}
	
	public List<SchemaField> getDataFields() {
		return new ArrayList<SchemaField>(this.dataFieldsMap.values());
	}

	public List<SchemaField> getAllFields() {
		return new ArrayList<SchemaField>(this.allFieldsMap.values());
	}
}
