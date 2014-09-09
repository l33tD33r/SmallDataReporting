package l33tD33r.app.ui.workspace.visualization;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import l33tD33r.app.database.report.Report;
import l33tD33r.app.database.report.ReportManager;
import l33tD33r.app.database.report.visualization.Chart;

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

        if (reportName == null) {
            return;
        }
        Report report = ReportManager.getSingleton().getReport(reportName);

        Chart chart = report.getChart();

        String title = report.getTitle();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefWidth(1920);
        scrollPane.setPrefHeight(1080);

        if (title.contains("Starting Position")) {
            StackBarView stackBarView = new StackBarView(report);

            scrollPane.setContent(stackBarView.getStackBarChart());
        } else if (title.contains("Technologies Tree")) {
            GraphView graphView = new GraphView(report, report.getColumnName(0), report.getColumnName(2), report.getColumnName(3), report.getColumnName(1));

            scrollPane.setContent(graphView.getGraphView());
        }

        reportChartPane.getChildren().add(scrollPane);
    }
}
