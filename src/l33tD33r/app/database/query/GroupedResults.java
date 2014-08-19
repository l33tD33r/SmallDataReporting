package l33tD33r.app.database.query;

import java.util.ArrayList;
import java.util.HashMap;

import l33tD33r.app.database.Date;
import l33tD33r.app.database.DateTime;
import l33tD33r.app.database.Time;

public class GroupedResults {

	private Query query;
	private HashMap<GroupKey,ArrayList<Row>> groupedRowsMap;
	private GroupRule[] groupRules;
	private DataType[] dataTypes;
	
	protected GroupedResults(Query query) {
		this.query = query;
		
		groupedRowsMap = new HashMap<GroupKey, ArrayList<Row>>();
		
		groupRules = new GroupRule[this.query.getColumnCount()];
		dataTypes = new DataType[this.query.getColumnCount()];
		for (int i=0; i < this.query.getColumnCount(); i++) {
			groupRules[i] = this.query.getGroupRule(i);
			dataTypes[i] = this.query.getDataType(i);
		}
	}
	
	protected void addRow(Row row) {
		GroupKey groupKey = createGroupKey(row);
		ArrayList<Row> groupRowList = groupedRowsMap.get(groupKey);
		if (groupRowList == null) {
			groupRowList = new ArrayList<Row>();
			groupedRowsMap.put(groupKey, groupRowList);
		}
		groupRowList.add(row);
	}
	
	private GroupKey createGroupKey(Row row) {
		ArrayList<Object> groupedValueList = new ArrayList<Object>();
		for (int i=0; i < groupRules.length; i++) {
			if (groupRules[i] == GroupRule.By) {
				groupedValueList.add(row.getValue(i));
			}
		}
		Object[] groupedValues = groupedValueList.toArray(new Object[groupedValueList.size()]);
		return new GroupKey(groupedValues);
	}
	
	protected ArrayList<Row> getGroupedRows() {
		ArrayList<Row> groupedRows = new ArrayList<Row>();
		for (ArrayList<Row> groupRows : groupedRowsMap.values()) {
			Row groupedRow = createGroupedRow(groupRows);
			groupedRows.add(groupedRow);
		}
		return groupedRows;
	}
	
	private Row createGroupedRow(ArrayList<Row> groupRows) {
		Object[] values = new Object[groupRules.length];
		for (Row groupRow : groupRows) {
			for (int i=0; i < groupRules.length; i++) {
				values[i] = applyGroupRule(values[i], groupRow.getValue(i), groupRules[i], dataTypes[i]);
			}
		}
		for (int i=0; i < groupRules.length; i++) {
            values[i] = summarizeGroupRule(values[i], groupRows.size(), groupRules[i], dataTypes[i]);
		}
		return new Row(values);
	}
	
	private Object applyGroupRule(Object currentValue, Object newValue, GroupRule groupRule, DataType dataType) {
		if (currentValue == null) {
			return newValue;
		}
		switch (dataType) {
			case String:
				return applyGroupRule((String)currentValue, (String)newValue, groupRule);
			case Integer:
				return applyGroupRule((Integer)currentValue, (Integer)newValue, groupRule);
			case Number:
				return applyGroupRule((Double)currentValue, (Double)newValue, groupRule);
			case Boolean:
				return applyGroupRule((Boolean)currentValue, (Boolean)newValue, groupRule);
			case Date:
				return applyGroupRule((Date)currentValue, (Date)newValue, groupRule);
			case Time:
				return applyGroupRule((Time)currentValue, (Time)newValue, groupRule);
			default:
				throw new RuntimeException("Unknown data type: " + dataType.name());
		}
	}
	
	private String applyGroupRule(String currentString, String newString, GroupRule groupRule) {
		switch (groupRule) {
			case By:
				return currentString;
			case Max:
				return (newString.compareToIgnoreCase(currentString) >=0) ? newString : currentString;
			case Min:
				return (currentString.compareToIgnoreCase(newString) >=0) ? currentString : newString;
			default:
				throw new RuntimeException("Invalid String group rule: " + groupRule.name());
		}
	}
	
	private Integer applyGroupRule(Integer currentInteger, Integer newInteger, GroupRule groupRule) {
		switch (groupRule) {
			case By:
				return currentInteger;
			case Max:
				return Math.max(currentInteger, newInteger);
			case Min:
				return Math.min(currentInteger, newInteger);
			case Sum:
			case Average:
				return currentInteger + newInteger;
			default:
				throw new RuntimeException("Invalid Integer group rule: " + groupRule.name());
		}
	}
	
	private Double applyGroupRule(Double currentDouble, Double newDouble, GroupRule groupRule) {
		switch (groupRule) {
			case By:
				return currentDouble;
			case Max:
				return Math.max(currentDouble, newDouble);
			case Min:
				return Math.min(currentDouble, newDouble);
			case Sum:
			case Average:
				return currentDouble + newDouble;
			default:
				throw new RuntimeException("Invalid Double group rule: " + groupRule.name());	
		}
	}
	
	private Boolean applyGroupRule(Boolean currentBoolean, Boolean newBoolean, GroupRule groupRule) {
		switch (groupRule) {
			case By:
				return currentBoolean;
			case Max:
				return (currentBoolean || newBoolean);
			case Min:
				return (currentBoolean && newBoolean);
			default:
				throw new RuntimeException("Invalid Boolean group rule: " + groupRule.name());	
		}
	}
	
	private Date applyGroupRule(Date currentDate, Date newDate, GroupRule groupRule) {
		switch (groupRule) {
			case By:
				return currentDate;
			case Max:
				return (currentDate.compareTo(newDate) > 0) ? currentDate : newDate;
			case Min:
				return (currentDate.compareTo(newDate) < 0) ? currentDate : newDate;
			default:
				throw new RuntimeException("Invalid Date group rule: " + groupRule.name());
		}
	}
	
	private Time applyGroupRule(Time currentTime, Time newTime, GroupRule groupRule) {
		switch (groupRule) {
			case By:
				return currentTime;
			case Max:
				return (currentTime.compareTo(newTime) > 0) ? currentTime : newTime;
			case Min:
				return (currentTime.compareTo(newTime) < 0) ? currentTime : newTime;
			default:
				throw new RuntimeException("Invalid Time group rule: " + groupRule.name());
		}
	}
	
	private DateTime applyGroupRule(DateTime currentDateTime, DateTime newDateTime, GroupRule groupRule) {
		switch (groupRule) {
			case By:
				return currentDateTime;
			case Max:
				return (currentDateTime.compareTo(newDateTime) > 0) ? currentDateTime : newDateTime;
			case Min:
				return (currentDateTime.compareTo(newDateTime) < 0) ? currentDateTime : newDateTime;
			default:
				throw new RuntimeException("Invalid DateTime group rule: " + groupRule.name());
		}
	}
	
	private Object summarizeGroupRule(Object value, int groupCount, GroupRule groupRule, DataType dataType) {
		switch (dataType) {
			case String:
				return summarizeGroupRule((String)value, groupCount, groupRule);
			case Integer:
				return summarizeGroupRule((Integer)value, groupCount, groupRule);
			case Number:
				return summarizeGroupRule((Double)value, groupCount, groupRule);
			case Boolean:
				return summarizeGroupRule((Boolean)value, groupCount, groupRule);
			case Date:
				return summarizeGroupRule((Date)value, groupCount, groupRule);
			case Time:
				return summarizeGroupRule((Time)value, groupCount, groupRule);
			default:
				throw new RuntimeException("Unknown data type" + dataType.name());
		}
	}
	
	private String summarizeGroupRule(String value, int groupCount, GroupRule groupRule) {
		switch (groupRule) {
			case By:
			case Min:
			case Max:
				return value;
			default:
				throw new RuntimeException("Invalid String group rule:" + groupRule.name());
		}
	}
	
	private Object summarizeGroupRule(Integer value, int groupCount, GroupRule groupRule) {
		switch (groupRule) {
			case By:
			case Min:
			case Max:
			case Sum:
				return value;
			case Average:
				return value / (double)groupCount;
			default:
				throw new RuntimeException("Invalid Integer group rule:" + groupRule.name());
		}
	}
	
	private Double summarizeGroupRule(Double value, int groupCount, GroupRule groupRule) {
		switch (groupRule) {
			case By:
			case Min:
			case Max:
			case Sum:
				return value;
			case Average:
				return value / groupCount;
			default:
				throw new RuntimeException("Invalid Double group rule:" + groupRule.name());
		}
	}
	
	private Boolean summarizeGroupRule(Boolean value, int groupCount, GroupRule groupRule) {
		switch (groupRule) {
			case By:
			case Min:
			case Max:
				return value;
			default:
				throw new RuntimeException("Invalid Boolean group rule:" + groupRule.name());
		}
	}
	
	private Date summarizeGroupRule(Date value, int groupCount, GroupRule groupRule) {
		switch (groupRule) {
			case By:
			case Min:
			case Max:
				return value;
			default:
				throw new RuntimeException("Invalid Date group rule:" + groupRule.name());
		}
	}
	
	private Time summarizeGroupRule(Time value, int groupCount, GroupRule groupRule) {
		switch (groupRule) {
			case By:
			case Min:
			case Max:
				return value;
			default:
				throw new RuntimeException("Invalid Time group rule:" + groupRule.name());
		}
	}
}
