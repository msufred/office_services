package org.gemseeker.app.views;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.gemseeker.app.Utils;
import org.gemseeker.app.data.Employee;
import org.gemseeker.app.data.H2Database;
import org.gemseeker.app.data.Office;
import org.gemseeker.app.views.svgicons.AddIcon7;
import org.gemseeker.app.views.svgicons.CreateIcon;
import org.gemseeker.app.views.svgicons.DeleteIcon;
import org.gemseeker.app.views.svgicons.RefreshIcon;
import org.gemseeker.app.views.svgicons.SearchIcon;
import org.gemseeker.app.views.tablecells.NameTableCell;

/**
 *
 * @author Gem
 */
public class EmployeesPanel extends AbstractPanelController {
    
    //<editor-fold desc="FXML Components" defaultstate="collapsed">
    @FXML private Button btnAdd;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;
    @FXML private Button btnRefresh;
    @FXML private Button btnImport;
    @FXML private Button btnMakeId;
    @FXML private CheckBox showDetails;
    
    @FXML private SplitPane splitPane;

    // Details
    @FXML private ScrollPane detailsGroup;
    @FXML private ImageView imageView;
    @FXML private ImageView signatureImageView;
    // -- personal information
    @FXML private Label lblName;
    @FXML private Label lblBirthday;
    @FXML private Label lblAddress;
    @FXML private Label lblPhone;
    // -- employment information
    @FXML private Label lblOffice;
    @FXML private Label lblPosition;
    @FXML private Label lblStatus;
    @FXML private Label lblHired;
    @FXML private Label lblResigned;
    @FXML private Label lblRetired;
    // -- emergency information
    @FXML private Label lblEPerson;
    @FXML private Label lblEAddress;
    @FXML private Label lblEPhone;
    // -- other
    @FXML private Label lblTin;
    @FXML private Label lblGsis;
    
    @FXML private Label lblSearch;
    @FXML private TextField tfSearch;
    
    @FXML private ComboBox<String> cbStatus;
    @FXML private ComboBox<Office> cbOffice;

    @FXML private TableView<Employee> employeesTable;
    @FXML private TableColumn<Employee, String> colStatus;
    @FXML private TableColumn<Employee, String> colName;
    @FXML private TableColumn<Employee, String> colPosition;
    @FXML private TableColumn<Employee, String> colOffice;
    //</editor-fold>

    // ContextMenu
    private MenuItem cmCreateId;
    private MenuItem cmEdit;
    private MenuItem cmDelete;
    private MenuItem cmUpdateStatus;
    private MenuItem cmUpdatePosition;

    private final CompositeDisposable disposables;

    private final AddEmployeeWindow addEmployeeWindow;
    private final EditEmployeeWindow editEmployeeWindow;
    private final ImportFromExcelWindow importFromExcelWindow;
    private final CreateIdWindow createIdWindow;

    private SplitController splitController;

    private FilteredList<Employee> filteredList;
    private final SimpleIntegerProperty selectedIndex = new SimpleIntegerProperty(-1);

    private FileChooser fileChooser;
    
    private final MainWindow mainWindow;
    
    public EmployeesPanel(MainWindow mainWindow) {
        super(EmployeesPanel.class.getResource("employees.fxml"));
        disposables = new CompositeDisposable();

        addEmployeeWindow = new AddEmployeeWindow(mainWindow, this);
        editEmployeeWindow = new EditEmployeeWindow(mainWindow, this);
        importFromExcelWindow = new ImportFromExcelWindow(mainWindow, this);
        createIdWindow = new CreateIdWindow(mainWindow);
        
        this.mainWindow = mainWindow;
    }

    @Override
    public void onLoad() {
        setupIcons();
        setupTable();
        setupListeners();
        
        cbStatus.setItems(FXCollections.observableArrayList(
                "All", "REG", "COS", "JO", "OTHER"
        ));

        splitController = new SplitController(splitPane, SplitController.Target.LAST);
        // splitController.hideTarget();
        detailsGroup.setVisible(false);

        fileChooser = new FileChooser();
    }
    
    private void setupIcons() {
        btnAdd.setGraphic(new AddIcon7(12));
        btnEdit.setGraphic(new CreateIcon(12));
        btnDelete.setGraphic(new DeleteIcon(12));
        btnRefresh.setGraphic(new RefreshIcon(12));
        lblSearch.setGraphic(new SearchIcon(12));
    }

    private void setupTable() {
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colName.setCellFactory(col -> new NameTableCell());
        colPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
        colOffice.setCellValueFactory(new PropertyValueFactory<>("office"));

        cmCreateId = new MenuItem("Create ID");
        cmEdit = new MenuItem("Edit");
        cmUpdateStatus = new MenuItem("Update Status");
        cmUpdatePosition = new MenuItem("Update Position");
        cmDelete = new MenuItem("Delete");
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(cmCreateId, cmEdit, cmUpdateStatus, cmUpdatePosition, cmDelete);
        employeesTable.setContextMenu(contextMenu);
        
        selectedIndex.bind(employeesTable.getSelectionModel().selectedIndexProperty());
    }

    private void setupListeners() {
        disposables.addAll(
                JavaFxObservable.actionEventsOf(btnAdd).subscribe(evt -> addEmployeeWindow.show()),
                JavaFxObservable.actionEventsOf(btnEdit).subscribe(evt -> editSelected()),
                JavaFxObservable.actionEventsOf(btnDelete).subscribe(evt -> deleteSelected()),
                JavaFxObservable.actionEventsOf(btnRefresh).subscribe(evt -> {
                    refresh();
                }),
                JavaFxObservable.actionEventsOf(btnImport).subscribe(evt -> {
                    importFromExcel();
                }),
                JavaFxObservable.actionEventsOf(btnMakeId).subscribe(evt -> {
                    createIdWindow.show(CreateIdWindow.ID_TYPE.ARTA_2021);
                }),
                JavaFxObservable.changesOf(showDetails.selectedProperty()).subscribe(selected -> {
                    boolean isSelected = selected.getNewVal();
                    if (isSelected) splitController.showTarget();
                    else splitController.hideTarget();
                }),
                JavaFxObservable.changesOf(tfSearch.textProperty()).subscribe(textChange -> {
                    String text = textChange.getNewVal();
                    if (text != null) {
                        if (!text.isEmpty()) filteredList.setPredicate(emp -> emp.getFirstName().toLowerCase().contains(text.toLowerCase()) ||
                                emp.getLastName().toLowerCase().contains(text.toLowerCase()));
                        else filteredList.setPredicate(emp -> true); // show all
                    }
                }),

                JavaFxObservable.changesOf(cbOffice.valueProperty()).subscribe(office -> {
                    // clear search field first
                    tfSearch.clear();
                    if (office.getNewVal() != null) {
                        String officeName = office.getNewVal().getName();
                        if (officeName.equals("All")) filteredList.setPredicate(emp -> true);
                        else filteredList.setPredicate(emp -> emp.getOffice().equalsIgnoreCase(officeName));
                    }
                }),
                JavaFxObservable.changesOf(cbStatus.valueProperty()).subscribe(status -> {
                    tfSearch.clear();
                    if (status.getNewVal() != null) {
                        if (status.getNewVal().equals("All")) filteredList.setPredicate(emp -> true);
                        else filteredList.setPredicate(emp -> emp.getStatus().equals(status.getNewVal()));
                    }
                }),
                JavaFxObservable.changesOf(selectedIndex).subscribe(index -> {
                    int indexValue = index.getNewVal().intValue();
                    disableActions(indexValue == -1);
                    if (indexValue > -1) showDetails(filteredList.getSource().get(indexValue));
                    else resetDetails();
                }),
                // Table Context Menus
                JavaFxObservable.actionEventsOf(cmCreateId).subscribe(evt -> {
                    Employee employee = employeesTable.getSelectionModel().getSelectedItem();
                    if (employee != null) {
                        createIdWindow.show(CreateIdWindow.ID_TYPE.ARTA_2021, employee);
                    }
                }),
                JavaFxObservable.actionEventsOf(cmEdit).subscribe(evt -> editSelected()),
                JavaFxObservable.actionEventsOf(cmUpdateStatus).subscribe(evt -> {
                    // TODO update status
                }),
                JavaFxObservable.actionEventsOf(cmUpdatePosition).subscribe(evt -> {
                    // TODO update position
                }),
                JavaFxObservable.actionEventsOf(cmDelete).subscribe(evt -> deleteSelected())
        );
    }
    
    public void refresh() {
        mainWindow.showProgress(true, "Fetching employees data...");
        disposables.add(Single.fromCallable(() -> {
            return H2Database.getInstance().getAllEmployees();
        }).flatMap(emps -> Single.fromCallable(() -> {
            ArrayList<Office> offices = H2Database.getInstance().getAllOffices();
            Office all = new Office();
            all.setName("All");
            offices.add(0, all);
            Platform.runLater(() -> cbOffice.setItems(FXCollections.observableArrayList(offices)));
            return emps;
        })).subscribeOn(Schedulers.io()).observeOn(JavaFxScheduler.platform()).subscribe(emps -> {
            resetDetails();
            filteredList = new FilteredList<>(FXCollections.observableArrayList(emps), p -> true);
            employeesTable.setItems(filteredList);
            cbStatus.getSelectionModel().select(0);
            cbOffice.getSelectionModel().select(0);
            mainWindow.showProgress(false);
        }, err -> {
            mainWindow.showProgress(false);
            showErrorDialog("Database Error", "Error while retrieving employees data.", err);
        }));
    }

    private void showDetails(Employee employee) {
        detailsGroup.setVisible(true);
        // show image
        File imageFile = new File(Utils.IMAGES_FOLDER + Utils.fileSeparator() + String.format("%d.jpg", employee.getId()));
        if (imageFile.exists()) imageView.setImage(new Image(imageFile.toURI().toString()));
        else imageView.setImage(null);
        // show signature
        File signatureFile = new File(Utils.SIGNATURES_FOLDER + Utils.fileSeparator() + String.format("%d.png", employee.getId()));
        if (signatureFile.exists()) signatureImageView.setImage(new Image(signatureFile.toURI().toString()));
        else signatureImageView.setImage(null);
        
        lblName.setText(employee.toString());
        LocalDate birthday = employee.getBirthday();
        if (birthday != null) lblBirthday.setText(Utils.DATE_FORMAT.format(birthday));
        lblAddress.setText(employee.getAddress());
        lblPhone.setText(employee.getPhone());
        
        lblOffice.setText(employee.getOffice());
        lblPosition.setText(employee.getPosition());
        lblStatus.setText(employee.getStatus());
        LocalDate dateHired = employee.getDateHired();
        if (dateHired != null) lblHired.setText(Utils.DATE_FORMAT.format(dateHired));
        LocalDate dateResigned = employee.getDateResigned();
        if(dateResigned != null) lblResigned.setText(Utils.DATE_FORMAT.format(dateResigned));
        LocalDate dateRetired = employee.getDateRetired();
        if (dateRetired != null) lblRetired.setText(Utils.DATE_FORMAT.format(dateRetired));
        
        lblEPerson.setText(employee.getContactPerson());
        lblEAddress.setText(employee.getEmergencyAddress());
        lblEPhone.setText(employee.getEmergencyAddress());
        
        lblTin.setText(employee.getTin());
        lblGsis.setText(employee.getGsis());
    }

    private void resetDetails() {
        detailsGroup.setVisible(false);
        imageView.setImage(null);
        signatureImageView.setImage(null);
        lblName.setText("");
        lblBirthday.setText("");
        lblAddress.setText("");
        lblPhone.setText("");
        lblOffice.setText("");
        lblPosition.setText("");
        lblStatus.setText("");
        lblHired.setText("");
        lblResigned.setText("");
        lblRetired.setText("");
        lblTin.setText("");
        lblGsis.setText("");
        lblEPerson.setText("");
        lblEAddress.setText("");
        lblEPerson.setText("");
    }

    private void importFromExcel() {
        fileChooser.setTitle("Select Excel File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel File", "*.xls", "*.xlsx"));
        File excelFile = fileChooser.showOpenDialog(mainWindow.getStage());
        if (excelFile != null) {
            // create a copy
            File temp = new File(Utils.APP_FOLDER + Utils.fileSeparator() + "temp.xlsx");
            try {
                FileUtils.copyFile(excelFile, temp);
                importFromExcelWindow.show(temp);
            } catch (IOException e) {
                showErrorDialog("IOException", "Failed to copy Excel file to a temporary location.", e);
            }
        }
    }

    private void editSelected() {
        Employee employee = employeesTable.getSelectionModel().getSelectedItem();
        if (employee != null) editEmployeeWindow.show(employee);
    }

    private void deleteSelected() {
        Employee selected = employeesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Optional<ButtonType> result = showConfirmDialog("Delete Employee?",
                    "This will delete this Employee entry permanently. Data related to this " +
                            "Employee will also be deleted. Continue?");
            if (result.isPresent() && result.get() == ButtonType.OK) {
                deleteEmployee(selected);
            }
        }
    }

    private void deleteEmployee(Employee employee) {
        mainWindow.showProgress(true, "Deleting Employee entry...");
        disposables.add(Single.fromCallable(() -> {
            return H2Database.getInstance().deleteEntry("employees", "id", employee.getId());
        }).subscribeOn(Schedulers.io()).observeOn(JavaFxScheduler.platform()).subscribe(success -> {
            mainWindow.showProgress(false);
            if (!success) showInfoDialog("Cannot Delete Entry", "Failed to delete Employee entry.");
            refresh();
        }, err -> {
            mainWindow.showProgress(false);
            showErrorDialog("Database Error", "Error occurred while deleting Employee entry.", err);
        }));
    }

    private void disableActions(boolean disable) {
        btnEdit.setDisable(disable);
        btnDelete.setDisable(disable);
        cmCreateId.setDisable(disable);
        cmEdit.setDisable(disable);
        cmDelete.setDisable(disable);
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onResume() {
        refresh();
    }

    @Override
    public void onDispose() {
        disposables.dispose();
        addEmployeeWindow.onDispose();
        editEmployeeWindow.onDispose();
        importFromExcelWindow.onDispose();
        createIdWindow.onDispose();
    }

}
