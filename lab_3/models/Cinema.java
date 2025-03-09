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
    
    // Создание зала
    public void addHall(Scanner scanner) {
        System.out.print("Введите название зала: ");
        String hallName = scanner.nextLine(); 

        // Создание зала и добавление названия в список
        Hall hall = new Hall(hallName);
        halls.add(hall);

        System.out.println("Зал \"" + hallName + "\" успешно добавлен!");
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
        return this.getHall(scanner); // Рекурсивный вызов без ошибки
    }

    public String getName() {
        return name;
    }
}
