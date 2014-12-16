package l33tD33r.app.ui.workspace.control;

import javafx.scene.control.TableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import l33tD33r.app.database.form.data.DataType;
import l33tD33r.app.database.form.data.Element;

/**
 * Created by Simon on 12/15/2014.
 */
public class ObjectTableCellWrapper extends TableCellWrapper<Object> {

    private DataType dataType;

    public ObjectTableCellWrapper(TableColumnWrapper columnWrapper, DataType dataType) {
        super(columnWrapper);
        this.dataType = dataType;
    }

    @Override
    protected TableCell<Element, Object> createTableCell() {
        TextFieldTableCell textFieldTableCell = new TextFieldTableCell();
        textFieldTableCell.setConverter(new DataStringConverter(dataType));
        textFieldTableCell.itemProperty().addListener(new ItemPropertyListener(getColumnWrapper(), textFieldTableCell));
        return textFieldTableCell;
    }
}
