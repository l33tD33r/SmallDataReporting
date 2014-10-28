package l33tD33r.app.database.form;

/**
 * Created by Simon on 10/25/2014.
 */
public abstract class OutputSource {

    public abstract OutputSourceType getType();

    public abstract String getOutput(Form form);
}
