package l33tD33r.app.database.data;

import java.util.ArrayList;

public class DataChangeSupport {

	private DataTable source;
	private ArrayList<DataChangeListener> dataChangeListeners;
	
	public DataChangeSupport(DataTable source) {
		this.source = source;
		this.dataChangeListeners = new ArrayList<DataChangeListener>();
	}
	
	public void addDataChangeListener(DataChangeListener dataChangeListener) {
		this.dataChangeListeners.add(dataChangeListener);
	}
	
	public void removeDataChangeListener(DataChangeListener dataChangeListener) {
		this.dataChangeListeners.remove(dataChangeListener);
	}
	
	public void fireDataChange() {
		fireDataChange(new DataChangeEvent(source));
	}
	
	public void fireDataChange(DataChangeEvent dataChangeEvent) {
		for (DataChangeListener dataChangeListener : this.dataChangeListeners) {
			dataChangeListener.dataChange(dataChangeEvent);
		}
	}
}
