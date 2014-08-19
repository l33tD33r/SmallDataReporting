package l33tD33r.app.database.report;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.w3c.dom.Document;
import l33tD33r.app.database.report.storage.ReportProvider;

public class ReportManager {

	//Singleton
	private static ReportManager singleton = null;
	
	public static boolean singletonCreated() {
		return singleton != null;
	}
	
	public static void createSingleton(ReportProvider reportProvider) {
		if (singletonCreated()) {
			throw new RuntimeException("ReportManager can only be created once");
		}
		singleton = new ReportManager(reportProvider);
	}

    public static ReportManager getSingleton() { return singleton; }
	
	private ReportProvider reportProvider;
	
	private LinkedHashMap<String,Report> reportsMap;
	
	private ReportManager(ReportProvider reportProvider) {
		this.reportProvider = reportProvider;
		this.reportsMap = new LinkedHashMap<String, Report>();
		loadReports();
	}
	
	private void loadReports() {
        Document reportDocument = reportProvider.getReportsDocument();
        if (reportDocument == null) {
            return;
        }
		for (Report report : ReportSerialization.deserializeReports(reportDocument)) {
			loadReport(report);
		}
	}
	
	private void loadReport(Report report) {
		reportsMap.put(report.getName(), report);
	}
	
	public void reloadReports() {
		reportsMap.clear();
		loadReports();
	}

    public Report getReport(String reportName) {
        return reportsMap.get(reportName);
    }

    public ArrayList<String> getReportNames() {
        return new ArrayList<>(reportsMap.keySet());
    }
}
