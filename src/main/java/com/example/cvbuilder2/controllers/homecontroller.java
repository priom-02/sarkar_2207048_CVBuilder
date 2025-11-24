package com.example.cvbuilder2.controllers;

import com.example.cvbuilder2.utility.scenecontroller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class homecontroller {

    @FXML
    private Button createBtn;

    @FXML
    private Button manageBtn;

    @FXML
    public void initialize() {

        createBtn.setOnAction(e -> scenecontroller.switchTo("form.fxml"));

        manageBtn.setOnAction(e -> scenecontroller.switchTo("list.fxml"));
    }
}
