package l33tD33r.app.database.report;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import l33tD33r.app.database.query.DataType;
import l33tD33r.app.database.query.ExpressionNode;
import l33tD33r.app.database.query.GroupRule;
import l33tD33r.app.database.query.SortRule;
import l33tD33r.app.database.utility.XmlUtils;

public class ReportSerialization {

	public static List<Report> deserializeReports(Document reportsDocument) {
		Element reportsElement = reportsDocument.getDocumentElement();
		
		ArrayList<Report> reports = new ArrayList<Report>();
		
		for (Element reportElement : XmlUtils.getChildElements(reportsElement, "Report")) {
			reports.add(createReport(reportElement));
		}
		
		return reports;
	}
	
	private static Report createReport(Element reportElement) {
		String type = reportElement.getAttribute("type");
		if ("Table".equals(type)) {
			return createTableView(reportElement);
		}
		throw new RuntimeException("Unknown view type: " + type);
	}
	
	private static TableReport createTableView(Element tableViewElement) {
		String tableName = XmlUtils.getElementStringValue(tableViewElement, "Table");
		String name = XmlUtils.getElementStringValue(tableViewElement, "Name");
		String title = XmlUtils.getElementStringValue(tableViewElement, "Title");
		Element filterElement = XmlUtils.getChildElement(tableViewElement, "Filter");
        Element resultFilterElement = XmlUtils.getChildElement(tableViewElement, "ResultFilter");
		String groupString = XmlUtils.getElementStringValue(tableViewElement, "Group");
		boolean group = Boolean.valueOf(groupString);
		ExpressionNode filterExpression = ExpressionManager.createExpressionNode(XmlUtils.getChildElement(filterElement, "Expression"));
		Element columnsElement = XmlUtils.getChildElement(tableViewElement, "Columns");
		Column[] columns = createColumns(columnsElement);
        ExpressionNode resultFilterExpression = null;
        if (resultFilterElement != null) {
            resultFilterExpression = ExpressionManager.createExpressionNode(XmlUtils.getChildElement(resultFilterElement, "Expression"));
        }
		return new TableReport(tableName, name, title, filterExpression, group, columns, resultFilterExpression);
	}
	
	private static Column[] createColumns(Element columnsElement) {
		ArrayList<Column> columnList = new ArrayList<Column>();
		for (Element columnElement : XmlUtils.getChildElements(columnsElement, "Column")) {
			String header = XmlUtils.getElementStringValue(columnElement, "Header");
			String widthString = XmlUtils.getElementStringValue(columnElement, "Width");
			int width = Integer.valueOf(widthString);
			String name = XmlUtils.getElementStringValue(columnElement, "Name");
			String groupRuleString = XmlUtils.getElementStringValue(columnElement, "GroupRule");
			GroupRule groupRule = (groupRuleString == null || groupRuleString.isEmpty()) ? null : GroupRule.valueOf(groupRuleString);
			String sortRuleString = XmlUtils.getElementStringValue(columnElement, "SortRule");
			SortRule sortRule = (groupRuleString == null || groupRuleString.isEmpty()) ? SortRule.Ascending : SortRule.valueOf(sortRuleString);
			Element expressionElement = XmlUtils.getChildElement(columnElement, "ColumnExpression");
			ExpressionNode expression = ExpressionManager.createExpressionNode(XmlUtils.getChildElement(expressionElement, "Expression"));
			String dataTypeString = XmlUtils.getElementStringValue(columnElement, "DataType");
			DataType dataType = DataType.valueOf(dataTypeString);
			columnList.add(new Column(header, width, name, groupRule, sortRule, expression, dataType));
		}
		return columnList.toArray(new Column[columnList.size()]);
	}
}
