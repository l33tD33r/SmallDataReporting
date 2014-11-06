package l33tD33r.app.ui.workspace.control;

import l33tD33r.app.database.form.data.ItemSource;

/**
 * Created by Simon on 2014-10-31.
 */
public abstract class ItemControlWrapper<TData> extends ControlWrapper {

    public abstract TData getValue();

    @Override
    public void updateValue() {
        updateItemValue(getView().getItemId(), getValue());
    }

    public void updateItemValue(String itemId, TData value) {
        ItemSource itemSource = getForm().getItem(itemId);

        itemSource.setValue(value);
    }

    @Override
    public void setupControl() {

    }
}
