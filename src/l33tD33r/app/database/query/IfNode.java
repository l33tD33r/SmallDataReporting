package l33tD33r.app.database.query;

/**
 * Created by Simon on 2/17/14.
 */
public class IfNode extends ExpressionNode {

    private ExpressionNode conditionNode;

    private ExpressionNode trueResultNode;

    private ExpressionNode falseResultNode;

    public IfNode(ExpressionNode conditionNode, ExpressionNode trueResultNode, ExpressionNode falseResultNode) {
        this.conditionNode = conditionNode;
        this.trueResultNode = trueResultNode;
        this.falseResultNode = falseResultNode;
    }

    @Override
    public ExpressionNodeType getType() {
        return ExpressionNodeType.If;
    }

    @Override
    protected Object evaluateValue(IDataRow dataRow) {
        if (conditionNode.evaluateBoolean(dataRow)) {
            return trueResultNode.evaluate(dataRow);
        } else {
            return falseResultNode.evaluate(dataRow);
        }
    }
}
