package l33tD33r.app.database.report;

import l33tD33r.app.database.query.*;

/**
 * Created by Simon on 8/21/2014.
 */
public class JoinReport extends Report {

    private String leftSideReportName;
    private String rightSideReportName;

    private JoinRules rules;

    public JoinReport(String leftSideReportName, String rightSideReportName, JoinRules rules, String name, String title, ExpressionNode sourceFilterExpression, boolean group, Column[] columns, ExpressionNode resultFilterExpression) {
        super(name, title, sourceFilterExpression, group, columns, resultFilterExpression);

        this.leftSideReportName = leftSideReportName;
        this.rightSideReportName = rightSideReportName;
        this.rules = rules;
    }

    @Override
    protected Query createQuery() {

        Report leftSideReport = ReportManager.getSingleton().getReport(leftSideReportName);
        Report rightSideReport = ReportManager.getSingleton().getReport(rightSideReportName);

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

        return QueryManager.createJoinQuery(leftSideReport.getQuery(), rightSideReport.getQuery(), rules, getName(), getSourceFilterExpression(), getGroup(), columnsName, columnsGroupRule, columnsSortRule, columnsExpression, columnsDataType, getResultFilterExpression());
    }
}
