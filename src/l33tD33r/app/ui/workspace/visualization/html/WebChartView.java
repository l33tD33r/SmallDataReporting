package l33tD33r.app.ui.workspace.visualization.html;

import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;

import l33tD33r.app.database.query.ResultRow;
import l33tD33r.app.database.report.Report;
import l33tD33r.app.database.report.ReportManager;
import l33tD33r.app.database.report.visualization.Chart;
import netscape.javascript.JSObject;

import java.util.*;

/**
 * Created by Simon on 8/23/2014.
 */
public class WebChartView {

    private Pane webReportChartPane;

    private WebView webView;
    private ChartBridge chartBridge;

    public WebChartView(Pane webReportChartPane) {
        this.webReportChartPane = webReportChartPane;

        webView = new WebView();

        chartBridge = new ChartBridge();
        JSObject window = (JSObject)webView.getEngine().executeScript("window");
        window.setMember("chartBridge", chartBridge);
        webView.getEngine().loadContent(getWebPage());
        webView.getEngine().setOnAlert(new EventHandler<WebEvent<String>>() {
            @Override
            public void handle(WebEvent<String> stringWebEvent) {
                System.out.println(stringWebEvent.getData());
            }
        });
        webView.getEngine().setOnError(new EventHandler<WebErrorEvent>() {
            @Override
            public void handle(WebErrorEvent webErrorEvent) {
                System.out.println(webErrorEvent.getMessage());
            }
        });

        webView.setPrefWidth(1920);
        webView.setPrefHeight(1080);


        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefWidth(1280);
        scrollPane.setPrefHeight(720);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(webView);

        this.webReportChartPane.getChildren().add(scrollPane);
    }

    public void loadChart(String reportName) {
        if (reportName == null) {
            webView.getEngine().executeScript("loadData()");
            return;
        }

        Report report = ReportManager.getSingleton().getReport(reportName);
        Chart chart = report.getChart();
        if (chart == null) {
            webView.getEngine().executeScript("loadData()");
            return;
        }

        chartBridge.setType(chart.getType());

        chartBridge.setTitle(chart.getTitle());
        chartBridge.setXAxisTitle(chart.getXAxisTitle());
        chartBridge.setYAxisTitle(chart.getYAxisTitle());

        Set<String> categories = new HashSet<>();
        Map<String,Map<String,Object[]>> allSeries = new LinkedHashMap<>();

        boolean hasCategoriesSet = true;

        for (int rowIndex=0; rowIndex < report.getQuery().getRowCount(); rowIndex++) {
            report.getQuery().setPosition(rowIndex);

            ResultRow currentRow = report.getQuery().getCurrentRow();

            Object category = null;
            if (chart.getCategoryColumnName() != null) {
                category = currentRow.getValue(chart.getCategoryColumnName());
            } else {
                hasCategoriesSet = false;
                category = currentRow.getValue(chart.getSeriesDataColumnNames()[0]);
            }
            String seriesName = currentRow.getStringValue(chart.getSeriesNameColumnName());

            Object[] values = new Object[chart.getSeriesDataColumnNames().length];

            for (int i=0; i < values.length; i++) {
                values[i] = currentRow.getValue(chart.getSeriesDataColumnNames()[i]);
            }

            if (category != null) {
                categories.add(category.toString());
            }

            Map<String,Object[]> seriesData = allSeries.get(seriesName);
            if (seriesData == null) {
                seriesData = new LinkedHashMap<>();
                allSeries.put(seriesName, seriesData);
            }
            seriesData.put(category.toString(), values);
        }

        if (hasCategoriesSet) {
            chartBridge.setCategories(categories.toArray(new String[categories.size()]));
        } else {
            chartBridge.setCategories(null);
        }

        List<ChartSeries> chartSeries = new ArrayList<>();
        for (String seriesName : allSeries.keySet()) {
            ChartSeries series = new ChartSeries();
            series.setName(seriesName);
            Map<String,Object[]> seriesData = allSeries.get(seriesName);

            Object[][] data = null;
            int i=0;

            if (hasCategoriesSet) {
                data = new Object[categories.size()][chart.getSeriesDataColumnNames().length];
                for (String category : categories) {
                    Object[] d = seriesData.get(category);
                    if (d == null) {
                        d = new String[chart.getSeriesDataColumnNames().length];
                        for (int j=0; j < d.length; j++) {
                            d[j] = "0.0";
                        }
                    }
                    data[i++] = d;
                }
            } else {
                data = new Object[seriesData.size()][chart.getSeriesDataColumnNames().length];
                for (Object[] d : seriesData.values()) {
                    data[i++] = d;
                }
            }

            series.setData(data);

            chartSeries.add(series);
        }
        chartBridge.setSeries(chartSeries.toArray(new ChartSeries[chartSeries.size()]));

        webView.getEngine().executeScript("window.loadData()");
    }

    private String getWebPage() {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head lang=\"en\">\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Graph View</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js\"></script>\n" +
                "    <script src=\"http://code.highcharts.com/highcharts.js\"></script>\n" +
                "    <div id=\"container\" style=\"width:100%; height:400px;\"></div>\n" +
                "    <script>\n" +
                "        var chartData = undefined;\n" +
                "\n" +
                "        function convertToArray(javaPseudoArray) {\n" +
                "            alert(\"pseudo array length:\" + javaPseudoArray.length);\n" +
                "            var array = [];\n" +
                "            for (i=0; i<javaPseudoArray.length; i++) {\n" +
                "                //alert(\"pseudo array item:\"+javaPseudoArray[i]);\n" +
                "                array.push(javaPseudoArray[i]);\n" +
                "            }\n" +
                "            return array\n" +
                "        }\n" +
                "\n" +
                "        function clearChart() {\n" +
                "            if (typeof chartData == \"undefined\") {\n" +
                "                return;\n" +
                "            }\n" +
                "            chartData.destroy();\n" +
                "        }\n" +
                "\n" +
                "        function getSeriesData(chartSeries, itemIndex, itemValueIndex) {\n" +
                "            var valueType = chartSeries.getSeriesDataType(itemIndex, itemValueIndex);\n" +
                "            switch (valueType) {\n" +
                "                case \"string\":\n" +
                "                    return chartSeries.getSeriesStringData(itemIndex, itemValueIndex);\n" +
                "                    break;\n" +
                "                case \"boolean\":\n" +
                "                    return chartSeries.getSeriesBooleanData(itemIndex, itemValueIndex);\n" +
                "                    break;\n" +
                "                case \"integer\":\n" +
                "                    return chartSeries.getSeriesIntegerData(itemIndex, itemValueIndex);\n" +
                "                    break;\n" +
                "                case \"number\":\n" +
                "                    return chartSeries.getSeriesNumberData(itemIndex, itemValueIndex);\n" +
                "                    break;\n" +
                "                case \"date\":\n" +
                "                    return chartSeries.getSeriesDateData(itemIndex, itemValueIndex);\n" +
                "                    break;\n" +
                "                case \"time\":\n" +
                "                    return chartSeries.getSeriesTimeData(itemIndex, itemValueIndex);\n" +
                "                    break;\n" +
                "                case \"datetime\":\n" +
                "                    return chartSeries.getSeriesDateTimeData(itemIndex, itemValueIndex);\n" +
                "                    break;\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        function loadData() {\n" +
                "            var chartType = chartBridge.getType();\n" +
                "\n" +
                "            var chartTitle = chartBridge.getTitle()\n" +
                "\n" +
                "            var xAxisTitle = chartBridge.getXAxisTitle();\n" +
                "            var yAxisTitle = chartBridge.getYAxisTitle();\n" +
                "\n" +
                "            var jChartCategories = chartBridge.getCategories();\n" +
                "\n" +
                "            var jChartSeries = chartBridge.getSeries();\n" +
                "\n" +
                "            try {\n" +
                "                var chartCategories;\n" +
                "                if (jChartCategories) {\n" +
                "                    var chartCategories = convertToArray(jChartCategories);\n" +
                "                }\n" +
                "\n" +
                "                var chartSeries = [];\n" +
                "                for (seriesIndex=0; seriesIndex < jChartSeries.length; seriesIndex++) {\n" +
                "\n" +
                "                    var s = jChartSeries[seriesIndex];\n" +
                "\n" +
                "                    var sName = s.getName();\n" +
                "\n" +
                "                    var sData = [];\n" +
                "                    for (seriesItemIndex=0; seriesItemIndex < s.getSeriesCount(); seriesItemIndex++) {\n" +
                "                        var seriesSize = s.getSeriesSize();\n" +
                "\n" +
                "                        if (seriesSize == 1) {\n" +
                "                            sData.push(getSeriesData(s, seriesItemIndex, 0));\n" +
                "                        } else {\n" +
                "                            sItemData = [];\n" +
                "                            for (seriesItemValueIndex=0; seriesItemValueIndex < seriesSize; seriesItemValueIndex++) {\n" +
                "                                sItemData.push(getSeriesData(s, seriesItemIndex, seriesItemValueIndex));\n" +
                "                            }\n" +
                "                            sData.push(sItemData);\n" +
                "                        }\n" +
                "                    }\n" +
                "\n" +
                "                    series = {\n" +
                "                        \"name\": sName,\n" +
                "                        \"data\": sData\n" +
                "                    };\n" +
                "\n" +
                "                    alert(\"Series:\" + series);\n" +
                "\n" +
                "                    chartSeries.push(series);\n" +
                "                }\n" +
                "            } catch(e) {\n" +
                "                alert(\"Error on processing bridge data:\" + e);\n" +
                "            }\n" +
                "\n" +
                "            clearChart();\n" +
                "\n" +
                "            switch (chartType) {\n" +
                "                case 'stackedcolumn':\n" +
                "                    loadStackedColumnChart(chartTitle, xAxisTitle, yAxisTitle, chartCategories, chartSeries);\n" +
                "                    break;\n" +
                "                case 'areaspline':\n" +
                "                    loadAreaSplineChart(chartTitle, xAxisTitle, yAxisTitle, chartCategories, chartSeries);\n" +
                "                    break;\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        function loadStackedColumnChart(chartTitle, xAxisTitle, yAxisTitle, dataCategories, dataSeries) {\n" +
                "            try {\n" +
                "                chartData = new Highcharts.Chart({\n" +
                "                chart: {\n" +
                "                    renderTo: 'container',\n" +
                "                    type: 'column'\n" +
                "                },\n" +
                "                title: {\n" +
                "                    text: chartTitle\n" +
                "                },\n" +
                "                xAxis: {\n" +
                "                    title: xAxisTitle,\n" +
                "                    categories: dataCategories\n" +
                "                },\n" +
                "                yAxis: {\n" +
                "                    title: {\n" +
                "                        text: yAxisTitle\n" +
                "                    },\n" +
                "                    stackLabels: {\n" +
                "                        enabled: true,\n" +
                "                        style: {\n" +
                "                            fontWeight: 'bold',\n" +
                "                            color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                legend: {\n" +
                "                    align: 'right',\n" +
                "                    x: -70,\n" +
                "                    verticalAlign: 'top',\n" +
                "                    y: 20,\n" +
                "                    floating: true,\n" +
                "                    backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',\n" +
                "                    borderColor: '#CCC',\n" +
                "                    borderWidth: 1,\n" +
                "                    shadow: false\n" +
                "                },\n" +
                "                tooltip: {\n" +
                "                    formatter: function () {\n" +
                "                        return  this.series.name + ': ' + this.y + '<br/>' +\n" +
                "                                'Total: ' + this.point.stackTotal;\n" +
                "                    }\n" +
                "                },\n" +
                "                plotOptions: {\n" +
                "                    column: {\n" +
                "                        stacking: 'normal',\n" +
                "                        dataLabels: {\n" +
                "                            enabled: true,\n" +
                "                            color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white',\n" +
                "                            style: {\n" +
                "                                textShadow: '0 0 3px black, 0 0 3px black'\n" +
                "                            }\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                series: dataSeries\n" +
                "            });\n" +
                "            } catch (e) {\n" +
                "                alert(\"Error on applying data to chart:\" + e);\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        function loadAreaSplineChart(chartTitle, xAxisTitle, yAxisTitle, dataCategories, dataSeries) {\n" +
                "            try {\n" +
                "                chartData = new Highcharts.Chart({\n" +
                "                chart: {\n" +
                "                    renderTo: 'container',\n" +
                "                    type: 'areaspline'\n" +
                "                },\n" +
                "                title: {\n" +
                "                    text: chartTitle\n" +
                "                },\n" +
                "                xAxis: {\n" +
                "                    title: xAxisTitle,\n" +
                "                    categories: dataCategories\n" +
                "                },\n" +
                "                yAxis: {\n" +
                "                    title: {\n" +
                "                        text: yAxisTitle\n" +
                "                    },\n" +
                "                    stackLabels: {\n" +
                "                        enabled: true,\n" +
                "                        style: {\n" +
                "                            fontWeight: 'bold',\n" +
                "                            color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                legend: {\n" +
                "                    align: 'right',\n" +
                "                    x: -70,\n" +
                "                    verticalAlign: 'top',\n" +
                "                    y: 20,\n" +
                "                    floating: true,\n" +
                "                    backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',\n" +
                "                    borderColor: '#CCC',\n" +
                "                    borderWidth: 1,\n" +
                "                    shadow: false\n" +
                "                },\n" +
                "                tooltip: {\n" +
                "                    formatter: function () {\n" +
                "                        return\n" +
                "                            '<b>' + this.series.name + '</b><br/>' +\n" +
                "                            this.x + ': ' + this.y + '<br/>';\n" +
                "                    }\n" +
                "                },\n" +
                "                plotOptions: {\n" +
                "                    column: {\n" +
                "                        stacking: 'normal',\n" +
                "                        dataLabels: {\n" +
                "                            enabled: true,\n" +
                "                            color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white',\n" +
                "                            style: {\n" +
                "                                textShadow: '0 0 3px black, 0 0 3px black'\n" +
                "                            }\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                series: dataSeries\n" +
                "            });\n" +
                "            } catch (e) {\n" +
                "                alert(\"Error on applying data to chart:\" + e);\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        $(document).ready(function() {\n" +
                "            // Do nothing\n" +
                "        });\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
    }
}
