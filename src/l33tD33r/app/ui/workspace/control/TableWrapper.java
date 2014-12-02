package l33tD33r.app.ui.workspace.control;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
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
import l33tD33r.app.database.form.data.ItemSource;
import l33tD33r.app.database.form.view.*;
import l33tD33r.app.ui.workspace.data.DataRecordReference;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

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

            ColumnWrapper columnWrapper = new ColumnWrapper(getForm(), column, rows);

            columnWrappers.add(columnWrapper);

            tableView.getColumns().add(columnWrapper.getTableColumn());
        }

        Collection collection = getForm().getCollection(getTable().getCollectionId());
        rows.addAll(collection.getElements());

        setControl(tableView);
    }

    @Override
    public void updateValue() {
//        Collection collection = getForm().getCollection(getTable().getCollectionId());
//
//        for (int i=0; i < rows.size(); i++) {
//            Element element = rows.get(i);
//
//            for (ColumnWrapper columnWrapper : columnWrappers) {
//
//                Column column = columnWrapper.getColumn();
//
//                ItemSource property = element.getProperty(column.getPropertyId());
//
//                Object value = columnWrapper.getTableColumn().getCellData(element);
//
//                //ObservableValue<Object> observableValue = columnWrapper.getTableColumn().getCellObservableValue(element);
//            }
//        }
    }

    private static class ColumnWrapper {

        private Form form;

        private Column column;

        private List<Element> rows;

        private TableColumn<Element,Object> tableColumn;

        public ColumnWrapper(Form form, Column column, List<Element> rows) {
            this.form = form;
            this.column = column;
            this.rows = rows;

            tableColumn = new TableColumn<>();
            tableColumn.setText(column.getHeader());
            tableColumn.setMinWidth(50);
            tableColumn.setMaxWidth(500);
            tableColumn.setPrefWidth(100);
            tableColumn.setEditable(column.isEditable());

            tableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Element, Object>, ObservableValue<Object>>() {
                @Override
                public ObservableValue<Object> call(TableColumn.CellDataFeatures<Element, Object> param) {
                    return new SimpleObjectProperty<Object>();
                }
            });

            tableColumn.setCellFactory(new Callback<TableColumn<Element, Object>, TableCell<Element, Object>>() {
                @Override
                public TableCell<Element, Object> call(TableColumn<Element, Object> param) {
                    TableCellWrapper cellWrapper = new TableCellWrapper(ColumnWrapper.this);
                    return cellWrapper.getTableCell();
                }
            });
        }

        public Form getForm() {
            return form;
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

    private static class TableCellWrapper {
        private ColumnWrapper columnWrapper;

        private TableCell<Element, Object> tableCell;

        public TableCellWrapper(ColumnWrapper columnWrapper) {
            this.columnWrapper = columnWrapper;
        }

        public TableCell<Element,Object> getTableCell() {
            if (tableCell == null) {
                tableCell = createTableCell();
            }
            return tableCell;
        }

        private TableCell<Element,Object> createTableCell() {
            final TableCell<Element,Object> tableCell;

            View view = columnWrapper.getColumn().getCellView();
            if (view == null) {
//                throw new RuntimeException(MessageFormat.format("Cannot create a TableCell for Column {0} because it is not editable", columnWrapper.getColumn().getPropertyId()));
                tableCell = new TableCell<>();
            } else {
                switch (view.getType()) {
                    case DropDown:
                        TableDropDownView dropDownView = (TableDropDownView) view;
                        ArrayList<DataRecordReference> dataRecordReferences = ControlFactory.getSingleton().createReferenceRecordList(dropDownView.getTable());
                        ComboBoxTableCell<Element, Object> comboBoxTableCell = new ComboBoxTableCell<>(dataRecordReferences.toArray());
                        comboBoxTableCell.setItem(dataRecordReferences.get(0));
                        tableCell = comboBoxTableCell;
                        break;
                    default:
                        throw new RuntimeException(MessageFormat.format("Unknown table cell view type {0}", columnWrapper.getColumn().getCellView().getType().name()));
                }
            }
            tableCell.itemProperty().addListener(new ChangeListener<Object>() {
                @Override
                public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
                    String propertyId = columnWrapper.getColumn().getPropertyId();

                    int index = tableCell.getTableRow().getIndex();

                    if (index >= columnWrapper.getRows().size()) {
                        return;
                    }
                    Element element = columnWrapper.getRows().get(index);
                    ItemSource property = element.getProperty(propertyId);
                    property.setValue(newValue);
                }
            });

            return tableCell;
        }
    }
}
