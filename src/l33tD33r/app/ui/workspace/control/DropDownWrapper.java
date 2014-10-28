package l33tD33r.app.ui.workspace.control;

import javafx.scene.control.ComboBox;
import l33tD33r.app.database.Time;

import java.util.Comparator;

/**
 * Created by Simon on 10/26/2014.
 */
public class DropDownWrapper<TItem, TData> extends ControlWrapper<TData> {

    private DataConverter<TItem, TData> valueConverter;

    public void setComboBox(ComboBox<TItem> comboBox) {
        setControl(comboBox);
    }

    public ComboBox<TItem> getComboBox() {
        return (ComboBox<TItem>)getControl();
    }

    public void setValueConverter(DataConverter<TItem, TData> valueConverter) {
        this.valueConverter = valueConverter;
    }

    @Override
    public TData getValue() {
        return valueConverter.convertData(getComboBox().getValue());
    }
}
