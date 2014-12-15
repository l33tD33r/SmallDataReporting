package l33tD33r.app.ui.workspace.control;

/**
 * Created by Simon on 11/7/2014.
 */
public class IntegerTextControlWrapper extends TextControlWrapper<Integer> {
    public IntegerTextControlWrapper() {
        setValueConverter(new DataConverter<String, Integer>() {
            @Override
            public Integer convertData(String s) {
                return Integer.parseInt(s);
            }

            @Override
            public String invertData(Integer integer) {
                return integer.toString();
            }
        });
    }
}
