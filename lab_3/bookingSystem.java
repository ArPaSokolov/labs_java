import java.util.*;
import models.*;

public class bookingSystem {
    // Администратор
    public static void Admin(List<Cinema> cinemas, List<Movie> movies, Scanner scanner) {
        while (true) { 
            System.out.println("Меню:");
            System.out.println("1. Добавить кинотеатр");
            System.out.println("2. Добавить зал");
            System.out.println("3. Добавить схему зала");
            System.out.println("4. Меню сеансов");
            System.out.println("5. Меню фильмов");
            System.out.println("0. Выйти");
            int input = scanner.nextInt();
            scanner.nextLine();
            Cinema curentCinema = null;
            Hall curentHall = null;

            switch (input) {
                case 1: // Добавить кинотеатр
                    FileManager.createCinema(scanner);
                    cinemas = FileManager.loadCinemas();
                    break;

                case 2: // Добавить зал
                    curentCinema = chooseCinema(cinemas, scanner);
                    if (curentCinema == null){
                        break;
                    }
                    FileManager.createHall(curentCinema, scanner);
                    break;

                case 3: //  Добавить схему зала
                    curentCinema = chooseCinema(cinemas, scanner);
                    curentHall = chooseHall(curentCinema, scanner);
                    FileManager.addSeats(curentCinema, curentHall, scanner);
                    break;

                case 4: // Добавить сеанс
                    System.out.println("Добавление сеанса временно не поддерживается ((");                    
                    break;

                case 5: //  Меню фильмов
                    filmMenu(movies, scanner);
                    break;
                    
                case 0: // Выход
                    System.out.println("Выход из программы...");
                    return;

                default:
                    System.out.println("Некорректный ввод!");
                    break;
            }
        }
    }

    // Обычный пользователь
    public static void User(List<Cinema> cinemas, List<Movie> movies, Scanner scanner) {
        while (true) { 
            Session selectedSession = null;
            System.out.println("Меню:");
            System.out.println("1. Выбрать фильм");
            System.out.println("2. Выбрать дату");
            System.out.println("0. Выйти");
            int input = scanner.nextInt();
            scanner.nextLine();

            switch (input) {
                case 1: // Выбрать сеанс по фильму
                    Movie selectedMovie = chooseMovie(movies, scanner);
                    if (selectedMovie == null) {
                        break;
                    }
                    selectedSession = chooseSessionByMovie(cinemas, selectedMovie, scanner);
                    if (selectedSession == null) {
                        break;
                    }
                    selectedSession.showSeats();
                    chooseSeat(selectedSession, scanner);
                    break;

                case 2: // Выбрать сеанс по дате
                    TreeSet<String> dates = findSessionDates(cinemas);
                    String selectedDate = chooseDate(dates, scanner);
                    selectedSession = chooseSessionByDate(cinemas, selectedDate, scanner);
                    selectedSession.showSeats();
                    chooseSeat(selectedSession, scanner);
                    break;

                case 0: // Выход
                    System.out.println("Выход из программы...");
                    return;

                default:
                    System.out.println("Некорректный ввод!");
                    break;
            }
        }
    }

    // Выбор даты
    public static String chooseDate(TreeSet<String> dates, Scanner scanner) {
        if (dates.isEmpty()) {
            System.out.println("Нет сеансов в ближайшее время!");
            return null;
        }

        System.out.println("Дату из списка:");
        List<String> datesList = new ArrayList<>(dates); // преобразование в список
        int index = 0;
        for (String date : datesList) {
            System.out.println((index + 1) + ". " + date);
            index++;
        }
        System.out.println("0. Назад");

        int inputIndex = scanner.nextInt();
        scanner.nextLine();
        if (inputIndex == 0) {
            return null;
        }
        inputIndex --;

        if (inputIndex < 0 || inputIndex >= datesList.size()) {
            System.out.println("Ошибка: Неверный номер фильма!");
            return null;
        }
        String selectedDate = datesList.get(inputIndex);
        System.out.println("Выбрана дата: " + selectedDate);
        return selectedDate;
    }

    // Выбор места
    public static void chooseSeat(Session currentSession, Scanner scanner) {
        System.out.print("Введите ряд и место для бронирования: ");
        int row = scanner.nextInt() - 1;
        int col = scanner.nextInt() - 1;

        if (currentSession.bookSeat(row, col)) {
            System.out.println("Место забронировано!");
            currentSession.showSeats();
        } else {
            System.out.println("Ошибка: место уже занято или некорректный ввод.");
        }      
    }

    // Выбор сеанса по фильму
    public static Session chooseSessionByMovie(List<Cinema> cinemas, Movie currentMovie, Scanner scanner) {
        List<Session> selectedSessions = new ArrayList<>();

        for (Cinema cinema : cinemas) { // идем по всем кинотеатрам
            List<Hall> halls = cinema.getHalls();
            for (Hall hall : halls) { // идем по всем залам
                List<Session> selectedSessionsInHall = hall.getSessionsByMovie(currentMovie.getMovieTitle());
                for (Session session : selectedSessionsInHall) {
                    selectedSessions.add(session);
                }
            }
        }
    
        if (selectedSessions.isEmpty()) {
            System.out.println("Нет сеансов с этим фильмом!");
            return null;
        }

        System.out.println("Выберите сеанс из списка:");
        int index = 0;
        for (Session session : selectedSessions) {
            System.out.println((index + 1) + ". " + session + " (" + session.getDate() + ")");
            index++;
        }
        System.out.println("0. Назад");

        int inputIndex = scanner.nextInt();
        scanner.nextLine();
        if (inputIndex == 0) {
            return null;
        }
        inputIndex --;

        if (inputIndex < 0 || inputIndex >= selectedSessions.size()) {
            System.out.println("Ошибка: Неверный номер фильма!");
            return null;
        }
        System.out.println("Выбран фильм: " + selectedSessions.get(inputIndex).getMovieTitle());
        return selectedSessions.get(inputIndex);
    }

    // Выбор сеанса по дате
    public static Session chooseSessionByDate(List<Cinema> cinemas, String selectedDate, Scanner scanner) {
        List<Session> sessionsOnDate = new ArrayList<>();

        for (Cinema cinema : cinemas) {
            for (Hall hall : cinema.getHalls()) {
                for (Session session : hall.getSessions()) {
                    if (session.getDate().equals(selectedDate)) {
                        sessionsOnDate.add(session);
                    }
                }
            }
        }
        for (int i = 0; i < sessionsOnDate.size(); i++) {
            System.out.println(i + ". " + sessionsOnDate.get(i).getMovieTitle() + " " + sessionsOnDate.get(i).getStartTime() + "-"+ sessionsOnDate.get(i).getEndTime());
        }

        System.out.print("Выберите номер сеанса: ");
        int movieIndex = scanner.nextInt();
        Session selectedSession = sessionsOnDate.get(movieIndex);

        System.out.println("Выбран сеанс на фильм " + selectedSession.getMovieTitle());
        return selectedSession;
    }

    // Выбор фильма
    public static Movie chooseMovie(List<Movie> movies, Scanner scanner) {
        if (movies.isEmpty()) {
            System.out.println("Список фильмов пуст!");
            return null;
        }

        System.out.println("Выберите фильм из списка:");
        int index = 0;
        for (Movie movie : movies) {
            System.out.println((index + 1) + ". " + movie.getMovieTitle());
            index++;
        }
        System.out.println("0. Назад");

        int inputIndex = scanner.nextInt();
        scanner.nextLine();
        if (inputIndex == 0) {
            return null;
        }
        inputIndex --;

        if (inputIndex < 0 || inputIndex >= movies.size()) {
            System.out.println("Ошибка: Неверный номер фильма!");
            return null;
        }
        System.out.println("Выбран фильм: " + movies.get(inputIndex).getMovieTitle());
        return movies.get(inputIndex);
    }

    // Выбор кинотеатра
    public static Cinema chooseCinema(List<Cinema> cinemas, Scanner scanner) {
        if (cinemas.isEmpty()) {
            System.out.println("Список кинотеатров пуст!");
            return null;
        }

        System.out.println("Выберите кинотеатр из списка:");
        int index = 0;
        for (Cinema cinema : cinemas) {
            System.out.println((index + 1) + ". " + cinema.getName());
            index++;
        }
        System.out.println("0. Назад");

        int inputIndex = scanner.nextInt();
        scanner.nextLine();
        if (inputIndex == 0) {
            return null;
        }
        inputIndex --;

        if (inputIndex < 0 || inputIndex >= cinemas.size()) {
            System.out.println("Ошибка: Неверный номер кинотеатра!");
            return null;
        }
        System.out.println("Выбран кинотеатр: " + cinemas.get(inputIndex).getName());
        return cinemas.get(inputIndex);
    }
    
    // Выбор зала
    public static Hall chooseHall(Cinema cinema, Scanner scanner) {
        List<Hall> halls = cinema.getHalls();
        if (halls.isEmpty()) {
            System.out.println("Список залов пуст!");
            return null;
        }

        System.out.println("Выберите зал из списка:");
        int index = 0;
        for (Hall hall : halls) {
            System.out.println((index + 1) + ". " + hall.getName());
            index++;
        }
        System.out.println("0. Назад");

        int inputIndex = scanner.nextInt();
        scanner.nextLine();
        if (inputIndex == 0) {
            return null;
        }
        inputIndex --;

        if (inputIndex < 0 || inputIndex >= halls.size()) {
            System.out.println("Ошибка: Неверный номер зала!");
            return null;
        }
        System.out.println("Выбран зал: " + halls.get(inputIndex).getName());
        return halls.get(inputIndex);
    }

    // Взаимодействие с фильмами
    public static void filmMenu(List<Movie> movies, Scanner scanner) {
        int filmSettings = Integer.MAX_VALUE;
        while (filmSettings != 0) {
            System.out.println("Меню фильмов:");
            System.out.println("1. Добавить фильм");
            System.out.println("2. Удалить фильм");
            System.out.println("3. Показать список фильмов");
            System.out.println("0. Назад");
            filmSettings = scanner.nextInt();
            scanner.nextLine();
            switch (filmSettings) {
                case 1: //  Добавить
                    FileManager.createMovie(movies, scanner);                    
                    break;

                case 2: //  Удалить
                    FileManager.removeMovie(movies, scanner);   
                    break;

                case 3: //  Список        
                    for (Movie movie : movies) {
                            System.out.println(movie);
                    }
                    System.out.println();       
                    break;

                default:
                    System.out.println("Некорректный ввод!");
                    break;
            }
        }
    }

    // Загрузка уникальных отсортированных дат
    public static TreeSet<String> findSessionDates(List<Cinema> cinemas) {
        TreeSet<String> uniqueDates = new TreeSet<>();

        for (Cinema cinema : cinemas) {
            for (Hall hall : cinema.getHalls()) {
                for (Session session : hall.getSessions()) {
                    uniqueDates.add(session.getDate());
                }
            }
        }
        return uniqueDates;
    }



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Запуск сервиса по бронированию билетов
        System.out.println("Запуск сервиса...");

        System.out.println("Загружаем кинотеатри и залы...");
        List<Cinema> cinemas = FileManager.loadCinemas(); // загружаем кинотеатры из файла

        System.out.println("Загружаем сеансы и схемы залов..."); 
        int total = 0;
        int loaded = 0;
        for (Cinema cinema : cinemas) { // идем по всем кинотеатрам
            List<Hall> halls = cinema.getHalls();
            for (Hall hall : halls) { // идем по всем залам
                loaded += FileManager.loadSeats(cinema, hall); // загружаем схемы
                total ++;
            }
            FileManager.loadSessions(cinema); // загружаем сеансы
        }
        if (loaded == total){
            System.out.println("Схемы залов загружены!");
        } else {
            System.out.println("Внимание! Залов найдено " + total + ", схем загружено " + loaded); // скорее всего залов в файле больше, чем файлов со схемами
        }

        System.out.println("Загружаем фильмы...");
        List<Movie> movies = FileManager.loadMovies(); // загружаем фильмы из файла
 
        User(cinemas, movies, scanner);
        // Admin(cinemas, movies, scanner);
    }
}
