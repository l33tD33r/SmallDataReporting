package l33tD33r.app.database.query;

import java.util.List;

/**
 * Created by Simon on 2/17/14.
 */
public class AndNode extends ExpressionNode {

    private List<ExpressionNode> expressionChildren;

    public AndNode(List<ExpressionNode> expressionChildren) {
        this.expressionChildren = expressionChildren;
    }

    @Override
    public ExpressionNodeType getType() {
        return ExpressionNodeType.Add;
    }

    @Override
    protected Object evaluateValue(IDataRow dataRow) {
        for (ExpressionNode expressionNode : expressionChildren) {
            if (!expressionNode.evaluateBoolean(dataRow)) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }
}
