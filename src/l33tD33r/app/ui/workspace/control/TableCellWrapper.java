package l33tD33r.app.ui.workspace.control;

import javafx.scene.control.TableCell;
import l33tD33r.app.database.form.data.PropertyElement;

/**
 * Created by Simon on 12/15/2014.
 */
public abstract class TableCellWrapper<TCellValue> {

    private TableColumnWrapper columnWrapper;

    private TableCell<PropertyElement,TCellValue> tableCell;

    public TableCellWrapper(TableColumnWrapper columnWrapper) {
        this.columnWrapper = columnWrapper;
    }

    public TableColumnWrapper getColumnWrapper() {
        return columnWrapper;
    }

    public TableCell<PropertyElement,TCellValue> getTableCell() {
        if (tableCell == null) {
            tableCell = createTableCell();
        }
        return tableCell;
    }

    protected abstract TableCell<PropertyElement,TCellValue> createTableCell();
}
