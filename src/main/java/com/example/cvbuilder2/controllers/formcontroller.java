package com.example.cvbuilder2.controllers;

import com.example.cvbuilder2.model.cvmodel;
import com.example.cvbuilder2.utility.scenecontroller;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class formcontroller {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;

    @FXML private TextArea educationArea;
    @FXML private TextArea skillsArea;
    @FXML private TextArea experienceArea;
    @FXML private TextArea projectsArea;

    @FXML private Button backBtn;
    @FXML private Button generateBtn;

    @FXML
    public void initialize() {

        backBtn.setOnAction(event -> scenecontroller.switchTo("home.fxml"));

        generateBtn.setOnAction(event -> {

            cvmodel cv = new cvmodel(
                    nameField.getText(),
                    emailField.getText(),
                    phoneField.getText(),
                    addressField.getText(),
                    educationArea.getText(),
                    skillsArea.getText(),
                    experienceArea.getText(),
                    projectsArea.getText()
            );

            scenecontroller.setCV(cv);
            scenecontroller.switchTo("preview.fxml");
        });
    }
}
