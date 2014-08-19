package l33tD33r.app.database.query;

import l33tD33r.app.database.Date;
import l33tD33r.app.database.DateTime;
import l33tD33r.app.database.Time;

public class GroupKey {

	private Object[] groupValues;
	
	public GroupKey(Object[] groupValues) {
		this.groupValues = groupValues;
	}
	
	public int getGroupCount() {
		return this.groupValues.length;
	}
	
	public Object getGroupValue(int index) {
		return this.groupValues[index];
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof GroupKey)) {
			return false;
		}
		return equals((GroupKey)o);
	}
	
	private boolean equals(GroupKey groupKey) {
		if (this.groupValues.length != groupKey.getGroupCount()) {
			return false;
		}
		for (int i=0; i < this.groupValues.length; i++) {
			if (!groupValues[i].equals(groupKey.getGroupValue(i))) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i < this.groupValues.length; i++) {
			Object groupValue = this.groupValues[i]; 
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
