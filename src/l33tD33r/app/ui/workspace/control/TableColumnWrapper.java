package l33tD33r.app.ui.workspace.control;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import l33tD33r.app.database.form.Form;
import l33tD33r.app.database.form.data.Collection;
import l33tD33r.app.database.form.data.Element;
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

    private Collection collection;

    private Column column;

    private List<Element> rows;

    private TableColumn<Element,Object> tableColumn;

    public TableColumnWrapper(Form form, Collection collection, Column column, List<Element> rows) {
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

        tableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Element, Object>, ObservableValue<Object>>() {
            @Override
            public ObservableValue<Object> call(TableColumn.CellDataFeatures<Element, Object> param) {
                Element element = param.getValue();
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

        tableColumn.setCellFactory(new Callback<TableColumn<Element, Object>, TableCell<Element, Object>>() {
            @Override
            public TableCell<Element, Object> call(TableColumn<Element, Object> param) {
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

    public Collection getCollection() {
        return collection;
    }

    public List<Element> getRows() {
        return rows;
    }

    public Column getColumn() {
        return column;
    }

    public TableColumn<Element,Object> getTableColumn() {
        return tableColumn;
    }

    private ItemTemplate getPropertyTemplate() {
        return getCollection().getPropertyTemplate(getColumn().getPropertyId());
    }
}
