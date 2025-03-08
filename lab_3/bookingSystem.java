import java.util.*;

class Cinema {
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

class Hall {
    private String name;
    private Seat[][] seats;

    // Конструктор
    public Hall(String name) {
        this.name = name;
    }

    // Создание мест в зале
    public void addSeats(Scanner scanner) {
        System.out.print("Введите количество рядов: ");
        int rows = scanner.nextInt();
        seats = new Seat[rows][];  // Создаём массив рядов

        for (int i = 0; i < rows; i++) {
            System.out.print("Введите количество мест в ряду " + (i + 1) + ": ");
            int cols = scanner.nextInt();
            seats[i] = new Seat[cols];  // Создаём массив мест в ряду

            for (int j = 0; j < cols; j++) {
                seats[i][j] = new Seat(String.valueOf(j + 1));  // Заполняем массив местами
            }
        }
    }

    // Отображение плпнп зала
    public void showSeats() {
        if (seats == null || seats.length == 0 || seats[0] == null || seats[0].length == 0) {
            System.out.println("Ошибка: в зале не добавлено мест!");
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

        // // Переводим в индексы массива
        // row --;
        // col --;  

        if (row < 0 || row >= seats.length || col < 0 || col >= seats[row].length) {
            System.out.println("Ошибка: такого места нет!");
        }
        if (seats[row][col].book()) {
            System.out.println("Вы успешно забронировали место!");
        } else {
            System.out.println("Ошибка: место уже занято!");
        }
    }

    public String getName() {
        return name;
    }
}

class Seat {
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
    public String seatState() {
        return isTaken ? "X" : number;
    }

}

public class bookingSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //Создание кинотеатра
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
