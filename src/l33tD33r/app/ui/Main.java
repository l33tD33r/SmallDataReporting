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
import l33tD33r.app.ui.workspace.WorkspaceController;

import java.io.File;
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
        schemaProvider= new FileSchemaProvider(".\\data\\gaming\\schema.xml");

        SchemaManager.createSingleton(schemaProvider);

        dataProvider = new FileDataProvider(".\\data\\gaming\\data.xml");

        DataManager.createSingleton(dataProvider);

        reportProvider = new FileReportProvider(".\\data\\gaming\\report.xml");

        ReportManager.createSingleton(reportProvider);

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
}
