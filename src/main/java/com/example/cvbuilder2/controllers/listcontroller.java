package com.example.cvbuilder2.controllers;

import com.example.cvbuilder2.database.Database;
import com.example.cvbuilder2.model.cvmodel;
import com.example.cvbuilder2.utility.scenecontroller;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class listcontroller {

    @FXML private TableView<cvmodel> table;
    @FXML private TableColumn<cvmodel, Number> colId;
    @FXML private TableColumn<cvmodel, String> colName;
    @FXML private TableColumn<cvmodel, String> colEmail;
    @FXML private TableColumn<cvmodel, String> colPhone;
    @FXML private TableColumn<cvmodel, Void> colActions;

    @FXML private Button newBtn;
    @FXML private Button refreshBtn;
    @FXML private Button exportBtn;
    @FXML private Button importBtn;

    private ObservableList<cvmodel> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // FIXED: normal getter usage
        colId.setCellValueFactory(cell -> new javafx.beans.property.SimpleLongProperty(cell.getValue().getId()));
        colName.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));
        colEmail.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEmail()));
        colPhone.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getPhone()));

        // actions column (edit/delete)
        colActions.setCellFactory(tc -> new TableCell<>() {
            private final Button edit = new Button("Edit");
            private final Button del = new Button("Delete");
            private final HBox box = new HBox(6, edit, del);

            {
                edit.setOnAction(e -> {
                    cvmodel item = getTableView().getItems().get(getIndex());
                    scenecontroller.setCV(item);
                    scenecontroller.switchTo("form.fxml");
                });

                del.setOnAction(e -> {
                    cvmodel item = getTableView().getItems().get(getIndex());
                    if (item == null) return;
                    Database.deleteCVAsync(item.getId()).whenComplete((ok, ex) -> {
                        if (ex != null) {
                            ex.printStackTrace();
                            Platform.runLater(() -> showError("Delete failed: " + ex.getMessage()));
                            return;
                        }
                        if (ok) {
                            Platform.runLater(() -> {
                                data.remove(item);
                                showInfo("Deleted");
                            });
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                setGraphic(empty ? null : box);
            }
        });

        table.setItems(data);

        newBtn.setOnAction(e -> {
            scenecontroller.setCV(null);
            scenecontroller.switchTo("form.fxml");
        });

        refreshBtn.setOnAction(e -> loadAll());

        exportBtn.setOnAction(e -> exportJson());
        importBtn.setOnAction(e -> importJson());

        loadAll();
    }

    private void loadAll() {
        Database.fetchAllAsync().whenComplete((list, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                Platform.runLater(() -> showError("Load failed: " + ex.getMessage()));
                return;
            }
            Platform.runLater(() -> {
                data.clear();
                data.addAll(list);
            });
        });
    }

    private void exportJson() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Export CVs to JSON");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        File file = fc.showSaveDialog(null);
        if (file != null) {
            try {
                Database.exportToJson(file, data);
                showInfo("Exported: " + file.getAbsolutePath());
            } catch (IOException ex) {
                ex.printStackTrace();
                showError("Export failed: " + ex.getMessage());
            }
        }
    }

    private void importJson() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Import CVs from JSON");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        File file = fc.showOpenDialog(null);
        if (file != null) {
            try {
                List<cvmodel> imported = Database.importFromJson(file);
                for (cvmodel c : imported) {
                    Database.saveCVAsync(c).thenAccept(id -> {
                        c.setId(id);
                        Platform.runLater(() -> data.add(0, c));
                    });
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                showError("Import failed: " + ex.getMessage());
            }
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
