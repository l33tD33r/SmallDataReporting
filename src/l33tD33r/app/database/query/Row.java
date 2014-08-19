package l33tD33r.app.database.query;

import l33tD33r.app.database.Date;
import l33tD33r.app.database.DateTime;
import l33tD33r.app.database.Time;

public class Row {

	private Object[] values;
	
	public Row(Object[] values) {
		this.values = values;
	}
	
	public Object getValue(int columnIndex) {
		return values[columnIndex];
	}
	
	public String getStringValue(int columnIndex) {
		return (String)getValue(columnIndex);
	}
	
	public Integer getIntegerValue(int columnIndex) {
		return (Integer)getValue(columnIndex);
	}
	
	public Double getNumberValue(int columnIndex) {
		return (Double)getValue(columnIndex);
	}
	
	public Date getDateValue(int columnIndex) {
		return (Date)getValue(columnIndex);
	}
	
	public Time getTimeValue(int columnIndex) {
		return (Time)getValue(columnIndex);
	}
	
	public DateTime getDateTimeValue(int columnIndex) {
		return (DateTime)getValue(columnIndex);
	}
	
	public Boolean getBooleanValue(int columnIndex) {
		return (Boolean)getValue(columnIndex);
	}
}
