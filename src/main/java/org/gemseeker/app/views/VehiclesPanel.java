package org.gemseeker.app.views;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import java.time.LocalDate;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.gemseeker.app.data.H2Database;
import org.gemseeker.app.data.Vehicle;
import org.gemseeker.app.data.VehicleTask;
import org.gemseeker.app.views.listcells.VehicleListItem;
import org.gemseeker.app.views.svgicons.CreateIcon;
import org.gemseeker.app.views.svgicons.DeleteIcon;
import org.gemseeker.app.views.svgicons.RefreshIcon;
import org.gemseeker.app.views.tablecells.VehicleDateTableCell;

/**
 *
 * @author Gem
 */
public class VehiclesPanel extends AbstractPanelController {

    // @FXML private MenuButton btnMenu;
    @FXML private MenuItem menuAddVehicle;
    @FXML private MenuItem menuAddTask;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;
    @FXML private Button btnRefresh;
    
    @FXML private ListView<Vehicle> vehicleListview;
    
    @FXML private TableView<VehicleTask> tasksTable;
    @FXML private TableColumn<VehicleTask, Integer> colVehicle;
    @FXML private TableColumn<VehicleTask, String> colStatus;
    @FXML private TableColumn<VehicleTask, LocalDate> colDate;
    @FXML private TableColumn<VehicleTask, String> colDriver;
    @FXML private TableColumn<VehicleTask, String> colPassenger;
    @FXML private TableColumn<VehicleTask, Double> colDestination;
    @FXML private TableColumn<VehicleTask, LocalDate> colDateOut;
    @FXML private TableColumn<VehicleTask, LocalDate> colDateIn;
    
    // Context Menus
    // -- Vehicle List
    private ContextMenu listContextMenu;
    private MenuItem cmEditVehicle = new MenuItem("Edit");
    private MenuItem cmNewTask = new MenuItem("New Task");
    private MenuItem cmShowHistory = new MenuItem("Show Task History");
    private MenuItem cmChangeStatus = new MenuItem("Change Status");
    private MenuItem cmDeleteVehicle = new MenuItem("Remove Vehicle");
    
    // -- Tasks Table
    private ContextMenu taskContextMenu;
    private MenuItem cmStartTask = new MenuItem("Start Task");
    private MenuItem cmCancelTask = new MenuItem("Cancel Task");
    private MenuItem cmMarkDone = new MenuItem("Mark Done");
    
    private final MainWindow mainWindow;
    private final CompositeDisposable disposables;
    private final AddVehicleWindow addVehicleWindow;
    
    private FilteredList<Vehicle> vehicleFilteredList;
    private FilteredList<VehicleTask> filteredList;
    private final SimpleObjectProperty<VehicleTask> selectedItem = new SimpleObjectProperty<>();
    
    public VehiclesPanel(MainWindow mainWindow) {
        super(VehiclesPanel.class.getResource("vehicles_panel.fxml"));
        this.mainWindow = mainWindow;
        disposables = new CompositeDisposable();
        
        addVehicleWindow = new AddVehicleWindow(mainWindow, this);
    }
    
    @Override
    public void onLoad() {
        setupIcons();
        setupListview();
        setupTable();
        
        selectedItem.bind(tasksTable.getSelectionModel().selectedItemProperty());
        
        disposables.addAll(
                JavaFxObservable.actionEventsOf(menuAddVehicle).subscribe(evt -> {
                    addVehicleWindow.show();
                }),
                JavaFxObservable.actionEventsOf(menuAddTask).subscribe(evt -> {
                    
                }),
                JavaFxObservable.actionEventsOf(btnEdit).subscribe(evt -> {
                    
                }),
                JavaFxObservable.actionEventsOf(btnDelete).subscribe(evt -> {
                    
                }),
                JavaFxObservable.actionEventsOf(btnRefresh).subscribe(evt -> {
                    refresh();
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
    
    private void setupListview() {
        listContextMenu = new ContextMenu();
        listContextMenu.getItems().addAll(cmEditVehicle, cmNewTask, cmShowHistory,
                cmChangeStatus, cmDeleteVehicle);
        vehicleListview.setContextMenu(listContextMenu);
        vehicleListview.setCellFactory(cell -> new VehicleListItem());
        
        disposables.addAll(
                JavaFxObservable.actionEventsOf(cmEditVehicle).subscribe(evt -> {
                    
                }),
                JavaFxObservable.actionEventsOf(cmNewTask).subscribe(evt -> {
                    
                }),
                JavaFxObservable.actionEventsOf(cmShowHistory).subscribe(evt -> {
                    
                }),
                JavaFxObservable.actionEventsOf(cmChangeStatus).subscribe(evt -> {
                    
                }),
                JavaFxObservable.actionEventsOf(cmDeleteVehicle).subscribe(evt -> {
                    
                })
        );
    }
    
    private void setupTable() {
        // context menus
        taskContextMenu = new ContextMenu();
        taskContextMenu.getItems().addAll(cmStartTask, cmCancelTask, cmMarkDone);
        tasksTable.setContextMenu(taskContextMenu);
        
        colVehicle.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        colVehicle.setCellFactory(col -> new TableCell<VehicleTask, Integer>() {
            @Override
            protected void updateItem(Integer id, boolean empty) {
                super.updateItem(id, empty);
                if (!empty) {
                    Vehicle vehicle = getVehicle(id);
                    if (vehicle != null) {
                        setText(vehicle.getName());
                        // TODO show graphics of vehicle status (like wrench if vehicle is for repair etc)
                    }
                    else setText("");
                }
            }
        });
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDate.setCellFactory(col -> new VehicleDateTableCell());
        colDriver.setCellValueFactory(new PropertyValueFactory<>("driver"));
        colPassenger.setCellValueFactory(new PropertyValueFactory<>("passenger"));
        colDestination.setCellValueFactory(new PropertyValueFactory<>("destination"));
        colDateOut.setCellValueFactory(new PropertyValueFactory<>("dateOut"));
        colDateOut.setCellFactory(col -> new VehicleDateTableCell());
        colDateIn.setCellValueFactory(new PropertyValueFactory<>("dateIn"));
        colDateIn.setCellFactory(col -> new VehicleDateTableCell());
        
        disposables.addAll(
                JavaFxObservable.actionEventsOf(cmStartTask).subscribe(evt -> {
                    
                }),
                JavaFxObservable.actionEventsOf(cmCancelTask).subscribe(evt -> {
                    
                }),
                JavaFxObservable.actionEventsOf(cmMarkDone).subscribe(evt -> {
                    
                })
        );
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onResume() {
        refresh();
    }
    
    public void refresh() {
        mainWindow.showProgress(true, "Fetching vehicles data...");
        disposables.add(Single.fromCallable(() -> {
            return H2Database.getInstance().getAllVehicles();
        }).subscribeOn(Schedulers.io()).observeOn(JavaFxScheduler.platform()).subscribe(vehicles -> {
            mainWindow.showProgress(false);
            vehicleFilteredList = new FilteredList<>(FXCollections.observableArrayList(vehicles), v -> true);
            vehicleListview.setItems(vehicleFilteredList);
            fetchVehicleTasks();
        }, err -> {
            mainWindow.showProgress(false);
            showErrorDialog("Database Error", "Error while retrieving vehicles data.", err);
        }));
    }
    
    private void fetchVehicleTasks() {
        mainWindow.showProgress(true, "Fetching vehicle tasks data...");
        disposables.add(Single.fromCallable(() -> {
            return H2Database.getInstance().getAllPendingAndActiveVehicleTasks();
        }).subscribeOn(Schedulers.io()).observeOn(JavaFxScheduler.platform()).subscribe(tasks -> {
            mainWindow.showProgress(false);
            filteredList = new FilteredList<>(FXCollections.observableArrayList(tasks), p -> true);
            tasksTable.setItems(filteredList);
        }, err -> {
            mainWindow.showProgress(false);
            showErrorDialog("Database Error", "Error while retrieving vehicle tasks data.", err);
        }));
    }
    
    private Vehicle getVehicle(int id) {
        if (vehicleFilteredList.getSource().size() > 0) {
            return vehicleFilteredList.getSource().stream()
                    .filter(v -> v.getId() == id)
                    .collect(Collectors.toList())
                    .get(0);
        }
        return null;
    }
    
    private void disableActions(boolean disable) {
        menuAddTask.setDisable(disable);
        btnEdit.setDisable(disable);
        btnDelete.setDisable(disable);
    }
    
    @Override
    public void onDispose() {
        disposables.dispose();
        addVehicleWindow.close();
    }

}
