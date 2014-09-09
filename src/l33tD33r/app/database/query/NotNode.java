package l33tD33r.app.database.query;

/**
 * Created by Simon on 8/24/2014.
 */
public class NotNode extends ExpressionNode {

    private ExpressionNode child;

    public NotNode(ExpressionNode child) {
        this.child = child;
    }

    @Override
    public ExpressionNodeType getType() {
        return ExpressionNodeType.Not;
    }

    @Override
    protected Object evaluateValue(IDataRow dataRow) {
        return !(child.evaluateBoolean(dataRow));
    }
}
