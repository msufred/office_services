package org.gemseeker.app.views;

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
import org.gemseeker.app.data.DataEntry;
import org.gemseeker.app.data.Employee;
import org.gemseeker.app.data.H2Database;
import org.gemseeker.app.data.Office;
import org.gemseeker.app.data.Position;
import org.gemseeker.app.views.svgicons.DeleteIcon;
import org.gemseeker.app.views.svgicons.UpIcon;

public class EditEmployeeWindow extends AbstractWindowController {

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

    private Employee employee;

    public EditEmployeeWindow(MainWindow mainWindow, EmployeesPanel employeesPanel) {
        super("Edit Employee", EditEmployeeWindow.class.getResource("add_employee.fxml"),
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
                        // set initial folder
                        String filePath = photoFile.getAbsolutePath();
                        String folder = filePath.substring(0, filePath.lastIndexOf(Utils.fileSeparator()));
                        File folderFile = new File(folder);
                        fileChooser.setInitialDirectory(folderFile);
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
                        // set initial folder
                        String filePath = signatureFile.getAbsolutePath();
                        String folder = filePath.substring(0, filePath.lastIndexOf(Utils.fileSeparator()));
                        File folderFile = new File(folder);
                        fileChooser.setInitialDirectory(folderFile);
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

    public void show(Employee employee) {
        super.show();
        this.employee = employee;
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
            ArrayList<Office> offices = (ArrayList<Office>) map.get("offices");
            cbPositions.setItems(FXCollections.observableArrayList(positions));
            cbOffices.setItems(FXCollections.observableArrayList(offices));
            
            if (employee != null) {
                tfId.setText(employee.getEmpId());
                tfFirstName.setText(employee.getFirstName());
                tfMiddleName.setText(employee.getMiddleName());
                tfLastName.setText(employee.getLastName());
                tfExtName.setText(employee.getExtName());
                taAddress.setText(employee.getAddress());
                tfPhone.setText(employee.getPhone());
                bdayDatePicker.setValue(employee.getBirthday());
                tfTin.setText(employee.getTin());
                tfGsis.setText(employee.getGsis());
                tfEmergencyPerson.setText(employee.getContactPerson());
                taEmergencyAddress.setText(employee.getEmergencyAddress());
                tfEmergencyPhone.setText(employee.getEmergencyNo());
                cbPositions.getEditor().setText(employee.getPosition());
                cbOffices.getEditor().setText(employee.getOffice());
                cbStatus.setValue(employee.getStatus());

                File image = new File(Utils.IMAGES_FOLDER + Utils.fileSeparator() + String.format("%d.jpg", employee.getId()));
                if (image.exists()) {
                    imageView.setImage(new Image(image.toURI().toString()));
                }

                File signature = new File(Utils.SIGNATURES_FOLDER + Utils.fileSeparator() + String.format("%d.png", employee.getId()));
                if (signature.exists()) signatureImageView.setImage(new Image(signature.toURI().toString()));
            }

            showProgress(false);
        }, err -> {
            showProgress(false);
            showErrorDialog("Database Error", "Error while retrieving data.", err);
        }));
    }

    private void saveAndClose() {
        showProgress(true);

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
            return H2Database.getInstance().updateEntry(employee);
        }).flatMap(success -> Single.fromCallable(() -> {
            if (success) {
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
            return success;
        })).flatMap(success -> Single.fromCallable(() -> {
            // copy employee photo & signature image if possible
            if (success) {
                if (photoFile != null) {
                    File dest = new File(Utils.IMAGES_FOLDER + Utils.fileSeparator() + String.format("%d.jpg", employee.getId()));
                    FileUtils.copyFile(photoFile, dest);
                }
                if (signatureFile != null) {
                    File dest = new File(Utils.SIGNATURES_FOLDER + Utils.fileSeparator() + String.format("%d.png", employee.getId()));
                    FileUtils.copyFile(signatureFile, dest);
                }
            }
            return success;
        })).subscribeOn(Schedulers.io()).observeOn(JavaFxScheduler.platform()).subscribe(success -> {
            showProgress(false);
            close();
            employeesPanel.refresh();
            if (!success) showInfoDialog("Updating Entry Failed", "Failed to updated Employee entry.");
        }, err -> {
            showProgress(false);
            showErrorDialog("Database Error", "Error while updating Employee entry.", err);
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
        showProgress(false);
        photoFile = null;
        signatureFile = null;
        employee = null;
    }

    @Override
    public void onDispose() {
        disposables.dispose();
    }
}
