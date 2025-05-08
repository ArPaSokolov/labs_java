package com.beautybook.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.beautybook.db.DBAdapter;
import com.beautybook.models.Master;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ClientController {
    @FXML
    private TableView<Master> mastersTable;
    
    @FXML
    private TableColumn<Master, String> nameColumn;
    
    @FXML
    private TableColumn<Master, String> specializationColumn;
    
    private ObservableList<Master> mastersData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Настройка колонок таблицы
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        specializationColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        
        // Загрузка данных
        loadMasters();
        
        // Привязка данных к таблице
        mastersTable.setItems(mastersData);
    }
    
    private void loadMasters() {
        String sql = "SELECT id, name, specialization FROM masters";
        
        try (Connection conn = DBAdapter.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            mastersData.clear(); 
            
            while (rs.next()) {
                mastersData.add(new Master(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("specialization")
                ));
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка загрузки мастеров: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleRefresh() {
        loadMasters();
    }
    
    @FXML
    private void handleBookAppointment() {
        Master selected = mastersTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                // Загрузка FXML
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/com/beautybook/views/booking-dialog.fxml"));
                Parent page = loader.load();
                
                // Создание диалога
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Запись к мастеру");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(mastersTable.getScene().getWindow());
                Scene scene = new Scene(page);
                dialogStage.setScene(scene);
                
                // Установка мастера
                BookingController controller = loader.getController();
                controller.setMasterId(selected.getId());
                controller.setDialogStage(dialogStage);
                
                // Показ диалога
                dialogStage.showAndWait();
                
                // Обновление данных после закрытия диалога
                if (controller.isOkClicked()) {
                    loadMasters();
                }
                
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Не удалось открыть форму записи");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Не выбран мастер");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, выберите мастера из таблицы");
            alert.showAndWait();
        }
    }
}