package org.gemseeker.app.views;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import org.gemseeker.app.data.Employee;
import org.gemseeker.app.data.H2Database;
import org.gemseeker.app.data.Office;
import org.gemseeker.app.data.Position;

public class ImportFromExcelWindow extends AbstractWindowController {

    @FXML private TableView<Employee> employeesTable;
    @FXML private TableColumn<Employee, String> colId;
    @FXML private TableColumn<Employee, String> colStatus;
    @FXML private TableColumn<Employee, String> colFirstName;
    @FXML private TableColumn<Employee, String> colMiddleName;
    @FXML private TableColumn<Employee, String> colLastName;
    @FXML private TableColumn<Employee, String> colExtName;
    @FXML private TableColumn<Employee, String> colAddress;
    @FXML private TableColumn<Employee, LocalDate> colBirthday;
    @FXML private TableColumn<Employee, String> colDesignation;
    @FXML private TableColumn<Employee, String> colOffice;
    @FXML private TableColumn<Employee, String> colTin;
    @FXML private TableColumn<Employee, String> colGsis;
    @FXML private Button btnImport;
    @FXML private Button btnDiscard;
    @FXML private ProgressBar progressBar;
    @FXML private Label progressLabel;

    private final MainWindow mainWindow;
    private final EmployeesPanel employeesPanel;
    private final CompositeDisposable disposables;

    private final int MAX_COL = 13;

    public ImportFromExcelWindow(MainWindow mainWindow, EmployeesPanel employeesPanel) {
        super("Import From Excel", ImportFromExcelWindow.class.getResource("import_data.fxml"),
                mainWindow == null ? null : mainWindow.stage);
        this.mainWindow = mainWindow;
        this.employeesPanel = employeesPanel;
        disposables = new CompositeDisposable();
    }

    @Override
    public void onLoad() {
        setProgressBar(progressBar);
        setProgressLabel(progressLabel);
        
        colId.setCellValueFactory(new PropertyValueFactory<>("idNo"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colMiddleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colExtName.setCellValueFactory(new PropertyValueFactory<>("extName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colBirthday.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        colDesignation.setCellValueFactory(new PropertyValueFactory<>("position"));
        colOffice.setCellValueFactory(new PropertyValueFactory<>("office"));
        colTin.setCellValueFactory(new PropertyValueFactory<>("tin"));
        colGsis.setCellValueFactory(new PropertyValueFactory<>("gsis"));

        disposables.addAll(
                JavaFxObservable.actionEventsOf(btnImport).subscribe(evt -> {
                    if (employeesTable.getItems().isEmpty()) {
                        showInfoDialog("No Data to Import", "Excel file empty or cannot be read. Try another file.");
                        close();
                    } else {
                        importEmployees();
                    }
                }),
                JavaFxObservable.actionEventsOf(btnDiscard).subscribe(evt -> close())
        );
    }

    public void show(File file) {
        super.show();
        if (file.exists()) {
            disableAll(true);
            showProgress(true);
            disposables.add(Single.fromCallable(() -> {
                ArrayList<Employee> employees = new ArrayList<>();
                try ( // read excel file
                    FileInputStream fileInputStream = new FileInputStream(file); 
                    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream)
                ) {
                    XSSFSheet sheetA = workbook.getSheetAt(0);
                    
                    Iterator<Row> rowIterator = sheetA.rowIterator();
                    int rowCount = 0;
                    while(rowIterator.hasNext()) {
                        // skip first row (title row)
                        if (rowCount == 0) {
                            rowIterator.next();
                        } else {
                            Row row = rowIterator.next();
                            Employee employee = new Employee();
                            H2Database database = H2Database.getInstance();
                            
                            for (int cellIndex = 0; cellIndex < MAX_COL; cellIndex++) {
                                Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                                if (cell != null) {
                                    switch (cellIndex) {
                                        case 0: // ID
                                            employee.setEmpId(cell.getStringCellValue());
                                            break;
                                        case 1: // Hiring Status
                                            employee.setStatus(cell.getStringCellValue());
                                            break;
                                        case 2: // First Name
                                            employee.setFirstName(cell.getStringCellValue());
                                            break;
                                        case 3: // Middle Name
                                            employee.setMiddleName(cell.getStringCellValue());
                                            break;
                                        case 4: // Last Name
                                            employee.setLastName(cell.getStringCellValue());
                                            break;
                                        case 5: // Ext
                                            employee.setExtName(cell.getStringCellValue());
                                            break;
                                        case 6: // Position
                                            String designation = cell.getStringCellValue();
                                            employee.setPosition(designation);
                                            break;
                                        case 7: // Office
                                            String officeName = cell.getStringCellValue();
                                            employee.setOffice(officeName);
                                            break;
                                        case 8: // Address
                                            employee.setAddress(cell.getStringCellValue());
                                            break;
                                        case 9: // Birthday
                                            Date date = cell.getDateCellValue();
                                            if (date != null) {
                                                employee.setBirthday(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                                            }
                                            break;
                                        case 10: // TIN
                                            employee.setTin(cell.getStringCellValue());
                                            break;
                                        case 11: // GSIS
                                            employee.setGsis(cell.getStringCellValue());
                                            break;
                                        default:
                                            break;
                                    } // end: switch
                                }
                            }
                            
                            employees.add(employee);
                        }
                        rowCount++;
                    }
                }
                return employees;
            }).subscribeOn(Schedulers.io()).observeOn(JavaFxScheduler.platform()).subscribe(employees -> {
                employeesTable.setItems(FXCollections.observableArrayList(employees));
                disableAll(false);
                showProgress(false);
            }, err -> {
                disableAll(false);
                showProgress(false);
                showErrorDialog("Error", "Error while reading excel file.", err);
            }));
        }
    }

    private void importEmployees() {
        showProgress(true);
        disposables.add(Completable
                .fromAction(() -> {
                    H2Database database = H2Database.getInstance();
                    for (Employee employee : employeesTable.getItems()) {
                        boolean success = database.addEntry(employee);
                        if (success) {
                            String officeName = employee.getOffice();
                            if (!officeName.isEmpty() && database.getOfficeByName(officeName) == null) {
                                Office o = new Office();
                                o.setName(officeName);
                                database.addEntry(o);
                            }

                            String position = employee.getPosition();
                            if (!position.isEmpty() && database.getPositionByName(position) == null) {
                                Position pos = new Position();
                                pos.setName(position);
                                database.addEntry(pos);
                            }
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(() -> {
                    showProgress(false);
                    close();
                    employeesPanel.refresh();
                }, err -> {
                    showProgress(false);
                    showErrorDialog("Database Error", "Failed to import data from Excel file.", err);
                }));
    }

    private void disableAll(boolean disable) {
        employeesTable.setDisable(disable);
        btnImport.setDisable(disable);
        btnDiscard.setDisable(disable);
    }
    
    @Override
    public void onClose() {
        employeesTable.setItems(null);
        disableAll(false);
        showProgress(false);
    }

    @Override
    public void onDispose() {
        disposables.dispose();
    }
}
