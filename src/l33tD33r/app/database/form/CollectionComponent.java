package l33tD33r.app.database.form;

import l33tD33r.app.database.form.data.PropertyCollection;

/**
 * Created by Simon on 2014-10-31.
 */
public abstract class CollectionComponent extends FormComponent {

    private PropertyCollection parentCollection;

    public PropertyCollection getParentCollection() {
        return parentCollection;
    }
    public void setParentCollection(PropertyCollection parentCollection) {
        this.parentCollection = parentCollection;
    }
}
