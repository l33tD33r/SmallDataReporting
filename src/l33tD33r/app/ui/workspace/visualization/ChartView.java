package l33tD33r.app.ui.workspace.visualization;

import javafx.collections.FXCollections;
import javafx.scene.chart.*;
import javafx.scene.layout.Pane;
import l33tD33r.app.database.query.Query;
import l33tD33r.app.database.query.Row;
import l33tD33r.app.database.report.Report;
import l33tD33r.app.database.report.ReportManager;

import java.util.*;

/**
 * Created by Simon on 2/17/14.
 */
public class ChartView {

    private Pane reportChartPane;

    public ChartView(Pane reportChartPane) {
        this.reportChartPane = reportChartPane;
    }

    public void loadChart(String reportName) {
        reportChartPane.getChildren().clear();

        Report report = ReportManager.getSingleton().getReport(reportName);
        String title = report.getTitle();

        if (!title.contains("Starting Position")) {
            return;
        }

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Position");

        if (title.contains("3")) {
            xAxis.setCategories(FXCollections.<String>observableArrayList("1", "2", "3"));
        } else if (title.contains("4")) {
            xAxis.setCategories(FXCollections.<String>observableArrayList("1", "2", "3", "4"));
        } else if (title.contains("5")) {
            xAxis.setCategories(FXCollections.<String>observableArrayList("1", "2", "3", "4", "5"));
        } else if (title.contains("6")) {
            xAxis.setCategories(FXCollections.<String>observableArrayList("1", "2", "3", "4", "5", "6"));
        }

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Wins");
        yAxis.setMinorTickVisible(false);
        yAxis.setTickLength(1.0);
        yAxis.setMinorTickLength(1.0);
        yAxis.setTickUnit(1.0);

        StackedBarChart<String,Number> barChart = new StackedBarChart<>(xAxis, yAxis);

        barChart.setTitle(title);

        Collection<XYChart.Series<String,Number>> allDataSeries = getAllDataSeries(report.getQuery());

        barChart.getData().addAll(allDataSeries);

        reportChartPane.getChildren().add(barChart);
    }

    private Collection<XYChart.Series<String,Number>> getAllDataSeries(Query query) {
        Map<String,XYChart.Series<String,Number>> playerDataSeriesMap = new HashMap<>();

        for (int rowIndex = 0; rowIndex < query.getRowCount(); rowIndex++) {
            query.setPosition(rowIndex);

            Row currentRow = query.getCurrentRow();

            String player = currentRow.getStringValue(2);

            XYChart.Series<String,Number> dataSeries = null;
            if (playerDataSeriesMap.containsKey(player)) {
                dataSeries = playerDataSeriesMap.get(player);
            } else {
                dataSeries = new XYChart.Series<>();
                dataSeries.setName(player);
                playerDataSeriesMap.put(player, dataSeries);
            }

            Integer position = currentRow.getIntegerValue(0);
            Integer wins = currentRow.getIntegerValue(1);
            dataSeries.getData().add(new XYChart.Data<String, Number>(position.toString(), wins));
        } 
        ArrayList<XYChart.Series<String,Number>> allDataSeries = new ArrayList<>(playerDataSeriesMap.values());

        allDataSeries.sort(new Comparator<XYChart.Series<String, Number>>() {
            @Override
            public int compare(XYChart.Series<String, Number> seriesA, XYChart.Series<String, Number> seriesB) {
                return seriesA.getName().compareTo(seriesB.getName());
            }
        });

        return allDataSeries;
    }
}
