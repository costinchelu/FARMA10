module FARMA10 {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.web;
    requires javafx.base;
    requires java.sql;
    requires ojdbc6;
    requires java.naming;
    requires layout;
    requires kernel;
    requires io;
    requires itextdoc;
    requires itextpdf;


    opens farma;
    opens farma.view to javafx.fxml;
    opens farma.model to javafx.base;

    exports farma.view to javafx.fxml;
}