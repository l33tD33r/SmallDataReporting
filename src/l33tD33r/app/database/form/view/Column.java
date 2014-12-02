package l33tD33r.app.database.form.view;

import l33tD33r.app.database.form.CollectionComponent;

/**
 * Created by Simon on 2014-10-31.
 */
public class Column extends CollectionComponent {

    private String propertyId;

    private String header;

    private View cellView;

    public Column(String propertyId, String header, View cellView) {
        setPropertyId(propertyId);
        setHeader(header);
        setCellView(cellView);
    }

    public String getPropertyId() {
        return propertyId;
    }
    private void setPropertyId(String propertyId){
        this.propertyId = propertyId;
    }

    public String getHeader() {
        return header;
    }
    private void setHeader(String header) {
        this.header = header;
    }

    public View getCellView() {
        return cellView;
    }
    private void setCellView(View cellView) {
        this.cellView = cellView;
    }

    public boolean isEditable() {
        return getCellView() != null;
    }
}
