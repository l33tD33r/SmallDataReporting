package l33tD33r.app.database.data.storage;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import l33tD33r.app.database.storage.FileProvider;

public class FileDataProvider extends FileProvider implements DataProvider {
	
	public FileDataProvider(String fileName) {
		super(fileName);
	}
	
	@Override
	public Document getDataDocument() {
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
	public void setDataDocument(Document dataDocument) {
		try {
			setDocument(dataDocument);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
