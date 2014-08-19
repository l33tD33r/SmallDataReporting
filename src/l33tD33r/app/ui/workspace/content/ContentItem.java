package l33tD33r.app.ui.workspace.content;

/**
 * Created by Simon on 2/15/14.
 */
public abstract class ContentItem {
    public abstract String getLabel();

    @Override
    public String toString() {
        return getLabel();
    }
}
