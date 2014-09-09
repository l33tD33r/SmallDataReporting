package l33tD33r.app.ui.workspace.report;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;
import l33tD33r.app.database.FieldType;
import l33tD33r.app.database.data.DataManager;
import l33tD33r.app.database.data.DataRecord;
import l33tD33r.app.database.data.DataTable;
import l33tD33r.app.database.query.Query;
import l33tD33r.app.database.query.ResultRow;
import l33tD33r.app.database.query.Row;
import l33tD33r.app.database.report.Report;
import l33tD33r.app.database.report.ReportManager;
import l33tD33r.app.database.schema.SchemaField;
import l33tD33r.app.database.schema.SchemaTable;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Simon on 2/16/14.
 */
public class ReportView {

    private DataTable currentDataTable;

    private Report currentReport;

    private TableView<Map> tableView;

    private ObservableList<Map> data = FXCollections.observableArrayList();

    private Callback<TableColumn<Map,String>,TableCell<Map,String>> cellFactoryForMap;

    public ReportView(TableView<Map> tableView) {
        this.tableView = tableView;

        this.tableView.setItems(data);

        cellFactoryForMap = new Callback<TableColumn<Map, String>, TableCell<Map, String>>() {
            @Override
            public TableCell<Map, String> call(TableColumn<Map, String> mapStringTableColumn) {
                return new TextFieldTableCell(new StringConverter() {
                    @Override
                    public String toString(Object o) {
                        return (o==null) ? "" : o.toString();
                    }

                    @Override
                    public Object fromString(String s) {
                        return s;
                    }
                });
            }
        };
    }

    public void loadTable(String qualifiedTableName) {
        tableView.getColumns().clear();
        data.clear();

        currentDataTable = DataManager.getSingleton().getTable(qualifiedTableName);

        TableColumn<Map, String> rowColumn = new TableColumn<>("Row");
        rowColumn.setMinWidth(50);
        rowColumn.setMaxWidth(200);
        rowColumn.setPrefWidth(100);
        rowColumn.setCellValueFactory(new MapValueFactory<String>("Row"));
        rowColumn.setCellFactory(cellFactoryForMap);
        tableView.getColumns().addAll(rowColumn);

        for (SchemaField field : currentDataTable.getSchema().getAllFields()) {
            if (field.getType() == FieldType.Set) {
                continue;
            }
            TableColumn<Map, String> dataColumn = new TableColumn<>(field.getName());
            dataColumn.setMinWidth(50);
            dataColumn.setMaxWidth(200);
            dataColumn.setPrefWidth(100);
            dataColumn.setCellValueFactory(new MapValueFactory<String>(field.getName()));
            dataColumn.setCellFactory(cellFactoryForMap);

            tableView.getColumns().add(dataColumn);
        }

        ArrayList<Map<String,String>> newData = new ArrayList<>();

        int row = 0;
        for (DataRecord dataRecord : currentDataTable.getAllRecords()) {
            Map<String, String> dataRow = new HashMap<>();

            dataRow.put("Row", Integer.toString(++row));

            for (SchemaField field : currentDataTable.getSchema().getAllFields()) {
                if (field.getType() == FieldType.Set) {
                    continue;
                }
                String fieldName = field.getName();
                String fieldValue = getFieldValue(field, dataRecord);
                dataRow.put(fieldName, fieldValue);
            }

            newData.add(dataRow);
        }

        data.addAll(newData);
    }

    private String getFieldValue(SchemaField field, DataRecord dataRecord) {
        if (field.getType() == FieldType.Reference) {
            List<SchemaField> reportFields = field.getRelatedTable().getReportFields();
            if (reportFields.size() == 0) {
                throw new RuntimeException(MessageFormat.format("Table ''{0}'' does not have any report fields specified", field.getRelatedTableName()));
            }

            StringBuilder sb = new StringBuilder();

            boolean addedFieldValue = false;

            for (SchemaField reportField : reportFields) {
                if (addedFieldValue) {
                    sb.append("|");
                } else {
                    addedFieldValue = true;
                }
                DataRecord referenceRecord = dataRecord.getFieldReference(field.getName());
                if (referenceRecord != null) {
                    sb.append(getFieldValue(reportField, referenceRecord));
                }
            }

            return sb.toString();
        } else {
            return  dataRecord.getFieldValueString(field.getName());
        }
    }

    private SchemaField getReferenceDisplayField(SchemaTable referenceTable) {
        String tableName= referenceTable.getName();
        String fieldName;
        if ("Game".equals(tableName)) {
            fieldName = "Name";
        } else if ("Location".equals(tableName)) {
            fieldName = "Name";
        } else if ("Player".equals(tableName)) {
            fieldName = "Name";
        } else if ("GameInstance".equals(tableName)) {
            fieldName = "DatePlayed";
        } else if ("GamePlayer".equals(tableName)) {
            fieldName = "Player";
        } else if ("SettlersOfCatan_GameInstance".equals(tableName)) {
            fieldName = "GameInstance";
        } else if ("SettlersOfCatan_GamePlayer".equals(tableName)) {
            fieldName = "GamePlayer";
        } else if ("SettlersOfCatan_Color".equals(tableName)) {
            fieldName = "Name";
        } else {
            throw new RuntimeException("Unknown Table:" + tableName);
        }

        return referenceTable.getField(fieldName);
    }


    public void loadReport(String reportName) {
        tableView.getColumns().clear();
        data.clear();

        if (reportName == null) {
            return;
        }

        currentReport = ReportManager.getSingleton().getReport(reportName);

        TableColumn<Map, String> rowColumn = new TableColumn<>("Row");
        rowColumn.setMinWidth(50);
        rowColumn.setMaxWidth(200);
        rowColumn.setPrefWidth(100);
        rowColumn.setCellValueFactory(new MapValueFactory<String>("Row"));
        rowColumn.setCellFactory(cellFactoryForMap);
        tableView.getColumns().addAll(rowColumn);

        for (int columnIndex=0; columnIndex < currentReport.getColumnCount(); columnIndex++) {
            TableColumn<Map, String> dataColumn = new TableColumn<>(currentReport.getColumnHeader(columnIndex));
            dataColumn.setMinWidth(50);
            dataColumn.setMaxWidth(200);
            dataColumn.setPrefWidth(100);
            dataColumn.setCellValueFactory(new MapValueFactory<String>(currentReport.getColumnName(columnIndex)));
            dataColumn.setCellFactory(cellFactoryForMap);

            tableView.getColumns().add(dataColumn);
        }

        ArrayList<Map<String,String>> newData = new ArrayList<>();

        Query query = currentReport.getQuery();

        for (int rowIndex=0; rowIndex < query.getRowCount(); rowIndex++) {
            query.setPosition(rowIndex);

            ResultRow currentRow = query.getCurrentRow();

            Map<String, String> dataRow = new HashMap<>();

            dataRow.put("Row", Integer.toString(rowIndex+1));

            for (int columnIndex=0; columnIndex < currentReport.getColumnCount(); columnIndex++) {
                String columnName = currentReport.getColumnName(columnIndex);
                String columnValue = currentRow.getValue(columnIndex).toString();

                dataRow.put(columnName, columnValue);
            }

            newData.add(dataRow);
        }

        data.addAll(newData);
    }

    public DataTable getCurrentDataTable() {return currentDataTable; }

    public Report getCurrentReport() {
        return currentReport;
    }
}
