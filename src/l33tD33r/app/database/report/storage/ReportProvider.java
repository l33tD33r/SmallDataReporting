package l33tD33r.app.database.report.storage;

import org.w3c.dom.Document;

public interface ReportProvider {
	Document getReportsDocument();
	void setReportsDocument(Document reportDocument);
}
