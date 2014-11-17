package l33tD33r.app.database.query;

/**
 * Created by Simon on 11/14/2014.
 */
public class AppendNode extends ExpressionNode {
    @Override
    public ExpressionNodeType getType() {
        return ExpressionNodeType.Append;
    }

    @Override
    protected Object evaluateValue(IDataRow dataRow) {
        return null;
    }
}
