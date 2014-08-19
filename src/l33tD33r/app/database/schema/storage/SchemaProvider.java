package l33tD33r.app.database.schema.storage;

import org.w3c.dom.Document;

public interface SchemaProvider {
	Document getSchemaDocument();
	void setSchemaDocument(Document schemaDocument);
}
