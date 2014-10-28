package l33tD33r.app.ui.workspace.control;

import javafx.scene.control.Control;
import l33tD33r.app.database.form.Form;
import l33tD33r.app.database.form.Item;
import l33tD33r.app.database.form.View;

/**
 * Created by Simon on 10/26/2014.
 */
public abstract class ControlWrapper<TData> {

    private Form form;

    private View view;

    private Control control;

    public void setForm(Form form) {
        this.form = form;
    }

    public void setView(View view) {
        this.view = view;
    }

    public String getLabel() {
        return view.getLabel();
    }

    public Control getControl() {
        return control;
    }
    public void setControl(Control control) {
        this.control = control;
    }

    public abstract TData getValue();

    public void updateValue() {
        updateItemValue(view.getItemId(), getValue());
    }

    public void updateItemValue(String itemId, TData value) {
        Item item = form.getItem(itemId);

        item.setValue(value);
    }
}
