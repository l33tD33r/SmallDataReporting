package l33tD33r.app.database.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import l33tD33r.app.database.Date;
import l33tD33r.app.database.Time;
import l33tD33r.app.database.data.DataTable;
import l33tD33r.app.database.query.*;
import l33tD33r.app.database.report.visualization.Chart;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import l33tD33r.app.database.utility.XmlUtils;

public class ReportSerialization {

	public static List<Report> deserializeReports(Document reportsDocument) {
		Element reportsElement = reportsDocument.getDocumentElement();
		
		ArrayList<Report> reports = new ArrayList<>();
		
		for (Element reportElement : XmlUtils.getChildElements(reportsElement, "Report")) {
			reports.add(createReport(reportElement));
		}
		
		return reports;
	}
	
	private static Report createReport(Element reportElement) {
        Report report = null;

		String type = reportElement.getAttribute("type");
        switch (type) {
            case "Table":
                report = createTableView(reportElement);
                break;
            case "Join":
                report = createJoinView(reportElement);
                break;
            case "Union":
                report = createUnionView(reportElement);
                break;
            default:
                throw new RuntimeException("Unknown view type: " + type);
        }

        Chart chart = null;
        Element chartElement = XmlUtils.getChildElement(reportElement, "Chart");
        if (chartElement != null) {
            chart = createChart(chartElement);
        }
        report.setChart(chart);

        return report;
	}
	
	private static TableReport createTableView(Element tableViewElement) {
		String tableName = XmlUtils.getElementStringValue(tableViewElement, "Table");
		String name = XmlUtils.getElementStringValue(tableViewElement, "Name");
		String title = XmlUtils.getElementStringValue(tableViewElement, "Title");

        Element sourceFilterElement = XmlUtils.getChildElement(tableViewElement, "SourceFilter");
        ExpressionNode sourceFilterExpression = null;
        if (sourceFilterElement != null) {
            sourceFilterExpression = ExpressionManager.createExpressionNode(XmlUtils.getChildElement(sourceFilterElement, "Expression"));
        }

        Element resultFilterElement = XmlUtils.getChildElement(tableViewElement, "ResultFilter");
        ExpressionNode resultFilterExpression = null;
        if (resultFilterElement != null) {
            resultFilterExpression = ExpressionManager.createExpressionNode(XmlUtils.getChildElement(resultFilterElement, "Expression"));
        }

        String groupString = XmlUtils.getElementStringValue(tableViewElement, "Group");
        boolean group = false;
        if (groupString != null){
            group = Boolean.valueOf(groupString);
        }

		Element columnsElement = XmlUtils.getChildElement(tableViewElement, "Columns");
		Column[] columns = createColumns(columnsElement);

		return new TableReport(tableName, name, title, sourceFilterExpression, group, columns, resultFilterExpression);
	}

    private static UnionReport createUnionView(Element unionViewElement) {
        Element sourceQueriesElement = XmlUtils.getChildElement(unionViewElement, "SourceQueries");
        if (sourceQueriesElement == null) {
            throw new RuntimeException("UnionQuery with no SourceQueries");
        }

        ArrayList<String> sourceQueries = new ArrayList<>();

        for (Element sourceQueryElement : XmlUtils.getChildElements(sourceQueriesElement, "SourceQuery")) {
            sourceQueries.add(XmlUtils.getStringValue(sourceQueryElement));
        }
        String[] sourceQueriesArray = new String[sourceQueries.size()];
        sourceQueries.toArray(sourceQueriesArray);

        String name = XmlUtils.getElementStringValue(unionViewElement, "Name");
        String title = XmlUtils.getElementStringValue(unionViewElement, "Title");

        Element sourceFilterElement = XmlUtils.getChildElement(unionViewElement, "SourceFilter");
        ExpressionNode sourceFilterExpression = null;
        if (sourceFilterElement != null) {
            sourceFilterExpression = ExpressionManager.createExpressionNode(XmlUtils.getChildElement(sourceFilterElement, "Expression"));
        }

        Element resultFilterElement = XmlUtils.getChildElement(unionViewElement, "ResultFilter");
        ExpressionNode resultFilterExpression = null;
        if (resultFilterElement != null) {
            resultFilterExpression = ExpressionManager.createExpressionNode(XmlUtils.getChildElement(resultFilterElement, "Expression"));
        }

        String groupString = XmlUtils.getElementStringValue(unionViewElement, "Group");
        boolean group = false;
        if (groupString != null){
            group = Boolean.valueOf(groupString);
        }

        Element columnsElement = XmlUtils.getChildElement(unionViewElement, "Columns");
        Column[] columns = createColumns(columnsElement);

        return new UnionReport(sourceQueriesArray, name, title, sourceFilterExpression, group, columns, resultFilterExpression);
    }

    private static JoinReport createJoinView(Element joinViewElement) {
        Element sourceQueriesElement = XmlUtils.getChildElement(joinViewElement, "SourceQueries");
        String[] sourceQueries = new String[2]; // Assume 2 for now

        int i=0;

        for (Element sourceQueryElement : XmlUtils.getChildElements(sourceQueriesElement, "SourceQuery")) {
            sourceQueries[i++] = XmlUtils.getStringValue(sourceQueryElement);
        }

        Element joinRulesElement = XmlUtils.getChildElement(joinViewElement, "JoinRules");

        JoinRules joinRules = createJoinRules(joinRulesElement);

        String name = XmlUtils.getElementStringValue(joinViewElement, "Name");
        String title = XmlUtils.getElementStringValue(joinViewElement, "Title");

        Element sourceFilterElement = XmlUtils.getChildElement(joinViewElement, "SourceFilter");
        ExpressionNode sourceFilterExpression = null;
        if (sourceFilterElement != null) {
            sourceFilterExpression = ExpressionManager.createExpressionNode(XmlUtils.getChildElement(sourceFilterElement, "Expression"));
        }

        Element resultFilterElement = XmlUtils.getChildElement(joinViewElement, "ResultFilter");
        ExpressionNode resultFilterExpression = null;
        if (resultFilterElement != null) {
            resultFilterExpression = ExpressionManager.createExpressionNode(XmlUtils.getChildElement(resultFilterElement, "Expression"));
        }

        String groupString = XmlUtils.getElementStringValue(joinViewElement, "Group");
        boolean group = false;
        if (groupString != null){
            group = Boolean.valueOf(groupString);
        }

        Element columnsElement = XmlUtils.getChildElement(joinViewElement, "Columns");
        Column[] columns = createColumns(columnsElement);

        return new JoinReport(sourceQueries[0], sourceQueries[1], joinRules, name, title, sourceFilterExpression, group, columns, resultFilterExpression);
    }

    private static JoinRules createJoinRules(Element joinRulesElement) {
        List<List<String>> sourceKeyNames = new ArrayList<>();

        Element sourcesElement = XmlUtils.getChildElement(joinRulesElement, "Sources");

        for (Element sourceNamesElement : XmlUtils.getChildElements(sourcesElement, "SourceNames")) {
            List<String> keyNames = new ArrayList<>();
            for (Element keyNameElement : XmlUtils.getChildElements(sourceNamesElement, "SourceName")) {
                keyNames.add(XmlUtils.getStringValue(keyNameElement));
            }
            sourceKeyNames.add(keyNames);
        }

        String typeString = XmlUtils.getElementStringValue(joinRulesElement, "Type");
        JoinType type = JoinType.valueOf(typeString);

        String[] left = new String[1];
        String[] right = new String[1];
        return new JoinRules(sourceKeyNames.get(0).toArray(left), sourceKeyNames.get(1).toArray(right), type);
    }
	
	private static Column[] createColumns(Element columnsElement) {
		ArrayList<Column> columnList = new ArrayList<Column>();
		for (Element columnElement : XmlUtils.getChildElements(columnsElement, "Column")) {

            String name = XmlUtils.getElementStringValue(columnElement, "Name");

			String header = XmlUtils.getElementStringValue(columnElement, "Header");

            String widthString = XmlUtils.getElementStringValue(columnElement, "Width");
            int width = 200;
            if (widthString != null) {
                width = Integer.valueOf(widthString);
            }

            String groupRuleString = XmlUtils.getElementStringValue(columnElement, "GroupRule");
            GroupRule groupRule = null;
            if (groupRuleString != null) {
                groupRule = GroupRule.valueOf(groupRuleString);
            }

            String sortRuleString = XmlUtils.getElementStringValue(columnElement, "SortRule");
            SortRule sortRule = SortRule.Ascending;
            if (sortRuleString != null) {
                sortRule = SortRule.valueOf(sortRuleString);
            }

			Element expressionElement = XmlUtils.getChildElement(columnElement, "ColumnExpression");
			ExpressionNode expression = ExpressionManager.createExpressionNode(XmlUtils.getChildElement(expressionElement, "Expression"));

			String dataTypeString = XmlUtils.getElementStringValue(columnElement, "DataType");
			DataType dataType = DataType.valueOf(dataTypeString);

			columnList.add(new Column(header, width, name, groupRule, sortRule, expression, dataType));
		}
		return columnList.toArray(new Column[columnList.size()]);
	}

    private Map<String,Object> createParameters(Element parametersElement) {
        Map<String,Object> parameters = new HashMap<>();

        for (Element parameterElement : XmlUtils.getChildElements(parametersElement, "Parameter")) {
            String name = XmlUtils.getElementStringValue(parameterElement, "Name");
            String value = XmlUtils.getElementStringValue(parameterElement, "Value");
            String type = XmlUtils.getElementStringValue(parameterElement, "Type");
            DataType dataType = DataType.valueOf(type);
            Object parsedValue = null;
            switch (dataType) {
                case String:
                    parsedValue = value;
                    break;
                case Integer:
                    parsedValue = Integer.valueOf(value);
                    break;
                case Number:
                    parsedValue = Double.valueOf(value);
                    break;
                case Boolean:
                    parsedValue = Boolean.valueOf(value);
                    break;
                case Date:
                    parsedValue = Date.valueOf(value);
                    break;
                case Time:
                    parsedValue = Time.valueOf(value);
                    break;
                default:
                    throw new RuntimeException("Unknown data type: " + dataType.name());
            }

            parameters.put(name, parsedValue);
        }

        return parameters;
    }

    private static Chart createChart(Element chartElement) {
        String type = XmlUtils.getElementStringValue(chartElement, "Type");
        String title = XmlUtils.getElementStringValue(chartElement, "Title");
        String xAxisTitle = XmlUtils.getElementStringValue(chartElement, "XAxisTitle");
        String yAxisTitle = XmlUtils.getElementStringValue(chartElement, "YAxisTitle");
        String categoryColumn = XmlUtils.getElementStringValue(chartElement, "CategoryColumn");
        String seriesNameColumn = XmlUtils.getElementStringValue(chartElement, "SeriesNameColumn");
        List<String> seriesDataColumnList = new ArrayList<>();
        Element seriesDataColumnsElement = XmlUtils.getChildElement(chartElement, "SeriesDataColumns");
        for (Element seriesDataColumnElement : XmlUtils.getChildElements(seriesDataColumnsElement, "SeriesDataColumn")) {
            seriesDataColumnList.add(XmlUtils.getStringValue(seriesDataColumnElement));
        }
        Chart chart = new Chart();
        chart.setType(type);
        chart.setTitle(title);
        chart.setXAxisTitle(xAxisTitle);
        chart.setYAxisTitle(yAxisTitle);
        chart.setCategoryColumnName(categoryColumn);
        chart.setSeriesNameColumnName(seriesNameColumn);
        chart.setSeriesDataColumnNames(seriesDataColumnList.toArray(new String[seriesDataColumnList.size()]));
        return chart;
    }
}
