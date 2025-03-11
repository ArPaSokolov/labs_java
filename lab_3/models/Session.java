package models;

import java.time.LocalTime;
import java.util.List;

public class Session {
    private Cinema cinema;
    private Hall hall;
    private Seat[][] seats;
    private String date;
    private String movieTitle;
    private LocalTime startTime;
    private LocalTime endTime;  

    public Session(Cinema cinema, Hall hall, String date, String movieTitle, LocalTime startTime, LocalTime endTime) {
        this.cinema = cinema;
        this.hall = hall;
        this.date = date;
        this.movieTitle = movieTitle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.seats = copySeatsFromHall(hall);
    }

    // Бронирование места на конкретный сеанс
    public boolean bookSeat(int row, int col) {
        if (row < 0 || row >= seats.length || col < 0 || col >= seats[row].length) {
            System.out.println("Ошибка: такого места нет!");
            return false;
        }
        return seats[row][col].book();
    }

    // Отображение мест на сеанс
    public void showSeats() {
        System.out.println("План зала " + hall.getName());
        for (int i = 0; i < seats.length; i++) {
            System.out.print("Ряд " + (i + 1) + ": ");
            for (Seat seat : seats[i]) {
                System.out.print("|" + seat.getState() + "| ");
            }
            System.out.println();
        }
    }

    // Проверка на свободные места
    public boolean hasAvailableSeats() {
        for (Seat[] row : seats) {
            for (Seat seat : row) {
                if (!seat.getState().contains("X")) {
                    return true;  // Найдено хотя бы одно свободное место
                }
            }
        }
        return false;  // Все места забронированы
    }

    public String generateTicket(List<Integer> seat) {
        return "Фильм: " + movieTitle + "\n" +
               "Кинотеатр: " + cinema.getName() + "\n" +
               "Зал: " + hall.getName() + "\n" +
               "Ряд: " + (seat.get(0) + 1) + "\n" +
               "Место: " + (seat.get(1) + 1) + "\n" +
               "Дата: " + date + "\n" +
               "Время начала: " + startTime + "\n" +
               "Время окончания: " + endTime;
    }

    public Hall getHall() {
        return hall;
    }

    public String getDate() {
        return date;
    }

    public Cinema getCinema() {
        return cinema;
    }
    
    public String getMovieTitle() {
        return movieTitle;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    // Получение схемы мест по залу
    private Seat[][] copySeatsFromHall(Hall hall) {
        Seat[][] originalSeats = hall.getSeats();
        Seat[][] sessionSeats = new Seat[originalSeats.length][];

        for (int i = 0; i < originalSeats.length; i++) {
            sessionSeats[i] = new Seat[originalSeats[i].length];
            for (int j = 0; j < originalSeats[i].length; j++) {
                sessionSeats[i][j] = new Seat(originalSeats[i][j].getState());
            }
        }
        return sessionSeats;
    }

    @Override
    public String toString() {
        return movieTitle + " " + startTime + " - " + endTime;
    }
}