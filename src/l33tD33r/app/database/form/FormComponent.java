package l33tD33r.app.database.form;

/**
 * Created by Simon on 2014-10-28.
 */
public abstract class FormComponent {

    private Form parentForm;

    public Form getParentForm() {
        return parentForm;
    }
    public void setParentForm(Form parentForm) {
        this.parentForm = parentForm;
    }
}
