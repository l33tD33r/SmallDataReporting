package l33tD33r.app.database.query;

import l33tD33r.app.database.Date;
import l33tD33r.app.database.DateTime;
import l33tD33r.app.database.Time;

public class KeySet {

	private Object[] keyValues;
	
	public KeySet(Object[] keyValues) {
		this.keyValues = keyValues;
	}
	
	public int getGroupCount() {
		return this.keyValues.length;
	}
	
	public Object getGroupValue(int index) {
		return this.keyValues[index];
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof KeySet)) {
			return false;
		}
		return equals((KeySet)o);
	}
	
	private boolean equals(KeySet keySet) {
		if (this.keyValues.length != keySet.getGroupCount()) {
			return false;
		}
		for (int i=0; i < this.keyValues.length; i++) {
			if (!keyValues[i].equals(keySet.getGroupValue(i))) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i < this.keyValues.length; i++) {
			Object groupValue = this.keyValues[i];
			if (groupValue instanceof String) {
				sb.append("String");
			} else if (groupValue instanceof Integer) {
				sb.append("Integer");
			} else if (groupValue instanceof Double) {
				sb.append("Double");
			} else if (groupValue instanceof Boolean) {
				sb.append("Boolean");
			} else if (groupValue instanceof Date) {
				sb.append("Date");
			} else if (groupValue instanceof Time) {
				sb.append("Time");
			} else if (groupValue instanceof DateTime) {
				sb.append("DateTime");
			}
			sb.append(groupValue.toString());
		}
		return sb.toString().hashCode();
	}
}
