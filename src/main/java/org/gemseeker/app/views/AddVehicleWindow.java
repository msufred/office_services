package org.gemseeker.app.views;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.gemseeker.app.data.H2Database;
import org.gemseeker.app.data.Vehicle;

/**
 *
 * @author Gem
 */
public class AddVehicleWindow extends AbstractWindowController {
    
    @FXML private TextField tfName;
    @FXML private TextField tfPlateNo;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    
    private final CompositeDisposable disposables;
    private final MainWindow mainWindow;
    private final VehiclesPanel vehiclesPanel;
    
    public AddVehicleWindow(MainWindow mainWindow, VehiclesPanel vehiclesPanel) {
        super("Add Vehicle", AddVehicleWindow.class.getResource("add_vehicle.fxml"), mainWindow.getStage());
        disposables = new CompositeDisposable();
        this.mainWindow = mainWindow;
        this.vehiclesPanel = vehiclesPanel;
    }

    @Override
    public void onLoad() {
        disposables.addAll(
                JavaFxObservable.actionEventsOf(btnSave).subscribe(evt -> {
                    if (!tfName.getText().isEmpty() && !tfPlateNo.getText().isEmpty()) {
                        saveAndClose();
                    } else {
                        showInfoDialog("Invalid", "Please fill up empty fields and try again.");
                    }
                }),
                JavaFxObservable.actionEventsOf(btnCancel).subscribe(evt -> {
                    close();
                })
        );
    }
    
    private void saveAndClose() {
        mainWindow.showProgress(true, "Add vehicle entry...");
        disposables.add(Single.fromCallable(() -> {
            Vehicle vehicle = new Vehicle();
            vehicle.setName(tfName.getText());
            vehicle.setPlateNo(tfPlateNo.getText());
            return H2Database.getInstance().addEntry(vehicle);
        }).subscribeOn(Schedulers.io()).observeOn(JavaFxScheduler.platform()).subscribe(success -> {
            mainWindow.showProgress(false);
            if (!success) {
                showInfoDialog("Failed", "Failed to add new vehicle entry.");
            }
            close();
            vehiclesPanel.refresh();
        }, err -> {
            mainWindow.showProgress(false);
            showErrorDialog("Database Error", "Error while adding new vehicle entry.", err);
        }));
    }

    @Override
    public void onClose() {
        tfName.clear();
        tfPlateNo.clear();
    }

    @Override
    public void onDispose() {
        disposables.dispose();
    }

}
