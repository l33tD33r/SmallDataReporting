package l33tD33r.app.database.query;

public class ColumnNode extends ExpressionNode {

	private String columnName;
	
	public ColumnNode(String columnName) {
		this.columnName = columnName;
	}
	
	@Override
	public ExpressionNodeType getType() {
		return ExpressionNodeType.Column;
	}
	
	@Override
	protected Object evaluateValue(IDataRow dataRow) {
		return dataRow.getValue(columnName);
	}
}
