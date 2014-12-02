package l33tD33r.app.database.form.data;

import java.util.ArrayList;

/**
 * Created by Simon on 2014-11-16.
 */
public class CollectionRefSource {

    private Collection collection;

    public ArrayList<ElementRefSource> getElementSources() {
        ArrayList<ElementRefSource> elementSources = new ArrayList<>();

        for (Element element : collection.getElements()) {

        }

        return elementSources;
    }
}
