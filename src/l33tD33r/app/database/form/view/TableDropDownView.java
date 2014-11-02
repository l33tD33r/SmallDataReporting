package l33tD33r.app.database.form.view;

import l33tD33r.app.database.form.data.SourceType;

/**
 * Created by Simon on 10/24/2014.
 */
public class TableDropDownView extends DropDownView {

    private String table;

    @Override
    public SourceType getSourceType() {
        return SourceType.Table;
    }

    public String getTable() {
        return table;
    }
    public void setTable(String table) {
        this.table = table;
    }
}
