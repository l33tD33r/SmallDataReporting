package l33tD33r.app.ui.workspace.visualization.html;

import l33tD33r.app.database.Date;
import l33tD33r.app.database.DateTime;
import l33tD33r.app.database.Time;

/**
 * Created by Simon on 8/23/2014.
 */
public class ChartSeries {

    private String name;

    private Object[][] data;

    private int seriesCount = 0;
    private int seriesSize = 0;

    public ChartSeries() {
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Object[][] getData() {
        return data;
    }
    public void setData(Object[][] data) {
        this.data = data;
        if (data == null) {
            return;
        }
        if (data.length == 0) {
            return;
        }
        seriesCount = data.length;
        if (data[0] == null) {
            return;
        }
        seriesSize = data[0].length;
    }

    public int getSeriesCount() {
        return data.length;
    }

    public int getSeriesSize() { return data[0].length; }

    public String getSeriesDataType(int row, int column) {
        Object d = getSeriesData(row, column);
        if (d instanceof String) {
            return "string";
        }
        if (d instanceof Boolean) {
            return "boolean";
        }
        if (d instanceof Integer) {
            return "integer";
        }
        if (d instanceof Double) {
            return "number";
        }
        if (d instanceof Date) {
            return "date";
        }
        if (d instanceof Time) {
            return "time";
        }
        if (d instanceof DateTime) {
            return "datetime";
        }
        return d.getClass().getName();
    }

    public String getSeriesStringData(int row, int column) {
        return (String)getSeriesData(row, column);
    }

    public boolean getSeriesBooleanData(int row, int column) {
        return ((Boolean)getSeriesData(row, column)).booleanValue();
    }

    public int getSeriesIntegerData(int row, int column) {
        return ((Integer)getSeriesData(row, column)).intValue();
    }

    public double getSeriesNumberData(int row, int column) {
        return ((Double)getSeriesData(row, column)).doubleValue();
    }

    public Date getSeriesDateData(int row, int column) {
        return ((Date)getSeriesData(row, column));
    }

    public String getSeriesTimeData(int row, int column) {
        return ((Time)getSeriesData(row, column)).toString();
    }

    public String getSeriesDateTimeData(int row, int column) {
        return ((DateTime)getSeriesData(row, column)).toString();
    }

    private Object getSeriesData(int row, int column) {
        return data[row][column];
    }
}
