package l33tD33r.app.ui.workspace.control;

import javafx.scene.control.TextInputControl;

/**
 * Created by Simon on 2014-10-31.
 */
public class TextControlWrapper<TData> extends ItemControlWrapper<TData> {

    private DataConverter<String, TData> valueConverter;

    public TextInputControl getTextControl() { return (TextInputControl)getControl(); }

    public void setTextControl(TextInputControl textField) { setControl(textField); }

    public void setValueConverter(DataConverter<String, TData> valueConverter) {
        this.valueConverter = valueConverter;
    }

    @Override
    public TData getValue() {
        return valueConverter.convertData(getTextControl().getText());
    }

    @Override
    protected void setValue(TData value) {
        getTextControl().setText(value == null ? null : value.toString());
    }
}
