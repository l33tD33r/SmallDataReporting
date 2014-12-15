package l33tD33r.app.database.form.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Simon on 2014-10-31.
 */
public class Collection {

    private String id;

    private ArrayList<ItemTemplate> propertyTemplates;

    private ArrayList<Element> elements;

    public Collection(String id) {
        setId(id);
        elements = new ArrayList<>();
        propertyTemplates = new ArrayList<>();
    }

    public String getId() { return id; }
    private void setId(String id) { this.id = id; }

    public ArrayList<ItemTemplate> getPropertyTemplates() {
        return new ArrayList<>(propertyTemplates);
    }

    public void addPropertyTemplate(ItemTemplate propertyTemplate) {
        propertyTemplates.add(propertyTemplate);
    }

    public ItemTemplate getPropertyTemplate(String id) {
        ItemTemplate propertyTemplate = null;
        for (ItemTemplate p : propertyTemplates) {
            if (p.getId().equalsIgnoreCase(id)) {
                propertyTemplate = p;
                break;
            }
        }
        return propertyTemplate;
    }

    public ArrayList<Element> getElements() {
        return new ArrayList<>(elements);
    }

    public Element getElement(int index) {
        return elements.get(index);
    }

    public Element addElement() {
        Element element = new Element();
        propertyTemplates.forEach(t -> element.addProperty(new ItemSource(t)));
        elements.add(element);
        return element;
    }

    public void setupInitialElements(List<Map<String,String>> elementsInitialValues) {
        for (Map<String,String> initialValues : elementsInitialValues) {
            Element element = addElement();

            for (String propertyId : initialValues.keySet()) {
                ItemSource property = element.getProperty(propertyId);
                property.setStringValue(initialValues.get(propertyId));
            }
        }
    }

    public void removeElement(int index) {
        elements.remove(index);
    }
}
