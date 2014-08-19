package l33tD33r.app.database.data;

public class DataRecordExistsException extends Exception {

	private DataRecord existingRecord;
	private DataRecord newRecord;
	
	public DataRecordExistsException(DataRecord existingRecord, DataRecord newRecord) {
		this.existingRecord = existingRecord;
		this.newRecord = newRecord;
	}
	
	public DataRecord getExistingRecord() {
		return this.existingRecord;
	}
	
	public DataRecord getNewRecord() {
		return this.newRecord;
	}
}
