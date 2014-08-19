package l33tD33r.app.ui.workspace.content;

/**
 * Created by Simon on 2/15/14.
 */
public class TableItem extends ContentItem {

    private String qualifiedName;
    private String packageName;
    private String name;

    public TableItem(String qualifiedName) {
        this.qualifiedName = qualifiedName;

        String[] splitNames = qualifiedName.split("_");

        switch (splitNames.length) {
            case 1:
                this.packageName = "";
                this.name = qualifiedName;
                break;
            case 2:
                this.packageName = splitNames[0];
                this.name = splitNames[1];
                break;
            default:
                throw new RuntimeException("");
        }
    }

    public String getQualifiedName() {
        return this.qualifiedName;
    }

    public String getPackageName() {
        return  this.packageName;
    }

    public boolean hasPackage() { return this.packageName != null && !this.packageName.isEmpty(); }

    public String getName() {
        return this.name;
    }

    @Override
    public String getLabel() {
        return getName();
    }

    public int compareTo(TableItem other) {
        return this.getQualifiedName().compareTo(other.getQualifiedName());
    }
}
