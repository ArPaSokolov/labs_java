package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cinema {
    private String name;
    private List<Hall> halls;

    // Конструктор
    public Cinema(String name) {
        this.name = name;
        this.halls = new ArrayList<>();
    }

    // Вывод информации о кинотеатрах
    public void showInfo() {
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

    // Получение зала
    public Hall getHall(Scanner scanner) {
        if (halls.isEmpty()) {
            System.out.println("Нет доступных залов в кинотеатре.");
            return null;
        }
        System.out.print("Введите название зала: ");
        String hallName = scanner.nextLine();
    
        for (Hall hall : halls) {
            if (hall.getName().equalsIgnoreCase(hallName)) {
                return hall;
            }
        }
        System.out.println("Зал с именем \"" + hallName + "\" не найден!");
        return this.getHall(scanner); // Рекурсивный вызов
    }

    // Получение всех залов
    public List<Hall> getHalls() {
        return halls;
    }

    public void addHall(Hall hall) {
        halls.add(hall);
    }
    
    public String getName() {
        return name;
    }
}
