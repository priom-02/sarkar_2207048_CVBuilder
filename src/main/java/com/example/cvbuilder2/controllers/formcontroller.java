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
    @FXML private Button saveBtn;   // (Since you added Save button)

    @FXML
    public void initialize() {

        backBtn.setOnAction(e -> scenecontroller.switchTo("home.fxml"));

        // SAVE BUTTON → only saves, does not switch page
        if (saveBtn != null) {
            saveBtn.setOnAction(e -> {
                if (isValid()) {
                    showInfo("Data saved successfully!");
                }
            });
        }

        // GENERATE CV BUTTON
        generateBtn.setOnAction(e -> {
            if (isValid()) {

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
                showInfo("CV data saved successfully!");
                scenecontroller.switchTo("preview.fxml");
            }
        });
    }

    // ============================
    // VALIDATION METHOD
    // ============================

    private boolean isValid() {

        if (isEmpty(nameField, "Full Name")) return false;
        if (isEmpty(emailField, "Email")) return false;
        if (isEmpty(phoneField, "Phone")) return false;
        if (isEmpty(addressField, "Address")) return false;
        if (isEmpty(educationArea, "Education")) return false;
        if (isEmpty(skillsArea, "Skills")) return false;
        if (isEmpty(experienceArea, "Experience")) return false;
        if (isEmpty(projectsArea, "Projects")) return false;

        return true; // All fields filled
    }

    private boolean isEmpty(TextInputControl field, String fieldName) {
        if (field.getText().trim().isEmpty()) {
            showError(fieldName + " is required.");
            return true;
        }
        return false;
    }

    // ============================
    // ALERT HELPERS
    // ============================

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Missing Field");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
