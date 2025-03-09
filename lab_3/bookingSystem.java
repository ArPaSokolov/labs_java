import java.util.Scanner;
import models.*;

public class bookingSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //Создание кинотеатрацф
        System.out.print("Введите название кинотеатра: ");
        String cinemaName = scanner.nextLine();

        Cinema cinema = new Cinema(cinemaName);

        //Создание зала в кинотеатре
        cinema.addHall(scanner);

        //Выбор зала
        Hall hall = cinema.getHall(scanner);

        //Добавление мест в выбранном зале
        hall.addSeats(scanner);

        // Выводим план зала
        hall.showSeats();

        // Бронируем место
        hall.bookSeat(scanner);

        // Выводим план зала
        hall.showSeats();  
    }
}
