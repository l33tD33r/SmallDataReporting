package l33tD33r.app.database.form.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simon on 2014-10-31.
 */
public class Element {

    private Map<String,ItemSource> properties;

    public Element() {
        properties = new HashMap<>();
    }

    public void addProperty(ItemSource property) {
        properties.put(property.getId(), property);
    }

    public ItemSource getProperty(String propertyId) {
        return properties.get(propertyId);
    }


}
