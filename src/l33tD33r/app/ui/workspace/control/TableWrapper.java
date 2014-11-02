package l33tD33r.app.ui.workspace.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import l33tD33r.app.database.form.data.Element;
import l33tD33r.app.database.form.view.Table;

/**
 * Created by Simon on 2014-10-31.
 */
public class TableWrapper extends ControlWrapper {

    private TableView<Element> tableView;

    private ObservableList<Element> rows;

    public TableWrapper() {
        tableView = new TableView<>();
        rows = FXCollections.observableArrayList();
        tableView.setItems(rows);
    }
    public Table getTable() {
        return (Table)getView();
    }
    public void setTable(Table table) {
        setView(table);
    }

    public void setupTableView() {

    }
}
