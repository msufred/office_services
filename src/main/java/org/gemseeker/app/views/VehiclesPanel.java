package org.gemseeker.app.views;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import org.gemseeker.app.data.VehicleTask;
import org.gemseeker.app.views.svgicons.AddIcon7;
import org.gemseeker.app.views.svgicons.CreateIcon;
import org.gemseeker.app.views.svgicons.DeleteIcon;
import org.gemseeker.app.views.svgicons.RefreshIcon;

/**
 *
 * @author Gem
 */
public class VehiclesPanel extends AbstractPanelController {

    @FXML private MenuButton btnMenu;
    @FXML private MenuItem menuAddVehicle;
    @FXML private MenuItem menuAddTask;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;
    @FXML private Button btnRefresh;
    
    @FXML private TableView<VehicleTask> tasksTable;
    
    private final MainWindow mainWindow;
    private final CompositeDisposable disposables;
    
    private FilteredList<VehicleTask> filteredList;
    private final SimpleObjectProperty<VehicleTask> selectedItem = new SimpleObjectProperty<>();
    
    public VehiclesPanel(MainWindow mainWindow) {
        super(VehiclesPanel.class.getResource("vehicles_panel.fxml"));
        this.mainWindow = mainWindow;
        disposables = new CompositeDisposable();
    }
    
    @Override
    public void onLoad() {
        setupIcons();
        
        selectedItem.bind(tasksTable.getSelectionModel().selectedItemProperty());
        
        disposables.addAll(
                JavaFxObservable.actionEventsOf(menuAddVehicle).subscribe(evt -> {
                    
                }),
                JavaFxObservable.actionEventsOf(menuAddTask).subscribe(evt -> {
                    
                }),
                JavaFxObservable.actionEventsOf(btnEdit).subscribe(evt -> {
                    
                }),
                JavaFxObservable.actionEventsOf(btnDelete).subscribe(evt -> {
                    
                }),
                JavaFxObservable.actionEventsOf(btnRefresh).subscribe(evt -> {
                    
                }),
                JavaFxObservable.changesOf(selectedItem).subscribe(item -> {
                    // if no selected item, disable related action buttons/menus
                    disableActions(item.getNewVal() == null);
                })
        );
    }
    
    private void setupIcons() {
        btnEdit.setGraphic(new CreateIcon(12));
        btnDelete.setGraphic(new DeleteIcon(12));
        btnRefresh.setGraphic(new RefreshIcon(12));
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onResume() {
    }
    
    private void disableActions(boolean disable) {
        menuAddTask.setDisable(disable);
        btnEdit.setDisable(disable);
        btnDelete.setDisable(disable);
    }
    
    @Override
    public void onDispose() {
        disposables.dispose();
    }

}
