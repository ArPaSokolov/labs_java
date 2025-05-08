package com.beautybook.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.beautybook.db.DBAdapter;
import com.beautybook.models.Appointment;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BookingController {
    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private DatePicker datePicker;
    @FXML private ChoiceBox<String> timeChoice;
    
    private int masterId;
    private Stage dialogStage;
    private boolean okClicked = false;
    
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    
    // Инициализация контроллера
    @FXML
    private void initialize() {
        // Заполняем ChoiceBox доступными временами
        timeChoice.getItems().addAll(
            "09:00", "10:00", "11:00", "12:00", 
            "14:00", "15:00", "16:00", "17:00"
        );
        timeChoice.setValue("10:00");
        
        // Устанавливаем минимальную дату (сегодня)
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isBefore(LocalDate.now()));
            }
        });
        datePicker.setValue(LocalDate.now());
    }
    
    // Установка мастера для записи
    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }
    
    // Установка сцены
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    // Была ли нажата кнопка OK
    public boolean isOkClicked() {
        return okClicked;
    }
    
    // Обработка нажатия кнопки записи
    @FXML
    private void handleBookAppointment() {
        if (isInputValid()) {
            Appointment appointment = new Appointment(
                nameField.getText(),
                phoneField.getText(),
                LocalDateTime.of(datePicker.getValue(), 
                               LocalTime.parse(timeChoice.getValue(), timeFormatter)),
                masterId
            );
            
            if (saveAppointment(appointment)) {
                okClicked = true;
                dialogStage.close();
            }
        }
    }
    
    // Валидация введенных данных
    private boolean isInputValid() {
        StringBuilder errorMessage = new StringBuilder();
        
        if (nameField.getText() == null || nameField.getText().isEmpty()) {
            errorMessage.append("Не указано имя!\n");
        }
        
        if (phoneField.getText() == null || phoneField.getText().isEmpty()) {
            errorMessage.append("Не указан телефон!\n");
        } else if (!phoneField.getText().matches("^\\+?[0-9\\s-]{10,}$")) {
            errorMessage.append("Телефон указан некорректно!\n");
        }
        
        if (datePicker.getValue() == null) {
            errorMessage.append("Не указана дата!\n");
        }
        
        if (timeChoice.getValue() == null) {
            errorMessage.append("Не указано время!\n");
        }
        
        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Ошибка в данных");
            alert.setHeaderText("Пожалуйста, исправьте ошибки:");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
            return false;
        }
    }
    
    // Сохранение записи в БД
    private boolean saveAppointment(Appointment appointment) {
        String sql = "INSERT INTO appointments(clientName, clientPhone, dateTime, masterId) VALUES(?, ?, ?, ?)";
        
        try (Connection conn = DBAdapter.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, appointment.getClientName());
            pstmt.setString(2, appointment.getClientPhone());
            pstmt.setString(3, appointment.getDateTime().toString());
            pstmt.setInt(4, appointment.getMasterId());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Создание записи не удалось, ни одна строка не изменена.");
            }
            
            // Подтверждение
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(dialogStage);
            alert.setTitle("Запись создана");
            alert.setHeaderText(null);
            alert.setContentText(appointment.getClientName() + ", вы успешно записаны " + 
                appointment.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy на HH:mm!")));
            alert.showAndWait();
            
            return true;
            
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Ошибка базы данных");
            alert.setHeaderText("Не удалось сохранить запись");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            
            return false;
        }
    }
    
    // Обработка отмены
    @FXML
    private void handleCancel() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(dialogStage);
        alert.setTitle("Подтверждение");
        alert.setHeaderText("Вы действительно хотите отменить запись?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            dialogStage.close();
        }
    }
}