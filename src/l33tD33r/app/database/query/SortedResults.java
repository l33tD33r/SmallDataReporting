package l33tD33r.app.database.query;

import java.util.ArrayList;

public class SortedResults {

	private Query query;
	private SortRule[] sortRules;
	private DataType[] dataTypes;
	
	private ArrayList<ResultRow> sortedRows;
	
	public SortedResults(Query query) {
		this.query = query;
		
		int columnCount = this.query.getColumnCount();
		this.sortRules = new SortRule[columnCount];
		this.dataTypes = new DataType[columnCount];
		
		for (int i=0; i < columnCount; i++) {
			this.sortRules[i] = this.query.getSortRule(i);
			this.dataTypes[i] = this.query.getDataType(i);
		}
		
		this.sortedRows = new ArrayList<>();
	}
	
	protected void addRow(ResultRow newRow) {
		int newRowIndex = -1;
		for (int i=0; i < sortedRows.size(); i++) {
			int result = compareRow(sortedRows.get(i), newRow);
			if (result >= 0) {
				newRowIndex = i;
				break;
			}
		}
		if (newRowIndex < 0) {
			newRowIndex = sortedRows.size();
		}
		sortedRows.add(newRowIndex, newRow);
	}
	
	private int compareRow(ResultRow a, ResultRow b) {
		for (int i=0; i < sortRules.length; i++) {
			int result;
			switch (dataTypes[i]) {
				case String:
					result = a.getStringValue(i).compareTo(b.getStringValue(i));
					break;
				case Integer:
					result = a.getIntegerValue(i).compareTo(b.getIntegerValue(i));
					break;
				case Number:
					result = a.getNumberValue(i).compareTo(b.getNumberValue(i));
					break;
				case Boolean:
					result = a.getBooleanValue(i).compareTo(b.getBooleanValue(i));
					break;
				case Date:
					result = a.getDateValue(i).compareTo(b.getDateValue(i));
					break;
				case Time:
					result = a.getTimeValue(i).compareTo(b.getTimeValue(i));
					break;
				default:
					throw new RuntimeException("");
			}
			if (result != 0) {
				return convertResults(result, sortRules[i]);
			}
		}
		return 0;
	}
	
	private int convertResults(int result, SortRule sortRule) {
		if (result == 0) {
			return 0;
		}
		switch (sortRule) {
			case Ascending:
				return result;
			case Descending:
				return result * -1;
			default:
				throw new RuntimeException("Unknown sort rule:" + sortRule.name());
		}
	}
	
	protected ArrayList<ResultRow> getSortedRows() {
		return sortedRows;
	}
}
