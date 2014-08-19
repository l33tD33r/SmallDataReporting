package l33tD33r.app.database.query;

import l33tD33r.app.database.Date;
import l33tD33r.app.database.Time;

public abstract class ExpressionNode {
	
	public ExpressionNode() {
	}
	
	public abstract ExpressionNodeType getType();
	
	protected abstract Object evaluateValue(IDataRow dataRow);
	
	public Object evaluate(IDataRow dataRow) {
		return evaluateValue(dataRow);
	}
	
	public String evaluateString(IDataRow dataRow) {
		Object value = evaluate(dataRow);
		if (!(value instanceof String)) {
			throw new RuntimeException(getTypeString(value.getClass()) + " is not a String");
		}
		return (String)value;
	}
	
	public Integer evaluateInteger(IDataRow dataRow) {
		Object value = evaluate(dataRow);
		if (!(value instanceof Integer)) {
			throw new RuntimeException(getTypeString(value.getClass()) + " is not a Integer");
		}
		return (Integer)value;
	}
	
	public Double evaluateNumber(IDataRow dataRow) {
		Object value = evaluate(dataRow);
		if (!(value instanceof Double)) {
			throw new RuntimeException(getTypeString(value.getClass()) + " is not a Number");
		}
		return (Double)value;
	}
	
	public Boolean evaluateBoolean(IDataRow dataRow) {
		Object value = evaluate(dataRow);
		if (!(value instanceof Boolean)) {
			throw new RuntimeException(getTypeString(value.getClass()) + " is not a Boolean");
		}
		return (Boolean)value;
	}
	
	public Date evaluateDate(IDataRow dataRow) {
		Object value = evaluate(dataRow);
		if (!(value instanceof Date)) {
			throw new RuntimeException(getTypeString(value.getClass()) + " is not a Date");
		}
		return (Date)value;
	}
	
	public Time evaluateTime(IDataRow dataRow) {
		Object value = evaluate(dataRow);
		if (!(value instanceof Time)) {
			throw new RuntimeException(getTypeString(value.getClass()) + " is not a Time");
		}
		return (Time)value;
	}
	
	private String getTypeString(Class<?> c) {
		if (String.class.equals(c)) {
			return "String";
		} else if (Integer.class.equals(c)) {
			return "Integer";
		} else if (Double.class.equals(c)) {
			return "Number";
		} else if (Boolean.class.equals(c)) {
			return "Boolean";
		} else if (Date.class.equals(c)) {
			return "Date";
		} else if (Date.class.equals(c)) {
			return "Time";
		} else {
			return c.getName();
		}
	}

    protected static DataType getDataType(Object value) {
        if (value instanceof String) {
            return DataType.String;
        }
        if (value instanceof Integer) {
            return DataType.Integer;
        }
        if (value instanceof Double) {
            return DataType.Number;
        }
        if (value instanceof Boolean){
            return DataType.Boolean;
        }
        if (value instanceof Date) {
            return DataType.Date;
        }
        if (value instanceof Time) {
            return DataType.Time;
        }
        throw new RuntimeException("Unknown data type:" + value.getClass().getName());
    }
}
