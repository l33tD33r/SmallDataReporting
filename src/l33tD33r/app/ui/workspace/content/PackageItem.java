package l33tD33r.app.ui.workspace.content;

/**
 * Created by Simon on 2/15/14.
 */
public class PackageItem extends ContentItem {

    private String name;

    public PackageItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getLabel() {
        return getName();
    }

    public int compareTo(PackageItem other) {
        return this.getName().compareTo(other.getName());
    }
}
