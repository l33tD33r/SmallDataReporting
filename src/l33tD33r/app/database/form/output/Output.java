package l33tD33r.app.database.form.output;

import l33tD33r.app.database.form.Form;
import l33tD33r.app.database.form.FormComponent;

/**
 * Created by Simon on 10/24/2014.
 */
public abstract class Output extends FormComponent {

    private Form form;

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public abstract OutputType getType();

    public abstract String generateOutput();
}
