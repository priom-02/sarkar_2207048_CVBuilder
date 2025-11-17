package com.example.cvbuilder2.controllers;

import com.example.cvbuilder2.model.cvmodel;
import com.example.cvbuilder2.utility.scenecontroller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class previewcontroller {

    @FXML private Button backBtn;

    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label addressLabel;

    @FXML private Label educationLabel;
    @FXML private Label skillsLabel;
    @FXML private Label experienceLabel;
    @FXML private Label projectsLabel;

    @FXML
    public void initialize() {

        cvmodel cv = scenecontroller.getCV();

        if (cv != null) {
            nameLabel.setText(cv.name);
            emailLabel.setText("Email: " + cv.email);
            phoneLabel.setText("Phone: " + cv.phone);
            addressLabel.setText("Address: " + cv.address);

            educationLabel.setText(cv.education);
            skillsLabel.setText(cv.skills);
            experienceLabel.setText(cv.experience);
            projectsLabel.setText(cv.projects);
        }

        backBtn.setOnAction(event -> scenecontroller.switchTo("form.fxml"));
    }
}
