package l33tD33r.app.database.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import l33tD33r.app.database.FieldType;
import l33tD33r.app.database.schema.storage.SchemaProvider;

public class SchemaManager {

	// Singleton
	private static SchemaManager singleton = null;
	
	public static boolean singletonCreated() {
		return singleton != null;
	}
	
	public static void createSingleton(SchemaProvider schemaProvider) {
		if (singletonCreated()) {
			throw new RuntimeException("SchemaManager can only be created once");
		}
		singleton = new SchemaManager(schemaProvider);
	}
	
	public static SchemaManager getSingleton() {
		return singleton;
	}
	
	private HashMap<String,SchemaTable> tablesMap;
	private SchemaProvider schemaProvider;
	
	private SchemaManager(SchemaProvider schemaProvider) {
		this.tablesMap = new HashMap<String,SchemaTable>();
		this.schemaProvider = schemaProvider;
		loadSchemaTables();
	}
	
	private void loadSchemaTables() {
		
		List<SchemaTable> mainSchemaTables = SchemaSerialization.deserializeSchemaDocument(this.schemaProvider.getSchemaDocument());
		for (SchemaTable mainSchemaTable : mainSchemaTables) {
			addTable(mainSchemaTable);
		}
		
		resolveReferenceSetFields();
	}
	
	public void addTable(SchemaTable schemaTable) {
		this.tablesMap.put(schemaTable.getName(), schemaTable);
	}
		
	private void resolveReferenceSetFields() {
		for (SchemaTable table : this.tablesMap.values()) {
			for (SchemaField field : table.getAllFields()) {
				if (field.getType() == FieldType.Reference) {
					SchemaTable relatedTable = this.tablesMap.get(field.getRelatedTableName());
                    if (relatedTable == null) {
                        throw new RuntimeException("Related table does not exist:"+field.getRelatedTableName());
                    }
					field.setRelatedTable(relatedTable);
					
					SchemaField setField = new SchemaField();
					setField.setType(FieldType.Set);
					setField.setName(field.getInverseFieldName());
					setField.setInverseFieldName(field.getName());
					setField.setRelatedTableName(table.getName());
					setField.setRelatedTable(table);
					
					relatedTable.addDataField(setField);
				}
			}
		}
	}
	
	public SchemaTable getTable(String name) {
		return this.tablesMap.get(name);
	}
	
	public List<String> getDataTableNames() {
		return new ArrayList<String>(this.tablesMap.keySet());
	}
}
