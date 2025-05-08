module com.beautybook {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    
    opens com.beautybook.controllers to javafx.fxml;
    opens com.beautybook.views to javafx.fxml;
    opens com.beautybook.models to javafx.base;
    
    exports com.beautybook;
    exports com.beautybook.controllers;
    exports com.beautybook.models;
}