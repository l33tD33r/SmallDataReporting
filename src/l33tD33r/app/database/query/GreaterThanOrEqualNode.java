package l33tD33r.app.database.query;

import l33tD33r.app.database.Date;
import l33tD33r.app.database.Time;

/**
 * Created by Simon on 7/30/14.
 */
public class GreaterThanOrEqualNode extends ExpressionNode {

    private ExpressionNode leftNode;
    private ExpressionNode rightNode;

    public GreaterThanOrEqualNode(ExpressionNode leftNode, ExpressionNode rightNode) {
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    @Override
    public ExpressionNodeType getType() { return ExpressionNodeType.GreaterThanOrEqual; }

    @Override
    protected Object evaluateValue(IDataRow dataRow) {
        Object leftValue = leftNode.evaluate(dataRow);
        Object rightValue = rightNode.evaluate(dataRow);

        DataType leftDataType = getDataType(leftValue);
        DataType rightDataType = getDataType(rightValue);

        if (leftDataType != rightDataType) {
            throw new RuntimeException("GreaterThanOrEqual - left side type:" + leftDataType.name() + " does not match right side type:" + rightDataType.name());
        }

        switch (leftDataType) {
            case String: {
                String leftString = (String)leftValue;
                String rightString = (String)rightValue;

                return Boolean.valueOf(leftString.compareTo(rightString) >= 0);
            }
            case Integer: {
                Integer leftInteger = (Integer)leftValue;
                Integer  rightInteger = (Integer)rightValue;

                return  Boolean.valueOf(leftInteger >= rightInteger);
            }
            case Number: {
                Double leftDouble = (Double)leftValue;
                Double rightDouble = (Double)rightValue;

                return Boolean.valueOf(leftDouble >= rightDouble);
            }
            case Boolean: {
                Boolean leftBoolean = (Boolean)leftValue;
                Boolean rightBoolean = (Boolean)rightValue;

                return Boolean.valueOf(leftBoolean.compareTo(rightBoolean) >= 0);
            }
            case Date: {
                Date leftDate = (Date)leftValue;
                Date rightDate = (Date)rightValue;

                return Boolean.valueOf(leftDate.compareTo(rightDate) >= 0);
            }
            case Time: {
                Time leftTime = (Time)leftValue;
                Time rightTime = (Time)rightValue;

                return Boolean.valueOf(leftTime.compareTo(rightTime) >= 0);
            }
            default: {
                throw new RuntimeException("Unknown DataType:" + leftDataType.name());
            }
        }
    }
}
