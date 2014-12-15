package l33tD33r.app.ui.workspace.control;

import javafx.scene.control.CheckBox;

/**
 * Created by Simon on 12/9/2014.
 */
public class BooleanCheckBoxControlWrapper extends ItemControlWrapper<Boolean> {

    public CheckBox getCheckBoxControl() {
        return (CheckBox)getControl();
    }
    public void setCheckBoxControl(CheckBox checkBox) {
        setControl(checkBox);
    }

    @Override
    public Boolean getValue() {
        return getCheckBoxControl().isSelected();
    }

    @Override
    protected void setValue(Boolean value) {
        getCheckBoxControl().setSelected(value);
    }
}
