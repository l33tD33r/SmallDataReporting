package l33tD33r.app.ui.workspace.control;

/**
 * Created by Simon on 2014-10-31.
 */
public class StringTextControlWrapper extends TextControlWrapper<String> {
    public StringTextControlWrapper() {
        setValueConverter(new DataConverter<String, String>() {
            @Override
            public String convertData(String s) {
                return s;
            }

            @Override
            public String invertData(String s) {
                return s;
            }
        });
    }
}
