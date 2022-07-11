package org.gemseeker.app.views.tablecells;

import javafx.scene.control.TableCell;
import org.gemseeker.app.data.Employee;

public class NameTableCell extends TableCell<Employee, String> {
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            Employee employee = (Employee) getTableRow().getItem();
            if (employee != null) setText(employee.toString());
            else setText("");
        } else {
            setText("");
        }
    }
}
