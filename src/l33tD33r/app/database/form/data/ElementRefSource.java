package l33tD33r.app.database.form.data;

import l33tD33r.app.database.form.action.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 2014-11-29.
 */
public class ElementRefSource {

    private PropertyElement element;

    private ArrayList<Field> fields;

    public ElementRefSource(PropertyElement element){
        fields = new ArrayList<>();
        setElement(element);
    }

    public PropertyElement getElement() {
        return element;
    }
    private void setElement(PropertyElement element) {
        this.element = element;
    }

    public List<Field> getFields() {
        return fields;
    }
    public void addField(Field field) {
        this.fields.add(field);
    }
}
