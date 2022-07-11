package org.gemseeker.app.views.ids;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.format.DateTimeFormatter;
import org.gemseeker.app.data.Employee;

public class EmployeeBack2021 extends IDFormController {

    @FXML private Label lblAddress;
    @FXML private Label lblBirthday;
    @FXML private Label lblTin;
    @FXML private Label lblGsis;
    @FXML private Label lblEPerson;
    @FXML private Label lblEPhone;

    public EmployeeBack2021() {
        super(EmployeeBack2021.class.getResource("employee_id_back_2021.fxml"));
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void fillIn(Employee employee) {
        lblAddress.setText(employee.getAddress());
        if (employee.getBirthday() != null) {
            lblBirthday.setText(employee.getBirthday().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
        }
        lblTin.setText(employee.getTin());
        lblGsis.setText(employee.getGsis());
        lblEPerson.setText(employee.getContactPerson());
        lblEPhone.setText(employee.getEmergencyNo());
    }

    @Override
    public void clearFields() {
        lblAddress.setText("");
        lblBirthday.setText("");
        lblTin.setText("");
        lblGsis.setText("");
        lblEPerson.setText("");
        lblEPhone.setText("");
    }

    @Override
    public void onDispose() {

    }
}
