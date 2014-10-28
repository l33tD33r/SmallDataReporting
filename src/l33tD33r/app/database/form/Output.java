package l33tD33r.app.database.form;

/**
 * Created by Simon on 10/24/2014.
 */
public abstract class Output {

    public abstract OutputType getType();

    public abstract String generateOutput(Form form);
}
