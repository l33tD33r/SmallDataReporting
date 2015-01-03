package l33tD33r.app.database.form.data;

import java.util.List;

/**
 * Created by Simon on 1/1/2015.
 */
public interface Collection {
    String getId();
    List<Element> getElements();
    int getSize();
    Element getElement(int index);
}
