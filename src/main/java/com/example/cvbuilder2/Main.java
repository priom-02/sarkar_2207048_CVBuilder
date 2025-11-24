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


        Database.createTable();


        scenecontroller.setMainStage(stage);

        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/com/example/cvbuilder2/home.fxml"));

        Scene scene = new Scene(loader.load());


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


        Database.shutdownExecutor();
    }

    public static void main(String[] args) {
        launch();
    }
}
