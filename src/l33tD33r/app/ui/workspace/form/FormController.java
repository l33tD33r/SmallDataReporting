package l33tD33r.app.ui.workspace.form;

import l33tD33r.app.database.form.Form;
import l33tD33r.app.database.form.Output;

/**
 * Created by Simon on 10/27/2014.
 */
public class FormController {

    private Form form;

    public Form getForm() {
        return form;
    }
    public void setForm(Form form) {
        this.form = form;
    }

    public void execute() {
        performActions();

        generateOutput();
    }

    private void performActions() {

    }

    private void generateOutput() {
        for (Output output : form.getOutputs()) {
            System.out.println(output.generateOutput(form));
        }
    }
}
