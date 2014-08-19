package l33tD33r.app.ui.workspace.content;

import org.w3c.dom.Document;
import l33tD33r.app.database.data.storage.DataProvider;

/**
 * Created by Simon on 2/15/14.
 */
public class FileDataProvider implements DataProvider {

    private Document dataDocument;

    @Override
    public Document getDataDocument() {
        return dataDocument;
    }

    @Override
    public void setDataDocument(Document dataDocument) {
        this.dataDocument = dataDocument;
    }
}
