package com.example.cvbuilder2.controllers;

import com.example.cvbuilder2.database.Database;
import com.example.cvbuilder2.model.cvmodel;
import com.example.cvbuilder2.utility.scenecontroller;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.concurrent.CompletableFuture;

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

    // when editing an existing record, this will be non-null
    private cvmodel editingModel;

    @FXML
    public void initialize() {
        backBtn.setOnAction(e -> scenecontroller.switchTo("home.fxml"));

        // If this controller is used for editing, scenecontroller.getCV() can pass a pre-filled model
        cvmodel maybe = scenecontroller.getCV();
        if (maybe != null && maybe.getId() != 0) {
            this.editingModel = maybe;
            populateFields(maybe);
        }

        generateBtn.setOnAction(e -> {

            if (!isValid()) return;

            cvmodel cv = new cvmodel(
                    nameField.getText().trim(),
                    emailField.getText().trim(),
                    phoneField.getText().trim(),
                    addressField.getText().trim(),
                    educationArea.getText().trim(),
                    skillsArea.getText().trim(),
                    experienceArea.getText().trim(),
                    projectsArea.getText().trim()
            );

            if (editingModel != null && editingModel.getId() != 0) {
                // update existing
                cv.setId(editingModel.getId());
                CompletableFuture<Boolean> fut = Database.updateCVAsync(cv);
                fut.whenComplete((ok, ex) -> {
                    if (ex != null) {
                        ex.printStackTrace();
                        Platform.runLater(() -> showError("Update failed: " + ex.getMessage()));
                        return;
                    }
                    Platform.runLater(() -> {
                        if (ok) {
                            scenecontroller.setCV(cv);
                            showInfo("CV updated.");
                            scenecontroller.switchTo("preview.fxml");
                        } else {
                            showError("Update returned false.");
                        }
                    });
                });
            } else {
                // insert new
                Database.saveCVAsync(cv).whenComplete((generatedId, ex) -> {
                    if (ex != null) {
                        ex.printStackTrace();
                        Platform.runLater(() -> showError("Save failed: " + ex.getMessage()));
                        return;
                    }
                    cv.setId(generatedId);
                    Platform.runLater(() -> {
                        scenecontroller.setCV(cv);
                        showInfo("CV saved.");
                        scenecontroller.switchTo("preview.fxml");
                    });
                });
            }
        });
    }

    private void populateFields(cvmodel cv) {
        nameField.setText(cv.getName());
        emailField.setText(cv.getEmail());
        phoneField.setText(cv.getPhone());
        addressField.setText(cv.getAddress());
        educationArea.setText(cv.getEducation());
        skillsArea.setText(cv.getSkills());
        experienceArea.setText(cv.getExperience());
        projectsArea.setText(cv.getProjects());
    }

    private boolean isValid() {
        if (nameField.getText().trim().isEmpty()) {
            showError("Full Name is required.");
            return false;
        }
        if (emailField.getText().trim().isEmpty()) {
            showError("Email is required.");
            return false;
        }
        // you can add more validation here (email format, phone format, etc.)
        return true;
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
