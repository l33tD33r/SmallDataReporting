package l33tD33r.app.database.report.visualization;

/**
 * Created by Simon on 8/24/2014.
 */
public class Chart {
    private String type;
    private String title;

    private String xAxisTitle;
    private String yAxisTitle;

    private String categoryColumnName;

    private String seriesNameColumnName;
    private String[] seriesDataColumnNames;

    public Chart() {
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

    public String getCategoryColumnName() {
        return categoryColumnName;
    }

    public void setCategoryColumnName(String categoryColumnName) {
        this.categoryColumnName = categoryColumnName;
    }

    public String getSeriesNameColumnName() {
        return seriesNameColumnName;
    }

    public void setSeriesNameColumnName(String seriesNameColumnName) {
        this.seriesNameColumnName = seriesNameColumnName;
    }

    public String[] getSeriesDataColumnNames() {
        return seriesDataColumnNames;
    }

    public void setSeriesDataColumnNames(String[] seriesDataColumnNames) {
        this.seriesDataColumnNames = seriesDataColumnNames;
    }
}
