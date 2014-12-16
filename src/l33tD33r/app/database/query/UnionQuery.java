package l33tD33r.app.database.query;


import java.util.ArrayList;

public class UnionQuery extends Query {

	private Query[] sourceQueries;
	
	public UnionQuery(Query[] sourceQueries,String name, ExpressionNode sourceFilterExpression, boolean group, Column[] columns, ExpressionNode resultFilterExpression) {
		super(name, sourceFilterExpression, group, columns, resultFilterExpression);
		this.sourceQueries = sourceQueries;
	}

	@Override
	public IDataSource getDataSource() {
		return new UnionDataSource();
	}
	
	private class UnionDataSource implements IDataSource {

		private ArrayList<UnionDataRow> unionDataRows;
		private int currentIndex;
		
		public UnionDataSource() {
			unionDataRows = createUnionRows();
			currentIndex = 0;
		}
		
		@Override
		public boolean hasMoreElements() {
			return currentIndex < unionDataRows.size();
		}

		@Override
		public IDataRow nextElement() {
			if (!hasMoreElements()) {
				throw new RuntimeException("no more elements");
			}

			UnionDataRow unionDataRow = unionDataRows.get(currentIndex);
			unionDataRow.setRowIndex(currentIndex);
			currentIndex++;
			return unionDataRow;
		}

		private ArrayList<UnionDataRow> createUnionRows() {
			ArrayList<UnionDataRow> unionDataRows = new ArrayList<>();


			for (Query sourceQuery : sourceQueries) {
				for (int sourceRowIndex = 0; sourceRowIndex < sourceQuery.getRowCount(); sourceRowIndex++) {
					sourceQuery.setPosition(sourceRowIndex);

					ResultRow currentRow = sourceQuery.getCurrentRow();

					unionDataRows.add(new UnionDataRow(UnionQuery.this, currentRow));
				}
			}

			return unionDataRows;
		}
	}

	private static class UnionDataRow implements IDataRow {

		private UnionQuery unionQuery;
		private ResultRow sourceDataRow;
		private int rowIndex;

		public UnionDataRow(UnionQuery unionQuery, ResultRow sourceDataRow) {
			this.unionQuery = unionQuery;
			this.sourceDataRow = sourceDataRow;
		}

		public int getRowIndex() {
			return rowIndex;
		}
		public void setRowIndex(int rowIndex) {
			this.rowIndex = rowIndex;
		}

		@Override
		public IContext getContext() {
			return unionQuery;
		}

		@Override
		public Object getValue(String name) {
			if ("RowIndex".equalsIgnoreCase(name)) {
				return getRowIndex();
			}
			return sourceDataRow.getValue(name.substring(1));
		}
	}
}
