module com.example.cvbuilder2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;

    opens com.example.cvbuilder2 to javafx.fxml;
    opens com.example.cvbuilder2.controllers to javafx.fxml;
    opens com.example.cvbuilder2.model to javafx.base, com.fasterxml.jackson.databind;
    opens com.example.cvbuilder2.database;

    exports com.example.cvbuilder2;
    exports com.example.cvbuilder2.controllers;
}
