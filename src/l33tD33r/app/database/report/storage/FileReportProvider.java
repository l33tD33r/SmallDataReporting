package l33tD33r.app.database.report.storage;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import l33tD33r.app.database.storage.FileProvider;

public class FileReportProvider extends FileProvider implements ReportProvider {

	public FileReportProvider(String fileName) {
		super(fileName);
	}

	@Override
	public Document getReportsDocument() {
		try {
			return getDocument();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setReportsDocument(Document reportDocument) {
		throw new RuntimeException("Not implemented");
	}

}
