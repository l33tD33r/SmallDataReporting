package l33tD33r.app.database.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import l33tD33r.app.database.Date;
import l33tD33r.app.database.Time;

public abstract class Query implements IColumnMap, IContext {

    private String name;

	private ExpressionNode sourceFilterExpression;

    private ExpressionNode resultFilterExpression;
	
	private Boolean group;
	
	private Column[] columns;
	
	private HashMap<String,Integer> columnNameIndexMap;
	
	private ResultRow[] rows;
	
	private int currentPosition;

    private Parameters parameters;
	
	protected Query(String name, ExpressionNode sourceFilterExpression, boolean group, Column[] columns, ExpressionNode resultFilterExpression) {
        this.name = name;
		this.sourceFilterExpression = sourceFilterExpression;
		this.group = group;
		this.columns = columns;
        this.resultFilterExpression = resultFilterExpression;
		this.currentPosition = 0;
        this.parameters = new Parameters();
	}

    public String getName() {
        return name;
    }
	
	public int getColumnCount() {
		return columns.length;
	}
	
	public Column getColumn(int columnIndex) {
		if (columnIndex < 0) {
			throw new RuntimeException("Column index '" + columnIndex + "' <= 0");
		}
		if (columnIndex >= columns.length) {
			throw new RuntimeException("Column index '" + columnIndex + "' >= column count '" + columns.length + "'");
		}
		return columns[columnIndex];
	}

	public DataType getDataType(int columnIndex) {
		return getColumn(columnIndex).getDataType();
	}
	
	public GroupRule getGroupRule(int columnIndex) {
		return getColumn(columnIndex).getGroupRule();
	}
	
	public SortRule getSortRule(int columnIndex) {
		return getColumn(columnIndex).getSortRule();
	}
	
	public int getColumnIndex(String columnName) {
		if (columnName == null || columnName.isEmpty()) {
			throw new RuntimeException("All columns must have a non-null, non-empty name");
		}
		if (columnNameIndexMap == null) {
            columnNameIndexMap = new HashMap<>();
			for (int i=0; i < columns.length; i++) {
				Column column = columns[i];
				columnNameIndexMap.put(column.getName(), Integer.valueOf(i));
			}
		}
		if ("RowIndex".equalsIgnoreCase(columnName)) {
			return -1;
		}
		return columnNameIndexMap.get(columnName);
	}

    public String getColumnName(int columnIndex) {
        if (columnIndex == -1) {
            return "RowIndex";
        }
        return getColumn(columnIndex).getName();
    }
	
	protected ResultRow[] getRows() {
		ArrayList<ResultRow> generatedRows = generateRows();
		ArrayList<ResultRow> groupedRows = group ? groupRows(generatedRows) : generatedRows;
        ArrayList<ResultRow> filteredRows = resultFilterExpression != null ? filterRows(groupedRows) : groupedRows;
		ArrayList<ResultRow> sortedRows = sortRows(filteredRows);

		ArrayList<ResultRow> finalRows;
		if (hasColumnSummarizations()) {
			finalRows = summarizeColumnRows(sortedRows);
		} else {
			finalRows = sortedRows;
		}
        for (int rowIndex=0; rowIndex<sortedRows.size();rowIndex++) {
            sortedRows.get(rowIndex).setRowIndex(rowIndex);
        }
		return sortedRows.toArray(new ResultRow[sortedRows.size()]);
	}
	
	private ArrayList<ResultRow> generateRows() {
		IDataSource dataSource = getDataSource();
		ArrayList<ResultRow> rowList = new ArrayList<>();
		while (dataSource.hasMoreElements()) {
			IDataRow dataRow = dataSource.nextElement();
			if (includeDataRow(dataRow)) {
				rowList.add(createRow(dataRow));
			}
		}
		return rowList;
	}
	
	private ArrayList<ResultRow> groupRows(ArrayList<ResultRow> generatedRows) {
		GroupedResults groupedResults = new GroupedResults(this);
		for (ResultRow generatedRow : generatedRows) {
			groupedResults.addRow(generatedRow);
		}
		return groupedResults.getGroupedRows();
	}

    private ArrayList<ResultRow> filterRows(ArrayList<ResultRow> groupedRows) {
        ArrayList<ResultRow> filteredRows = new ArrayList<>();
        for (ResultRow groupedRow : groupedRows) {
            ResultDataRow resultRow = new ResultDataRow(this, groupedRow);
            if (includeResultRow(resultRow)) {
                filteredRows.add(groupedRow);
            }
        }
        return  filteredRows;
    }

    private boolean includeResultRow(IDataRow resultRow) {
        if (resultFilterExpression == null) {
            return Boolean.TRUE;
        }
        return  resultFilterExpression.evaluateBoolean(resultRow);
    }
	
	private ArrayList<ResultRow> sortRows(ArrayList<ResultRow> filteredRows) {
		SortedResults sortedResults = new SortedResults(this);
		for (ResultRow filteredRow : filteredRows) {
			sortedResults.addRow(filteredRow);
		}
		return sortedResults.getSortedRows();
	}

	private boolean hasColumnSummarizations() {
		for (Column column : columns) {
			if (column.hasSummarization()) {
				return true;
			}
		}
		return false;
	}

	private ArrayList<ResultRow> summarizeColumnRows(List<ResultRow> sortedRows) {
		SummarizeColumnResults summarizeColumnResults = new SummarizeColumnResults(this);
		sortedRows.forEach(r-> summarizeColumnResults.addRow(r));
		return summarizeColumnResults.getSummarizedRows();
	}
	
	public abstract IDataSource getDataSource();
	
	private Boolean includeDataRow(IDataRow dataRow) {
		if (sourceFilterExpression == null) {
			return Boolean.TRUE;
		}
		return sourceFilterExpression.evaluateBoolean(dataRow);
	}

	protected ResultRow createRow(IDataRow dataRow) {
		ArrayList<Object> rowValues = new ArrayList<Object>();
		for (Column column : columns) {
			Object rowValue = column.getExpression().evaluate(dataRow);
			rowValues.add(rowValue);
		}
		return new ResultRow(rowValues.toArray(new Object[rowValues.size()]), this);
	}
	
	public int getRowCount() {
		if (rows == null) {
			rows = getRows();
		}
		return rows.length;
	}

	public int getPosition() {
		if (rows == null) {
			throw new RuntimeException("rows == null");
		}
		return currentPosition;
	}

	public void setPosition(int position) {
		if (rows == null) {
			throw new RuntimeException("rows == null");
		}
		if (position < 0) {
			throw new RuntimeException("position < 0");
		}
		if (position >= rows.length) {
			throw new RuntimeException("position >= row count");
		}
		currentPosition = position;
	}
	
	public ResultRow getCurrentRow() {
		if (rows == null) {
			throw new RuntimeException("rows == null");
		}
		return rows[currentPosition];
	}

	public Object getValue(int columnIndex) {
        ResultRow row = getCurrentRow();
		return row.getValue(columnIndex);
	}
/*
	public String getStringValue(int columnIndex) {
		Row row = getCurrentRow();
		return row.getStringValue(columnIndex);
	}

	public Integer getIntegerValue(int columnIndex) {
		Row row = getCurrentRow();
		return row.getIntegerValue(columnIndex);
	}

	public Double getNumberValue(int columnIndex) {
		Row row = getCurrentRow();
		return row.getNumberValue(columnIndex);
	}
	
	public Boolean getBooleanValue(int columnIndex) {
		Row row = getCurrentRow();
		return row.getBooleanValue(columnIndex);
	}

	public Date getDateValue(int columnIndex) {
		Row row = getCurrentRow();
		return row.getDateValue(columnIndex);
	}

	public Time getTimeValue(int columnIndex) {
		Row row = getCurrentRow();
		return row.getTimeValue(columnIndex);
	}
*/
    public void setParameters(Map<String,Object> nameValueMap) {
        for (Map.Entry<String,Object> entry : nameValueMap.entrySet()) {
            setParameterInner(entry.getKey(), entry.getValue());
        }
    }

    public void setParameter(String name, Object value) {
        setParameterInner(name, value);
    }

    private void setParameterInner(String name, Object value) {
        parameters.setParameter(name, value);
    }

    public Object getParameter(String name) {
        return parameters.getParameter(name);
    }

    protected static class ResultDataRow implements IDataRow {

        private Query query;
        private ResultRow row;

		private int rowIndex;

        public ResultDataRow(Query query, ResultRow row) {
            this.query = query;
            this.row = row;
        }

        @Override
        public IContext getContext() {
            return query;
        }

        private int getColumnIndex(String name) {
            return query.getColumnIndex(name);
        }

//        @Override
//        public DataType getType(String name) {
//            int columnIndex = getColumnIndex(name);
//            return query.getDataType(columnIndex);
//        }

        @Override
        public Object getValue(String name) {
            int columnIndex = getColumnIndex(name);
            return row.getValue(columnIndex);
        }

        /*
        @Override
        public String getStringValue(String name) {
            int columnIndex = getColumnIndex(name);
            return row.getStringValue(columnIndex);
        }

        @Override
        public Integer getIntegerValue(String name) {
            int columnIndex = getColumnIndex(name);
            return row.getIntegerValue(columnIndex);
        }

        @Override
        public Double getNumberValue(String name) {
            int columnIndex = getColumnIndex(name);
            return row.getNumberValue(columnIndex);
        }

        @Override
        public Boolean getBooleanValue(String name) {
            int columnIndex = getColumnIndex(name);
            return  row.getBooleanValue(columnIndex);
        }

        @Override
        public Date getDateValue(String name) {
            int columnIndex = getColumnIndex(name);
            return row.getDateValue(columnIndex);
        }

        @Override
        public Time getTimeValue(String name) {
            int columnIndex = getColumnIndex(name);
            return  row.getTimeValue(columnIndex);
        }
        */
    }
}
