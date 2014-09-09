package l33tD33r.app.database.report;

import l33tD33r.app.database.query.DataType;
import l33tD33r.app.database.query.ExpressionNode;
import l33tD33r.app.database.query.GroupRule;
import l33tD33r.app.database.query.Query;
import l33tD33r.app.database.query.QueryManager;
import l33tD33r.app.database.query.SortRule;

public class TableReport extends Report {

	private String tableName;
	
	protected TableReport(String tableName, String name, String title, ExpressionNode sourceFilterExpression, boolean group, Column[] columns, ExpressionNode resultFilterExpression) {
		super(name, title, sourceFilterExpression, group, columns, resultFilterExpression);
		this.tableName = tableName;
	}

	@Override
	protected Query createQuery() {
		Column[] columns = getColumns();
		String[] columnsName = new String[columns.length];
		GroupRule[] columnsGroupRule = new GroupRule[columns.length];
		SortRule[] columnsSortRule = new SortRule[columns.length];
		ExpressionNode[] columnsExpression = new ExpressionNode[columns.length];
		DataType[] columnsDataType = new DataType[columns.length];
		for (int i=0; i < columns.length; i++) {
			Column column = columns[i];
			columnsName[i] = column.getName();
			columnsGroupRule[i] = column.getGroupRule();
			columnsSortRule[i] = column.getSortRule();
			columnsExpression[i] = column.getExpression();
			columnsDataType[i] = column.getDataType();
		}
		return QueryManager.createTableQuery(tableName, getName(), getSourceFilterExpression(), getGroup(), columnsName, columnsGroupRule, columnsSortRule, columnsExpression, columnsDataType, getResultFilterExpression());
	}
}
