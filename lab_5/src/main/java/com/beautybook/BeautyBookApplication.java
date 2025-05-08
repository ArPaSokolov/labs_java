package com.beautybook;

import com.beautybook.db.DBAdapter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BeautyBookApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Инициализация БД
        DBAdapter.initializeDB();
        
        Parent root = FXMLLoader.load(getClass().getResource("/com/beautybook/views/client-view.fxml"));
        Scene scene = new Scene(root, 250, 350);
        scene.getStylesheets().add(getClass().getResource("/com/beautybook/views/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("BeautyBook");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}