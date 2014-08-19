package l33tD33r.app.database.query;

import l33tD33r.app.database.Date;
import l33tD33r.app.database.Time;

public interface IDataRow {
	DataType getType(String name);
	Object getValue(String name);
	String getStringValue(String name);
	Integer getIntegerValue(String name);
	Double getNumberValue(String name);
	Boolean getBooleanValue(String name);
	Date getDateValue(String name);
	Time getTimeValue(String name);
}
