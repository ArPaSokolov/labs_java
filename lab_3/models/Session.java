package models;

import java.time.LocalTime;

public class Session {
    private String movieTitle;
    private LocalTime startTime;
    private LocalTime endTime;
    private Hall hall;
    private String date; 

    public Session(String movieTitle, LocalTime startTime, LocalTime endTime, Hall hall, String date) {
        this.movieTitle = movieTitle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hall = hall;
        this.date = date;
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