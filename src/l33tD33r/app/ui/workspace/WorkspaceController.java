package l33tD33r.app.ui.workspace;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import l33tD33r.app.database.data.DataTable;
import l33tD33r.app.database.form.FormManager;
import l33tD33r.app.database.utility.FileUtils;
import l33tD33r.app.ui.workspace.data.CreateRecordStage;
import l33tD33r.app.ui.workspace.form.FormStage;
import l33tD33r.app.ui.workspace.visualization.html.WebChartView;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import l33tD33r.app.database.data.DataManager;
import l33tD33r.app.database.report.ReportManager;
import l33tD33r.app.database.schema.SchemaManager;
import l33tD33r.app.database.utility.XmlUtils;
import l33tD33r.app.ui.Main;
import l33tD33r.app.ui.workspace.content.*;

import javafx.event.ActionEvent;
import l33tD33r.app.ui.workspace.report.ReportView;
import l33tD33r.app.ui.workspace.visualization.ChartView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by Simon on 2/15/14.
 */
public class WorkspaceController {

    @FXML private TreeView<ContentItem> contentTreeView;

    @FXML private TableView<Map> reportTableView;

    @FXML private Pane reportChartPane;

    @FXML private Pane webReportChartPane;

    private TreeItem<ContentItem> tableRootItem, reportsRootItem, formsRootItem;

    private Main mainApplication;

    private ReportView reportView;

    private ChartView chartView;

    private WebChartView webChartView;

    public void initialize() {
        loadTreeRoot();
    }

    public void setStage(Main mainApplication) {
        this.mainApplication = mainApplication;

        loadTables();

        loadReports();

        loadForms();
    }

    private void loadTreeRoot() {
        reportView = new ReportView(reportTableView);

        chartView = new ChartView(reportChartPane);

        webChartView = new WebChartView(webReportChartPane);

        TreeItem<ContentItem> root = new TreeItem<>(new FolderItem("Content"));
        root.setExpanded(true);

        tableRootItem = new TreeItem<>(new FolderItem("Tables"));
        reportsRootItem = new TreeItem<>(new FolderItem("Reports"));
        formsRootItem = new TreeItem<>(new FormItem("Forms"));

        root.getChildren().addAll(tableRootItem, reportsRootItem, formsRootItem);

        contentTreeView.setRoot(root);
        contentTreeView.setShowRoot(false);

        contentTreeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    TreeItem<ContentItem> clickedNode = contentTreeView.getSelectionModel().getSelectedItem();
                    ContentItem clickedItem = clickedNode.getValue();
                    if (clickedItem instanceof TableItem) {
                        TableItem tableItem = (TableItem)clickedItem;
                        reportView.loadTable(tableItem.getQualifiedName());

                        chartView.loadChart(null);

                        webChartView.loadChart(null);
                    } else if (clickedItem instanceof ReportItem) {
                        ReportItem reportItem = (ReportItem)clickedItem;
                        reportView.loadReport(reportItem.getName());

                        chartView.loadChart(reportItem.getName());

                        webChartView.loadChart(reportItem.getName());
                    } else if (clickedItem instanceof FormItem) {
                        FormItem formItem = (FormItem)clickedItem;

                        FormStage formStage = new FormStage(FormManager.getSingleton().getForm(formItem.getName()));
                        formStage.show();
                    }
                }
            }
        });
    }

    public void handleImportData(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        File initialDirectory = new File("E:\\Development\\C#\\Projects\\ConvertGamingHistoryStorageXml\\ConvertGamingHistoryStorageXml\\Data\\UpdatedSystemData");
        fileChooser.setInitialDirectory(initialDirectory);
        File file = fileChooser.showOpenDialog(mainApplication.getStage());
        if (file != null) {
            importFile(file);
        }
    }

    private void importFile(File file) {
        Document document = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            document = XmlUtils.createDocument(fis);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (document == null) {
            return;
        }

        if ("Data".equals(XmlUtils.getElementName(document.getDocumentElement()))) {
            mainApplication.getDataProvider().setDataDocument(document);
            DataManager.getSingleton().reloadDataTables();
            loadTables();
            return;
        }

        if ("Reports".equals(XmlUtils.getElementName(document.getDocumentElement()))) {
            mainApplication.getReportProvider().setReportsDocument(document);
            ReportManager.getSingleton().reloadReports();
            loadReports();
            return;
        }

        if ("Forms".equals(XmlUtils.getElementName(document.getDocumentElement()))) {
            mainApplication.getFormProvider().setFormsDocument(document);
            FormManager.getSingleton().reloadForms();
        }
    }

    public void handleSaveDataChanges(ActionEvent event) {
        File transactionDataFile = new File(Main.DATA_RESOURCE_DIRECTORY_PATH + "\\transaction." + Main.DATA_FILE_NAME);
        File dataFile = new File(Main.DATA_RESOURCE_DIRECTORY_PATH + "\\" + Main.DATA_FILE_NAME);

        try {
            FileUtils.copyFile(transactionDataFile, dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadTables() {
        tableRootItem.getChildren().clear();

        ArrayList<TableItem> allTables = new ArrayList<>();

        for (String tableQualifiedName : SchemaManager.getSingleton().getDataTableNames()) {
            allTables.add(new TableItem(tableQualifiedName));
        }

        ArrayList<PackageItem> allPackages = findPackages(allTables);

        for (PackageItem packageItem : allPackages) {

            TreeItem<ContentItem> packageNode = new TreeItem<>(packageItem);

            ArrayList<TableItem> packageTables = findTables(packageItem.getName(), allTables);

            boolean addDirectlyToTableRoot = false;
            if (packageItem.getName() == null || packageItem.getName().isEmpty()) {
                addDirectlyToTableRoot = true;
            }

            for (TableItem tableItem : packageTables) {
                TreeItem<ContentItem> tableNode = new TreeItem<>(tableItem);

                if (addDirectlyToTableRoot) {
                    tableRootItem.getChildren().add(tableNode);
                } else {
                    packageNode.getChildren().add(tableNode);
                    packageNode.setExpanded(true);
                }
            }

            if (!addDirectlyToTableRoot) {
                tableRootItem.getChildren().add(packageNode);
            }
        }

        tableRootItem.setExpanded(true);
    }

    private ArrayList<PackageItem> findPackages(ArrayList<TableItem> allTables) {
        ArrayList<PackageItem> allPackages = new ArrayList<>();

        HashSet<String> packageNames = new HashSet<>();

        for (TableItem table : allTables) {
            String packageName;
            if (table.hasPackage()) {
                packageName = table.getPackageName();
            } else {
                packageName = "";
            }

            packageNames.add(packageName);
        }

        for (String packageName : packageNames) {
            allPackages.add(new PackageItem(packageName));
        }

        allPackages.sort(new Comparator<PackageItem>() {
            @Override
            public int compare(PackageItem a, PackageItem b) {
                return a.compareTo(b);
            }
        });

        return allPackages;
    }

    private ArrayList<TableItem> findTables(String packageName, ArrayList<TableItem> allTables) {
        ArrayList<TableItem> packageTables = new ArrayList<>();

        for (TableItem table : allTables) {
            boolean addPackage = false;

            if (table.hasPackage()) {
                addPackage = table.getPackageName().equals(packageName);
            } else {
                addPackage = (packageName == null || packageName.isEmpty());
            }

            if (addPackage) {
                packageTables.add(table);
            }
        }

        packageTables.sort(new Comparator<TableItem>() {
            @Override
            public int compare(TableItem a, TableItem b) {
                return a.compareTo(b);
            }
        });

        return  packageTables;
    }

    private void loadReports() {
        reportsRootItem.getChildren().clear();

        ArrayList<ReportItem> allReports = new ArrayList<>();

        for (String reportName : ReportManager.getSingleton().getReportNames()) {
            allReports.add(new ReportItem(reportName));
        }

        allReports.sort((a, b) -> a.compareTo(b));

        for (ReportItem reportItem : allReports) {
            reportsRootItem.getChildren().add(new TreeItem<>(reportItem));
        }

        reportsRootItem.setExpanded(true);
    }

    private void loadForms() {
        formsRootItem.getChildren().clear();

        ArrayList<FormItem> allForms = new ArrayList<>();

        for (String formName : FormManager.getSingleton().getReportNames()) {
            allForms.add(new FormItem(formName));
        }

        allForms.sort((a, b) -> a.compareTo(b));

        for (FormItem formItem : allForms) {
            formsRootItem.getChildren().add(new TreeItem<>(formItem));
        }

        formsRootItem.setExpanded(true);
    }

    public void handleAddRecord(ActionEvent event) {

        DataTable currentDataTable = reportView.getCurrentDataTable();
        if (currentDataTable == null) {
            return;
        }

        CreateRecordStage createRecordStage = new CreateRecordStage(reportView);
        createRecordStage.show();
    }
}
