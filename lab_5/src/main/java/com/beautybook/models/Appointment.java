package com.beautybook.models;

import java.time.LocalDateTime;

public class Appointment {
    private String clientName;
    private String clientPhone;
    private LocalDateTime dateTime;
    private int masterId;
    
    public Appointment(String clientName, String clientPhone, LocalDateTime dateTime, int masterId) {
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.dateTime = dateTime;
        this.masterId = masterId;
    }
    
    public String getClientName() { 
        return clientName; 
    }

    public String getClientPhone() { 
        return clientPhone; 
    }

    public LocalDateTime getDateTime() { 
        return dateTime; 
    }

    public int getMasterId() { 
        return masterId; 
    }
}