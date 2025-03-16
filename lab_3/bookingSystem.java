import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import models.*;

public class bookingSystem {
    // Режим администратора
    public static void Admin(List<Cinema> cinemas, List<Movie> movies, Scanner scanner) {
        while (true) { 
            // Меню выбора действия
            System.out.println("Меню:");
            System.out.println("1. Добавить кинотеатр");
            System.out.println("2. Добавить зал");
            System.out.println("3. Добавить схему зала");
            System.out.println("4. Добавить сеанс");
            System.out.println("5. Меню фильмов");
            System.out.println("0. Выйти");
            int input = scanner.nextInt();
            scanner.nextLine();
            Cinema selectedCinema = null;
            Hall selectedHall = null;

            switch (input) {
                case 1: // Добавить кинотеатр
                    Cinema newCinema = FileManager.createCinema(scanner);
                    cinemas.add(newCinema);
                    break;

                case 2: // Добавить зал
                    selectedCinema = chooseCinema(cinemas, scanner);
                    if (selectedCinema == null){
                        break;
                    }
                    FileManager.createHall(selectedCinema, scanner);
                    break;

                case 3: //  Добавить схему зала
                    selectedCinema = chooseCinema(cinemas, scanner);
                    selectedHall = chooseHall(selectedCinema, scanner);
                    FileManager.createSeats(selectedCinema, selectedHall, scanner);
                    break;

                case 4: // Добавить сеанс

                    // Получаем файл вставки
                    selectedCinema = chooseCinema(cinemas, scanner);
                    selectedHall = chooseHall(selectedCinema, scanner);

                    // Получаем данные для сеанса
                    Movie selectedMovie = chooseMovie(movies, scanner);

                    System.out.print("Введите дату (в формате YYYY-MM-DD): ");
                    String date = scanner.nextLine();

                    System.out.print("Введите время начала сеанса (в формате HH:mm): ");
                    String startTimeStr = scanner.nextLine();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    LocalTime startTime = LocalTime.parse(startTimeStr, formatter);

                    String movieLength = selectedMovie.getLength(); 
                    LocalTime endTime = startTime.plusMinutes(Integer.parseInt(movieLength)); // рассчитываем время окончания

                    // Создаем сеанс
                    Session newSession = new Session(selectedCinema, selectedHall, date, selectedMovie.getMovieTitle(), startTime, endTime);

                    // Добавляем сеанс
                    int insertPosition = FileManager.findSessionInsertPosition(selectedCinema, newSession);
                    
                    FileManager.createSession(newSession, insertPosition);

                    System.out.println("Сеанс добавлен! \nФильм: " + selectedMovie.getMovieTitle() + "\nЗал: " + selectedHall.getName() + "\nДата: " + date + "\nВремя: " + startTime + " - " + endTime);
                    System.out.println();
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

    // Режим обычного пользователя
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
                    selectedSession = chooseSessionByMovie(cinemas, selectedMovie.getMovieTitle(), scanner);
                    if (selectedSession == null) {
                        break;
                    }
                    if (selectedSession.hasAvailableSeats()) {
                        selectedSession.showSeats();
                        List<Integer> seat = chooseSeat(selectedSession, scanner);
                        if (seat == null) {
                            break;
                        }
                        System.out.println(selectedSession.generateTicket(seat));
                        // Демонстрация брони
                        System.out.println();
                        selectedSession.showSeats();

                    } else {
                        System.out.println("На этом сеансе нет свободных мест :(");
                    }
                    break;

                case 2: // Выбрать сеанс по дате
                    TreeSet<String> dates = findSessionDates(cinemas);
                    String selectedDate = chooseDate(dates, scanner);
                    selectedSession = chooseSessionByDate(cinemas, selectedDate, scanner);
                    if (selectedSession.hasAvailableSeats()) {
                        selectedSession.showSeats();
                        List<Integer> seat = chooseSeat(selectedSession, scanner);
                        if (seat == null) {
                            break;
                        }
                        System.out.println(selectedSession.generateTicket(seat));
                        // Демонстрация брони
                        System.out.println();
                        selectedSession.showSeats();

                    } else {
                        System.out.println("На этом сеансе нет свободных мест :(");
                    }
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
        // Проверка на наличие сеансов пасписании
        if (dates.isEmpty()) {
            System.out.println("Нет сеансов в ближайшее время!");
            return null;
        }

        // Меню выбора даты
        System.out.println("Выберите дату из списка:");
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
    public static List<Integer> chooseSeat(Session currentSession, Scanner scanner) {
        System.out.print("Введите ряд и место для бронирования: ");
        int row = scanner.nextInt() - 1;
        int col = scanner.nextInt() - 1;

        if (currentSession.bookSeat(row, col)) {
            List<Integer> seat = new ArrayList<>();
            seat.add(row);
            seat.add(col);
            return seat;
        } else {
            System.out.println("Ошибка: место уже занято или некорректный ввод.");
            return null;
        }      
    }

    // Выбор сеанса по фильму
    public static Session chooseSessionByMovie(List<Cinema> cinemas, String currentMovieTitle, Scanner scanner) {
        
        // Получение подходящих сеансов
        List<Session> selectedSessions = new ArrayList<>();

        for (Cinema cinema : cinemas) { // идем по всем кинотеатрам
            List<Hall> halls = cinema.getHalls();
            for (Hall hall : halls) { // идем по всем залам
                List<Session> selectedSessionsInHall = hall.getSessionsByMovie(currentMovieTitle);
                for (Session session : selectedSessionsInHall) { // идем по полученным сеансам из текущего зала
                    selectedSessions.add(session);
                }
            }
        }

        // Нашлись ли сеансы
        if (selectedSessions.isEmpty()) {
            System.out.println("Нет сеансов с этим фильмом!");
            return null;
        }

        // Меню выбора сеанса из подходящих
        System.out.println("Выберите сеанс из списка:");
        int index = 0;
        for (Session session : selectedSessions) {
            System.out.println((index + 1) + ". " + session + " (" + session.getDate() + ")" + " (" + session.getCinema().getName() + ", Зал " + session.getHall().getName()+ ")");
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
        return selectedSessions.get(inputIndex);
    }

    // Выбор сеанса по дате
    public static Session chooseSessionByDate(List<Cinema> cinemas, String selectedDate, Scanner scanner) {

        // Получение подходящих сеансов
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

        // Выбор сеанса из подходящих
        System.out.println("Выберите номер сеанса: ");
        for (int i = 0; i < sessionsOnDate.size(); i++) {
            System.out.println((i + 1) + ". " + sessionsOnDate.get(i).getMovieTitle() + " " + sessionsOnDate.get(i).getStartTime() + "-"+ sessionsOnDate.get(i).getEndTime() + " (" + sessionsOnDate.get(i).getCinema().getName() + ", Зал " + sessionsOnDate.get(i).getHall().getName()+ ")");
        }
        int movieIndex = scanner.nextInt() - 1;
        Session selectedSession = sessionsOnDate.get(movieIndex);
        return selectedSession;
    }

    // Выбор фильма
    public static Movie chooseMovie(List<Movie> movies, Scanner scanner) {
        // Проверка, есть ли фильмы
        if (movies.isEmpty()) {
            System.out.println("Список фильмов пуст!");
            return null;
        }

        // Меню выбора фильма
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
        // Добавлены ли кинотеатры
        if (cinemas.isEmpty()) {
            System.out.println("Список кинотеатров пуст!");
            return null;
        }

        // Меню выбоа кинотеатра
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
        // Добавлены ли залы
        List<Hall> halls = cinema.getHalls();
        if (halls.isEmpty()) {
            System.out.println("Список залов пуст!");
            return null;
        }

        // Меню выбора зала
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
        int menuIndex = Integer.MAX_VALUE;
        while (menuIndex != 0) {
            System.out.println("Меню фильмов:");
            System.out.println("1. Добавить фильм");
            System.out.println("2. Удалить фильм");
            System.out.println("3. Показать список фильмов");
            System.out.println("0. Назад");
            menuIndex = scanner.nextInt();
            scanner.nextLine();
            switch (menuIndex) {
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

    // Получение уникальных отсортированных дат
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
        List<Cinema> cinemas = FileManager.loadCinemas(); // загружаем кинотеатры и залы из файла cinemasList.txt

        System.out.println("Загружаем сеансы и схемы залов..."); 
        int total = 0;
        int loaded = 0;
        for (Cinema cinema : cinemas) { // идем по всем кинотеатрам
            List<Hall> halls = cinema.getHalls();
            for (Hall hall : halls) { // идем по всем залам
                loaded += FileManager.loadSeats(cinema, hall); // загружаем схемы из файлов <сinemaName>_<hallName>.txt
                total ++;
            }
            FileManager.loadSessions(cinema); // загружаем сеансы (сеансам нужны схемы) <сinemaName>.txt
        }
        if (loaded == total){
            System.out.println("Сеансы и схемы залов загружены успешно!");
        } else {
            System.out.println("Внимание! Залов найдено " + total + ", схем загружено " + loaded);
        }

        System.out.println("Загружаем фильмы...");
        List<Movie> movies = FileManager.loadMovies(); // загружаем фильмы из файла moviesList.txt

        // Консольное приложение по бронированию билетов
        String userRole = AuthManager.login(); // вход пользователя

        if (userRole.equals("admin")) { // администрирование сервиса 
            System.out.println("Режим администратора!");
            Admin(cinemas, movies, scanner);
        } else { // бронирование билетов
            System.out.println("Привет, " + userRole + "!");
            User(cinemas, movies, scanner);
        }
    }
}
