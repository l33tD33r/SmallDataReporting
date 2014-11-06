package l33tD33r.app.ui.workspace.control;

import javafx.beans.Observable;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.util.Callback;
import l33tD33r.app.database.form.Form;
import l33tD33r.app.database.form.data.Collection;
import l33tD33r.app.database.form.data.Element;
import l33tD33r.app.database.form.view.Column;
import l33tD33r.app.database.form.view.Table;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Simon on 2014-10-31.
 */
public class TableWrapper extends CollectionControlWrapper {

    private TableView<Element> tableView;

    private ObservableList<Element> rows;

    private ArrayList<ColumnWrapper> columnWrappers;

    public TableWrapper() {
        tableView = new TableView<>();
        rows = FXCollections.observableArrayList();
        columnWrappers = new ArrayList<>();

        tableView.setEditable(true);
        tableView.setItems(rows);
    }
    public Table getTable() {
        return (Table)getView();
    }
    public void setTable(Table table) {
        setView(table);
    }

    @Override
    public void setupControl() {

        for (Column column : getTable().getColumns()) {

            ColumnWrapper columnWrapper = new ColumnWrapper(getForm(), column);

            columnWrappers.add(columnWrapper);

            tableView.getColumns().add(columnWrapper.getTableColumn());
        }

        Collection collection = getForm().getCollection(getTable().getCollectionId());

        for (int i=0; i<6; i++) {
            collection.addElement();
        }

        rows.addAll(collection.getElements());

        setControl(tableView);
    }
    private static class ColumnWrapper {

        private Form form;

        private Column column;

        private TableColumn<Element,Object> tableColumn;

        public ColumnWrapper(Form form, Column column) {
            this.form = form;
            this.column = column;

            tableColumn = new TableColumn<>();
            tableColumn.setText(column.getHeader());
            tableColumn.setMinWidth(50);
            tableColumn.setMaxWidth(500);
            tableColumn.setPrefWidth(100);
            tableColumn.setEditable(true);

            tableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Element, Object>, ObservableValue<Object>>() {
                @Override
                public ObservableValue<Object> call(TableColumn.CellDataFeatures<Element, Object> param) {
                    return new SimpleObjectProperty<Object>();
                }
            });

            tableColumn.setCellFactory(new Callback<TableColumn<Element, Object>, TableCell<Element, Object>>() {
                @Override
                public TableCell<Element, Object> call(TableColumn<Element, Object> param) {
                    return new PropertyTableCell(form, ColumnWrapper.this);
                }
            });
        }

        public Column getColumn() {
            return column;
        }

        public TableColumn<Element,Object> getTableColumn() {
            return tableColumn;
        }
    }

    private static class PropertyTableCell extends TableCell<Element,Object> {

        private Form form;

        private ColumnWrapper columnWrapper;

        private ItemControlWrapper controlWrapper;

        public PropertyTableCell(Form form, ColumnWrapper columnWrapper) {
            this.form = form;
            this.columnWrapper = columnWrapper;

            controlWrapper = (ItemControlWrapper)ControlFactory.getSingleton().createControl(form, columnWrapper.getColumn().getCellView());
        }

        @Override
        public void startEdit() {
            super.startEdit();

            setText(null);
            setGraphic(controlWrapper.getControl());
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setGraphic(null);
        }

        @Override
        public void commitEdit(Object newValue) {
            super.commitEdit(newValue);
        }

        @Override
        protected void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);

            Object value = controlWrapper.getValue();

            if (value != null) {
                setText(value.toString());
            }
        }
    }
}
