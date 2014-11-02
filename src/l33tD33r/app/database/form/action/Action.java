package l33tD33r.app.database.form.action;

import l33tD33r.app.database.form.FormComponent;

/**
 * Created by Simon on 2014-10-28.
 */
public abstract class Action extends FormComponent {

    public abstract ActionType getType();

    public abstract void execute();
}
