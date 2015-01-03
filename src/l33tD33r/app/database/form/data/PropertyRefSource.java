package l33tD33r.app.database.form.data;

import java.text.MessageFormat;

/**
 * Created by Simon on 12/1/2014.
 */
public class PropertyRefSource implements ValueSource {

private ItemTemplate propertyTemplate;

private PropertyElement currentElement;

    public PropertyRefSource() {
    }

    public ItemTemplate getPropertyTemplate() {
        return propertyTemplate;
    }
    public void setPropertyTemplate(ItemTemplate propertyTemplate) {
        this.propertyTemplate = propertyTemplate;
    }

    public PropertyElement getCurrentElement() {
        return currentElement;
    }
    public void setCurrentElement(PropertyElement currentElement) {
        this.currentElement = currentElement;
    }

    @Override
    public DataType getType() {
        return getPropertyTemplate().getType();
    }

    @Override
    public Object getValue() {
        PropertyElement currentElement = getCurrentElement();
        if (currentElement == null) {
            throw new RuntimeException(MessageFormat.format("Cannot get value for property '{0}' since there is no current element set", getPropertyTemplate().getId()));
        }
        return currentElement.getProperty(getPropertyTemplate().getId()).getValue();
    }
}
