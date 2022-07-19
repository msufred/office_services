package org.gemseeker.app.views.tablecells;

import javafx.scene.control.TableCell;
import org.gemseeker.app.data.VehicleTask;

/**
 *
 * @author Gem
 */
public class VehicleStatusTableCell extends TableCell<VehicleTask, String> {

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) setText(item);
        else setText("");
    }

}
