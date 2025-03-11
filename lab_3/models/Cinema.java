package models;

import java.util.ArrayList;
import java.util.List;

public class Cinema {
    private String name;
    private List<Hall> halls;

    // Конструктор
    public Cinema(String name) {
        this.name = name;
        this.halls = new ArrayList<>();
    }

    // Вывод информации о кинотеатрах
    public void showHalls() {
        System.out.println("Кинотеатр: " + name);
        if (halls.isEmpty()) {
            System.out.println(" Нет залов.");
        } else {
            System.out.println(" Залы:");
            for (Hall hall : halls) {
                System.out.println(" -" + hall.getName());
            }
        }
    }

    public void addHall(Hall hall) {
        halls.add(hall);
    }

    // Получение зала
    public Hall getHallByName(String hallName) {
        for (Hall hall : halls) {
            if (hall.getName().equalsIgnoreCase(hallName)) {
                return hall;
            }
        }
        System.out.println("Зал с именем \"" + hallName + "\" не найден!");
        return null;
    }

    // Получение всех залов
    public List<Hall> getHalls() {
        return halls;
    }
    
    public String getName() {
        return name;
    }
}
