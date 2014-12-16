package l33tD33r.app.ui.workspace.control;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;
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
        final ObservableValue<Boolean> observableBoolean = new SimpleBooleanProperty();
        CheckBoxTableCell<Element,Boolean> checkBoxTableCell = new CheckBoxTableCell<Element,Boolean>(
                (Integer param) -> observableBoolean
        );
        observableBoolean.addListener(new ItemPropertyListener<Boolean>(getColumnWrapper(), checkBoxTableCell));
        return checkBoxTableCell;
    }
}
