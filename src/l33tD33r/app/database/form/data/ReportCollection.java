package l33tD33r.app.database.form.data;

import l33tD33r.app.database.query.Query;
import l33tD33r.app.database.query.ResultRow;
import l33tD33r.app.database.report.Column;
import l33tD33r.app.database.report.Report;
import l33tD33r.app.database.report.ReportManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Simon on 1/1/2015.
 */
public class ReportCollection implements Collection {

    private String id;

    private String reportName;

    private String[] columnNames;

    private ArrayList<Element> elements;

    public ReportCollection(String id, String reportName, String[] columnNames) {
        this.id = id;
        this.reportName = reportName;
        this.columnNames = columnNames;
    }

    public String getId() {
        return id;
    }

    private ArrayList<Element> generateElements() {

        Report report = ReportManager.getSingleton().getReport(reportName);

        ArrayList<Column> columnList = new ArrayList<>();

        for (Column column : report.getColumns()) {
            for (String columnName : columnNames) {
                if (columnName.equalsIgnoreCase(column.getName())) {
                    columnList.add(column);
                    break;
                }
            }
        }

        Column[] columns = new Column[columnList.size()];

        columnList.toArray(columns);

        Query query = report.getQuery();

        ArrayList<Element> elements = new ArrayList<>();

        for (int rowIndex = 0; rowIndex < query.getRowCount(); rowIndex++) {
            query.setPosition(rowIndex);

            ResultRow currentRow = query.getCurrentRow();

            Element currentElement = new ReportElement(columns, currentRow);

            elements.add(currentElement);
        }

        return elements;
    }

    @Override
    public List<Element> getElements() {
        if (elements == null) {
            elements = generateElements();
        }
        return new ArrayList<>(elements);
    }

    public void resetElements() {
        elements = null;
    }

    @Override
    public int getSize() {
        return getElements().size();
    }

    @Override
    public Element getElement(int index) {
        return getElements().get(index);
    }
}
