package l33tD33r.app.database.data;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import l33tD33r.app.database.FieldType;
import l33tD33r.app.database.utility.XmlUtils;

public class DataSerialization {

    public static List<DataTable> deserializeData(Document dataDocument) {
        Element dataElement = dataDocument.getDocumentElement();

        Element dataTablesElement = XmlUtils.getChildElement(dataElement, "DataTables");

        ArrayList<DataTable> dataTables = new ArrayList<DataTable>();

        for (Element dataTableElement : XmlUtils.getChildElements(dataTablesElement, "DataTable")) {
            dataTables.add(deserializeDataTable(dataTableElement));
        }

        return dataTables;
    }

    private static DataTable deserializeDataTable(Element dataTableElement) {

        String dataTableName = dataTableElement.getAttribute("name");
        DataTable dataTable = new DataTable(dataTableName);

        Element dataRecordsElement = XmlUtils.getChildElement(dataTableElement, "DataRecords");

        for (Element dataRecordElement : XmlUtils.getChildElements(dataRecordsElement, "DataRecord")) {

            String id = dataRecordElement.getAttribute("id");

            DataRecord dataRecord;
            if (id == null || id.isEmpty()) {
                dataRecord = dataTable.createNewRecord();
            } else {
                dataRecord = dataTable.createExistingRecord(id);
            }

            Element dataFieldsElement = XmlUtils.getChildElement(dataRecordElement, "DataFields");

            for (Element dataFieldElement : XmlUtils.getChildElements(dataFieldsElement, "DataField")) {
                String name = dataFieldElement.getAttribute("name");
                DataField dataField = dataRecord.getField(name);

                String valueString = XmlUtils.getStringValue(dataFieldElement);
                dataField.setValueString(valueString);
            }

            dataTable.loadDataRecord(dataRecord);
        }

        return dataTable;
    }

    public static Document serializeDataTables(List<DataTable> dataTables) {
        Document dataDocument = XmlUtils.createDocument();

        Element dataElement = dataDocument.createElement("Data");
        dataDocument.appendChild(dataElement);

        Element dataTablesElement = dataDocument.createElement("DataTables");
        dataElement.appendChild(dataTablesElement);

        for (DataTable dataTable : dataTables) {
            Element dataTableElement = serializeDataTable(dataDocument, dataTable);
            dataTablesElement.appendChild(dataTableElement);
        }

        return dataDocument;
    }

    private static Element serializeDataTable(Document dataDocument, DataTable dataTable) {

        Element dataTableElement = dataDocument.createElement("DataTable");
        dataTableElement.setAttribute("name", dataTable.getName());

        Element dataRecordsElement = dataDocument.createElement("DataRecords");
        dataTableElement.appendChild(dataRecordsElement);

        for (DataRecord dataRecord : dataTable) {

            Element dataRecordElement = dataDocument.createElement("DataRecord");
            dataRecordElement.setAttribute("id", dataRecord.getId());
            dataRecordsElement.appendChild(dataRecordElement);

            Element dataFieldsElement = dataDocument.createElement("DataFields");
            dataRecordElement.appendChild(dataFieldsElement);

            for (DataField dataField : dataRecord) {
                if (dataField.getType() == FieldType.Set) {
                    continue;
                }
                Element dataFieldElement = dataDocument.createElement("DataField");
                dataFieldElement.setAttribute("name", dataField.getName());
                XmlUtils.writeStringValue(dataDocument, dataFieldElement, dataField.getValueString());
                dataFieldsElement.appendChild(dataFieldElement);
            }
        }

        return dataTableElement;
    }

    public static DataTable createEmptyDataTable(String dataTableName) {
        return deserializeDataTable(createEmptyDataTableDocument(dataTableName));
    }

    private static Element createEmptyDataTableDocument(String dataTableName) {
        Document dataTableDocument = XmlUtils.createDocument();

        Element dataTableElement = dataTableDocument.createElement("DataTable");
        dataTableElement.setAttribute("name", dataTableName);
        dataTableDocument.appendChild(dataTableElement);

        Element dataRecordsElement = dataTableDocument.createElement("DataRecords");
        dataTableElement.appendChild(dataRecordsElement);

        return dataTableElement;
    }
}
