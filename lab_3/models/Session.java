package models;

import java.time.LocalTime;

public class Session {
    private String movieTitle;
    private LocalTime startTime;
    private LocalTime endTime;
    private Hall hall;
    private String date; 
    private Seat[][] seats; 

    public Session(String movieTitle, LocalTime startTime, LocalTime endTime, Hall hall, String date) {
        this.movieTitle = movieTitle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hall = hall;
        this.date = date;
        this.seats = copySeatsFromHall(hall);
    }

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

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Hall getHall() {
        return hall;
    }

    @Override
    public String toString() {
        return movieTitle + " " + startTime + " - " + endTime;
    }
}