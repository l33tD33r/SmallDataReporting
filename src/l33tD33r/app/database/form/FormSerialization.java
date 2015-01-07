package l33tD33r.app.database.form;

import l33tD33r.app.database.form.action.Action;
import l33tD33r.app.database.form.action.BatchInsertAction;
import l33tD33r.app.database.form.action.Field;
import l33tD33r.app.database.form.action.InsertAction;
import l33tD33r.app.database.form.data.*;
import l33tD33r.app.database.form.output.*;
import l33tD33r.app.database.form.view.*;
import l33tD33r.app.database.utility.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Simon on 10/25/2014.
 */
public class FormSerialization {

    public static List<Form> deserializeForms(Document formsDocument) {
        ArrayList<Form> forms = new ArrayList<>();

        Element formsElement = formsDocument.getDocumentElement();

        for (Element formElement : XmlUtils.getChildElements(formsElement, "Form")) {
            FormSerialization serialization = new FormSerialization(formElement);
            forms.add(serialization.getForm());
        }

        return forms;
    }

    private Form form;

    public Form getForm() {
        return form;
    }

    private FormSerialization(Element formElement) {
        createForm(formElement);
    }

    private Form createForm(Element formElement) {

        form = new Form();

        String name = XmlUtils.getElementStringValue(formElement, "Name");
        form.setName(name);

        String title = XmlUtils.getElementStringValue(formElement, "Title");
        form.setTitle(title);

        Element itemsElement = XmlUtils.getChildElement(formElement, "Items");
        if (itemsElement != null) {
            for (Element itemElement : XmlUtils.getChildElements(itemsElement, "Item")) {
                ItemSource itemSource = createItem(itemElement);
                form.addItem(itemSource);
            }
        }

        Element collectionsElement = XmlUtils.getChildElement(formElement, "Collections");
        if (collectionsElement != null) {
            for (Element collectionElement : XmlUtils.getChildElements(collectionsElement, "Collection")) {
                Collection collection = createCollection(collectionElement);
                form.addCollection(collection);
            }
        }

        Element viewsElement = XmlUtils.getChildElement(formElement, "Views");
        if (viewsElement != null) {
            for (Element viewElement : XmlUtils.getChildElements(viewsElement, "View")) {
                View view = createView(viewElement);
                form.addView(view);
            }
        }

        Element actionsElement = XmlUtils.getChildElement(formElement, "Actions");
        if (actionsElement != null) {
            for (Element actionElement : XmlUtils.getChildElements(actionsElement, "Action")) {
                Action action = createAction(actionElement);
                form.addAction(action);
            }
        }

        Element outputsElement = XmlUtils.getChildElement(formElement, "Outputs");
        if (outputsElement != null) {
            for (Element outputElement : XmlUtils.getChildElements(outputsElement, "Output")) {
                Output output = createOutput(outputElement);
                form.addOutput(output);
            }
        }

        return form;
    }

    private ItemTemplate createItemTemplate(Element itemElement) {
        String id = itemElement.getAttribute("id");
        String typeName = itemElement.getAttribute("type");
        DataType type = DataType.valueOf(typeName);

        return new ItemTemplate(id, type);
    }

    private ItemSource createItem(Element itemElement) {
        ItemTemplate template = createItemTemplate(itemElement);

        ItemSource itemSource = new ItemSource(template);

        String value = itemElement.getAttribute("value");
        if (value != null && !value.isEmpty()) {
            itemSource.setStringValue(value);
        }

        return itemSource;
    }

    private Collection createCollection(Element collectionElement) {
        String id = collectionElement.getAttribute("id");

        if (id == null || id.isEmpty()) {
            throw new RuntimeException("Collection - id is required");
        }

        String type = collectionElement.getAttribute("type");

        switch (type) {
            case "Property":
                return createPropertyCollection(id, collectionElement);
            case "Report":
                return createReportCollection(id, collectionElement);
            default:
                throw new RuntimeException(MessageFormat.format("Collection ''{0}'' ", id, type));
        }
    }

    private PropertyCollection createPropertyCollection(String id, Element collectionElement) {
        PropertyCollection collection = new PropertyCollection(id);

        Element propertiesElement = XmlUtils.getChildElement(collectionElement, "Properties");
        if (propertiesElement == null) {
            throw new RuntimeException("Collection - at least one property is required");
        }
        for (Element propertyElement : XmlUtils.getChildElements(propertiesElement, "Property")) {
            collection.addPropertyTemplate(createItemTemplate(propertyElement));
        }

        int minSize = XmlUtils.getElementIntegerValue(collectionElement, "MinSize", -1);

        collection.setMinSize(minSize);

        int maxSize = XmlUtils.getElementIntegerValue(collectionElement, "MaxSize", -1);

        collection.setMaxSize(maxSize);

        Element seedElementsElement = XmlUtils.getChildElement(collectionElement, "SeedElements");

        if (seedElementsElement != null) {
            List<Map<String,String>> seedElementsInitialValues = createCollectionElementsInitialValues(seedElementsElement);

            collection.setupSeedElements(seedElementsInitialValues);
        }

        Element initialElementsElement = XmlUtils.getChildElement(collectionElement, "InitialElements");
        if (initialElementsElement != null) {
            List<Map<String,String>> initialElementsInitialValues = createCollectionElementsInitialValues(initialElementsElement);

            collection.setupInitialElements(initialElementsInitialValues);
        }

        return collection;
    }

    private List<Map<String,String>> createCollectionElementsInitialValues(Element elementsElement) {
        ArrayList<Map<String,String>> elementsInitialValues = new ArrayList<>();
        for (Element elementElement : XmlUtils.getChildElements(elementsElement, "Element")) {
            Map<String,String> initialValues = new HashMap<>();
            for (Element valueElement : XmlUtils.getChildElements(elementElement)) {
                String key = valueElement.getTagName();
                String propertyValue = XmlUtils.getStringValue(valueElement);
                initialValues.put(key, propertyValue);
            }
            elementsInitialValues.add(initialValues);
        }
        return elementsInitialValues;
    }

    private ReportCollection createReportCollection(String id, Element collectionElement) {
        String reportName = XmlUtils.getElementStringValue(collectionElement, "Report");

        ArrayList<String> columnNameList = new ArrayList<>();

        Element columnNamesElement = XmlUtils.getChildElement(collectionElement, "Columns");

        for (Element columnNameElement : XmlUtils.getChildElements(columnNamesElement, "Column")) {
            String columnName = XmlUtils.getStringValue(columnNameElement);
            columnNameList.add(columnName);
        }

        String[] columnNames = new String[columnNameList.size()];
        columnNameList.toArray(columnNames);

        return new ReportCollection(id, reportName, columnNames);
    }

    private View createView(Element viewElement) {
        String model = viewElement.getAttribute("model");

        String type = viewElement.getAttribute("type");
        View view = null;
        switch (type) {
            case "DropDown":
                view = createDropDownView(viewElement);
                break;
            case "TextField":
                view = createTextFieldView();
                break;
            case "TextArea":
                view = createTextAreaView();
                break;
            case "IntegerField":
                view = createIntegerFieldView();
                break;
            case "BooleanCheckBox":
                view = createBooleanCheckBoxView();
                break;
            case "DatePicker":
                view = createDatePickerView();
                break;
            case "Table":
                view = createTableView(viewElement);
                break;
            default:
                throw new RuntimeException("Unknown item type:" + type);
        }

        String itemId = viewElement.getAttribute("item");
        if (itemId != null && !itemId.isEmpty()) {
            view.setItemId(itemId);
        }

        String label = viewElement.getAttribute("label");
        if (label != null && !label.isEmpty()) {
            view.setLabel(label);
        }

        return view;
    }

    private DropDownView createDropDownView(Element viewElement) {
        String source = viewElement.getAttribute("source");

        DropDownView dropDownView = null;
        switch (source) {
            case "Table":
                dropDownView = createTableDropDownView(viewElement);
                break;
            default:
                throw new RuntimeException("Unknown drop-down source:" + source);
        }

        return dropDownView;
    }

    private TableDropDownView createTableDropDownView(Element viewElement) {
        TableDropDownView tableDropDownView = new TableDropDownView();

        String table = viewElement.getAttribute("table");
        tableDropDownView.setTable(table);

        return tableDropDownView;
    }

    private TextFieldView createTextFieldView() {
        return new TextFieldView();
    }

    private TextAreaView createTextAreaView() {
        return new TextAreaView();
    }

    private IntegerFieldView createIntegerFieldView() {
        return new IntegerFieldView();
    }

    private BooleanCheckBoxView createBooleanCheckBoxView() {
        return new BooleanCheckBoxView();
    }

    private DatePickerView createDatePickerView() {
        return new DatePickerView();
    }

    private Table createTableView(Element viewElement) {
        Table table = new Table();

        String collectionId = viewElement.getAttribute("collection");
        table.setCollectionId(collectionId);

        Boolean allowAdd = XmlUtils.getAttributeBooleanValue(viewElement, "allowAdd");
        table.setAllowAdd(allowAdd);

        Boolean allowRemove = XmlUtils.getAttributeBooleanValue(viewElement, "allowRemove");
        table.setAllowRemove(allowRemove);

        Boolean allowMove = XmlUtils.getAttributeBooleanValue(viewElement, "allowMove");
        table.setAllowMove(allowMove);

        Element columnsElement = XmlUtils.getChildElement(viewElement, "Columns");
        if (columnsElement == null) {
            throw new RuntimeException("TableView - requires at least one column");
        }
        for (Element columnElement : XmlUtils.getChildElements(columnsElement, "Column")) {
            String property = columnElement.getAttribute("property");

            String header = columnElement.getAttribute("header");

            Element cellEditorElement = XmlUtils.getChildElement(columnElement, "CellEditor");

            View cellView = null;
            if (cellEditorElement != null) {
                cellView = createView(cellEditorElement);
            }

            Column column = new Column(property, header, cellView);
            table.addColumn(column);
        }

        return table;
    }

    private Action createAction(Element actionElement) {
        String type = actionElement.getAttribute("type");

        Action action = null;
        switch (type) {
            case "Insert":
                action = createInsertAction(actionElement);
                break;
            case "BatchInsert":
                action = createBatchInsertAction(actionElement);
                break;
            default:
                throw new RuntimeException("Unknown action type:" + type);
        }

        return action;
    }

    private InsertAction createInsertAction(Element actionElement) {
        InsertAction insertAction = new InsertAction();

        String table = actionElement.getAttribute("table");
        insertAction.setTable(table);

        String updateItemId = actionElement.getAttribute("updateItem");
        if (updateItemId != null && !updateItemId.isEmpty()) {
            insertAction.setUpdateItemSource(form.getItem(updateItemId));
        }

        Element fieldsElement = XmlUtils.getChildElement(actionElement, "Fields");
        if (fieldsElement == null) {
            throw new RuntimeException("InsertAction has no fields defined");
        }
        for (Element fieldElement : XmlUtils.getChildElements(fieldsElement, "Field")) {
            Field field = createField(fieldElement);

            insertAction.addField(field);
        }

        return insertAction;
    }

    private BatchInsertAction createBatchInsertAction(Element actionElement) {
        BatchInsertAction batchInsertAction = new BatchInsertAction();

        String table = actionElement.getAttribute("table");
        batchInsertAction.setTable(table);

        String sourceCollectionId = actionElement.getAttribute("sourceCollection");
        PropertyCollection sourceCollection = (PropertyCollection)form.getCollection(sourceCollectionId);
        if (sourceCollection == null) {
            throw new RuntimeException(MessageFormat.format("Form does not contain a collection with id '{0}'", sourceCollectionId));
        }
        batchInsertAction.setSourceCollection(sourceCollection);

        String updatePropertyId = actionElement.getAttribute("updateProperty");
        if (updatePropertyId != null && !updatePropertyId.isEmpty()) {
            batchInsertAction.setUpdatePropertyId(updatePropertyId);
        }

        Element fieldsElement = XmlUtils.getChildElement(actionElement, "Fields");

        for (Element fieldElement : XmlUtils.getChildElements(fieldsElement, "Field")) {
            Field field = createBatchField(fieldElement, sourceCollection);

            batchInsertAction.addFields(field);
        }

        return batchInsertAction;
    }

    private Field createField(Element fieldElement) {
        return createFieldInner(fieldElement, null);
    }
    private Field createBatchField(Element fieldElement, PropertyCollection collection) {
        return createFieldInner(fieldElement, collection);
    }
    private Field createFieldInner(Element fieldElement, PropertyCollection collection) {
        Field field = new Field();

        String name = fieldElement.getAttribute("name");
        field.setName(name);

        ValueSource valueSource = null;
        if (collection != null) {
            valueSource = createCollectionValueSource(collection, fieldElement);
        } else {
            valueSource = createValueSource(fieldElement);
        }
        field.setValueSource(valueSource);

        return field;
    }

    private Output createOutput(Element outputElement) {
        String type = outputElement.getAttribute("type");

        Output output = null;
        switch (type) {
            case "Console":
                output = createConsoleOutput(outputElement);
                break;
            default:
                throw  new RuntimeException("");
        }

        return output;
    }

    private ConsoleOutput createConsoleOutput(Element outputElement) {
        ConsoleOutput consoleOutput = new ConsoleOutput();

        for (Element childElement : XmlUtils.getChildElements(outputElement)) {
            String childTagName = XmlUtils.getElementName(childElement);

            WriteLine writeLine = null;
            switch (childTagName) {
                case "WriteLine":
                    writeLine = createWriteLine(childElement);
                    break;
                default:
                    throw new RuntimeException("Unknown console tag:" + childTagName);
            }
            consoleOutput.addWriteLine(writeLine);
        }

        return consoleOutput;
    }

    private WriteLine createWriteLine(Element writeLineElement) {
        WriteLine writeLine = new WriteLine();

        ValueSource valueSource = createValueSource(writeLineElement);
        writeLine.setValueSource(valueSource);

        return writeLine;
    }

    private ValueSource createValueSource(Element sourceElement) {
        String sourceType = sourceElement.getAttribute("source");

        switch (sourceType) {
            case "Item":
                return createItemRefSource(sourceElement);
            case "Value":
                return createFixedValueSource(sourceElement);
            default:
                throw new RuntimeException("Unknown source type:" + sourceType);
        }
    }

    private ItemRefSource createItemRefSource(Element sourceElement) {
        String itemId = sourceElement.getAttribute("item");

        if (itemId == null || itemId.isEmpty()) {
            throw new RuntimeException("ItemRefSource - no item id specified");
        }

        ItemSource itemSource = form.getItem(itemId);
        if (itemSource == null) {
            throw new RuntimeException(MessageFormat.format("ItemRefSource - the form ''{0}'' does not contain an item with id ''{1}''", form.getName(), itemId));
        }
        return new ItemRefSource(itemSource);
    }

    private FixedValueSource createFixedValueSource(Element sourceElement) {
        String stringValue = sourceElement.getAttribute("value");
        String typeName = sourceElement.getAttribute("type");
        if (typeName == null || typeName.isEmpty()) {
            throw new RuntimeException("FixedValueSource - no type specified");
        }
        DataType type = DataType.valueOf(typeName);
        return new FixedValueSource(type, type.parse(stringValue));
    }

    private ValueSource createCollectionValueSource(PropertyCollection collection, Element sourceElement) {
        String sourceType = sourceElement.getAttribute("source");

        switch (sourceType) {
            case "Property":
                return createPropertyRefSource(collection, sourceElement);
            case "Value":
                return createFixedValueSource(sourceElement);
            case "Item":
                return createItemRefSource(sourceElement);
            default:
                throw new RuntimeException("Unknown collection source type:" + sourceType);
        }
    }

    private PropertyRefSource createPropertyRefSource(PropertyCollection collection, Element sourceElement) {
        String propertyId = sourceElement.getAttribute("property");

        if (propertyId == null || propertyId.isEmpty()) {
            throw new RuntimeException("PropertyRefSource - no property id specified");
        }

        ItemTemplate propertyTemplate = collection.getPropertyTemplate(propertyId);

        PropertyRefSource propertySource = new PropertyRefSource();
        propertySource.setPropertyTemplate(propertyTemplate);
        return propertySource;
    }
}
