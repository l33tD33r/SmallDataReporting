package l33tD33r.app.database.query;

/**
 * Created by Simon on 8/21/2014.
 */
public interface IColumnMap {
    int getColumnIndex(String name);
    String getColumnName(int index);
}
