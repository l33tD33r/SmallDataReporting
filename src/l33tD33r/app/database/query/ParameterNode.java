package l33tD33r.app.database.query;

/**
 * Created by Simon on 8/22/2014.
 */
public class ParameterNode extends ExpressionNode {

    private String parameterName;

    public ParameterNode(String parameterName) {
        this.parameterName = parameterName;
    }

    @Override
    public ExpressionNodeType getType() {
        return ExpressionNodeType.Parameter;
    }

    @Override
    protected Object evaluateValue(IDataRow dataRow) {
        return dataRow.getContext().getParameter(parameterName);
    }
}
