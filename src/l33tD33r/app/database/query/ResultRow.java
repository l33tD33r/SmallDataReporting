package l33tD33r.app.database.query;

import l33tD33r.app.database.Date;
import l33tD33r.app.database.DateTime;
import l33tD33r.app.database.Time;

import java.text.MessageFormat;

/**
 * Created by Simon on 8/21/2014.
 */
public class ResultRow {

    private int rowIndex = -1;

    private Object[] values;

    private IColumnMap columnMap;

    public ResultRow(Object[] values, IColumnMap columnMap) {
        this.values = values;
        this.columnMap = columnMap;
    }

    public int getRowIndex() {
        return rowIndex;
    }
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public Object getValue(int columnIndex) {
        if (columnIndex == -1) {
            if (getRowIndex() == -1) {
                throw new RuntimeException("RowIndex value requested when it has an invalid value");
            }
            return getRowIndex();
        }
        return values[columnIndex];
    }

    public String getStringValue(int columnIndex) {
        return (String)getValue(columnIndex);
    }

    public Integer getIntegerValue(int columnIndex) {
        return (Integer)getValue(columnIndex);
    }

    public Double getNumberValue(int columnIndex) {
        return (Double)getValue(columnIndex);
    }

    public Date getDateValue(int columnIndex) {
        return (Date)getValue(columnIndex);
    }

    public Time getTimeValue(int columnIndex) {
        return (Time)getValue(columnIndex);
    }

    public DateTime getDateTimeValue(int columnIndex) {
        return (DateTime)getValue(columnIndex);
    }

    public Boolean getBooleanValue(int columnIndex) {
        return (Boolean)getValue(columnIndex);
    }

    public Object getValue(String columnName) {
        Integer columnIndex = columnMap.getColumnIndex(columnName);
        if (columnIndex == null) {
            throw new RuntimeException(MessageFormat.format("Unknown column name '{0}'", columnName));
        }
        return getValue(columnIndex);
    }

    public String getStringValue(String columnName) {
        return (String)getValue(columnName);
    }

    public Integer getIntegerValue(String columnName) { return (Integer)getValue(columnName); }

    public Double getNumberValue(String columnName) { return (Double)getValue(columnName); }

    public Date getDateValue(String columnName) { return (Date)getValue(columnName); }

    public Time getTimeValue(String columnName) {
        return (Time)getValue(columnName);
    }

    public DateTime getDateTimeValue(String columnName) {
        return (DateTime)getValue(columnName);
    }

    public Boolean getBooleanValue(String columnName) {
        return (Boolean)getValue(columnName);
    }
}
