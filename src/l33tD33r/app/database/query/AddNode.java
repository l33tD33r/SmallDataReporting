package l33tD33r.app.database.query;

import java.util.List;

/**
 * Created by Simon on 11/14/2014.
 */
public class AddNode extends ExpressionNode {

    private List<ExpressionNode> expressionChildren;

    public AddNode(List<ExpressionNode> expressionChildren) {
        this.expressionChildren = expressionChildren;
    }

    @Override
    public ExpressionNodeType getType() {
        return ExpressionNodeType.Add;
    }

    @Override
    protected Object evaluateValue(IDataRow dataRow) {
        Object firstValue = null;
        for (ExpressionNode childNode : expressionChildren) {
            Object childValue = childNode.evaluate(dataRow);
        }
    }

    private class ValueCombination {
        private DataType dataType;
    }
}
