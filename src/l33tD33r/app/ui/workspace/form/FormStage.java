package l33tD33r.app.ui.workspace.form;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import l33tD33r.app.database.form.Form;
import l33tD33r.app.database.form.Output;
import l33tD33r.app.database.form.View;
import l33tD33r.app.ui.workspace.control.ControlFactory;
import l33tD33r.app.ui.workspace.control.ControlWrapper;

import java.util.ArrayList;

/**
 * Created by Simon on 10/25/2014.
 */
public class FormStage extends Stage {

    private Form form;

    private ArrayList<ControlWrapper> controls;

    public FormStage(Form form) {
        super(StageStyle.DECORATED);
        this.form = form;

        controls = new ArrayList<>();

        setTitle(form.getTitle());
        setScene(createScene());
    }

    private Scene createScene() {
        BorderPane rootNode = new BorderPane();
        GridPane fieldsPanel = new GridPane();

        fieldsPanel.setPadding(new Insets(5, 5, 5, 5));
        fieldsPanel.setHgap(5);
        fieldsPanel.setVgap(5);
        rootNode.setCenter(fieldsPanel);

        int row = 0;

        for (View view : form.getViews()) {

            ControlWrapper controlWrapper = ControlFactory.getSingleton().createControl(form, view);
            controls.add(controlWrapper);

            Label label = new Label(controlWrapper.getLabel() + ":");

            Control control = controlWrapper.getControl();

            fieldsPanel.add(label, 0, row);
            fieldsPanel.add(control, 1, row);

            row++;
        }

        HBox buttonPanel = new HBox();
        buttonPanel.setPadding(new Insets(5, 5, 5, 5));

        Button runButton = new Button("Run");
        runButton.setOnAction(e -> {
            run();
        });
        runButton.setAlignment(Pos.CENTER_LEFT);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> {
            close();
        });
        closeButton.setAlignment(Pos.CENTER_RIGHT);

        buttonPanel.getChildren().addAll(runButton, closeButton);

        rootNode.setBottom(buttonPanel);

        return new Scene(rootNode);
    }

    private void run() {
        for (ControlWrapper control : controls) {
            control.updateValue();
        }

        FormController formController = new FormController();
        formController.setForm(form);

        formController.execute();
    }
}
