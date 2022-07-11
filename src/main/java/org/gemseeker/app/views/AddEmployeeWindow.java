package org.gemseeker.app.views;

import org.gemseeker.app.data.Position;
import org.gemseeker.app.data.Employee;
import org.gemseeker.app.data.DataEntry;
import org.gemseeker.app.data.Office;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.gemseeker.app.Utils;
import org.gemseeker.app.data.H2Database;
import org.gemseeker.app.views.svgicons.DeleteIcon;
import org.gemseeker.app.views.svgicons.UpIcon;

public class AddEmployeeWindow extends AbstractWindowController {

    @FXML private TextField tfId;
    @FXML private ImageView imageView;
    @FXML private ImageView signatureImageView;
    @FXML private Button btnUpload; // upload photo
    @FXML private Button btnRemove; // remove photo
    @FXML private Button btnUpload1; // upload photo
    @FXML private Button btnRemove1; // remove photo
    @FXML private TextField tfFirstName;
    @FXML private TextField tfMiddleName;
    @FXML private TextField tfLastName;
    @FXML private TextField tfExtName;
    @FXML private TextArea taAddress;
    @FXML private TextField tfPhone;
    @FXML private ComboBox<Position> cbPositions;
    @FXML private ComboBox<String> cbStatus;
    @FXML private ComboBox<Office> cbOffices;
    @FXML private DatePicker bdayDatePicker;
    @FXML private DatePicker dateHired;
    @FXML private DatePicker dateTerminated;
    @FXML private DatePicker dateRetired;
    @FXML private TextField tfEmergencyPerson;
    @FXML private TextField tfEmergencyPhone;
    @FXML private TextArea taEmergencyAddress;
    @FXML private TextField tfTin;
    @FXML private TextField tfGsis;
    @FXML private ProgressBar progressBar;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    private final MainWindow mainWindow;
    private final EmployeesPanel employeesPanel;
    private final CompositeDisposable disposables;

    // File handling variables
    private FileChooser fileChooser;
    private File photoFile; // hold temporary image file
    private File signatureFile;

    public AddEmployeeWindow(MainWindow mainWindow, EmployeesPanel employeesPanel) {
        super("Add Employee", AddEmployeeWindow.class.getResource("add_employee.fxml"),
                mainWindow == null ? null : mainWindow.stage);
        this.mainWindow = mainWindow;
        this.employeesPanel = employeesPanel;
        disposables = new CompositeDisposable();
    }

    @Override
    public void initWindow(Stage stage) {
        super.initWindow(stage);
        stage.initModality(Modality.APPLICATION_MODAL);
    }

    @Override
    public void show() {
        super.show();
        refresh();
    }

    public void refresh() {
        showProgress(true);
        disposables.add(Single.fromCallable(() -> {
            HashMap<String, ArrayList<? extends DataEntry>> map = new HashMap<>();
            H2Database database = H2Database.getInstance();
            map.put("positions", database.getAllPositions());
            map.put("offices", database.getAllOffices());
            return map;
        }).subscribeOn(Schedulers.io()).observeOn(JavaFxScheduler.platform()).subscribe(map -> {
            ArrayList<Position> positions = (ArrayList<Position>) map.get("positions");
            positions.sort((p1, p2) -> {
                return p1.getName().compareTo(p2.getName());
            });
            cbPositions.setItems(FXCollections.observableArrayList(positions));
            ArrayList<Office> offices = (ArrayList<Office>) map.get("offices");
            offices.sort((o1, o2) -> {
                return o1.getName().compareTo(o2.getName());
            });
            cbOffices.setItems(FXCollections.observableArrayList(offices));
            showProgress(false);
        }, err -> {
            showProgress(false);
            showErrorDialog("Database Error", "Error while retrieving data.", err);
        }));
    }

    @Override
    public void onLoad() {
        setProgressBar(progressBar);
        
        btnUpload.setGraphic(new UpIcon(12));
        btnRemove.setGraphic(new DeleteIcon(12));
        btnUpload1.setGraphic(new UpIcon(12));
        btnRemove1.setGraphic(new DeleteIcon(12));

        Utils.setAsNumericalTextField(tfEmergencyPhone);

        cbStatus.setItems(FXCollections.observableArrayList("REG", "COS", "JO", "OTHER"));

        disposables.addAll(
                JavaFxObservable.actionEventsOf(btnUpload).subscribe(evt -> {
                    if (fileChooser == null) {
                        fileChooser = new FileChooser();
                    }
                    fileChooser.setTitle("Select Photo");
                    fileChooser.getExtensionFilters().clear();
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File", "*.jpg"));

                    photoFile = fileChooser.showOpenDialog(stage);
                    if (photoFile != null) {
                        imageView.setImage(new Image(photoFile.toURI().toString()));
                    } else {
                        imageView.setImage(null);
                    }
                }),
                JavaFxObservable.actionEventsOf(btnUpload1).subscribe(evt -> {
                    if (fileChooser == null) {
                        fileChooser = new FileChooser();
                    }
                    fileChooser.setTitle("Select Image");
                    fileChooser.getExtensionFilters().clear();
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG File", "*.png"));

                    signatureFile = fileChooser.showOpenDialog(stage);
                    if (signatureFile != null) {
                        signatureImageView.setImage(new Image(signatureFile.toURI().toString()));
                    } else {
                        signatureImageView.setImage(null);
                    }
                }),
                JavaFxObservable.actionEventsOf(btnRemove).subscribe(evt -> {
                    imageView.setImage(null);
                }),
                JavaFxObservable.actionEventsOf(btnRemove1).subscribe(evt -> {
                    signatureImageView.setImage(null);
                }),
                JavaFxObservable.actionEventsOf(btnCancel).subscribe(evt -> {
                    close();
                }),
                JavaFxObservable.actionEventsOf(btnSave).subscribe(evt -> {
                    if (!tfFirstName.getText().isEmpty() && !tfLastName.getText().isEmpty() &&
                            !cbPositions.getEditor().getText().isEmpty()) {
                        saveAndClose();
                    } else {
                        showInfoDialog("Invalid/Empty Fields", "Please fill in required fields and try " +
                                "again.");
                        tfFirstName.requestFocus();
                    }
                })
        );
    }

    private void saveAndClose() {
        showProgress(true);

        Employee employee = new Employee();
        employee.setEmpId(tfId.getText());
        employee.setFirstName(tfFirstName.getText());
        employee.setMiddleName(tfMiddleName.getText());
        employee.setLastName(tfLastName.getText());
        employee.setExtName(tfExtName.getText());
        employee.setAddress(taAddress.getText());
        employee.setPhone(tfPhone.getText());
        employee.setBirthday(bdayDatePicker.getValue());
        
        // employment information
        employee.setPosition(cbPositions.getEditor().getText());
        employee.setOffice(cbOffices.getEditor().getText());
        employee.setStatus(cbStatus.getValue());
        employee.setDateHired(dateHired.getValue());
        employee.setDateResigned(dateTerminated.getValue());
        employee.setDateRetired(dateRetired.getValue());
        
        // emergency information
        employee.setContactPerson(tfEmergencyPerson.getText());
        employee.setEmergencyAddress(taEmergencyAddress.getText());
        employee.setEmergencyNo(tfEmergencyPhone.getText());
        
        // other information
        employee.setTin(tfTin.getText());
        employee.setGsis(tfGsis.getText());

        disposables.add(Single.fromCallable(() -> {
            return H2Database.getInstance().addEntryReturnId(employee);
        }).flatMap(id -> Single.fromCallable(() -> {
            if (id != -1) {
                H2Database db = H2Database.getInstance();
                String officeName = employee.getOffice();
                if (!officeName.isEmpty() && db.getOfficeByName(officeName) == null) {
                    Office o = new Office();
                    o.setName(officeName);
                    db.addEntry(o);
                }
                
                String position = employee.getPosition();
                if (!position.isEmpty() && db.getPositionByName(position) == null) {
                    Position pos = new Position();
                    pos.setName(position);
                    db.addEntry(pos);
                }
            }
            return id;
        })).flatMap(id -> Single.fromCallable(() -> {
            // copy employee photo & signature image if possible
            if (id != -1) {
                if (photoFile != null) {
                    File dest = new File(Utils.IMAGES_FOLDER + Utils.fileSeparator() + String.format("%d.jpg", id));
                    FileUtils.copyFile(photoFile, dest);
                }
                if (signatureFile != null) {
                    File dest = new File(Utils.SIGNATURES_FOLDER + Utils.fileSeparator() + String.format("%d.png", id));
                    FileUtils.copyFile(signatureFile, dest);
                }
            }
            return id;
        })).subscribeOn(Schedulers.io()).observeOn(JavaFxScheduler.platform()).subscribe(id -> {
            showProgress(false);
            close();
            employeesPanel.refresh();
            if (id == -1) showInfoDialog("Adding Entry Failed", "Failed to add new Employee entry to" +
                    " the database.");
        }, err -> {
            showProgress(false);
            showErrorDialog("Database Error", "Error while adding new Employee entry.", err);
        }));
    }

    @Override
    public void onClose() {
        tfId.clear();
        tfFirstName.clear();
        tfMiddleName.clear();
        tfLastName.clear();
        tfExtName.clear();
        taAddress.clear();
        tfPhone.clear();
        cbPositions.setValue(null);
        cbPositions.getEditor().clear();
        cbOffices.setValue(null);
        cbOffices.getEditor().clear();
        tfEmergencyPerson.clear();
        tfEmergencyPhone.clear();
        taEmergencyAddress.clear();
        imageView.setImage(null);
        signatureImageView.setImage(null);
        bdayDatePicker.setValue(null);
        tfTin.clear();
        tfGsis.clear();
        dateHired.setValue(null);
        dateTerminated.setValue(null);
        dateRetired.setValue(null);
        showProgress(false);
        photoFile = null;
    }

    @Override
    public void onDispose() {
        disposables.dispose();
    }
}
