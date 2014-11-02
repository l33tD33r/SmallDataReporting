package l33tD33r.app.ui.workspace.control;

/**
 * Created by Simon on 2014-10-31.
 */
public class StringTextControlWrapper extends TextControlWrapper<String> {
    public StringTextControlWrapper() {
        setValueConverter(s -> s);
    }
}
