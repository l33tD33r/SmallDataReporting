package l33tD33r.app.ui.workspace.control;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import l33tD33r.app.database.form.Form;
import l33tD33r.app.database.form.data.PropertyCollection;
import l33tD33r.app.database.form.data.PropertyElement;
import l33tD33r.app.database.form.data.ItemSource;
import l33tD33r.app.database.form.data.ItemTemplate;
import l33tD33r.app.database.form.view.Column;
import l33tD33r.app.database.form.view.View;

import java.util.List;

/**
 * Created by Simon on 12/15/2014.
 */
public class TableColumnWrapper {

    private Form form;

    private PropertyCollection collection;

    private Column column;

    private List<PropertyElement> rows;

    private TableColumn<PropertyElement,Object> tableColumn;

    public TableColumnWrapper(Form form, PropertyCollection collection, Column column, List<PropertyElement> rows) {
        this.form = form;
        this.collection = collection;
        this.column = column;
        this.rows = rows;

        tableColumn = new TableColumn<>(column.getPropertyId());
        tableColumn.setText(column.getHeader());
        tableColumn.setMinWidth(50);
        tableColumn.setMaxWidth(500);
        tableColumn.setPrefWidth(100);
        tableColumn.setEditable(column.isEditable());

        tableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PropertyElement, Object>, ObservableValue<Object>>() {
            @Override
            public ObservableValue<Object> call(TableColumn.CellDataFeatures<PropertyElement, Object> param) {
                PropertyElement element = param.getValue();
                ItemSource valueSource = element.getProperty(getColumn().getPropertyId());
                Object value = valueSource.getValue();

                ObservableValue observableValue;
                switch (getPropertyTemplate().getType()) {
                    case Boolean:
                        observableValue= new SimpleBooleanProperty(value == null ? false : (Boolean)value);
                        break;
                    default:
                        observableValue = new SimpleObjectProperty(value);
                        break;
                }
                return observableValue;
            }
        });

        tableColumn.setCellFactory(new Callback<TableColumn<PropertyElement, Object>, TableCell<PropertyElement, Object>>() {
            @Override
            public TableCell<PropertyElement, Object> call(TableColumn<PropertyElement, Object> param) {
                TableCellWrapper cellWrapper = null;
                View cellView = getColumn().getCellView();
                if (cellView == null) {
                    cellWrapper = new ObjectTableCellWrapper(TableColumnWrapper.this, getPropertyTemplate().getType());
                } else {
                    switch (cellView.getType()) {
                        case DropDown:
                            cellWrapper = new DataRecordReferenceDropDownTableCellWrapper(TableColumnWrapper.this);
                            break;
                        case IntegerField:
                            cellWrapper = new IntegerFieldTableCellWrapper(TableColumnWrapper.this);
                            break;
                        case BooleanCheckBox:
                            cellWrapper = new BooleanCheckBoxTableCellWrapper(TableColumnWrapper.this);
                            break;
                    }
                }
                return cellWrapper.getTableCell();
            }
        });
    }

    public Form getForm() {
        return form;
    }

    public PropertyCollection getCollection() {
        return collection;
    }

    public List<PropertyElement> getRows() {
        return rows;
    }

    public Column getColumn() {
        return column;
    }

    public TableColumn<PropertyElement,Object> getTableColumn() {
        return tableColumn;
    }

    private ItemTemplate getPropertyTemplate() {
        return getCollection().getPropertyTemplate(getColumn().getPropertyId());
    }
}
