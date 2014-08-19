package l33tD33r.app.ui.workspace.content;

/**
 * Created by Simon on 2/15/14.
 */
public class FolderItem extends ContentItem {
    private String name;

    public FolderItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    @Override
    public String getLabel() {
        return getName();
    }
}
