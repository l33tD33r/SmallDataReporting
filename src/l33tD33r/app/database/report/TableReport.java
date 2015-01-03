package l33tD33r.app.database.report;

import l33tD33r.app.database.query.*;

public class TableReport extends Report {

	private String tableName;
	
	protected TableReport(String tableName, String name, String title, boolean hidden, ExpressionNode sourceFilterExpression, boolean group, Column[] columns, ExpressionNode resultFilterExpression) {
		super(name, title, hidden, sourceFilterExpression, group, columns, resultFilterExpression);
		this.tableName = tableName;
	}

	@Override
	protected Query createQuery() {
		Column[] columns = getColumns();
		String[] columnsName = new String[columns.length];
		GroupRule[] columnsGroupRule = new GroupRule[columns.length];
		SortRule[] columnsSortRule = new SortRule[columns.length];
		ExpressionNode[] columnsExpression = new ExpressionNode[columns.length];
		ColumnSummarization[] columnSummarizations = new ColumnSummarization[columns.length];
		DataType[] columnsDataType = new DataType[columns.length];
		for (int i=0; i < columns.length; i++) {
			Column column = columns[i];
			columnsName[i] = column.getName();
			columnsGroupRule[i] = column.getGroupRule();
			columnsSortRule[i] = column.getSortRule();
			columnsExpression[i] = column.getExpression();
			columnsDataType[i] = column.getDataType();
			columnSummarizations[i] = column.getSummarization();
		}
		return QueryManager.createTableQuery(tableName, getName(), getSourceFilterExpression(), getGroup(), columnsName, columnsGroupRule, columnsSortRule, columnsExpression, columnsDataType, columnSummarizations, getResultFilterExpression());
	}
}
