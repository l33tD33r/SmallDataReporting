package l33tD33r.app.ui.workspace.control;

import javafx.scene.control.TableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import l33tD33r.app.database.form.data.DataType;
import l33tD33r.app.database.form.data.Element;

/**
 * Created by Simon on 12/15/2014.
 */
public class BooleanCheckBoxTableCellWrapper extends TableCellWrapper<Boolean> {

    public BooleanCheckBoxTableCellWrapper(TableColumnWrapper columnWrapper) {
        super(columnWrapper);
    }

    @Override
    protected TableCell<Element, Boolean> createTableCell() {
        CheckBoxTableCell<Element,Boolean> checkBoxTableCell = new CheckBoxTableCell<>();
        checkBoxTableCell.setConverter(new DataStringConverter(DataType.Boolean));
        return checkBoxTableCell;
    }
}
