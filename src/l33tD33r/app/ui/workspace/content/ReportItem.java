package l33tD33r.app.ui.workspace.content;

/**
 * Created by Simon on 2/15/14.
 */
public class ReportItem extends ContentItem {

    private String name;

    public ReportItem(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String getLabel() {
        return getName();
    }

    public int compareTo(ReportItem other) {
        return getName().compareTo(other.getName());
    }
}
