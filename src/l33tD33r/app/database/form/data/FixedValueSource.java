package l33tD33r.app.database.form.data;

/**
 * Created by Simon on 2014-10-28.
 */
public class FixedValueSource implements ValueSource {

    private DataType type;
    private Object value;

    public FixedValueSource(DataType type, Object value) {
        setType(type);
        setValue(value);
    }

    public DataType getType() {
        return type;
    }
    private void setType(DataType type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }
    private void setValue(Object value) {
        this.value = value;
    }
}
