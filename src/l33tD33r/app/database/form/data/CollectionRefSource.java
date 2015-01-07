package l33tD33r.app.database.form.data;

import l33tD33r.app.database.form.action.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 2014-11-16.
 */
public class CollectionRefSource {

    private PropertyCollection collection;

    private ArrayList<PropertyRefSource> propertySources;

    private ArrayList<Field> fields;

    public CollectionRefSource() {

    }

    public PropertyCollection getCollection() {
        return collection;
    }
    public void setCollection(PropertyCollection collection) {
        this.collection = collection;
    }

    public ArrayList<PropertyRefSource> getPropertySources() {
        return propertySources;
    }
    public void setPropertySources(List<PropertyRefSource> propertySources) {
        this.propertySources = new ArrayList<>(propertySources);
    }

    public ArrayList<Field> getFields() {
        return fields;
    }
    public void setFields(List<Field> fields) {
        this.fields = new ArrayList<>(fields);
    }

    public ArrayList<ElementRefSource> getElementSources() {
        ArrayList<ElementRefSource> elementSources = new ArrayList<>();

        for (Element element : collection.getElements()) {
            ElementRefSource elementSource = new ElementRefSource((PropertyElement)element);

            elementSources.add(elementSource);
        }

        return elementSources;
    }

    public void updateCurrentElement(PropertyElement element) {
        getPropertySources().forEach(s -> s.setCurrentElement(element));
    }
}
