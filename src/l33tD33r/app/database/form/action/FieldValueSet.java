package l33tD33r.app.database.form.action;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Simon on 1/1/2015.
 */
public class FieldValueSet {

    private Map<String,Object> fieldValues;

    public FieldValueSet() {
        fieldValues = new HashMap<>();
    }

    public Map<String,Object> getFieldValues() {
        return fieldValues;
    }

    public Set<String> getFieldNames() {
        return fieldValues.keySet();
    }

    public Object getFieldValue(String fieldName) {
        Object fieldValue = getFieldValues().get(fieldName);
        if (fieldValue == null) {
            throw new RuntimeException(MessageFormat.format("No value provided for field named ''{0}''", fieldName));
        }
        return fieldValue;
    }

    public void addFieldValue(String fieldName, Object fieldValue) {
        if (fieldName == null || fieldName.isEmpty()) {
            throw new IllegalArgumentException("Field name cannot be null or empty");
        }
        if (fieldValue == null) {
            throw new IllegalArgumentException("Field value cannot be null");
        }
        getFieldValues().put(fieldName, fieldValue);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FieldValueSet) {
            return equals((FieldValueSet)obj);
        }
        return false;
    }

    public boolean equals(FieldValueSet other) {
        return getFieldNames().equals(other.getFieldValues());
    }

    @Override
    public int hashCode() {
        return fieldValues.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{");

        boolean addedField = false;

        for (String fieldName : getFieldNames()) {
            sb.append(MessageFormat.format("{0}=''{1}''", fieldName, getFieldValue(fieldName)));
            if (addedField) {
                sb.append(", ");
            } else {
                addedField = true;
            }
        }

        sb.append("}");

        return sb.toString();
    }
}
