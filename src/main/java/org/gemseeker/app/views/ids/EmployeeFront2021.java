package org.gemseeker.app.views.ids;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import org.gemseeker.app.Utils;
import org.gemseeker.app.data.Employee;

public class EmployeeFront2021 extends IDFormController {

    @FXML private ImageView photoImageView;
    @FXML private ImageView signatureImageView;
    @FXML private Label lblIdNo;
    @FXML private Label lblFirstName;
    @FXML private Label lblLastName;
    @FXML private Label lblPosition;

    public EmployeeFront2021() {
        super(EmployeeFront2021.class.getResource("employee_id_front_2021.fxml"));
    }


    @Override
    public void onLoad() {

    }

    @Override
    public void fillIn(Employee employee) {
        if (employee != null) {
            lblIdNo.setText(String.format("ID No: %s", employee.getEmpId()));
            String firstName = employee.getFirstName();
            if (!employee.getMiddleName().isEmpty()) firstName += String.format("%s.", employee.getMiddleName().charAt(0));
            lblFirstName.setText(firstName);
            lblLastName.setText(employee.getLastName());
            lblPosition.setText(employee.getPosition());

            File photoFile = new File(Utils.IMAGES_FOLDER + Utils.fileSeparator() + String.format("%d.jpg", employee.getId()));
            if (photoFile.exists()) photoImageView.setImage(new Image(photoFile.toURI().toString()));
            File sigFile = new File(Utils.SIGNATURES_FOLDER + Utils.fileSeparator() + String.format("%d.png", employee.getId()));
            if (sigFile.exists()) signatureImageView.setImage(new Image(sigFile.toURI().toString()));
        }
    }

    @Override
    public void clearFields() {
        lblIdNo.setText("");
        lblFirstName.setText("");
        lblLastName.setText("");
        lblPosition.setText("");
        photoImageView.setImage(null);
        signatureImageView.setImage(null);
    }

    @Override
    public void onDispose() {

    }
}
