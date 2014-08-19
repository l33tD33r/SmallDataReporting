package l33tD33r.app.database.schema.storage;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import l33tD33r.app.database.storage.FileProvider;

public class FileSchemaProvider extends FileProvider implements SchemaProvider {

	public FileSchemaProvider(String fileName) {
		super(fileName);
	}
	
	@Override
	public Document getSchemaDocument() {
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
	public void setSchemaDocument(Document schemaDocument) {
		throw new RuntimeException("Not Implemented");
	}
}
