package l33tD33r.app.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import l33tD33r.app.database.data.DataManager;
import l33tD33r.app.database.data.storage.DataProvider;
import l33tD33r.app.database.data.storage.FileDataProvider;
import l33tD33r.app.database.report.ReportManager;
import l33tD33r.app.database.report.storage.FileReportProvider;
import l33tD33r.app.database.report.storage.ReportProvider;
import l33tD33r.app.database.schema.SchemaManager;
import l33tD33r.app.database.schema.storage.FileSchemaProvider;
import l33tD33r.app.database.utility.FileUtils;
import l33tD33r.app.ui.workspace.WorkspaceController;

import java.io.File;
import java.io.IOException;
//import l33tD33r.app.ui.workspace.content.FileDataProvider;
//import l33tD33r.app.ui.workspace.content.FileReportProvider;

/**
 * Created by Simon on 2/14/14.
 */
public class Main extends Application {

    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    private FileSchemaProvider schemaProvider;

    private FileDataProvider dataProvider;

    public DataProvider getDataProvider() { return dataProvider; }

    private FileReportProvider reportProvider;

    public ReportProvider getReportProvider() { return reportProvider; }

    @Override
    public void start(Stage stage) throws Exception {

        loadFiles();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("workspace.fxml"));

        Parent root = loader.load();

        this.stage = stage;

        ((WorkspaceController)loader.getController()).setStage(this);

        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("SmallData Reporting");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void loadFiles() throws IOException {

        File existingSchemaFile = new File(GAMING_RESOURCE_DIRECTORY_PATH + "\\" + SCHEMA_FILE_NAME);
        File transactionSchemaFile = new File(GAMING_RESOURCE_DIRECTORY_PATH + "\\transaction." + SCHEMA_FILE_NAME);

        FileUtils.copyFile(existingSchemaFile, transactionSchemaFile);

        schemaProvider = new FileSchemaProvider(transactionSchemaFile.getAbsolutePath());

        SchemaManager.createSingleton(schemaProvider);

        File existingReportFile = new File(GAMING_RESOURCE_DIRECTORY_PATH + "\\" + REPORT_FILE_NAME);
        File transactionReportFile = new File(GAMING_RESOURCE_DIRECTORY_PATH + "\\transaction." + REPORT_FILE_NAME);

        FileUtils.copyFile(existingReportFile, transactionReportFile);

        reportProvider = new FileReportProvider(transactionReportFile.getAbsolutePath());

        ReportManager.createSingleton(reportProvider);

        File existingDataFile = new File(GAMING_RESOURCE_DIRECTORY_PATH + "\\" + DATA_FILE_NAME);
        File transactionDataFile = new File(GAMING_RESOURCE_DIRECTORY_PATH + "\\transaction." + DATA_FILE_NAME);

        FileUtils.copyFile(existingDataFile, transactionDataFile);

        dataProvider = new FileDataProvider(transactionDataFile.getAbsolutePath());

        DataManager.createSingleton(dataProvider);
    }

    public static String GAMING_RESOURCE_DIRECTORY_PATH = ".\\data\\gaming";

    public static String SCHEMA_FILE_NAME = "schema.xml";

    public static String REPORT_FILE_NAME = "report.xml";

    public static String DATA_FILE_NAME = "data.xml";
}
