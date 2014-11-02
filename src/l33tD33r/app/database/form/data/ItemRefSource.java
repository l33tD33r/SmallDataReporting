package l33tD33r.app.database.form.data;

/**
 * Created by Simon on 2014-10-28.
 */
public class ItemRefSource implements ValueSource
{
    private ItemSource itemSource;

    public ItemRefSource(ItemSource itemSource) {
        this.itemSource = itemSource;
    }

    @Override
    public DataType getType() {
        return itemSource.getType();
    }

    @Override
    public Object getValue() {
        return itemSource.getValue();
    }
}
