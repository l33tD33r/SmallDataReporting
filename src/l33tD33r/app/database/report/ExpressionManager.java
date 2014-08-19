package l33tD33r.app.database.report;

import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import l33tD33r.app.database.Date;
import l33tD33r.app.database.Time;
import l33tD33r.app.database.query.*;
import l33tD33r.app.database.utility.XmlUtils;

public class ExpressionManager {

	public static ExpressionNode createExpressionNode(String expressionTree) {
		Document expressionDocument;
		try {
			expressionDocument = XmlUtils.deserializeDocument(expressionTree);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Element expressionNodeElement = expressionDocument.getDocumentElement();
		return createExpressionNode(expressionNodeElement);
	}
	
	protected static ExpressionNode createExpressionNode(Element expressionNodeElement) {
        if (expressionNodeElement == null) {
            return null;
        }
		String type = expressionNodeElement.getAttribute("type");

        switch (type) {
            case "Field": {
                return createFieldNode(expressionNodeElement);
            }
            case "Column": {
                return createColumnNode(expressionNodeElement);
            }
            case "Equals": {
                return createEqualsNode(expressionNodeElement);
            }
            case "GreaterThanOrEqual": {
                return createGreatorThanOrEqualsNode(expressionNodeElement);
            }
            case "Value": {
                return createValueNode(expressionNodeElement);
            }
            case "If": {
                return createIfNode(expressionNodeElement);
            }
            case "And": {
                return createAndNode(expressionNodeElement);
            }
            case "SetCount": {
                return createSetCount(expressionNodeElement);
            }
        }
		throw new RuntimeException("Unknown expression type: " + type);
	}
	
	private static FieldNode createFieldNode(Element fieldNodeElement) {
		String fieldPathExpression = XmlUtils.getElementStringValue(fieldNodeElement, "FieldPath");
		String[] fieldPath;
		fieldPath = fieldPathExpression.split("\\.");
//		if (fieldPathExpression.contains(".")) {
//			fieldPath = fieldPathExpression.split("\\.");
//		} else {
//			fieldPath = new String[] {fieldPathExpression};
//		}
		return new FieldNode(fieldPath);
	}
	
	private static ColumnNode createColumnNode(Element columnNodeElement) {
		String columnName = XmlUtils.getElementStringValue(columnNodeElement, "Name");
		return new ColumnNode(columnName);
	}
	
	private static ValueNode createValueNode(Element valueNodeElement) {
		String type = XmlUtils.getElementStringValue(valueNodeElement, "Type");
		String value = XmlUtils.getElementStringValue(valueNodeElement, "Value");
		DataType dataType = DataType.valueOf(type);
		switch (dataType) {
			case String:
				return new ValueNode(value);
			case Integer:
				return new ValueNode(Integer.valueOf(value));
			case Number:
				return new ValueNode(Double.valueOf(value));
			case Boolean:
				return new ValueNode(Boolean.valueOf(value));
			case Date:
				return new ValueNode(Date.valueOf(value));
			case Time:
				return new ValueNode(Time.valueOf(value));
			default:
				throw new RuntimeException("Unknown data type: " + dataType.name());
		}
	}
	
	private static EqualsNode createEqualsNode(Element equalsNodeElement) {
		Element leftElement = XmlUtils.getChildElement(equalsNodeElement, "Left");
		Element rightElement = XmlUtils.getChildElement(equalsNodeElement, "Right");
		
		return new EqualsNode(createExpressionNode(XmlUtils.getChildElement(leftElement, "Expression")), createExpressionNode(XmlUtils.getChildElement(rightElement, "Expression")));
	}

    private static GreaterThanOrEqualNode createGreatorThanOrEqualsNode(Element greaterThanOrEqualsNodeElement) {
        Element leftElement = XmlUtils.getChildElement(greaterThanOrEqualsNodeElement, "Left");
        Element rightElement = XmlUtils.getChildElement(greaterThanOrEqualsNodeElement, "Right");

        return new GreaterThanOrEqualNode(createExpressionNode(XmlUtils.getChildElement(leftElement, "Expression")), createExpressionNode(XmlUtils.getChildElement(rightElement, "Expression")));
    }

    private static IfNode createIfNode(Element ifNodeElement) {
        Element conditionElement = XmlUtils.getChildElement(ifNodeElement, "Condition");
        Element trueValueElement = XmlUtils.getChildElement(ifNodeElement, "TrueValue");
        Element falseValueElement = XmlUtils.getChildElement(ifNodeElement, "FalseValue");

        return new IfNode(
                createExpressionNode(XmlUtils.getChildElement(conditionElement, "Expression")),
                createExpressionNode(XmlUtils.getChildElement(trueValueElement, "Expression")),
                createExpressionNode(XmlUtils.getChildElement(falseValueElement, "Expression"))
        );
    }

    private static AndNode createAndNode(Element andNodeElement) {
        ArrayList<ExpressionNode> andChildren = new ArrayList<>();

        Element childrenElement = XmlUtils.getChildElement(andNodeElement, "Children");

        for (Element andChildElement : XmlUtils.getChildElements(childrenElement, "Expression")) {
            andChildren.add(createExpressionNode(andChildElement));
        }

        return new AndNode(andChildren);
    }

    private static SetCountNode createSetCount(Element setCountElement) {
        String fieldPathExpression = XmlUtils.getElementStringValue(setCountElement, "FieldPath");
        String[] fieldPath;
        fieldPath = fieldPathExpression.split("\\.");
        return new SetCountNode(fieldPath);
    }
}
