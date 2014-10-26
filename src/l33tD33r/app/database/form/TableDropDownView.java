package l33tD33r.app.database.form;

/**
 * Created by Simon on 10/24/2014.
 */
public class TableDropDownView extends DropDownView {

    private String table;

    @Override
    public DropDownSource getSourceType() {
        return DropDownSource.Table;
    }

    public String getTable() {
        return table;
    }
    public void setTable(String table) {
        this.table = table;
    }
}
