package l33tD33r.app.database.query;


public class EqualsNode extends ExpressionNode {

	private ExpressionNode leftNode;
	private ExpressionNode rightNode;
	
	public EqualsNode(ExpressionNode leftNode, ExpressionNode rightNode) {
		this.leftNode = leftNode;
		this.rightNode = rightNode;
	}
	
	@Override
	public ExpressionNodeType getType() {
		return ExpressionNodeType.Equal;
	}

	@Override
	protected Object evaluateValue(IDataRow dataRow) {
		Object valueA = leftNode.evaluate(dataRow);
		Object valueB = rightNode.evaluate(dataRow);
		return Boolean.valueOf(valueA.equals(valueB));
	}
}
