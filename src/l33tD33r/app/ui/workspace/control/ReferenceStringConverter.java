package l33tD33r.app.ui.workspace.control;

import javafx.util.StringConverter;
import l33tD33r.app.ui.workspace.data.DataRecordReference;

import java.util.ArrayList;

/**
 * Created by Simon on 12/9/2014.
 */
public class ReferenceStringConverter extends StringConverter<Object> {

    public ArrayList<DataRecordReference> dataRecordReferences;

    public ReferenceStringConverter(ArrayList<DataRecordReference> dataRecordReferences) {
        this.dataRecordReferences = dataRecordReferences;
    }

    @Override
    public String toString(Object object) {
        if (object == null) {
            return "";
        }
        DataRecordReference dataRecordReference = (DataRecordReference)object;
        return dataRecordReference.getLabel();
    }

    @Override
    public Object fromString(String label) {
        if (label == null || label.isEmpty()) {
            return null;
        }
        for (DataRecordReference dataRecordReference : dataRecordReferences) {
            if (dataRecordReference.getLabel().equals(label)) {
                return dataRecordReference;
            }
        }
        return null;
    }
}
