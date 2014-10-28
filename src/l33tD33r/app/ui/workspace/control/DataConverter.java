package l33tD33r.app.ui.workspace.control;

/**
 * Created by Simon on 10/26/2014.
 */
public interface DataConverter<TInput, TOutput> {
    TOutput convertData(TInput input);
}
