package l33tD33r.app.database.query;


public class Column {

	private String name;
	private DataType dataType;
	private GroupRule groupRule;
	private SortRule sortRule;
	private ExpressionNode expression;
	private ColumnSummarization summarization;

	public Column(String name, GroupRule groupRule, SortRule sortRule, ExpressionNode expression, DataType dataType, ColumnSummarization summarization) {
		this.name = name;
		this.groupRule = groupRule;
		this.sortRule = sortRule;
		this.expression = expression;
		this.dataType = dataType;
		this.summarization = summarization;
	}
	
	public String getName() {
		return name;
	}
	
	public GroupRule getGroupRule() {
		return groupRule;
	}
	
	public SortRule getSortRule() {
		return sortRule;
	}
	
	public DataType getDataType() {
		return dataType;
	}
	
	public ExpressionNode getExpression() {
		return expression;
	}

	public ColumnSummarization getSummarization() {
		return summarization;
	}

	public boolean hasSummarization() {
		return getSummarization() != null;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Column) {
			return equals((Column)obj);
		}
		return false;
	}

	public boolean equals(Column column) {
		return getName().equalsIgnoreCase(column.getName());
	}

	@Override
	public int hashCode() {
		return getName().toLowerCase().hashCode();
	}
}
