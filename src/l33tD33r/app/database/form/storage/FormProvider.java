package l33tD33r.app.database.form.storage;

import org.w3c.dom.Document;

/**
 * Created by Simon on 10/24/2014.
 */
public interface FormProvider {
    Document getFormsDocument();
    void setFormsDocument(Document formsDocument);
}
