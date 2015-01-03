package l33tD33r.app.database.form.action;

import l33tD33r.app.database.form.data.ValueSource;

/**
 * Created by Simon on 2014-10-28.
 */
public class Field {

    private String name;

    private ValueSource valueSource;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public ValueSource getValueSource() {
        return valueSource;
    }
    public void setValueSource(ValueSource valueSource) {
        this.valueSource = valueSource;
    }
}
