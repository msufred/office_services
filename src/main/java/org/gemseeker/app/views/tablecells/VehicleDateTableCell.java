package org.gemseeker.app.views.tablecells;

import java.time.LocalDate;
import javafx.scene.control.TableCell;
import org.gemseeker.app.Utils;
import org.gemseeker.app.data.Vehicle;
import org.gemseeker.app.data.VehicleTask;

/**
 *
 * @author Gem
 */
public class VehicleDateTableCell extends TableCell<VehicleTask, LocalDate> {

    @Override
    protected void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) {
            VehicleTask task = (VehicleTask) getTableRow().getItem();
            if (task == null) setText("");
            else {
                if (task.getStatus().equals(Vehicle.STATUS_AVAILABLE)) setText("");
                else setText(Utils.DATE_FORMAT2.format(item));
            }
        }
    }

}
