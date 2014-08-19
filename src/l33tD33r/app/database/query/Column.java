package l33tD33r.app.database.query;


public class Column {

	private String name;
	private DataType dataType;
	private GroupRule groupRule;
	private SortRule sortRule;
	private ExpressionNode expression;

	public Column(String name, GroupRule groupRule, SortRule sortRule, ExpressionNode expression, DataType dataType) {
		this.name = name;
		this.groupRule = groupRule;
		this.sortRule = sortRule;
		this.expression = expression;
		this.dataType = dataType;
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
}
