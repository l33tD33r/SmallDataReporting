package l33tD33r.app.database.data.storage;

import org.w3c.dom.Document;

public interface DataProvider {
	Document getDataDocument();
	void setDataDocument(Document dataDocument);
}
