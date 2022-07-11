package org.gemseeker.app;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.gemseeker.app.data.H2Database;
import org.gemseeker.app.views.MainWindow;

/**
 *
 * @author Gem
 */
public class Main extends Application {
    
    /**
     * The start-up service that will run preliminary tasks before the main
     * window shows up on the computer desktop.
     */
    private final Service<Void> startup = new Service<Void>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    // create app folder (if not created yet)
                    File appFolder = new File(Utils.APP_FOLDER);
                    if (!appFolder.exists()) appFolder.mkdirs();

                    // create logs folder (if not created yet)
                    File logFolder = new File(Utils.LOG_FOLDER);
                    if (!logFolder.exists()) logFolder.mkdirs();

                    // create data folder (if not created yet)
                    File dataFolder = new File(Utils.DATA_FOLDER);
                    if (!dataFolder.exists()) dataFolder.mkdirs();

                    // create images folder (if not created yet)
                    File imagesFolder = new File(Utils.IMAGES_FOLDER);
                    if (!imagesFolder.exists()) imagesFolder.mkdirs();

                    // create temp folder (if not created yet)
                    File tempFolder = new File(Utils.TEMP_FOLDER);
                    if (!tempFolder.exists()) tempFolder.mkdirs();

                    // settings.xml
                    File settingsFile = new File(Utils.SETTINGS_FILE);
                    if (!settingsFile.exists()) {
                        File origFile = new File("settings.xml");
                        FileUtils.copyFile(origFile, settingsFile);
                    }

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        System.err.println(e);
                    }
                    return null;
                }
            };
        }
    };
    
    private MainWindow mainWindow;
    private Alert errorDialog;

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainWindow = new MainWindow();
        errorDialog = new Alert(Alert.AlertType.ERROR);
        doStart();
    }

    private void doStart() {
        startup.restart();

        startup.setOnRunning(evt -> {
            System.out.print("Startup running...");
        });

        startup.setOnFailed(evt -> {
            System.out.println("FAILED");
            Platform.exit();
            System.exit(1);
        });

        startup.setOnSucceeded(evt -> {
            System.out.println("SUCCESS");
            
            // Call database for the first time. This will make sure to initialize
            // the embedded database and execute necessary updates.
            try {
                H2Database.getInstance();
            } catch (ClassNotFoundException | SQLException | IOException e) {
                System.err.println("Failed to update database.");
            }
            
            mainWindow.show();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
