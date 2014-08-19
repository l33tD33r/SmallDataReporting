package l33tD33r.app.ui.workspace;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
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

    private TreeItem<ContentItem> tableRootItem, reportsRootItem;

    private Main mainApplication;

    private ReportView reportView;

    private ChartView chartView;

    public void initialize() {
        loadTreeRoot();
    }

    public void setStage(Main mainApplication) {
        this.mainApplication = mainApplication;

        loadTables();

        loadReports();
    }

    private void loadTreeRoot() {
        reportView = new ReportView(reportTableView);

        chartView = new ChartView(reportChartPane);

        TreeItem<ContentItem> root = new TreeItem<>(new FolderItem("Content"));
        root.setExpanded(true);

        tableRootItem = new TreeItem<>(new FolderItem("Tables"));
        reportsRootItem = new TreeItem<>(new FolderItem("Reports"));

        root.getChildren().addAll(tableRootItem, reportsRootItem);

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
                    } else if (clickedItem instanceof ReportItem) {
                        ReportItem reportItem = (ReportItem)clickedItem;
                        reportView.loadReport(reportItem.getName());

                        chartView.loadChart(reportItem.getName());
                    }
                }
            }
        });
    }

    public void handleFileImport(ActionEvent event) {
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

        } catch (SAXException e) {

        } catch (IOException e) {

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

        allReports.sort(new Comparator<ReportItem>() {
            @Override
            public int compare(ReportItem a, ReportItem b) {
                return a.compareTo(b);
            }
        });

        for (ReportItem reportItem : allReports) {
            reportsRootItem.getChildren().add(new TreeItem<ContentItem>(reportItem));
        }

        reportsRootItem.setExpanded(true);
    }
}
