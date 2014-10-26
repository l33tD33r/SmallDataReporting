package l33tD33r.app.database.form;

import l33tD33r.app.database.utility.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 10/25/2014.
 */
public class FormSerialization {

    public static List<Form> deserializeForms(Document formsDocument) {
        ArrayList<Form> forms = new ArrayList<>();

        Element formsElement = formsDocument.getDocumentElement();

        for (Element reportElement : XmlUtils.getChildElements(formsElement, "Form")) {
            forms.add(createForm(reportElement));
        }

        return forms;
    }

    private static Form createForm(Element formElement) {

        Form form = new Form();

        String name = XmlUtils.getElementStringValue(formElement, "Name");
        form.setName(name);

        String title = XmlUtils.getElementStringValue(formElement, "Title");
        form.setTitle(title);

        Element itemsElement = XmlUtils.getChildElement(formElement, "Items");
        if (itemsElement != null) {
            for (Element itemElement : XmlUtils.getChildElements(itemsElement, "Item")) {
                Item item = createItem(itemElement);
                form.addItem(item);
            }
        }

        Element viewsElement = XmlUtils.getChildElement(formElement, "Views");
        if (viewsElement != null) {
            for (Element viewElement : XmlUtils.getChildElements(viewsElement, "View")) {
                View view = createView(viewElement);
                form.addView(view);
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

    private static Item createItem(Element itemElement) {
        String type = itemElement.getAttribute("type");
        Item item = null;
        switch (type) {
            case "Reference":
                item = createReferenceItem(itemElement);
                break;
            default:
                throw new RuntimeException("Unknown item type:" + type);
        }

        String id = itemElement.getAttribute("id");
        item.setId(id);

        return item;
    }

    private static ReferenceItem createReferenceItem(Element itemElement) {
        ReferenceItem referenceItem = new ReferenceItem();

        String table = itemElement.getAttribute("table");
        referenceItem.setTable(table);

        return referenceItem;
    }

    private static View createView(Element viewElement) {
        String type = viewElement.getAttribute("type");
        View view = null;
        switch (type) {
            case "DropDown":
                view = createDropDownView(viewElement);
                break;
            default:
                throw new RuntimeException("Unknown item type:" + type);
        }
        String itemId = viewElement.getAttribute("item");
        view.setItemId(itemId);

        String label = viewElement.getAttribute("label");
        if (label != null) {
            view.setLabel(label);
        }

        return view;
    }

    private static DropDownView createDropDownView(Element viewElement) {
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

    private static TableDropDownView createTableDropDownView(Element viewElement) {
        TableDropDownView tableDropDownView = new TableDropDownView();

        String table = viewElement.getAttribute("table");
        tableDropDownView.setTable(table);

        return tableDropDownView;
    }

    private static Output createOutput(Element outputElement) {
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

    private static ConsoleOutput createConsoleOutput(Element outputElement) {
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

    private static WriteLine createWriteLine(Element writeLineElement) {
        WriteLine writeLine = new WriteLine();

        OutputSource outputSource = createOutputSource(writeLineElement);
        writeLine.setSource(outputSource);

        return writeLine;
    }

    private static OutputSource createOutputSource(Element outputSourceElement) {
        String outputSourceType = outputSourceElement.getAttribute("source");

        OutputSource outputSource = null;
        switch (outputSourceType) {
            case "Item":
                outputSource = createItemOutputSource(outputSourceElement);
                break;
            default:
                throw new RuntimeException("Unknown output source type:" + outputSourceType);
        }

        return outputSource;
    }

    private static ItemOutputSource createItemOutputSource(Element outputSourceElement) {
        ItemOutputSource itemOutputSource = new ItemOutputSource();

        String itemId = outputSourceElement.getAttribute("item");
        itemOutputSource.setItemId(itemId);

        return itemOutputSource;
    }
}
