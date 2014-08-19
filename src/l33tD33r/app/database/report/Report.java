package l33tD33r.app.database.report;

import l33tD33r.app.database.query.ExpressionNode;
import l33tD33r.app.database.query.Query;

public abstract class Report {
	
	private String name;
	
	private String title;

	private ExpressionNode sourceFilterExpression;

	private boolean group;
	
	private Column[] columns;

    private ExpressionNode resultFilterExpression;
	
	private Query query;
	
	protected Report(String name, String title, ExpressionNode sourceFilterExpression, boolean group, Column[] columns, ExpressionNode resultFilterExpression) {
		this.name = name;
		this.title = title;
		this.sourceFilterExpression = sourceFilterExpression;
		this.group = group;
		this.columns = columns;
        this.resultFilterExpression = resultFilterExpression;
		this.query = null;
	}
	
	public String getName() {
		return name;
	}
	
	public String getTitle() {
		return title;
	}
	
	protected abstract Query createQuery();
	
	public Query getQuery() {
		if (query == null) {
			query = createQuery();
		}
		return query;
	}
	
	protected ExpressionNode getSourceFilterExpression() {
		return sourceFilterExpression;
	}

    protected ExpressionNode getResultFilterExpression() { return resultFilterExpression; }
	
	protected Boolean getGroup() {
		return group;
	}
	
	protected Column[] getColumns() {
		return columns;
	}
	
	public int getColumnCount() {
		return columns.length;
	}
	
	public String getColumnHeader(int columnIndex) {
		return columns[columnIndex].getHeader();
	}

    public String getColumnName(int columnIndex) {
        return columns[columnIndex].getName();
    }
	
	public int getColumnWidth(int columnIndex) {
		return columns[columnIndex].getWidth();
	} 
}
