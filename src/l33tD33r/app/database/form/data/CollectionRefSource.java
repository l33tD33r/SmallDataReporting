package l33tD33r.app.database.form.data;

import java.util.ArrayList;

/**
 * Created by Simon on 2014-11-16.
 */
public class CollectionRefSource {

    private Collection collection;

    public CollectionRefSource() {

    }

    public Collection getCollection() {
        return collection;
    }
    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public ArrayList<ElementRefSource> getElementSources() {
        ArrayList<ElementRefSource> elementSources = new ArrayList<>();

        for (Element element : collection.getElements()) {
            ElementRefSource elementSource = new ElementRefSource(element);

            elementSources.add(elementSource);
        }

        return elementSources;
    }
}
