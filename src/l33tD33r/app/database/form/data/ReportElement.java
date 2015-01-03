package l33tD33r.app.database.form.data;

import l33tD33r.app.database.query.ResultRow;
import l33tD33r.app.database.report.Column;

import java.util.Map;
import java.util.Set;

/**
 * Created by Simon on 1/1/2015.
 */
public class ReportElement implements Element {

    private Map<String,ValueSource> columnValueSources;

    public ReportElement(Column[] columns, ResultRow reportRow) {
        for (Column column : columns) {
            Object columnValue = reportRow.getValue(column.getName());

            FixedValueSource valueSource = new FixedValueSource(DataType.valueOf(column.getDataType().name()), columnValue);

            columnValueSources.put(column.getName(), valueSource);
        }
    }

    public Set<String> getColumnNames() {
        return columnValueSources.keySet();
    }

    public ValueSource getColumnValueSource(String columnName) {
        ValueSource columnValueSource = columnValueSources.get(columnName);
        return columnValueSource;
    }

    @Override
    public ValueSource getValueSource(String valueSourceId) {
        return getColumnValueSource(valueSourceId);
    }
}
