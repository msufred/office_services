package org.gemseeker.app.views;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import org.gemseeker.app.data.H2Database;
import org.gemseeker.app.data.Office;

public class EditOfficeWindow extends AbstractWindowController {

    @FXML private TextField tfName;
    @FXML private TextField tfCode;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    private final OfficesWindow officesWindow;
    private final CompositeDisposable disposables;

    private Office office;

    public EditOfficeWindow(OfficesWindow officesWindow) {
        super("Edit Office", EditOfficeWindow.class.getResource("add_office.fxml"),
                officesWindow.stage);
        this.officesWindow = officesWindow;
        disposables = new CompositeDisposable();
    }

    @Override
    public void onLoad() {
        // automatically change input text to uppercase
        tfCode.setTextFormatter(new TextFormatter<>(change -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));

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

    public void show(Office office) {
        show();
        if (office != null) {
            tfName.setText(office.getName());
            tfCode.setText(office.getCode());
        }
        this.office = office;
    }

    private void saveAndClose() {
        office.setName(tfName.getText());
        office.setCode(tfCode.getText());
        disposables.add(Single.fromCallable(() -> {
            return H2Database.getInstance().updateEntry(office);
        }).subscribeOn(Schedulers.io()).observeOn(JavaFxScheduler.platform()).subscribe(success -> {
            if (!success) showInfoDialog("Failed", "Failed to update Office entry.");
            close();
            officesWindow.refresh();
        }, err -> {
            showErrorDialog("Database Error", "Error while updating Office entry.", err);
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
