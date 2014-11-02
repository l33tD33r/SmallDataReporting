package l33tD33r.app.ui.workspace.form;

import l33tD33r.app.database.form.Form;
import l33tD33r.app.database.form.action.Action;
import l33tD33r.app.database.form.output.Output;

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

        generateOutputs();
    }

    private void performActions() {
        form.getActions().forEach(this::performAction);
    }

    private void performAction(Action action) {
        action.execute();
    }

    private void generateOutputs() {
        form.getOutputs().forEach(this::generateOutput);
    }

    private void generateOutput(Output output) {
        System.out.println(output.generateOutput());
    }
}
