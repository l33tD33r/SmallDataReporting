package l33tD33r.app.ui.workspace.control;

import javafx.util.StringConverter;
import l33tD33r.app.database.form.data.DataType;

import java.text.MessageFormat;

/**
 * Created by Simon on 12/9/2014.
 */
public class DataStringConverter extends StringConverter {

    private DataType type;

    public DataStringConverter(DataType type) {
        if (type == DataType.Reference) {
            throw new RuntimeException("Invalid DataType 'Reference'");
        }
        setType(type);
    }

    public DataType getType() {
        return type;
    }
    private void setType(DataType type) {
        this.type = type;
    }

    @Override
    public String toString(Object object) {
        if (object == null) {
            return null;
        }
        return object.toString();
    }

    @Override
    public Object fromString(String string) {
        if (getType() == null) {
            return string;
        }
        return getType().parse(string);
    }
}
