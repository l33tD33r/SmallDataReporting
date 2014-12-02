package l33tD33r.app.database.form.data;

import java.text.MessageFormat;

/**
 * Created by Simon on 10/24/2014.
 */
public class ItemSource implements ValueSource {

    private ItemTemplate template;

    private Object value;

    public ItemSource(ItemTemplate template) {
        setTemplate(template);
    }

    private void setTemplate(ItemTemplate template) {
        this.template = template;
    }

    public String getId() {
        return template.getId();
    }
    public DataType getType() {
        return template.getType();
    }

    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        this.value = value;
    }

    public void setStringValue(String stringValue) {
        Object value = getType().parse(stringValue);

        setValue(value);
    }

}
