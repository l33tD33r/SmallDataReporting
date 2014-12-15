package l33tD33r.app.database.form.view;

import java.util.ArrayList;

/**
 * Created by Simon on 2014-10-31.
 */
public class Table extends View {

    private String collectionId;

    private boolean allowAdd = false;
    private boolean allowRemove = false;
    private boolean allowMove = false;

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

    public boolean isAllowAdd() {
        return allowAdd;
    }
    public void setAllowAdd(boolean allowAdd) {
        this.allowAdd = allowAdd;
    }

    public boolean isAllowRemove() {
        return allowRemove;
    }
    public void setAllowRemove(boolean allowRemove) {
        this.allowRemove = allowRemove;
    }

    public boolean isAllowMove() {
        return allowMove;
    }
    public void setAllowMove(boolean allowMove) {
        this.allowMove = allowMove;
    }

    public ArrayList<Column> getColumns() {
        return new ArrayList<>(columns);
    }
    public void addColumn(Column column) {
        columns.add(column);
    }
}
