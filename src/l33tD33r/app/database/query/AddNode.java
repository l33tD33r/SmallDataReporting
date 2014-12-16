package l33tD33r.app.database.query;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 11/14/2014.
 */
public class AddNode extends ExpressionNode {

    private DataType resultDataType;

    private List<ExpressionNode> expressionChildren;

    public AddNode(DataType resultDataType, List<ExpressionNode> expressionChildren) {
        this.resultDataType = resultDataType;
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
        return addValues.getResult();
    }

    private class ValueCombination {
        private DataType dataType;
    }

    private class AddValues {

        private ArrayList<Object> values;

        public AddValues(ArrayList<Object> values) {
            this.values = values;
        }

        public Object getResult() {

            switch (resultDataType) {
                case Integer:
                    return getIntegerResult();
                default:
                    throw new RuntimeException(MessageFormat.format("Unsupported result data type ''{0}''", resultDataType.name()));
            }
        }

        private Integer getIntegerResult() {
            Integer result = 0;

            for (Object value : values) {
                if (value instanceof Integer) {
                    result += (Integer)value;
                } else if (value instanceof Double) {
                    result += ((Double)value).intValue();
                } else {
                    throw new RuntimeException(MessageFormat.format("Value ''{0}'' of type ''{1}'' is not valid for Integer result", value, value.getClass()));
                }
            }

            return result;
        }
    }
}
