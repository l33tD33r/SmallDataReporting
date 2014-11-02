package l33tD33r.app.database.form;

import l33tD33r.app.database.form.data.Collection;
import l33tD33r.app.database.form.view.View;

/**
 * Created by Simon on 2014-10-31.
 */
public abstract class CollectionComponent extends FormComponent {

    private Collection parentCollection;

    public Collection getParentCollection() {
        return parentCollection;
    }
    public void setParentCollection(Collection parentCollection) {
        this.parentCollection = parentCollection;
    }
}
