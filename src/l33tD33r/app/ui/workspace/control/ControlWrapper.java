package l33tD33r.app.ui.workspace.control;

import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
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

    private Region region;

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

    protected Control getControl() {
        return control;
    }
    protected void setControl(Control control) {
        this.control = control;
    }

    public final Region getRegion() {
        if (region == null) {
            region = createRegion();
        }
        return region;
    }

    protected Region createRegion() {
        VBox vBox = new VBox();
        vBox.setSpacing(0);

        Label label = new Label(getLabel() + ":");

        vBox.getChildren().addAll(label, getControl());
        return vBox;
    }

    public abstract void applyControlValue();

    public abstract void setupControl();
}
