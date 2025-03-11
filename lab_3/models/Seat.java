package models;

public class Seat {
    private String number;
    private boolean isTaken;

    // Конструктор
    public Seat(String number) {
        this.number = number;
        this.isTaken = false;
    }

    // Изменение статуса места
    public boolean book() {  
        if (!isTaken) {
            isTaken = true;
            return true;
        }
        return false;
    }

    // Если место свободно, возвращается номер, иначе крестик
    public String getState() {
        return isTaken ? "X" : number;
    }
}
