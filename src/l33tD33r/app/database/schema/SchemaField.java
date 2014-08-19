package l33tD33r.app.database.schema;

import java.util.ArrayList;

import l33tD33r.app.database.Date;
import l33tD33r.app.database.FieldType;
import l33tD33r.app.database.Time;
import l33tD33r.app.database.restriction.Restriction;

public class SchemaField {

	private String name;
	private FieldType type;
	private Object defaultValue;
	
	// Reference and Set fields
	private String relatedTableName;
	private String inverseFieldName;
	
	private SchemaTable relatedTable;
	
	private ArrayList<Restriction> restrictions;
	
	public SchemaField() {
		this.restrictions = new ArrayList<Restriction>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FieldType getType() {
		return type;
	}

	public void setType(FieldType type) {
		this.type = type;
	}
	
	public Object getDefaultValue() {
		if (this.defaultValue == null) {
			return getDefaultValue(getType());
		}
		return this.defaultValue;
	}
	
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getRelatedTableName() {
		return relatedTableName;
	}

	public void setRelatedTableName(String relatedTableName) {
		this.relatedTableName = relatedTableName;
	}

	public String getInverseFieldName() {
		return inverseFieldName;
	}

	public void setInverseFieldName(String inverseFieldName) {
		this.inverseFieldName = inverseFieldName;
	}

	public SchemaTable getRelatedTable() {
		return relatedTable;
	}

	public void setRelatedTable(SchemaTable relatedTable) {
		this.relatedTable = relatedTable;
	}
	
	public void addRestriction(Restriction restriction) {
		this.restrictions.add(restriction);
	}
	
	private static Object getDefaultValue(FieldType type) {
		switch (type) {
			case String:
				return "";
			case Integer:
				return Integer.valueOf(0);
			case Number:
				return Double.valueOf(0.0);
			case Boolean:
				return Boolean.valueOf(false);
			case Date:
				return Date.past();
			case Time:
				return Time.midnight();
			case Reference:
			case Set:
			default:
				return null;
		}
	}
}
