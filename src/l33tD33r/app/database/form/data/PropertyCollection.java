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

    private ArrayList<PropertyElement> elements;

    public PropertyCollection(String id) {
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

    public List<Element> getElements() {
        return new ArrayList<>(elements);
    }

    public int getSize() {
        return elements.size();
    }

    public Element getElement(int index) {
        return elements.get(index);
    }

    public PropertyElement addElement() {
        PropertyElement element = new PropertyElement();
        propertyTemplates.forEach(t -> element.addProperty(new ItemSource(t)));
        elements.add(element);
        return element;
    }

    public void setupInitialElements(List<Map<String,String>> elementsInitialValues) {
        for (Map<String,String> initialValues : elementsInitialValues) {
            PropertyElement element = addElement();

            for (String propertyId : initialValues.keySet()) {
                ItemSource property = element.getProperty(propertyId);
                property.setStringValue(initialValues.get(propertyId));
            }
        }
    }

    public void removeElement(int index) {
        elements.remove(index);
    }

    private int elementCountForInsert = 0;

    public int getElementCountForInsert() {
        return elementCountForInsert;
    }

    public void setElementCountForInsert(int count) {
        elementCountForInsert = count;
    }

    public ArrayList<PropertyElement> getInsertElements() {
        ArrayList<PropertyElement> insertElements = new ArrayList<>();

        for (int i=0; i < getElementCountForInsert(); i++) {
            insertElements.add((PropertyElement)getElement(i));
        }

        return insertElements;
    }
}
