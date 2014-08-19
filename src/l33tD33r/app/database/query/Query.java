package l33tD33r.app.database.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import l33tD33r.app.database.Date;
import l33tD33r.app.database.Time;

public abstract class Query {

	private ExpressionNode sourceFilterExpression;

    private ExpressionNode resultFilterExpression;
	
	private Boolean group;
	
	private Column[] columns;
	
	private HashMap<String,Integer> columnNameIndexMap;
	
	private Row[] rows;
	
	private int currentPosition;

    private Parameters parameters;
	
	protected Query(ExpressionNode sourceFilterExpression, boolean group, Column[] columns, ExpressionNode resultFilterExpression) {
		this.sourceFilterExpression = sourceFilterExpression;
		this.group = group;
		this.columns = columns;
        this.resultFilterExpression = resultFilterExpression;
		this.currentPosition = 0;
        this.parameters = new Parameters();
	}
	
	public int getColumnCount() {
		return columns.length;
	}
	
	private Column getColumn(int columnIndex) {
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
		return getColumn(currentPosition).getSortRule();
	}
	
	public int getColumnIndex(String columnName) {
		if (columnNameIndexMap == null) {
            columnNameIndexMap = new HashMap<>();
			for (int i=0; i < columns.length; i++) {
				Column column = columns[i];
				columnNameIndexMap.put(column.getName(), Integer.valueOf(i));
			}
		}
		return columnNameIndexMap.get(columnName);
	}
	
	protected Row[] getRows() {
		ArrayList<Row> generatedRows = generateRows();
		ArrayList<Row> groupedRows = group ? groupRows(generatedRows) : generatedRows;
        ArrayList<Row> filteredRows = resultFilterExpression != null ? filterRows(groupedRows) : groupedRows;
		ArrayList<Row> sortedRows = sortRows(filteredRows);
		return sortedRows.toArray(new Row[sortedRows.size()]);
	}
	
	private ArrayList<Row> generateRows() {
		IDataSource dataSource = getDataSource();
		ArrayList<Row> rowList = new ArrayList<Row>();
		while (dataSource.hasMoreElements()) {
			IDataRow dataRow = dataSource.nextElement();
			if (includeDataRow(dataRow)) {
				rowList.add(createRow(dataRow));
			}
		}
		return rowList;
	}
	
	private ArrayList<Row> groupRows(ArrayList<Row> generatedRows) {
		GroupedResults groupedResults = new GroupedResults(this);
		for (Row generatedRow : generatedRows) {
			groupedResults.addRow(generatedRow);
		}
		return groupedResults.getGroupedRows();
	}

    private ArrayList<Row> filterRows(ArrayList<Row> groupedRows) {
        ArrayList<Row> filteredRows = new ArrayList<>();
        for (Row groupedRow : groupedRows) {
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
	
	private ArrayList<Row> sortRows(ArrayList<Row> filteredRows) {
		SortedResults sortedResults = new SortedResults(this);
		for (Row filteredRow : filteredRows) {
			sortedResults.addRow(filteredRow);
		}
		return sortedResults.getSortedRows();
	}
	
	public abstract IDataSource getDataSource();
	
	private Boolean includeDataRow(IDataRow dataRow) {
		if (sourceFilterExpression == null) {
			return Boolean.TRUE;
		}
		return sourceFilterExpression.evaluateBoolean(dataRow);
	}

	protected Row createRow(IDataRow dataRow) {
		ArrayList<Object> rowValues = new ArrayList<Object>();
		for (Column column : columns) {
			Object rowValue = column.getExpression().evaluate(dataRow);
			rowValues.add(rowValue);
		}
		return new Row(rowValues.toArray(new Object[rowValues.size()]));
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
	
	public Row getCurrentRow() {
		if (rows == null) {
			throw new RuntimeException("rows == null");
		}
		return rows[currentPosition];
	}

	public Object getValue(int columnIndex) {
		Row row = getCurrentRow();
		return row.getValue(columnIndex);
	}

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

    protected static class ResultDataRow implements IDataRow {

        private Query query;
        private Row row;

        public ResultDataRow(Query query, Row row) {
            this.query = query;
            this.row = row;
        }

        private int getColumnIndex(String name) {
            return query.getColumnIndex(name);
        }

        @Override
        public DataType getType(String name) {
            int columnIndex = getColumnIndex(name);
            return query.getDataType(columnIndex);
        }

        @Override
        public Object getValue(String name) {
            int columnIndex = getColumnIndex(name);
            return row.getValue(columnIndex);
        }

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
    }
}
