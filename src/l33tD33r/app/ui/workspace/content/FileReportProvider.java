package l33tD33r.app.ui.workspace.content;

import org.w3c.dom.Document;
import l33tD33r.app.database.report.storage.ReportProvider;

/**
 * Created by Simon on 2/15/14.
 */
public class FileReportProvider implements ReportProvider {

    private Document reportDocument;

    @Override
    public Document getReportsDocument() {
        return reportDocument;
    }

    @Override
    public void setReportsDocument(Document reportDocument) {
        this.reportDocument = reportDocument;
    }
}
