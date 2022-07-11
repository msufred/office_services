package org.gemseeker.app.views;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import java.util.Optional;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.gemseeker.app.data.H2Database;
import org.gemseeker.app.data.Position;
import org.gemseeker.app.views.svgicons.AddIcon7;
import org.gemseeker.app.views.svgicons.CreateIcon;
import org.gemseeker.app.views.svgicons.DeleteIcon;
import org.gemseeker.app.views.svgicons.RefreshIcon;

public class PositionsWindow extends AbstractWindowController {

    @FXML
    private Button btnAdd;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;
    @FXML private Button btnRefresh;
    
    @FXML private TableView<Position> table;
    @FXML private TableColumn<Position, String> colCode;
    @FXML private TableColumn<Position, String> colName;

    private final CompositeDisposable disposables;

//    private final AddPositionWindow addPositionWindow;
    
    private FilteredList<Position> filteredList;
    private final SimpleObjectProperty<Position> selectedItem = new SimpleObjectProperty<>();

    public PositionsWindow() {
        super("Positions", PositionsWindow.class.getResource("offices.fxml"), null);
        disposables = new CompositeDisposable();
//        addPositionWindow = new AddPositionWindow(this);
    }

    @Override
    public void onLoad() {
        btnAdd.setGraphic(new AddIcon7(12));
        btnEdit.setGraphic(new CreateIcon(12));
        btnDelete.setGraphic(new DeleteIcon(12));
        btnRefresh.setGraphic(new RefreshIcon(12));
        
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        selectedItem.bind(table.getSelectionModel().selectedItemProperty());

        disposables.addAll(
                JavaFxObservable.actionEventsOf(btnAdd).subscribe(evt -> {
//                    addPositionWindow.show();
                }),
                JavaFxObservable.actionEventsOf(btnEdit).subscribe(evt -> {

                }),
                JavaFxObservable.actionEventsOf(btnDelete).subscribe(evt -> {
                    if (selectedItem.get() != null) {
                        Optional<ButtonType> result = showConfirmDialog("Delete Position Entry",
                                "Are you sure you want to continue?", ButtonType.YES, ButtonType.CANCEL);
                        if (result.isPresent() && result.get() == ButtonType.YES) {
                            delete(selectedItem.get());
                        }
                    }
                }),
                JavaFxObservable.actionEventsOf(btnRefresh).subscribe(evt -> {
                    refresh();
                }),
                JavaFxObservable.changesOf(selectedItem).subscribe(office -> {
                    disableActions(office.getNewVal() == null);
                })
        );
    }

    @Override
    public void show() {
        super.show();
        refresh();
    }

    public void refresh() {
        disposables.add(Single.fromCallable(() -> {
            return H2Database.getInstance().getAllPositions();
        }).subscribeOn(Schedulers.io()).observeOn(JavaFxScheduler.platform()).subscribe(positions -> {
            positions.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
            filteredList = new FilteredList(FXCollections.observableArrayList(positions), p -> true);
            table.setItems(filteredList);
        }));
    }
    
    private void delete(Position position) {
        disposables.add(Single.fromCallable(() -> {
            return H2Database.getInstance().deleteEntry("positions", "id", position.getId());
        }).subscribeOn(Schedulers.io()).observeOn(JavaFxScheduler.platform()).subscribe(success -> {
            if (!success) showInfoDialog("Failed", "Failed to delete position entry.");
            refresh();
        }, err -> {
            showErrorDialog("Database Error", "Error while deleting position entry.", err);
        }));
    }

    private void disableActions(boolean disable) {
        btnEdit.setDisable(disable);
        btnDelete.setDisable(disable);
    }

    @Override
    public void onClose() {

    }

    @Override
    public void onDispose() {
        disposables.dispose();
//        addPositionWindow.onDispose();
    }
}
