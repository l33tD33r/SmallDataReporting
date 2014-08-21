package l33tD33r.app.ui.workspace.data;

/**
 * Created by Simon on 8/20/2014.
 */
public class DataRecordReference {
    private String id;
    private String label;

    public DataRecordReference(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return getLabel();
    }
}
