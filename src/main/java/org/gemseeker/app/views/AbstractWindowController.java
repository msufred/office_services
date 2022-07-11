package org.gemseeker.app.views;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public abstract class AbstractWindowController {

    protected Stage stage;
    protected Scene scene;
    protected Pane root;
    protected Stage owner;
    protected String windowTitle;
    protected URL fxmlUrl;

    protected ProgressBar _progressBar;
    protected Label _progressLabel;
    
    private final EventHandler<WindowEvent> onCloseRequest = evt -> onClose();

    private final Alert infoDialog;
    private final Alert errorDialog;
    private final Alert confirmDialog;

    public AbstractWindowController(String windowTitle, URL fxmlUrl, Stage owner) {
        this.windowTitle = windowTitle;
        this.fxmlUrl = fxmlUrl;
        this.owner = owner;

        infoDialog = new Alert(Alert.AlertType.INFORMATION);
        infoDialog.setTitle("Information");
        errorDialog = new Alert(Alert.AlertType.ERROR);
        errorDialog.setHeaderText("Error");
        confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmation");
    }

    protected Stage getStage() {
        if (stage == null) {
            stage = new Stage();
            stage.setTitle(windowTitle);
            stage.initOwner(owner);
            File icon = new File("app-icon.png");
            if (icon.exists()) stage.getIcons().add(new Image(icon.toURI().toString()));
            stage.setOnHidden(onCloseRequest);
            stage.setScene(getScene());
            initWindow(stage);
        }
        return stage;
    }

    protected Scene getScene() {
        if (scene == null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(fxmlUrl);
            loader.setController(this);
            try {
                root = loader.load();
                onLoad();
                scene = new Scene(root);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load fxml file: " + e);
            }
        }
        return scene;
    }

    public void show() {
        getStage().show();
    }

    public void close() {
        getStage().close();
    }

    public void initWindow(Stage stage) {

    }

    /**
     * Called right after FXML file is loaded and added to a scene.
     */
    public abstract void onLoad();

    /**
     * Called when window is closed/hidden.
     */
    public abstract void onClose();

    /**
     * Called when the parent window/application is exited.
     */
    public abstract void onDispose();

    public void showInfoDialog(String title, String content) {
        infoDialog.setHeaderText(title);
        infoDialog.setContentText(content);
        infoDialog.showAndWait();
    }

    public void showErrorDialog(String title, String content, Throwable t) {
        errorDialog.setHeaderText(title);
        errorDialog.setContentText(content + "\n" + t);
        errorDialog.showAndWait();
    }

    public Optional<ButtonType> showConfirmDialog(String title, String content, ButtonType...buttonTypes) {
        confirmDialog.setHeaderText(title);
        confirmDialog.setContentText(content);
        if (buttonTypes.length > 0) {
            confirmDialog.getButtonTypes().setAll(buttonTypes);
        }
        return confirmDialog.showAndWait();
    }
    
    public void setProgressBar(ProgressBar progressBar) {
        this._progressBar = progressBar;
    }
    
    public void setProgressLabel(Label progressLabel) {
        this._progressLabel = progressLabel;
    }
    
    public void showProgress(boolean show) {
        showProgress(show, "");
    }
    
    public void showProgress(boolean show, String progressText) {
        if (_progressBar != null) _progressBar.setVisible(show);
        if (_progressLabel != null) {
            _progressLabel.setVisible(show);
            _progressLabel.setText(progressText);
        }
    }
}
