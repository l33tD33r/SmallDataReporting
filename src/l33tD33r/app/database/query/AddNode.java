package l33tD33r.app.database.query;

import java.util.ArrayList;
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
        ArrayList<Object> values = new ArrayList<>();
        for (ExpressionNode childNode : expressionChildren) {
            Object childValue = childNode.evaluate(dataRow);
            values.add(childValue);
        }

        AddValues addValues = new AddValues(values);
        return addValues.getValue();
    }

    private class ValueCombination {
        private DataType dataType;
    }

    private class AddValues {

        private ArrayList<Object> values;

        public AddValues(ArrayList<Object> values) {
            this.values = values;
        }

        public Object getValue() {
            return null;
        }
    }
}
