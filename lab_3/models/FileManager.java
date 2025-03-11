package models;

import java.io.*;
import java.nio.file.*;
import java.time.LocalTime;
import java.util.*;

public class FileManager {
    private static final String MOVIES_FILE = "lab_3\\cinemas\\moviesList.txt";
    private static final String CINEMAS_FILE = "lab_3\\cinemas\\cinemasList.txt";
    private static final String SESSIONS_PATH = "lab_3\\cinemas\\";
    private static final String SHEMAS_PATH = "lab_3\\shemas\\";

    // Вставка данных в любую часть файла
    public static void insertLine2File(File filePath, String newLine, int insertPosition) {
        File tempFile = new File("temp.txt"); // временный файл

        try (
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String line;
            int lineNumber = 0;
            boolean inserted = false;

            while ((line = reader.readLine()) != null) {
                // Вставляем строку на нужную позицию
                if (lineNumber == insertPosition) {
                    writer.write(newLine);
                    writer.newLine();
                    inserted = true;
                }
                writer.write(line);
                writer.newLine();
                lineNumber++;
            }

            // Если insertPosition больше числа строк — дописываем в конец
            if (!inserted) {
                writer.write(newLine);
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Ошибка при обработке файла: " + e.getMessage());
            return;
        }

        // Заменяем оригинальный файл временным
        if (!filePath.delete()) {
            System.out.println("Ошибка: не удалось удалить оригинальный файл.");
            return;
        }
        if (!tempFile.renameTo(filePath)) {
            System.out.println("Ошибка: не удалось переименовать временный файл.");
        } else {
            System.out.println("Запись вставлена в файл!");
        }
    }

    // Чтение кинотеатров и залов из файла
    public static List<Cinema> loadCinemas() {
        List<Cinema> cinemas = new ArrayList<>();
        Cinema currentCinema = null;
        boolean isCurrentCinema = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(CINEMAS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Кинотеатр: ")) {
                    if (currentCinema != null) {
                        cinemas.add(currentCinema);  // добавляем кинотеатр
                    }
                    String cinemaName = line.replace("Кинотеатр: ", "").trim();
                    currentCinema = new Cinema(cinemaName);
                    isCurrentCinema = true;
                } else if (isCurrentCinema && !line.isBlank()) {
                    currentCinema.addHall(new Hall(line)); // добавляем зал
                }
            }
            if (currentCinema != null) {
                cinemas.add(currentCinema);  // добавляем последний кинотеатр
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
        System.out.println("Кинотеатры и залы загружены из файла!");
        return cinemas;
    }

    // Чтение фильмов из файла
    public static List<Movie> loadMovies() {
        List<Movie> movies = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(MOVIES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    movies.add(new Movie(parts[0], parts[1]));
                }
            }
            System.out.println("Фильмы загружены из файла!");
        } catch (IOException e) {
            System.out.println("Файл с фильмами не найден!");
        }
        return movies;
    }

    // Чтение схемы зала из файла
    public static int loadSeats(Cinema cinema, Hall hall) {
        String fileName = SHEMAS_PATH + cinema.getName() + "_" + hall.getName() + ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            List<Seat[]> seatRows = new ArrayList<>(); // временный список для хранения рядов

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Ряд")) {
                    String[] parts = line.split("\\|");
                    int numberOfSeats = parts.length/2;

                    Seat[] rowSeats = new Seat[numberOfSeats];
                    for (int colIndex = 0; colIndex < numberOfSeats; colIndex++) {
                        rowSeats[colIndex] = new Seat(String.valueOf(colIndex + 1));
                    }
                    seatRows.add(rowSeats);
                }
            }
            hall.setSeats(seatRows.toArray(new Seat[0][]));
            return 1;
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
            return 0;
        }
    }

    // Чтение сеансов
    public static void loadSessions(Cinema cinema) {
        String fileName = SESSIONS_PATH + cinema.getName() + ".txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            String date = null;
            Hall currentHall = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Если дата
                if (line.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    date = line;
                }
                // Если название зала
                else if (line.contains("Зал")) {
                    String[] parts = line.split(" ");
                    currentHall = cinema.getHallByName(parts[1]);
                }
                // Если сеанс
                else if (!line.isEmpty()) {
                    String[] parts = line.split("\\|");
                    if (parts.length < 3) continue;

                    String movieTitle = parts[0];
                    LocalTime startTime = LocalTime.parse(parts[1]);
                    LocalTime endTime = LocalTime.parse(parts[2]);

                    Session session = new Session(cinema, currentHall, date, movieTitle, startTime, endTime);
                    currentHall.addSession(session);
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке сеансов: " + e.getMessage());
        }
    }

    // Создание кинотеатра
    public static Cinema createCinema(Scanner scanner) {
        System.out.println("Введите название кинотеатра: ");
        String cinemaName = scanner.nextLine();
        Cinema newCinema = new Cinema(cinemaName);

        // Создаем файлик с сеансами в кинотеатре
        try (PrintWriter writer = new PrintWriter(new FileWriter(SESSIONS_PATH + cinemaName + ".txt"))) {
        } catch (IOException e) {
            System.out.println("Ошибка сохранения кинотеатра: " + e.getMessage());
        }

        // Добавим кинотеатр в список кинотеатров
        File cinemasFile = new File(CINEMAS_FILE);
        insertLine2File(cinemasFile, "\nКинотеатр: " + cinemaName, Integer.MAX_VALUE); // вставляем в конец списка
        return newCinema;
    }

    // Создание зала
    public static void createHall(Cinema cinema, Scanner scanner) {
        System.out.print("Введите название зала: ");
        String hallName = scanner.nextLine(); 

        // Создание зала и добавление названия в список
        Hall hall = new Hall(hallName);
        cinema.addHall(hall);

        if (findHallLineNumber(cinema, hall) > 0) {
            System.out.println("Зал \"" + hallName + "\" уже существует!");
        } else {
            File cinemasFile = new File(CINEMAS_FILE);
            insertLine2File(cinemasFile, hallName, findCinemaLineNumber(cinema));
            cinema.addHall(hall);

            System.out.println("Зал \"" + hallName + "\" успешно добавлен!");
        }
    }

    // Создать схему зала
    public static void createSeats(Cinema cinema, Hall hall, Scanner scanner) {
        String fileName = SHEMAS_PATH + cinema.getName() + "_" + hall.getName() + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("План зала \"" + hall.getName() + "\":");
            writer.newLine();

            System.out.print("Введите количество рядов: ");
            int rows = scanner.nextInt();
            hall.setSeats(new Seat[rows][]); 

            // Заполнение мест
            for (int i = 0; i < rows; i++) {
                System.out.print("Введите количество мест в ряду " + (i + 1) + ": ");
                int cols = scanner.nextInt();
                hall.getSeats()[i] = new Seat[cols];

                writer.write("Ряд " + (i + 1) + " ");

                for (int j = 0; j < cols; j++) {
                    hall.getSeats()[i][j] = new Seat(String.valueOf(j + 1));
                    String formattedSeat = String.format("|%-2d|", j + 1);
                    writer.write(formattedSeat);
                }
                writer.newLine();
            }
            System.out.println("Схема зала сохранена в файл: " + fileName);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении схемы в файл: " + e.getMessage());
        }
    }

    // Создание фильма
    public static void createMovie(List<Movie> movies, Scanner scanner) {
        System.out.println("\nДобавление нового фильма");
        System.out.println("Введите название фильма: ");
        String title = scanner.nextLine();
        System.out.println("Введите длительность фильма в минутах: ");
        String length = scanner.nextLine();

        movies.add(new Movie(title, length));

        // Вставка фильма в конец списка
        File moviesFile = new File(MOVIES_FILE);
        insertLine2File(moviesFile, title + ";" + length, Integer.MAX_VALUE);
    }

    // Создание сеанса
    public static void createSession(Session newSession, int insertPosition) {
        String insertString = null;

        if (insertPosition == Integer.MAX_VALUE) { // не нашли такой даты, добавляем сами
            insertString = "\n" + newSession.getDate() + "\nЗал " + newSession.getHall().getName() + "\n" + newSession.getMovieTitle() + "|" + newSession.getStartTime() + "|" + newSession.getEndTime();
        } else { // нашли дату
            insertString = newSession.getMovieTitle() + "|" + newSession.getStartTime() + "|" + newSession.getEndTime();
        }

        // Добавление
        newSession.getHall().addSession(newSession);
        File sessionsFile = new File(SESSIONS_PATH + newSession.getCinema().getName() + ".txt");
        insertLine2File(sessionsFile, insertString, insertPosition);
    }

    // Удаление фильма
    public static void removeMovie(List<Movie> movies, Scanner scanner) {
        if (movies.isEmpty()) {
            System.out.println("Список фильмов пустой");
            return;
        }
    
        System.out.println("Список фильмов, доступных для удаления:");
        for (int i = 1; i < movies.size(); i++) {
            System.out.println(i + ". " + movies.get(i));
        }

        System.out.print("Введите номер фильма для удаления: ");
        int index = scanner.nextInt();
        if (index >= 0 && index < movies.size()) {
            String deletedFilmName = movies.get(index).getMovieTitle();
            movies.remove(index);
            System.out.println("Удален фильм: " + deletedFilmName);
            updateMoviesList(movies);
        } else {
            System.out.println("Некорректный номер!");
        }
    }

    // Обновление списка фильмов (перезаписываем файл)
    public static boolean updateMoviesList(List<Movie> movies) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(MOVIES_FILE))) {
            for (Movie movie : movies) {
                writer.println(movie.getMovieTitle() + ";" + movie.getLength());
            }
            System.out.println("Список обновлен!");
            return true;
        } catch (IOException e) {
            System.out.println("Ошибка сохранения: " + e.getMessage());
            return false;
        }
    }

    // Поиск кинотеатра по названию
    public static int findCinemaLineNumber(Cinema cinema) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CINEMAS_FILE))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.startsWith("Кинотеатр: ")) {
                    String currentCinemaName = line.substring(11).trim(); // Извлекаем название
                    if (currentCinemaName.equalsIgnoreCase(cinema.getName())) {
                        return lineNumber; // Возвращаем номер строки, если нашли кинотеатр
                    }
                }
            }
            return -1;

        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
            return -1;
        }
    }

    // Поиск зала по названию
    public static int findHallLineNumber(Cinema cinema, Hall hall) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CINEMAS_FILE))) {
            String line;
            int lineNumber = 0;
            boolean search = true;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.startsWith("Кинотеатр: ") && search) {
                    return -1;
                }
                else if (line.startsWith("Кинотеатр: ")) {
                    String currentCinemaName = line.substring(11).trim();
                    if (currentCinemaName.equalsIgnoreCase(cinema.getName())) {
                        search = true; // если нашли кинотеатр
                    }
                }
                else if (line.equalsIgnoreCase(hall.getName())) {
                    return lineNumber;
                }
            }

            return -1;
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
            return -1;
        }
    }

    // Метод для нахождения позиции строки, куда нужно записать новый сеанс
    public static int findSessionInsertPosition(Cinema cinema, Session session) {
        String fileName = SESSIONS_PATH + cinema.getName() + ".txt";
        List<String> lines = new ArrayList<>();
        
        try {
            lines = Files.readAllLines(Paths.get(fileName));
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла сеансов: " + e.getMessage());
            return -1;
        }

        String newDate = session.getDate();
        String newHallName = session.getHall().getName();

        // Перебираем строки файла и ищем место для вставки
        boolean dateFound = false;
        boolean hallFound = false;

        int insertPosition = Integer.MAX_VALUE;  // по умолчанию вставляем в конец

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();

            // Находим дату
            if (line.matches("\\d{4}-\\d{2}-\\d{2}")) {
                if (line.equals(newDate)) {
                    dateFound = true;  // Дата найдена
                    System.out.println("Нашел дату");
                } else if (dateFound) {
                    return i - 1;
                }
            }

            // Находим зал
            if (dateFound && line.contains("Зал " + newHallName)) {
                insertPosition = i + 1;
                System.out.println("Нашел зал");
                return insertPosition;  // Зал найден
            }
        }
        return insertPosition;
    }
}