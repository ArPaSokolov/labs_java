package models;

import java.util.*;

public class Hall {
    private String name;
    private Seat[][] seats;
    private List<Session> sessions;

    // Конструктор
    public Hall(String name) {
        this.name = name;
        this.seats = new Seat[0][0];
        this.sessions = new ArrayList<>();
    }
    
    public void setSeats(Seat[][] seats) {
        this.seats = seats;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public void addSession(Session session) {
        this.sessions.add(session);
    }
    
    public Seat[][] getSeats() {
        return seats;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    // Получение сеансов по фильму
    public List<Session> getSessionsByMovie(String movieName) {
        List<Session> requiredSessions = new ArrayList<>();
        for (Session session : sessions) {
            if (session.getMovieTitle().equals(movieName)) {
                requiredSessions.add(session);
            }
        }
        return requiredSessions;
    }

    // Получение сеансов по дате
    public List<Session> getSessionsByDate(String date) {
        List<Session> requiredSessions = new ArrayList<>();
        for (Session session : sessions) {
            if (session.getDate().equals(date)) {
                requiredSessions.add(session);
            }
        }
        return requiredSessions;
    }

    public String getName() {
        return name;
    }
}