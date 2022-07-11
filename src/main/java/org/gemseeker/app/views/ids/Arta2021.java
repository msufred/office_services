package org.gemseeker.app.views.ids;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import org.gemseeker.app.Utils;
import org.gemseeker.app.data.Employee;

public class Arta2021 extends IDFormController {

    @FXML private ImageView imageView;
    @FXML private Label lblFirstName;
    @FXML private Label lblLastName;
    @FXML private Label lblPosition;

    // copy
    @FXML private ImageView imageView1;
    @FXML private Label lblFirstName1;
    @FXML private Label lblLastName1;
    @FXML private Label lblPosition1;

    public Arta2021() {
        super(Arta2021.class.getResource("arta_2021.fxml"));
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void fillIn(Employee employee) {
        clearFields();
        if (employee != null) {
            if (employee.getMiddleName().isEmpty()) {
                lblFirstName.setText(employee.getFirstName().toUpperCase());
                lblFirstName1.setText(employee.getFirstName().toUpperCase());
            } else {
                lblFirstName.setText(String.format("%s %s.", employee.getFirstName(),
                        employee.getMiddleName().charAt(0)).toUpperCase());
                lblFirstName1.setText(String.format("%s %s.", employee.getFirstName(),
                        employee.getMiddleName().charAt(0)).toUpperCase());
            }
            lblLastName.setText(employee.getLastName().toUpperCase());
            lblPosition.setText(employee.getPosition());
            lblLastName1.setText(employee.getLastName().toUpperCase());
            lblPosition1.setText(employee.getPosition());

            File imageFile = new File(Utils.IMAGES_FOLDER + Utils.fileSeparator() +
                    String.format("%d.jpg", employee.getId()));
            if (imageFile.exists()) {
                imageView.setImage(new Image(imageFile.toURI().toString()));
                imageView1.setImage(new Image(imageFile.toURI().toString()));
            }
        }
    }

    @Override
    public void clearFields() {
        lblFirstName.setText("");
        lblLastName.setText("");
        lblPosition.setText("");
        imageView.setImage(null);

        lblFirstName1.setText("");
        lblLastName1.setText("");
        lblPosition1.setText("");
        imageView1.setImage(null);
    }

    @Override
    public void onDispose() {

    }
}
