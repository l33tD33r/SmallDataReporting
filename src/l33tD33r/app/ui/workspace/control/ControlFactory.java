package l33tD33r.app.ui.workspace.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import l33tD33r.app.database.data.DataManager;
import l33tD33r.app.database.data.DataTable;
import l33tD33r.app.database.form.view.*;
import l33tD33r.app.database.form.Form;
import l33tD33r.app.ui.workspace.data.DataRecordReference;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Simon on 10/26/2014.
 */
public class ControlFactory {

    private static ControlFactory singleton = new ControlFactory();

    public static ControlFactory getSingleton() {
        return singleton;
    }

    public ControlWrapper createControl(Form form, View view) {
        ControlWrapper control = null;
        switch (view.getType()) {
            case DropDown:
                control = createDropDown(form, (DropDownView)view);
                break;
            case TextField:
                control = createTextField();
                break;
            case TextArea:
                control = createTextArea();
                break;
            case IntegerField:
                control = createIntegerField();
                break;
            case BooleanCheckBox:
                control = createBooleanCheckBox();
                break;
            case DatePicker:
                control = createDatePicker();
                break;
            case Table:
                control = createTable((Table)view);
                break;
            default:
                throw new RuntimeException("Unknown view type:" + view.getType().name());
        }

        control.setForm(form);
        control.setView(view);

        control.setupControl();

        return control;
    }

    public DropDownWrapper createDropDown(Form form, DropDownView dropDownView) {

        DropDownWrapper dropDown = null;

        switch (dropDownView.getSourceType()) {
            case Table:
                dropDown = createTableDropDown((TableDropDownView)dropDownView);
                break;
            default:
                throw new RuntimeException("Unknown source type:" + dropDownView.getSourceType().name());
        }

        return dropDown;
    }

    public DropDownWrapper<DataRecordReference, String> createTableDropDown(TableDropDownView tableDropDownView) {
        final DropDownWrapper<DataRecordReference, String> dropDown = new DropDownWrapper<>();
        dropDown.setComboBox(createTableComboBox(tableDropDownView.getTable()));
        dropDown.setValueConverter(new DataConverter<DataRecordReference, String>() {
            @Override
            public String convertData(DataRecordReference dataRecordReference) {
                return dataRecordReference == null ? null : dataRecordReference.getId();
            }

            @Override
            public DataRecordReference invertData(String s) {
                for (DataRecordReference dataRecordReference : dropDown.getComboBox().getItems()) {
                    if (dataRecordReference.getId().equalsIgnoreCase(s)) {
                        return dataRecordReference;
                    }
                }
                return null;
            }
        });
        return dropDown;
    }

    public ComboBox<DataRecordReference> createTableComboBox(String table) {
        ObservableList<DataRecordReference> referenceRecordObservableList = FXCollections.observableList(createReferenceRecordObservableList(table));
        ComboBox<DataRecordReference> comboBox = new ComboBox<>(referenceRecordObservableList);
        return comboBox;
    }

    public ObservableList<DataRecordReference> createReferenceRecordObservableList(String table) {
        return FXCollections.observableList(createReferenceRecordList(table));
    }

    public ArrayList<DataRecordReference> createReferenceRecordList(String table) {
        ArrayList<DataRecordReference> referenceRecordList = new ArrayList<>();
        DataTable relatedDataTable = DataManager.getSingleton().getTable(table);
        String reportFieldName = relatedDataTable.getSchema().getReportFieldName();
        relatedDataTable.getAllRecords().forEach(
                r -> referenceRecordList.add(new DataRecordReference(r.getId(), r.getFieldValueString(reportFieldName)))
        );
        referenceRecordList.sort((a, b) -> a.getLabel().compareTo(b.getLabel()));
        return referenceRecordList;
    }

    public StringTextControlWrapper createTextField() {
        StringTextControlWrapper stringTextControlWrapper = new StringTextControlWrapper();
        stringTextControlWrapper.setTextControl(new TextField());
        return stringTextControlWrapper;
    }

    public StringTextControlWrapper createTextArea() {
        StringTextControlWrapper stringTextControlWrapper = new StringTextControlWrapper();
        stringTextControlWrapper.setTextControl(new TextArea());
        return stringTextControlWrapper;
    }

    public IntegerTextControlWrapper createIntegerField() {
        IntegerTextControlWrapper integerTextControlWrapper = new IntegerTextControlWrapper();
        integerTextControlWrapper.setTextControl(new TextField());
        return integerTextControlWrapper;
    }

    public BooleanCheckBoxControlWrapper createBooleanCheckBox() {
        BooleanCheckBoxControlWrapper booleanCheckBoxControlWrapper = new BooleanCheckBoxControlWrapper();
        booleanCheckBoxControlWrapper.setCheckBoxControl(new CheckBox());
        return booleanCheckBoxControlWrapper;
    }

    public DatePickerWrapper createDatePicker() {
        DatePickerWrapper datePickerWrapper = new DatePickerWrapper();
        DatePicker datePicker = new DatePicker();
        datePickerWrapper.setDatePicker(datePicker);
        return datePickerWrapper;
    }

    public TableWrapper createTable(Table table) {
        TableWrapper tableWrapper = new TableWrapper();
        tableWrapper.setTable(table);
        return tableWrapper;
    }
}
