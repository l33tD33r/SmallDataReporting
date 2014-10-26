package l33tD33r.app.database.form;

/**
 * Created by Simon on 10/24/2014.
 */
public abstract class DropDownView extends View {

    @Override
    public ViewType getType() {
        return ViewType.DropDown;
    }

    public abstract DropDownSource getSourceType();
}
