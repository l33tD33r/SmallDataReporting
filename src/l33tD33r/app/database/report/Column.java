package l33tD33r.app.database.report;

import l33tD33r.app.database.query.DataType;
import l33tD33r.app.database.query.ExpressionNode;
import l33tD33r.app.database.query.GroupRule;
import l33tD33r.app.database.query.SortRule;

public class Column {

	private String header;
	private int width;
	private String name;
	private GroupRule groupRule;
	private SortRule sortRule;
	private ExpressionNode expression;
	private DataType dataType;
	
	public Column(String header, int width, String name, GroupRule groupRule, SortRule sortRule, ExpressionNode expression, DataType dataType) {
		this.header = header;
		this.width = width;
		this.name = name;
		this.groupRule = groupRule;
		this.sortRule = sortRule;
		this.expression = expression;
		this.dataType = dataType;
	}
	
	public String getHeader() {
		return header;
	}
	
	public int getWidth() {
		return width;
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
	
	public ExpressionNode getExpression() {
		return expression;
	}
	
	public DataType getDataType() {
		return dataType;
	}
}
