package l33tD33r.app.database.form.view;

import java.util.ArrayList;

/**
 * Created by Simon on 2014-10-31.
 */
public class Table extends View {

    private String collectionId;

    private ArrayList<Column> columns;

    public Table() {
        columns = new ArrayList<>();
    }

    @Override
    public ViewType getType() {
        return ViewType.Table;
    }

    public String getCollectionId() {
        return collectionId;
    }
    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public ArrayList<Column> getColumns() {
        return new ArrayList<>(columns);
    }
    public void addColumn(Column column) {
        columns.add(column);
    }
}
