package l33tD33r.app.ui.workspace.control;

import javafx.scene.control.TableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import l33tD33r.app.database.form.data.DataType;
import l33tD33r.app.database.form.data.PropertyElement;

/**
 * Created by Simon on 12/15/2014.
 */
public class IntegerFieldTableCellWrapper extends TableCellWrapper<Integer> {

    public IntegerFieldTableCellWrapper(TableColumnWrapper columnWrapper) {
        super(columnWrapper);
    }

    @Override
    protected TableCell<PropertyElement, Integer> createTableCell() {
        TextFieldTableCell textFieldTableCell = new TextFieldTableCell(new DataStringConverter(DataType.Integer));
        textFieldTableCell.itemProperty().addListener(new ItemPropertyListener(getColumnWrapper(), textFieldTableCell));
        return textFieldTableCell;
    }
}
