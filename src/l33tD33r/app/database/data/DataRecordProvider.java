package l33tD33r.app.database.data;

import java.util.List;
import java.util.Map;

public interface DataRecordProvider {
	List<DataRecord> retrieveRecords(String tableName);
	List<DataRecord> retrieveRecords(String tableName, Map<String,Object> filterValues);
	DataRecord createNewRecord(String tableName);
	void insertRecord(String tableName, DataRecord newRecord) throws DataRecordExistsException;
	void insertRecords(String tableName, List<DataRecord> newRecords) throws DataRecordExistsException;
	void updateRecord(String tableName, DataRecord updatedRecord) throws DataRecordDoesNotExistException;
	void updateRecords(String tableName, List<DataRecord> updatedRecords) throws DataRecordDoesNotExistException;
	void deleteRecord(String tableName, DataRecord deleteRecord) throws DataRecordDoesNotExistException;
	void deleteRecords(String tableName, List<DataRecord> deleteRecords) throws DataRecordDoesNotExistException;
	void deleteAllRecords(String tableName);
}
