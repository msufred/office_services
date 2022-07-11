package org.gemseeker.app.views;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.print.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import org.gemseeker.app.data.Employee;
import org.gemseeker.app.data.H2Database;
import org.gemseeker.app.views.ids.Arta2021;
import org.gemseeker.app.views.ids.EmployeeBack2021;
import org.gemseeker.app.views.ids.EmployeeFront2021;
import org.gemseeker.app.views.ids.IDFormController;
import org.gemseeker.app.views.svgicons.PrintIcon;
import org.gemseeker.app.views.svgicons.UpIcon;

public class CreateIdWindow extends AbstractWindowController {

    @FXML private ComboBox<ID_TYPE> cbTypes;
    @FXML private ComboBox<Employee> cbEmployees;
    @FXML private Button btnExport;
    @FXML private Button btnPrint;
    @FXML private StackPane stackPane;
    @FXML private Slider zoomSlider;
    @FXML private Button btnZoomOut;
    @FXML private Button btnZoomIn;

    public enum ID_TYPE {
        ARTA_2021("Arta 2021"),
        EMPLOYEE_2021_FRONT("Employee ID 2021 (Front)"),
        EMPLOYEE_2021_BACK("Employee ID 2021 (Back)");

        final String value;
        ID_TYPE(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private final MainWindow mainWindow;
    private final CompositeDisposable disposables;

    private final FileChooser fileChooser;
    private final Scale scale;

    private ID_TYPE mCurrentIdType;
    private IDFormController mCurrentFormController;

    // ID Form Controllers
    private Arta2021 arta2021;
    private EmployeeFront2021 employeeFront2021;
    private EmployeeBack2021 employeeBack2021;

    public CreateIdWindow(MainWindow mainWindow) {
        super("Create ID", CreateIdWindow.class.getResource("create_id.fxml"),
                mainWindow == null ? null : mainWindow.stage);
        this.mainWindow = mainWindow;
        disposables = new CompositeDisposable();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Export Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File", "*.png"));
        scale = new Scale();
    }

    @Override
    public void onLoad() {
        cbTypes.setItems(FXCollections.observableArrayList(ID_TYPE.values()));

        scale.xProperty().bind(zoomSlider.valueProperty());
        scale.yProperty().bind(zoomSlider.valueProperty());
        scale.setPivotX(0);
        scale.setPivotY(0);
        stackPane.getTransforms().add(scale);

        btnPrint.setGraphic(new PrintIcon(12));
        btnExport.setGraphic(new UpIcon(12));

        disposables.addAll(
                JavaFxObservable.changesOf(cbTypes.valueProperty()).subscribe(type -> {
                    ID_TYPE value = type.getNewVal();
                    if (value != null) {
                        switch (value) {
                            case ARTA_2021:
                                loadIdForm(ID_TYPE.ARTA_2021);
                                btnPrint.setDisable(false);
                                break;
                            case EMPLOYEE_2021_FRONT:
                                loadIdForm(ID_TYPE.EMPLOYEE_2021_FRONT);
                                btnPrint.setDisable(true);
                                break;
                            case EMPLOYEE_2021_BACK:
                                loadIdForm(ID_TYPE.EMPLOYEE_2021_BACK);
                                btnPrint.setDisable(true);
                                break;
                        }
                    }
                }),
                JavaFxObservable.changesOf(cbEmployees.valueProperty()).subscribe(employeeChange -> {
                    Employee employee = employeeChange.getNewVal();
                    if (employee != null) {
                        if (mCurrentIdType == ID_TYPE.ARTA_2021) {
                            arta2021.fillIn(employee);
                        }
                    }
                }),
                JavaFxObservable.actionEventsOf(btnZoomOut).subscribe(evt -> {
                    double newValue = zoomSlider.getValue() - 0.1;
                    if (newValue < 0.0) newValue = 0;
                    zoomSlider.adjustValue(newValue);
                }),
                JavaFxObservable.actionEventsOf(btnZoomIn).subscribe(evt -> {
                    double newValue = zoomSlider.getValue() + 0.1;
                    if (newValue > 1.0) newValue = 1;
                    zoomSlider.adjustValue(newValue);
                }),
                JavaFxObservable.actionEventsOf(btnExport).subscribe(evt -> {
                    if (cbEmployees.getValue() != null) {
                        exportImage();
                    }
                }),
                JavaFxObservable.actionEventsOf(btnPrint).subscribe(evt -> {
                    doPrint();
                })
        );
    }

    public void show(ID_TYPE type) {
        super.show();
        prepare(type, null);
    }

    public void show(ID_TYPE type, Employee employee) {
        super.show();
        prepare(type, employee);
    }

    private void prepare(ID_TYPE type, Employee employee) {
        disposables.add(Single.fromCallable(() -> {
            return H2Database.getInstance().getAllEmployees();
        }).subscribeOn(Schedulers.io()).observeOn(JavaFxScheduler.platform()).subscribe(employees -> {
            cbEmployees.setItems(FXCollections.observableArrayList(employees));
            if (employee != null) {
                cbEmployees.setValue(employee);
            }

            cbTypes.setValue(type);
//            loadIdForm(type);
        }, err -> {
            showErrorDialog("Database Error", "Error occurred while retrieving data.", err);
        }));
    }

    private void loadIdForm(ID_TYPE type) {
        if (mCurrentFormController != null) mCurrentFormController.clearFields();

        Pane pane = null;
        switch (type) {
            case ARTA_2021:
                if (arta2021 == null) arta2021 = new Arta2021();
                pane = arta2021.getContent();
                mCurrentFormController = arta2021;
                break;
            case EMPLOYEE_2021_FRONT:
                if (employeeFront2021 == null) employeeFront2021 = new EmployeeFront2021();
                pane = employeeFront2021.getContent();
                mCurrentFormController = employeeFront2021;
                break;
            case EMPLOYEE_2021_BACK:
                if (employeeBack2021 == null) employeeBack2021 = new EmployeeBack2021();
                pane = employeeBack2021.getContent();
                mCurrentFormController = employeeBack2021;
                break;
            default:
                break;
        };

        if (pane != null) {
            stackPane.getChildren().clear();
            stackPane.getChildren().add(pane);
        }
        mCurrentIdType = type;

        Employee employee = cbEmployees.getValue();
        if (employee != null && mCurrentFormController != null) {
            mCurrentFormController.fillIn(employee);
        }
    }

    private void exportImage() {
        File savedFile = fileChooser.showSaveDialog(stage);
        if (savedFile != null) {
            try {
                Pane pane = (Pane) stackPane.getChildren().get(0);
                WritableImage writableImage = new WritableImage((int) pane.getWidth(), (int) pane.getHeight());
                pane.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", savedFile);

                // open saved file directory
                Runtime.getRuntime().exec("explorer /select, " + savedFile.toURI().toString());
            } catch (IOException e) {
                showErrorDialog("Write Image Error", "Failed to export image.", e);
            }
        }
    }

    private void doPrint() {
        if (mCurrentFormController == null) {
            showInfoDialog("Cannot Print", "Failed to print. Please try again.");
            return;
        }

        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null && printerJob.showPrintDialog(stage)) {
            Printer printer = printerJob.getPrinter();
            PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);

            Pane pane = mCurrentFormController.getContent();
            double scaleX = pageLayout.getPrintableWidth() / pane.getWidth();
            Scale scale = new Scale(scaleX, scaleX);
            pane.getTransforms().add(scale);
            boolean success = printerJob.printPage(pageLayout, pane);
            if (success) printerJob.endJob();

            pane.getTransforms().remove(scale);
        }
    }

    @Override
    public void onClose() {
        if (arta2021 != null) arta2021.clearFields();
        if (employeeFront2021 != null) employeeFront2021.clearFields();
        mCurrentIdType = null;
    }

    @Override
    public void onDispose() {
        disposables.dispose();
    }
}
