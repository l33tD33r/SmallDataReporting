package l33tD33r.app.database.report;

import l33tD33r.app.database.query.ExpressionNode;
import l33tD33r.app.database.query.Query;
import l33tD33r.app.database.report.visualization.Chart;

import java.util.Map;

public abstract class Report {
	
	private String name;
	
	private String title;

	private boolean hidden;

	private ExpressionNode sourceFilterExpression;

	private boolean group;
	
	private Column[] columns;

    private ExpressionNode resultFilterExpression;

    private Map<String,Object> parameters;

    private Chart chart;
	
	private Query query;
	
	protected Report(String name, String title, boolean hidden, ExpressionNode sourceFilterExpression, boolean group, Column[] columns, ExpressionNode resultFilterExpression) {
		this.name = name;
		this.title = title;
		this.hidden = hidden;
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

	public boolean isHidden() {
		return hidden;
	}
	
	protected abstract Query createQuery();
	
	public Query getQuery() {
		if (query == null) {
			query = createQuery();
			if (getParameters() != null) {
				query.setParameters(getParameters());
			}
		}
		return query;
	}

	public void resetQuery() {
		query = null;
	}
	
	protected ExpressionNode getSourceFilterExpression() {
		return sourceFilterExpression;
	}

    protected ExpressionNode getResultFilterExpression() { return resultFilterExpression; }
	
	protected Boolean getGroup() {
		return group;
	}

	public Column[] getColumns() {
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

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }

    public Map<String,Object> getParameters() {
        return parameters;
    }
    public void setParameters(Map<String,Object> parameters) {
        this.parameters = parameters;
    }
}
