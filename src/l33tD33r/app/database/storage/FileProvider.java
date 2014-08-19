package l33tD33r.app.database.storage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import l33tD33r.app.database.utility.FileUtils;
import l33tD33r.app.database.utility.XmlUtils;

public abstract class FileProvider {

	private String fileName;
	
	protected FileProvider(String fileName) {
		this.fileName = fileName;
	}
	
	protected Document getDocument() throws FileNotFoundException, IOException, SAXException {
		FileInputStream fis = new FileInputStream(fileName);
		return XmlUtils.createDocument(fis);
	}
	
	protected void setDocument(Document document) throws FileNotFoundException, IOException {
		String dataContent = XmlUtils.serializeDocumentToXml(document);
		FileOutputStream fos = new FileOutputStream(fileName);
		FileUtils.writeBytes(fos, dataContent.getBytes());
	}
}
