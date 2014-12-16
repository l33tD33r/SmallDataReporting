package l33tD33r.app.database.report;

import l33tD33r.app.database.query.*;

/**
 * Created by Simon on 12/16/2014.
 */
public class UnionReport extends Report {

    private String[] sourceReportNames;

    public UnionReport(String[] sourceReportNames, String name, String title, ExpressionNode sourceFilterExpression, boolean group, Column[] columns, ExpressionNode resultFilterExpression) {
        super(name, title, sourceFilterExpression, group, columns, resultFilterExpression);

        this.sourceReportNames = sourceReportNames;
    }

    @Override
    protected Query createQuery() {

        Query[] sourceQueries = new Query[sourceReportNames.length];
        for (int i=0; i < sourceReportNames.length; i++) {
            Report componentReport = ReportManager.getSingleton().getReport(sourceReportNames[i]);
            sourceQueries[i] = componentReport.getQuery();
        }

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

        return QueryManager.createUnionQuery(sourceQueries, getName(), getSourceFilterExpression(), getGroup(), columnsName, columnsGroupRule, columnsSortRule, columnsExpression, columnsDataType, getResultFilterExpression());
    }
}
