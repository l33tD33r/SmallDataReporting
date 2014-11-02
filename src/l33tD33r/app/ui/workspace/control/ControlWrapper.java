package l33tD33r.app.ui.workspace.control;

import javafx.scene.control.Control;
import l33tD33r.app.database.form.Form;
import l33tD33r.app.database.form.data.ItemSource;
import l33tD33r.app.database.form.view.View;

/**
 * Created by Simon on 10/26/2014.
 */
public abstract class ControlWrapper {

    private Form form;

    private View view;

    private Control control;

    public Form getForm() {
        return form;
    }
    public void setForm(Form form) {
        this.form = form;
    }

    public View getView() {
        return view;
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


}
