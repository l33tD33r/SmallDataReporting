package l33tD33r.app.ui.workspace.control;

/**
 * Created by Simon on 11/7/2014.
 */
public class IntegerTextControlWrapper extends TextControlWrapper<Integer> {
    public IntegerTextControlWrapper() {
        setValueConverter(s -> Integer.parseInt(s));
    }
}
