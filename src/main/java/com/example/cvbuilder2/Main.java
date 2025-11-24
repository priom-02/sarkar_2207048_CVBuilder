package com.example.cvbuilder2;

import com.example.cvbuilder2.database.Database;
import com.example.cvbuilder2.utility.scenecontroller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Create table when app starts
        Database.createTable();

        // Store stage for switching scenes
        scenecontroller.setMainStage(stage);

        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/com/example/cvbuilder2/home.fxml"));

        Scene scene = new Scene(loader.load());

        // Attach CSS
        scene.getStylesheets().add(
                getClass().getResource("/com/example/cvbuilder2/styles.css")
                        .toExternalForm()
        );

        stage.setTitle("CV Builder");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        // Ensure executor thread shuts down safely
        Database.shutdownExecutor();
    }

    public static void main(String[] args) {
        launch();
    }
}
