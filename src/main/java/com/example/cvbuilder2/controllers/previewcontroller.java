package com.example.cvbuilder2.controllers;

import com.example.cvbuilder2.database.Database;
import com.example.cvbuilder2.model.cvmodel;
import com.example.cvbuilder2.utility.scenecontroller;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class previewcontroller {

    @FXML private Button backBtn;
    @FXML private Button editBtn;     // optional (add to preview.fxml if you want)
    @FXML private Button deleteBtn;   // optional

    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label addressLabel;

    @FXML private Label educationLabel;
    @FXML private Label skillsLabel;
    @FXML private Label experienceLabel;
    @FXML private Label projectsLabel;

    private cvmodel current;

    @FXML
    public void initialize() {

        current = scenecontroller.getCV();

        if (current != null) {
            nameLabel.setText(current.getName());
            emailLabel.setText("Email: " + current.getEmail());
            phoneLabel.setText("Phone: " + current.getPhone());
            addressLabel.setText("Address: " + current.getAddress());

            educationLabel.setText(current.getEducation());
            skillsLabel.setText(current.getSkills());
            experienceLabel.setText(current.getExperience());
            projectsLabel.setText(current.getProjects());
        }

        backBtn.setOnAction(event -> scenecontroller.switchTo("form.fxml"));

        // edit: open form with this model loaded
        if (editBtn != null) {
            editBtn.setOnAction(e -> {
                scenecontroller.setCV(current);
                scenecontroller.switchTo("form.fxml");
            });
        }

        if (deleteBtn != null) {
            deleteBtn.setOnAction(e -> {
                if (current == null || current.getId() == 0) {
                    showError("No record selected or saved id missing.");
                    return;
                }
                Database.deleteCVAsync(current.getId()).whenComplete((ok, ex) -> {
                    if (ex != null) {
                        ex.printStackTrace();
                        Platform.runLater(() -> showError("Delete failed: " + ex.getMessage()));
                        return;
                    }
                    Platform.runLater(() -> {
                        if (ok) {
                            showInfo("Deleted successfully.");
                            scenecontroller.switchTo("list.fxml"); // go to list
                        } else {
                            showError("Delete returned false.");
                        }
                    });
                });
            });
        }
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Info");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }
}
