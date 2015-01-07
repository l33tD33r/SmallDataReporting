package l33tD33r.app.database.form.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Simon on 2014-10-31.
 */
public class PropertyCollection implements Collection{

    private String id;

    private ArrayList<ItemTemplate> propertyTemplates;

    private ArrayList<PropertyElement> seedElements;

    private ArrayList<PropertyElement> elements;

    private int maxSize;

    private int minSize;

    public PropertyCollection(String id) {
        setId(id);
        seedElements = new ArrayList<>();
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

    public List<Element> getElements() {
        return new ArrayList<>(elements);
    }

    public int getSize() {
        return elements.size();
    }

    public Element getElement(int index) {
        return elements.get(index);
    }

    public void addElement() {
        if (getSize() >= getMaxSize()) {
            return;
        }

        PropertyElement element = null;

        if (hasSeedElements()) {
            element = retrieveNextSeedElement();
        } else {
            element = createElement();
        }

        addElement(element);
    }

    private void addElement(PropertyElement element) {
        elements.add(element);
    }

    private boolean hasSeedElements() {
        return seedElements.size() > 0;
    }

    private PropertyElement retrieveNextSeedElement() {
        return seedElements.remove(0);
    }

    private void addSeedElement(PropertyElement element) {
        seedElements.add(element);
    }

    private PropertyElement createElement() {
        PropertyElement element = new PropertyElement();
        propertyTemplates.forEach(t -> element.addProperty(new ItemSource(t)));
        return element;
    }

    public void setupSeedElements(List<Map<String,String>> seedElementsInitialValues) {
        int index = 0;
        for (Map<String,String> initialValues : seedElementsInitialValues) {
            addSeedElement(createElement());

            PropertyElement element = seedElements.get(index);

            for (String propertyId : initialValues.keySet()) {
                ItemSource property = element.getProperty(propertyId);
                property.setStringValue(initialValues.get(propertyId));
            }

            index++;
        }
    }

    public void setupInitialElements(List<Map<String,String>> initialElementsInitialValues) {
        int index = 0;
        for (Map<String,String> initialValues : initialElementsInitialValues) {
            addElement(createElement());

            PropertyElement element = elements.get(index);

            for (String propertyId : initialValues.keySet()) {
                ItemSource property = element.getProperty(propertyId);
                property.setStringValue(initialValues.get(propertyId));
            }

            index++;
        }
    }

    public void removeElement(int index) {
        if (elements.size() <= getMinSize()) {
            return;
        }
        elements.remove(index);
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getMinSize() {
        return minSize;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }
}
