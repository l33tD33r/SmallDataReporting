package l33tD33r.app.ui.workspace.content;

/**
 * Created by Simon on 10/25/2014.
 */
public class FormItem extends ContentItem {

    private String name;

    public FormItem(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String getLabel() {
        return getName();
    }

    public int compareTo(FormItem other) {
        return getName().compareTo(other.getName());
    }
}
