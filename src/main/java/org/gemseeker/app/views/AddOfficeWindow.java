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
import org.gemseeker.app.data.Office;

/**
 *
 * @author Gem
 */
public class AddOfficeWindow extends AbstractWindowController {
    
    @FXML private TextField tfName;
    @FXML private TextField tfCode;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    
    private final CompositeDisposable disposables;
    private final OfficesWindow officesWindow;
    
    public AddOfficeWindow(OfficesWindow officesWindow) {
        super("Add Office", AddOfficeWindow.class.getResource("add_office.fxml"),
                officesWindow.stage);
        this.officesWindow = officesWindow;
        disposables = new CompositeDisposable();
    }

    @Override
    public void onLoad() {
        disposables.addAll(
                JavaFxObservable.actionEventsOf(btnCancel).subscribe(evt -> {
                    close();
                }),
                JavaFxObservable.actionEventsOf(btnSave).subscribe(evt -> {
                    if (!tfName.getText().isEmpty() && !tfCode.getText().isEmpty()) {
                        saveAndClose();
                    } else {
                        showInfoDialog("Invalid/Empty Fields", "Please fill in all fields and try again.");
                        tfName.requestFocus();
                    }
                })
        );
    }
    
    private void saveAndClose() {
        disposables.add(Single.fromCallable(() -> {
            Office office = new Office();
            office.setName(tfName.getText());
            office.setCode(tfCode.getText());
            return H2Database.getInstance().addEntry(office);
        }).subscribeOn(Schedulers.io()).observeOn(JavaFxScheduler.platform()).subscribe(success -> {
            if (!success) showInfoDialog("Failed", "Failed to save new office entry.");
            close();
            officesWindow.refresh();
        }, err -> {
            showErrorDialog("Database Error", "Error while saving office entry.", err);
        }));
    }

    @Override
    public void onClose() {
        tfName.clear();
        tfCode.clear();
    }

    @Override
    public void onDispose() {
        disposables.dispose();
    }

}
