package l33tD33r.app.database.query;



public class UnionQuery extends Query {

	private Query[] componentQueries;
	
	protected UnionQuery(Query[] componentQueries,String name, ExpressionNode sourceFilterExpression, boolean group, Column[] columns, ExpressionNode resultFilterExpression) {
		super(name, sourceFilterExpression, group, columns, resultFilterExpression);
		this.componentQueries = componentQueries;
	}

	@Override
	public IDataSource getDataSource() {
		return new UnionDataSource();
	}
	
	private class UnionDataSource implements IDataSource {

		private IDataSource[] dataSources; 
		private int currentIndex;
		
		public UnionDataSource() {
			this.dataSources = new IDataSource[componentQueries.length];
			for (int i=0; i < componentQueries.length; i++) {
				this.dataSources[i] = componentQueries[i].getDataSource();
			}
			this.currentIndex = 0;
		}
		
		@Override
		public boolean hasMoreElements() {
			return currentIndex < dataSources.length && dataSources[currentIndex].hasMoreElements();
		}

		@Override
		public IDataRow nextElement() {
			if (!hasMoreElements()) {
				throw new RuntimeException("no more elements");
			}
			IDataRow dataRow = dataSources[currentIndex].nextElement();
			if (!dataSources[currentIndex].hasMoreElements()) {
				currentIndex++;
			}
			return dataRow;
		}
		
	}
}
