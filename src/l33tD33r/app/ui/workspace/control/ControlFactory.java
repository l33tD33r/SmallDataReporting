package l33tD33r.app.ui.workspace.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import l33tD33r.app.database.data.DataManager;
import l33tD33r.app.database.data.DataTable;
import l33tD33r.app.database.form.view.DropDownView;
import l33tD33r.app.database.form.Form;
import l33tD33r.app.database.form.view.TableDropDownView;
import l33tD33r.app.database.form.view.View;
import l33tD33r.app.ui.workspace.data.DataRecordReference;

import java.util.ArrayList;

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
            default:
                throw new RuntimeException("Unknown view type:" + view.getType().name());
        }

        control.setForm(form);
        control.setView(view);

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
        DropDownWrapper<DataRecordReference, String> dropDown = new DropDownWrapper<>();
        dropDown.setComboBox(createTableComboBox(tableDropDownView.getTable()));
        dropDown.setValueConverter(reference -> reference.getId());
        return dropDown;
    }

    public ComboBox<DataRecordReference> createTableComboBox(String table) {
        ArrayList<DataRecordReference> referenceRecordList = new ArrayList<>();
        DataTable relatedDataTable = DataManager.getSingleton().getTable(table);
        String reportFieldName = relatedDataTable.getSchema().getReportFieldName();
        relatedDataTable.getAllRecords().forEach(
                r -> referenceRecordList.add(new DataRecordReference(r.getId(), r.getFieldValueString(reportFieldName)))
        );

        ObservableList<DataRecordReference> referenceRecordObservableList = FXCollections.observableList(referenceRecordList);
        ComboBox<DataRecordReference> comboBox = new ComboBox<>(referenceRecordObservableList);
        return comboBox;
    }

    public StringTextControlWrapper createTextField() {
        StringTextControlWrapper stringTextControlWrapper = new StringTextControlWrapper();
        stringTextControlWrapper.setControl(new TextField());
        return stringTextControlWrapper;
    }

    public StringTextControlWrapper createTextArea() {
        StringTextControlWrapper stringTextControlWrapper = new StringTextControlWrapper();
        stringTextControlWrapper.setControl(new TextArea());
        return stringTextControlWrapper;
    }
}
