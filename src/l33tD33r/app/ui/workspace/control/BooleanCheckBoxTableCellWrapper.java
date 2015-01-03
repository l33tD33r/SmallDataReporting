package l33tD33r.app.ui.workspace.control;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import l33tD33r.app.database.form.data.PropertyElement;

/**
 * Created by Simon on 12/15/2014.
 */
public class BooleanCheckBoxTableCellWrapper extends TableCellWrapper<Boolean> {

    public BooleanCheckBoxTableCellWrapper(TableColumnWrapper columnWrapper) {
        super(columnWrapper);
    }

    @Override
    protected TableCell<PropertyElement, Boolean> createTableCell() {
        final ObservableValue<Boolean> observableBoolean = new SimpleBooleanProperty();
        CheckBoxTableCell<PropertyElement,Boolean> checkBoxTableCell = new CheckBoxTableCell<PropertyElement,Boolean>(
                (Integer param) -> observableBoolean
        );
        observableBoolean.addListener(new ItemPropertyListener<Boolean>(getColumnWrapper(), checkBoxTableCell));
        return checkBoxTableCell;
    }
}
