package l33tD33r.app.database.form.data;

/**
 * Created by Simon on 2014-11-16.
 */
public class CollectionRefSource implements ValueSource {
    @Override
    public DataType getType() {
        return DataType.String;
    }

    @Override
    public Object getValue() {
        return null;
    }
}
