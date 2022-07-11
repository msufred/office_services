package org.gemseeker.app.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public abstract class AbstractPanelController {

    protected URL fxmlUrl;
    protected Pane root;

    private final Alert infoDialog;
    private final Alert errorDialog;
    private final Alert confirmDialog;

    protected ProgressBar _progressBar;
    protected Label _progressLabel;
    
    public AbstractPanelController(URL fxmlUrl) {
        this.fxmlUrl = fxmlUrl;

        infoDialog = new Alert(Alert.AlertType.INFORMATION);
        infoDialog.setTitle("Information");
        errorDialog = new Alert(Alert.AlertType.ERROR);
        errorDialog.setHeaderText("Error");
        confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Action");
    }

    protected Pane getContent() {
        if (root == null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(fxmlUrl);
            loader.setController(this);
            try {
                root = loader.load();
                onLoad();
            } catch (IOException e) {
                throw new RuntimeException("Failed to load fxml file: " + e);
            }
        }
        return root;
    }

    public abstract void onLoad();
    public abstract void onPause();
    public abstract void onResume();
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
