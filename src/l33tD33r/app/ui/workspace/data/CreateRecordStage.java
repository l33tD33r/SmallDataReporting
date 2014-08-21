package l33tD33r.app.ui.workspace.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import l33tD33r.app.database.*;
import l33tD33r.app.database.Date;
import l33tD33r.app.database.data.DataManager;
import l33tD33r.app.database.data.DataRecord;
import l33tD33r.app.database.data.DataRecordExistsException;
import l33tD33r.app.database.data.DataTable;
import l33tD33r.app.database.schema.SchemaField;
import l33tD33r.app.ui.workspace.report.ReportView;

import java.time.LocalDate;
import java.util.*;

/**
 * Created by Simon on 8/20/2014.
 */
public class CreateRecordStage extends Stage {

    private ReportView reportView;

    private DataTable dataTable;

    private Map<String,Control> controlMap;

    public CreateRecordStage(ReportView reportView) {
        super(StageStyle.DECORATED);

        this.reportView = reportView;

        dataTable = reportView.getCurrentDataTable();

        setTitle("Create Record - " + dataTable.getName());
        setScene(createScene());
    }

    private Scene createScene() {
        BorderPane rootNode = new BorderPane();
        GridPane fieldsPanel = new GridPane();

        fieldsPanel.setPadding(new Insets(5, 5, 5, 5));
        fieldsPanel.setHgap(5);
        fieldsPanel.setVgap(5);
        rootNode.setCenter(fieldsPanel);

        int row = 0;

        controlMap = new HashMap<>();

        for (SchemaField field : dataTable.getSchema().getAllFields()) {
            if (field.getType() == FieldType.Set) {
                continue;
            }

            Label fieldLabel = new Label(field.getName() + ":");

            Control fieldControl = createFieldControl(field);

            controlMap.put(field.getName(), fieldControl);

            fieldsPanel.add(fieldLabel, 0, row);
            fieldsPanel.add(fieldControl, 1, row);

            row++;
        }

        HBox buttonPanel = new HBox();
        buttonPanel.setPadding(new Insets(5, 5, 5, 5));

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            createRecord();

            reportView.loadTable(dataTable.getName());

//            clearControlValues();
        });
        saveButton.setAlignment(Pos.CENTER_LEFT);
//        saveButton.setPadding(new Insets(5, 5, 0, 0));

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> {
            close();
        });
        closeButton.setAlignment(Pos.CENTER_RIGHT);
//        cancelButton.setPadding(new Insets(5, 5, 0, 0));

        buttonPanel.getChildren().addAll(saveButton, closeButton);

//        fieldsPanel.add(buttonPanel, 0, 2, row, 1);
        rootNode.setBottom(buttonPanel);

        return new Scene(rootNode);
    }

    private Control createFieldControl(SchemaField field) {
        switch (field.getType()) {
            case Boolean: {
                return new CheckBox();
            }
            case String: {
                return new TextField();
            }
            case Date: {
                return new DatePicker();
            }
            case Reference: {
                return createComboBox(field);
            }
            default: {
                return new Label("Unsupported type: " + field.getType().name());
            }
        }
    }

    private ComboBox<DataRecordReference> createComboBox(SchemaField field) {
        ArrayList<DataRecordReference> referenceRecordList = new ArrayList<>();
        DataTable relatedDataTable = DataManager.getSingleton().getTable(field.getRelatedTableName());
        String reportFieldName = relatedDataTable.getSchema().getReportFieldName();
        relatedDataTable.getAllRecords().forEach(
                r -> referenceRecordList.add(new DataRecordReference(r.getId(), r.getFieldValueString(reportFieldName)))
        );

        ObservableList<DataRecordReference> referenceRecordObservableList = FXCollections.observableList(referenceRecordList);
        ComboBox<DataRecordReference> comboBox = new ComboBox<>(referenceRecordObservableList);
        return comboBox;
    }

    private void createRecord() {
        DataRecord newRecord = dataTable.createNewRecord();

        for (Map.Entry<String,Control> entry : controlMap.entrySet()) {
            String fieldName = entry.getKey();
            Control fieldControl = entry.getValue();

            Object value = null;

            if (fieldControl instanceof CheckBox) {
                value = Boolean.valueOf(((CheckBox) fieldControl).isSelected());
            } else if (fieldControl instanceof TextField) {
                value = ((TextField)fieldControl).getText();
            } else if (fieldControl instanceof DatePicker) {
                value = l33tD33r.app.database.Date.valueOf(((DatePicker) fieldControl).getValue().toString());
            } else if (fieldControl instanceof ComboBox) {
                value = ((ComboBox<DataRecordReference>)fieldControl).getValue().getId();
            } else {
                // Ignore
            }

            if (value == null) {
                continue;
            }
            newRecord.getField(fieldName).setValue(value);
        }

        try {
            dataTable.insertRecord(newRecord);
        } catch (DataRecordExistsException exception) {
            throw new RuntimeException("Insert record failed", exception);
        }
    }

//    private void clearControlValues() {
//        controlMap.values().forEach( c-> {
//            if (c instanceof TextField) {
//                ((TextField)c).setText("");
//            } else if (c instanceof CheckBox) {
//                ((CheckBox)c).setSelected(false);
//            } else if (c instanceof DatePicker) {
//                ((DatePicker)c).setValue(LocalDate.parse(Date.today().toString()));
//            } else if (c instanceof ComboBox) {
//                ((ComboBox<DataRecordReference>)c).setValue(null);
//            }
//        });
//    }
}
