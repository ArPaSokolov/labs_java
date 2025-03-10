package models;

import java.util.*;

public class Hall {
    private String name;
    private Seat[][] seats;

    // Конструктор
    public Hall(String name) {
        this.name = name;
        this.seats = new Seat[0][0];
    }

    // Отображение плана зала
    public void showSeats() {
        if (seats == null || seats.length == 0 || seats[0] == null || seats[0].length == 0) {
            System.out.println("Ошибка: в зале не добавлено мест!");
            return;
        }

        System.out.println("План зала \"" + name + "\":");
        int rowNumber = 1;
        for (Seat[] row : seats) {
            System.out.print("Ряд " + rowNumber + " ");
            for (Seat seat : row) {
                System.out.printf("|%-2s| ", seat.seatState());
            }
            rowNumber ++;
            System.out.println();
        }
    }

    // Бронирование места
    public void bookSeat(Scanner scanner) {
        System.out.print("Введите номер ряда и место: ");
        int row = scanner.nextInt() - 1;
        int col = scanner.nextInt() - 1;

        if (row < 0 || row >= seats.length || col < 0 || col >= seats[row].length) {
            System.out.println("Ошибка: такого места нет!");
        }
        if (seats[row][col].book()) {
            System.out.println("Вы успешно забронировали место!");
        } else {
            System.out.println("Ошибка: место уже занято!");
        }
    }

    public Seat[][] getSeats() {
        return seats;
    }
    
    public void setSeats(Seat[][] seats) {
        this.seats = seats;
    }

    public String getName() {
        return name;
    }
}