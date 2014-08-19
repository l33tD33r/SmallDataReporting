package l33tD33r.app.database.query;


public class ValueNode extends ExpressionNode {
	
	private Object value;
	
	public ValueNode(Object value) {
		this.value = value;
	}

	@Override
	public ExpressionNodeType getType() {
		return ExpressionNodeType.Value;
	}

	@Override
	public Object evaluate(IDataRow dataRow) {
		return value;
	}

	@Override
	protected Object evaluateValue(IDataRow dataRow) {
		return value;
	}
}
