package l33tD33r.app.database.data;

import java.util.EventObject;

@SuppressWarnings("serial")
public class DataChangeEvent extends EventObject {

	public DataChangeEvent(DataTable source) {
		super(source);
	}

	public DataTable getDataSource() {
		return (DataTable)getSource();
	}
}
