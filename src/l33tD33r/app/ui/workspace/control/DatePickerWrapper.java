package l33tD33r.app.ui.workspace.control;

import javafx.scene.control.DatePicker;
import l33tD33r.app.database.Date;

import java.time.LocalDate;

/**
 * Created by Simon on 12/11/2014.
 */
public class DatePickerWrapper extends ItemControlWrapper<Date> {

    public DatePicker getDatePicker() {
        return (DatePicker)getControl();
    }

    public void setDatePicker(DatePicker datePicker) {
        setControl(datePicker);
    }

    @Override
    public Date getValue() {
        return Date.valueOf(getDatePicker().getValue().toString());
    }

    @Override
    protected void setValue(Date value) {
        getDatePicker().setValue(value == null ? null : LocalDate.parse(value.toString()));
    }
}
