package l33tD33r.app.ui.workspace.control;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import l33tD33r.app.database.form.data.PropertyElement;
import l33tD33r.app.database.form.data.ItemSource;

/**
 * Created by Simon on 12/15/2014.
 */
public class ItemPropertyListener<TCellValue> implements ChangeListener<TCellValue> {

    private TableColumnWrapper columnWrapper;

    private TableCell<PropertyElement,TCellValue> tableCell;

    public ItemPropertyListener(TableColumnWrapper columnWrapper, TableCell<PropertyElement,TCellValue> tableCell) {
        this.columnWrapper = columnWrapper;
        this.tableCell = tableCell;
    }

    @Override
    public void changed(ObservableValue<? extends TCellValue> observable, TCellValue oldValue, TCellValue newValue) {
        String propertyId = columnWrapper.getColumn().getPropertyId();

        int index = tableCell.getTableRow().getIndex();

        if (index < 0 || index >= columnWrapper.getRows().size()) {
            return;
        }
        PropertyElement element = columnWrapper.getRows().get(index);
        ItemSource property = element.getProperty(propertyId);

        property.setValue(newValue);
    }
}
