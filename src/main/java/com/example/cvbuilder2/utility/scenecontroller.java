package com.example.cvbuilder2.utility;

import com.example.cvbuilder2.model.cvmodel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class scenecontroller {

    private static Stage mainStage;
    private static cvmodel storedCV;

    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    public static void switchTo(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(scenecontroller.class.getResource(
                    "/com/example/cvbuilder2/" + fxmlName
            ));

            Scene scene = new Scene(loader.load());

            scene.getStylesheets().add(
                    scenecontroller.class.getResource("/com/example/cvbuilder2/styles.css").toExternalForm()
            );

            mainStage.setScene(scene);
            mainStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setCV(cvmodel cv) {
        storedCV = cv;
    }

    public static cvmodel getCV() {
        return storedCV;
    }
}
