package org.gemseeker.app.views;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SplashWindow extends AbstractWindowController {

    public SplashWindow() {
        super("", SplashWindow.class.getResource("splash.fxml"), null);
    }

    @Override
    public void initWindow(Stage stage) {
        super.initWindow(stage);
        stage.initStyle(StageStyle.UNDECORATED);
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onClose() {

    }

    @Override
    public void onDispose() {

    }
}
