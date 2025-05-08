package com.beautybook.models;

public class Master {
    private final int id;
    private final String name;
    private final String specialization;
    
    public Master(int id, String name, String specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
    }
    
    public int getId() { 
        return id; 
    }

    public String getName() { 
        return name; 
    }
    
    public String getSpecialization() { 
        return specialization; 
    }
}