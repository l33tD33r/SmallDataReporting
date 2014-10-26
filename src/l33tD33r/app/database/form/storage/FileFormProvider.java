package l33tD33r.app.database.form.storage;

import l33tD33r.app.database.storage.FileProvider;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Simon on 10/25/2014.
 */
public class FileFormProvider extends FileProvider implements FormProvider {

    public FileFormProvider(String fileName) {
        super(fileName);
    }

    @Override
    public Document getFormsDocument() {
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
    public void setFormsDocument(Document formsDocument) {
        throw new RuntimeException("Not implemented");
    }
}
