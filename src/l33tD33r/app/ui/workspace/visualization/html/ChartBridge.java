package l33tD33r.app.ui.workspace.visualization.html;

import netscape.javascript.JSException;
import sun.plugin.javascript.JSObject;

/**
 * Created by Simon on 8/23/2014.
 */
public class ChartBridge {

    private String type;
    private String title;
    private String xAxisTitle;
    private String yAxisTitle;
    private String[] categories;
    private ChartSeries[] series;

    public ChartBridge() {

    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getXAxisTitle() {
        return xAxisTitle;
    }
    public void setXAxisTitle(String xAxisTitle) {
        this.xAxisTitle = xAxisTitle;
    }

    public String getYAxisTitle() {
        return yAxisTitle;
    }
    public void setYAxisTitle(String yAxisTitle) {
        this.yAxisTitle = yAxisTitle;
    }

    public String[] getCategories() {
        return categories;
    }
    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public ChartSeries[] getSeries() { return series; }
    public void setSeries(ChartSeries[] series) {
        this.series = series;
    }
}
