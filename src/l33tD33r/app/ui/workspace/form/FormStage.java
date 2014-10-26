package l33tD33r.app.ui.workspace.form;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import l33tD33r.app.database.form.DropDownView;
import l33tD33r.app.database.form.Form;
import l33tD33r.app.database.form.TableDropDownView;
import l33tD33r.app.database.form.View;
import l33tD33r.app.ui.workspace.data.DataRecordReference;

/**
 * Created by Simon on 10/25/2014.
 */
public class FormStage extends Stage {

    private Form form;

    public FormStage(Form form) {
        super(StageStyle.DECORATED);
        this.form = form;

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

        return new Scene(rootNode);
    }

    private Control createControl(View view) {
        switch (view.getType()) {
            case DropDown:
                DropDownView dropDownView = (DropDownView)view;
                switch (dropDownView.getSourceType()) {
                    case Table:
                        return createTableComboBox((TableDropDownView)dropDownView);
                    default:
                        throw new RuntimeException("Unknown drop-down source type:" + dropDownView.getSourceType().name());
                }
            default:
                throw new RuntimeException("Unknown view type:" + view.getType());
        }
    }

    private ComboBox<DataRecordReference> createTableComboBox(TableDropDownView tableDropDownView) {
        return null;
    }
}
