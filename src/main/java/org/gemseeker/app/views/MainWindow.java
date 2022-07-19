package org.gemseeker.app.views;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.gemseeker.app.data.H2Database;

public class MainWindow extends AbstractWindowController {

    @FXML private MenuItem menuOffices;
    @FXML private MenuItem menuWarehouses;
    @FXML private MenuItem menuPositions;
    
    @FXML private ToggleButton toggleEmployees;
    @FXML private ToggleButton toggleVehicles;
    @FXML private ToggleButton toggleInventory;
    @FXML private StackPane container;
    @FXML private ProgressBar progressBar;
    @FXML private Label progressLabel;
    
    private final CompositeDisposable disposables;
    
    // Panels
    private final EmployeesPanel employeesPanel;
    private final VehiclesPanel vehiclesPanel;
    private AbstractPanelController mCurrentPanel;
    
    // Windows
    private final OfficesWindow officesWindow;
    private final PositionsWindow positionsWindow;
    
    public MainWindow() {
        super("RAFIS 12", MainWindow.class.getResource("main_window.fxml"), null);
        disposables = new CompositeDisposable();
        
        employeesPanel = new EmployeesPanel(this);
        vehiclesPanel = new VehiclesPanel(this);
        
        officesWindow = new OfficesWindow();
        positionsWindow = new PositionsWindow();
    }

    @Override
    public void initWindow(Stage stage) {
        super.initWindow(stage);
        stage.setMaximized(true);
    }

    @Override
    public void onLoad() {
        setProgressBar(progressBar);
        setProgressLabel(progressLabel);
        
        addEventFilters(toggleEmployees, toggleVehicles, toggleInventory);
        
        disposables.addAll(
                JavaFxObservable.actionEventsOf(menuOffices).subscribe(evt -> {
                    officesWindow.show();
                }),
                JavaFxObservable.actionEventsOf(menuWarehouses).subscribe(evt -> {
                    
                }),
                JavaFxObservable.actionEventsOf(menuPositions).subscribe(evt -> {
                    positionsWindow.show();
                }),
                JavaFxObservable.actionEventsOf(toggleEmployees).subscribe(evt -> {
                    boolean selected = toggleEmployees.isSelected();
                    if (selected) changePanel(employeesPanel);
                }),
                JavaFxObservable.actionEventsOf(toggleVehicles).subscribe(evt -> {
                    boolean selected = toggleVehicles.isSelected();
                    if (selected) changePanel(vehiclesPanel);
                }),
                JavaFxObservable.actionEventsOf(toggleInventory).subscribe(evt -> {
                    boolean selected = toggleInventory.isSelected();
                    if (selected) {
                        System.out.println("Inventory");
                    }
                })
        );
    }

    @Override
    public void show() {
        super.show();
        changePanel(employeesPanel);
    }
    
    private void changePanel(AbstractPanelController panelController) {
        // ignore if panelController is the same as the current panel
        if (mCurrentPanel != null && mCurrentPanel == panelController) return;
        
        if (mCurrentPanel != null) mCurrentPanel.onPause();
        container.getChildren().clear();
        container.getChildren().add(panelController.getContent());
        panelController.onResume();
        mCurrentPanel = panelController;
    }
    
    private void addEventFilters(ToggleButton...toggles) {
        for (ToggleButton toggle : toggles) {
            toggle.addEventFilter(MouseEvent.MOUSE_PRESSED, evt -> {
                if (toggle.isSelected()) evt.consume();
            });
        }
    }

    @Override
    public void onClose() {
        onDispose();
        try {
            H2Database.getInstance().closeDatabase();
        } catch (ClassNotFoundException | IOException | SQLException e) {
            System.err.println("Failed to close database.");
        }
    }

    @Override
    public void onDispose() {
        // Dispose AbstracPanelControllers
        employeesPanel.onDispose();
        vehiclesPanel.onDispose();
        
        // Dispose AbstractWindowControllers
        officesWindow.onDispose();
        positionsWindow.onDispose();
        
        // Finally, dispose all observables of this controller
        disposables.dispose();
    }
}
