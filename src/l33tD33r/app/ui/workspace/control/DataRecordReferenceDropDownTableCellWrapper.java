package l33tD33r.app.ui.workspace.control;

import javafx.scene.control.TableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import l33tD33r.app.database.form.data.Element;
import l33tD33r.app.database.form.view.TableDropDownView;
import l33tD33r.app.ui.workspace.data.DataRecordReference;

import java.util.ArrayList;

/**
 * Created by Simon on 12/15/2014.
 */
public class DataRecordReferenceDropDownTableCellWrapper extends TableCellWrapper<DataRecordReference> {

    public DataRecordReferenceDropDownTableCellWrapper(TableColumnWrapper columnWrapper) {
        super(columnWrapper);
    }

    @Override
    protected TableCell<Element, DataRecordReference> createTableCell() {
        TableDropDownView tableDropDownView = (TableDropDownView)getColumnWrapper().getColumn().getCellView();
        ArrayList<DataRecordReference> dataRecordReferences = ControlFactory.getSingleton().createReferenceRecordList(tableDropDownView.getTable());
        DataRecordReference[] dataRecordReferencesArray = new DataRecordReference[dataRecordReferences.size()];
        ComboBoxTableCell<Element, DataRecordReference> comboBoxTableCell = new ComboBoxTableCell<>(new ReferenceStringConverter(dataRecordReferences), dataRecordReferences.toArray(dataRecordReferencesArray));
        comboBoxTableCell.itemProperty().addListener(new ItemPropertyListener(getColumnWrapper(), comboBoxTableCell));
        return comboBoxTableCell;
    }
}
