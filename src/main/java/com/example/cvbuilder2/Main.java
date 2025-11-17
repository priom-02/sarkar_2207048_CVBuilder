package com.example.cvbuilder2;

import com.example.cvbuilder2.utility.scenecontroller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {


        scenecontroller.setMainStage(stage);


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cvbuilder2/home.fxml"));
        Scene scene = new Scene(loader.load());

        stage.setTitle("CV Builder");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
