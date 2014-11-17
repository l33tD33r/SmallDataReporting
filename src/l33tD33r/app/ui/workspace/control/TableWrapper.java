package l33tD33r.app.ui.workspace.control;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.Callback;
import l33tD33r.app.database.form.Form;
import l33tD33r.app.database.form.data.Collection;
import l33tD33r.app.database.form.data.Element;
import l33tD33r.app.database.form.view.Column;
import l33tD33r.app.database.form.view.Table;
import l33tD33r.app.ui.workspace.data.DataRecordReference;

import java.util.ArrayList;

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

        private TableColumn<Element,DataRecordReference> tableColumn;

        public ColumnWrapper(Form form, Column column) {
            this.form = form;
            this.column = column;

            tableColumn = new TableColumn<>();
            tableColumn.setText(column.getHeader());
            tableColumn.setMinWidth(50);
            tableColumn.setMaxWidth(500);
            tableColumn.setPrefWidth(100);
            tableColumn.setEditable(true);

            tableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Element, DataRecordReference>, ObservableValue<DataRecordReference>>() {
                @Override
                public ObservableValue<DataRecordReference> call(TableColumn.CellDataFeatures<Element, DataRecordReference> param) {
                    return new SimpleObjectProperty<DataRecordReference>();
                }
            });

            tableColumn.setCellFactory(new Callback<TableColumn<Element, DataRecordReference>, TableCell<Element, DataRecordReference>>() {
                @Override
                public TableCell<Element, DataRecordReference> call(TableColumn<Element, DataRecordReference> param) {
//                    return new PropertyTableCell(form, ColumnWrapper.this);
                    ComboBoxTableCellWrapper cellWrapper = new ComboBoxTableCellWrapper(form, ColumnWrapper.this);
                    return cellWrapper.getComboBoxTableCell();
                }
            });
        }

        public Column getColumn() {
            return column;
        }

        public TableColumn<Element,DataRecordReference> getTableColumn() {
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
        public void commitEdit(Object newValue) {
            super.commitEdit(newValue);
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setGraphic(null);
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

    private static class ComboBoxTableCellWrapper {

        private Form form;

        private ColumnWrapper columnWrapper;

        private DropDownWrapper<DataRecordReference, String> dropDownWrapper;

        private ComboBoxTableCell<Element,DataRecordReference> tableCell;

        public ComboBoxTableCellWrapper(Form form, ColumnWrapper columnWrapper) {
            super();
            this.form = form;
            this.columnWrapper = columnWrapper;

            dropDownWrapper = (DropDownWrapper<DataRecordReference, String>)ControlFactory.getSingleton().createControl(form, columnWrapper.getColumn().getCellView());

            tableCell = new ComboBoxTableCell<>(dropDownWrapper.getComboBox().getItems());
        }

        public ComboBoxTableCell<Element,DataRecordReference> getComboBoxTableCell() {
            return tableCell;
        }
    }
}
