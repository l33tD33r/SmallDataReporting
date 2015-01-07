package l33tD33r.app.ui.workspace.control;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import l33tD33r.app.database.form.Form;
import l33tD33r.app.database.form.data.*;
import l33tD33r.app.database.form.view.*;
import l33tD33r.app.ui.workspace.data.DataRecordReference;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 2014-10-31.
 */
public class TableWrapper extends CollectionControlWrapper {

    private TableView<PropertyElement> tableView;

    private ObservableList<PropertyElement> rows;

    private ArrayList<TableColumnWrapper> columnWrappers;

    private PropertyCollection collection;

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

    public PropertyCollection getCollection() {
        if (collection == null) {
            collection = (PropertyCollection)getForm().getCollection(getTable().getCollectionId());
        }
        return collection;
    }

    @Override
    public void setupControl() {

        for (Column column : getTable().getColumns()) {

            TableColumnWrapper columnWrapper = new TableColumnWrapper(getForm(), getCollection(), column, rows);

            columnWrappers.add(columnWrapper);

            tableView.getColumns().add(columnWrapper.getTableColumn());
        }

        PropertyCollection collection = getCollection();

        for (Element element : collection.getElements()) {
            rows.add((PropertyElement)element);
        }

        setControl(tableView);
    }

    @Override
    public Region createRegion() {
        VBox tableBox = new VBox();
        tableBox.setSpacing(0);

        Label label = new Label(getLabel() + ":");

        tableBox.getChildren().addAll(label, getControl());

        if (getTable().isAllowAdd() || getTable().isAllowRemove()) {

            HBox buttonBox = new HBox();
            buttonBox.setSpacing(10);
            buttonBox.setPadding(new Insets(5, 0, 0, 0));

            if (getTable().isAllowAdd()) {
                Button addButton = new Button("Add");
                addButton.setOnAction( e-> {
                    PropertyCollection collection = getCollection();
                    collection.addElement();
                    if (collection.getElements().size() > rows.size()) {
                        rows.add((PropertyElement)collection.getElement(rows.size()));
                    }
                });

                buttonBox.getChildren().add(addButton);
            }

            if (getTable().isAllowRemove()) {
                Button removeButton = new Button("Remove");
                removeButton.setOnAction(e -> {
                    PropertyCollection collection = getCollection();
                    collection.removeElement(collection.getSize()-1);
                    if (rows.size() > collection.getSize()) {
                        rows.remove(rows.size()-1);
                    }
                });

                buttonBox.getChildren().add(removeButton);
            }

            tableBox.getChildren().add(buttonBox);
        }
        return tableBox;
    }

    @Override
    public void applyControlValue() {
//        Collection collection = getForm().getCollection(getTable().getCollectionId());
//
//        for (int i=0; i < rows.size(); i++) {
//            Element element = rows.get(i);
//
//            for (TableColumnWrapper columnWrapper : columnWrappers) {
//
//                Column column = columnWrapper.getColumn();
//
//                ItemSource property = element.getProperty(column.getPropertyId());
//
//                Object value = columnWrapper.getTableColumn().getCellData(element);
//
//                ObservableValue observableValue = columnWrapper.getTableColumn().getCellObservableValue(element);
//
//            }
//
//        }
    }

    private static class ColumnWrapper {

        private Form form;

        private PropertyCollection collection;

        private Column column;

        private List<PropertyElement> rows;

        private TableColumn<PropertyElement,Object> tableColumn;

        public ColumnWrapper(Form form, PropertyCollection collection, Column column, List<PropertyElement> rows) {
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
                    ObservableValue<Object> observableValue = new SimpleObjectProperty<Object>(value);
                    return observableValue;
                }
            });

            tableColumn.setCellFactory(new Callback<TableColumn<PropertyElement, Object>, TableCell<PropertyElement, Object>>() {
                @Override
                public TableCell<PropertyElement, Object> call(TableColumn<PropertyElement, Object> param) {
                    TableCellWrapper cellWrapper = null;
                    switch (getPropertyTemplate().getType()) {
                        case String:
                            cellWrapper = new TableCellWrapper(ColumnWrapper.this);
                            break;
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

    private static class TableCellWrapper {
        private ColumnWrapper columnWrapper;

        private TableCell<PropertyElement, Object> tableCell;

        public TableCellWrapper(ColumnWrapper columnWrapper) {
            this.columnWrapper = columnWrapper;
        }

        public TableCell<PropertyElement,Object> getTableCell() {
            if (tableCell == null) {
                tableCell = createTableCell();
            }
            return tableCell;
        }

        private TableCell<PropertyElement,Object> createTableCell() {
            final TableCell<PropertyElement,Object> tableCell;

            StringConverter converter = new StringConverter() {
                @Override
                public String toString(Object o) {
                    return (o==null) ? "" : o.toString();
                }

                @Override
                public Object fromString(String s) {
                    return s;
                }
            };

            final View view = columnWrapper.getColumn().getCellView();
            if (view == null) {
//                throw new RuntimeException(MessageFormat.format("Cannot create a TableCell for Column {0} because it is not editable", columnWrapper.getColumn().getPropertyId()));
                tableCell = new TextFieldTableCell(converter);
            } else {
                switch (view.getType()) {
                    case DropDown:
                        TableDropDownView dropDownView = (TableDropDownView) view;
                        ArrayList<DataRecordReference> dataRecordReferences = ControlFactory.getSingleton().createReferenceRecordList(dropDownView.getTable());
                        ComboBoxTableCell<PropertyElement, Object> comboBoxTableCell = new ComboBoxTableCell<PropertyElement, Object>(new ReferenceStringConverter(dataRecordReferences), dataRecordReferences.toArray());
                        comboBoxTableCell.itemProperty().addListener(new ItemPropertyListener(columnWrapper, comboBoxTableCell));
                        tableCell = comboBoxTableCell;
                        break;
                    case IntegerField:
                        IntegerFieldView integerFieldView = (IntegerFieldView)view;
                        TextFieldTableCell textFieldTableCell = new TextFieldTableCell(new DataStringConverter(DataType.Integer));
                        textFieldTableCell.itemProperty().addListener(new ItemPropertyListener(columnWrapper, textFieldTableCell));
                        tableCell = textFieldTableCell;
                        break;
                    case BooleanCheckBox:
                        BooleanCheckBoxView booleanCheckBoxView = (BooleanCheckBoxView)view;
                        CheckBoxTableCell checkBoxTableCell = new CheckBoxTableCell(null, new DataStringConverter(DataType.Boolean)) {
                            @Override
                            public void updateItem(Object item, boolean empty) {
                                super.updateItem(item, empty);

                                Node graphic = getGraphic();
                                if (graphic instanceof CheckBox) {
                                    CheckBox checkBox = (CheckBox)graphic;

                                    String propertyId = columnWrapper.getColumn().getPropertyId();

                                    int index = getIndex();

                                    if (index < 0 || index >= columnWrapper.getRows().size()) {
                                        return;
                                    }
                                    PropertyElement element = columnWrapper.getRows().get(index);
                                    ItemSource property = element.getProperty(propertyId);
                                    property.setValue(checkBox.isSelected());
                                }
                            }
                        };
//                        checkBoxTableCell.itemProperty().addListener(new InvalidationListener() {
//                            @Override
//                            public void invalidated(Observable observable) {
//                                System.out.print("CheckBox Invalided - Item Listener");
//                            }
//                        });
                        checkBoxTableCell.selectedProperty().addListener(new InvalidationListener() {
                            @Override
                            public void invalidated(Observable observable) {
                                System.out.print("CheckBox Invalided - Selected Listener");
                            }
                        });
//                        checkBoxTableCell.itemProperty().addListener(new ItemPropertyListener(columnWrapper, checkBoxTableCell));
                        checkBoxTableCell.selectedProperty().addListener(new BooleanPropertyListener(columnWrapper, checkBoxTableCell));

                        tableCell = checkBoxTableCell;
                        break;
                    default:
                        throw new RuntimeException(MessageFormat.format("Unknown table cell view type {0}", columnWrapper.getColumn().getCellView().getType().name()));
                }
            }
//            tableCell.itemProperty().addListener(new ChangeListener<Object>() {
//                @Override
//                public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
//                    String propertyId = columnWrapper.getColumn().getPropertyId();
//
//                    int index = tableCell.getTableRow().getIndex();
//
//                    if (index < 0 || index >= columnWrapper.getRows().size()) {
//                        return;
//                    }
//                    Element element = columnWrapper.getRows().get(index);
//                    ItemSource property = element.getProperty(propertyId);
//
//                    property.setValue(newValue);
//                }
//            });

            return tableCell;
        }
    }

    private static class ItemPropertyListener implements ChangeListener<Object> {

        private ColumnWrapper columnWrapper;

        private TableCell<PropertyElement,Object> tableCell;

        public ItemPropertyListener(ColumnWrapper columnWrapper, TableCell<PropertyElement,Object> tableCell) {
            this.columnWrapper = columnWrapper;
            this.tableCell = tableCell;
        }

        @Override
        public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
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

    private static class BooleanPropertyListener implements ChangeListener<Boolean> {

        private ColumnWrapper columnWrapper;

        private TableCell<PropertyElement,Boolean> tableCell;

        public BooleanPropertyListener(ColumnWrapper columnWrapper, TableCell<PropertyElement,Boolean> tableCell) {
            this.columnWrapper = columnWrapper;
            this.tableCell = tableCell;
        }

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
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
}
