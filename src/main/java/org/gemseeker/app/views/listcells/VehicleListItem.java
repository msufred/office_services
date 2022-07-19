package org.gemseeker.app.views.listcells;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;
import org.gemseeker.app.data.Vehicle;

/**
 *
 * @author Gem
 */
public class VehicleListItem extends ListCell<Vehicle> {

    @FXML private Label lblName;
    @FXML private Label lblPlateNo;
    @FXML private Label lblStatus;
    private Pane root;
    
    public VehicleListItem() {
        initFxml();
    }
    
    private void initFxml() {
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(VehicleListItem.class.getResource("vehicle_list_item.fxml"));
        loader.setController(this);
        try {
            root = loader.load();
        } catch (IOException e) {
            System.err.println("Failed to load fmxl file: " + e);
        }
    }
    
    @Override
    protected void updateItem(Vehicle item, boolean empty) {
        super.updateItem(item, empty);
        setText("");
        if (item != null && !empty) {
            if (root != null) {
                lblName.setText(item.getName());
                lblPlateNo.setText(item.getPlateNo());
                lblStatus.setText(item.getStatus());
                setGraphic(root);
            } else {
                setGraphic(null);
            }
        }
    }

}
