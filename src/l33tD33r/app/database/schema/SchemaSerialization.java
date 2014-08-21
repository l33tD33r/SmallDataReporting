package l33tD33r.app.database.schema;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import l33tD33r.app.database.FieldType;
import l33tD33r.app.database.restriction.MaximumCharacterCount;
import l33tD33r.app.database.restriction.MaximumValue;
import l33tD33r.app.database.restriction.MinimumCharacterCount;
import l33tD33r.app.database.restriction.MinimumValue;
import l33tD33r.app.database.restriction.Restriction;
import l33tD33r.app.database.utility.XmlUtils;

public class SchemaSerialization {
	
	public static List<SchemaTable> deserializeSchemaDocument(Document schemaDoc) {
		ArrayList<SchemaTable> schemaTables = new ArrayList<SchemaTable>();
		
		Element rootElement = schemaDoc.getDocumentElement();
		
		Element dataTablesElement = XmlUtils.getChildElement(rootElement, "DataTables");
		if (dataTablesElement != null) {
			for (Element dataTableElement : XmlUtils.getChildElements(dataTablesElement, "DataTable")) {
				SchemaTable table = createTable(dataTableElement);
				schemaTables.add(table);
			}
		}
		return schemaTables;
	}
	
	private static SchemaTable createTable(Element dataTableElement) {
		SchemaTable table = new SchemaTable();
		String name = dataTableElement.getAttribute("name");
		table.setName(name);
        String reportFieldName = dataTableElement.getAttribute("reportField");
        table.setReportFieldName(reportFieldName);
		
		for (SchemaField keyField : getKeyFields(dataTableElement)) {
			table.addKeyField(keyField);
		}
		
		for (SchemaField dataField : getDataFields(dataTableElement)) {
			table.addDataField(dataField);
		}
		
		return table;
	}
	
	private static ArrayList<SchemaField> getKeyFields(Element tableElement) {
		ArrayList<SchemaField> keyFields = new ArrayList<SchemaField>();
		Element keyFieldsElement = XmlUtils.getChildElement(tableElement, "KeyFields");
		if (keyFieldsElement != null) {
			keyFields.addAll(getFields(keyFieldsElement));
		}
		return keyFields;
	}
	
	private static ArrayList<SchemaField> getDataFields(Element tableElement) {
		ArrayList<SchemaField> dataFields = new ArrayList<SchemaField>();
		Element dataFieldsElement = XmlUtils.getChildElement(tableElement, "DataFields");
		if (dataFieldsElement != null) {
			dataFields.addAll(getFields(dataFieldsElement));
		}
		return dataFields;
	}
	
	private static ArrayList<SchemaField> getFields(Element fieldsElement) {
		ArrayList<SchemaField> fields = new ArrayList<SchemaField>();
		
		for (Element stringFieldElement : XmlUtils.getChildElements(fieldsElement, "StringField")) {
			SchemaField stringField = createField(FieldType.String, stringFieldElement);
			fields.add(stringField);
		}
		
		for (Element dateFieldElement : XmlUtils.getChildElements(fieldsElement, "DateField")) {
			SchemaField dateField = createField(FieldType.Date, dateFieldElement);
			fields.add(dateField);
		}
		
		for (Element timeFieldElement : XmlUtils.getChildElements(fieldsElement, "TimeField")) {
			SchemaField timeField = createField(FieldType.Time, timeFieldElement);
			fields.add(timeField);
		}
		
		for (Element integerFieldElement : XmlUtils.getChildElements(fieldsElement, "IntegerField")) {
			SchemaField integerField = createField(FieldType.Integer, integerFieldElement);
			fields.add(integerField);
		}
		
		for (Element numberFieldElement : XmlUtils.getChildElements(fieldsElement, "NumberField")) {
			SchemaField numberField = createField(FieldType.Number, numberFieldElement);
			fields.add(numberField);
		}
		
		for (Element booleanFieldElement : XmlUtils.getChildElements(fieldsElement, "BooleanField")) {
			SchemaField numberField = createField(FieldType.Boolean, booleanFieldElement);
			fields.add(numberField);
		}
		
		for (Element referenceFieldElement : XmlUtils.getChildElements(fieldsElement, "ReferenceField")) {
			SchemaField referenceField = createField(FieldType.Reference, referenceFieldElement);
			fields.add(referenceField);
		}
		
		return fields;
	}
	
	private static SchemaField createField(FieldType fieldType, Element fieldElement) {
		SchemaField field = new SchemaField();
		field.setType(fieldType);
		
		setFieldName(field, fieldElement);
		
		setReferenceDetails(field, fieldElement);
		
		setRestrictions(field, fieldElement);
		
		return field;
	}
	
	private static void setFieldName(SchemaField field, Element fieldElement) {
		String name = fieldElement.getAttribute("name");
		if (name == null || name.isEmpty()) {
			throw new RuntimeException("Field name empty");
		}
		field.setName(name);		
	}
	
	private static void setReferenceDetails(SchemaField field, Element fieldElement) {
		String relatedTableName = fieldElement.getAttribute("relatedTable");
		if (relatedTableName != null && !relatedTableName.isEmpty()) {
			field.setRelatedTableName(relatedTableName);
		}
		
		String setFieldName = fieldElement.getAttribute("setFieldName");
		if (setFieldName != null && !setFieldName.isEmpty()) {
			field.setInverseFieldName(setFieldName);
		}
	}
	
	private static void setRestrictions(SchemaField field, Element fieldElement){
		Element restrictionsElement = XmlUtils.getChildElement(fieldElement, "Restrictions");
		
		if (restrictionsElement == null) {
			return;
		}
		
		for (Element restrictionElement : XmlUtils.getChildElements(restrictionsElement)) {
			Restriction restriction = createRestriction(restrictionElement);
			field.addRestriction(restriction);
		}
	}
	
	private static Restriction createRestriction(Element restrictionElement) {
		String tagName = restrictionElement.getTagName();
		if ("MinimumCharacterCount".equals(tagName)) {
			Integer minimum = XmlUtils.getIntegerValue(restrictionElement);
			return new MinimumCharacterCount(minimum);
		} else if ("MaximumCharacterCount".equals(tagName)) {
			Integer maximum = XmlUtils.getIntegerValue(restrictionElement);
			return new MaximumCharacterCount(maximum);
		} else if ("MinimumValue".equals(tagName)) {
			Integer minimum = XmlUtils.getIntegerValue(restrictionElement);
			return new MinimumValue(minimum);
		} else if ("MaximumValue".equals(tagName)) {
			Integer maximum = XmlUtils.getIntegerValue(restrictionElement);
			return new MaximumValue(maximum);
		}
		
		return null;
	}
}
