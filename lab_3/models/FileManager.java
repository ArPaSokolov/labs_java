package models;

import java.io.*;
import java.util.*;

public class FileManager {
    private static final String MOVIES_FILE = "lab_3\\movies.txt";
    private static final String CINEMAS_FILE = "lab_3\\cinemas";
    private static final String SESSIONS_PATH = "lab_3\\";

    // Вставка данных в любую часть файла
    public static void insertLine2File(File filePath, String newLine, int insertPosition) {
        File tempFile = new File("temp.txt");

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

    // Чтение фильмов из файла
    public static List<Movie> loadMovies() {
        List<Movie> movies = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(MOVIES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    movies.add(new Movie(parts[0], parts[1]));  // Создаём объект Movie
                }
            }
            System.out.println("Фильмы загружены из файла!");
        } catch (IOException e) {
            System.out.println("Файл с фильмами не найден!");
        }
        return movies;
    }

    // Добавление нового фильма
    public static void addMovie(List<Movie> movies, Scanner scanner) {
        System.out.println("\nДобавление нового фильма");
        System.out.println("Введите название фильма: ");
        String title = scanner.nextLine();
        System.out.println("Введите длительность фильма (чч:мм): ");
        String length = scanner.nextLine();

        movies.add(new Movie(title, length));

        // Вставка фильма в конец списка
        File moviesFile = new File(MOVIES_FILE);
        insertLine2File(moviesFile, title + ";" + length, Integer.MAX_VALUE);
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
 
    // Чтение кинотеатров из файла
    public static List<Cinema> loadCinemas() {
        List<Cinema> cinemas = new ArrayList<>();
        Cinema currentCinema = null;
        boolean isCurrentCinema = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(CINEMAS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Кинотеатр: ")) {
                    if (currentCinema != null) {
                        cinemas.add(currentCinema);  // Добавляем кинотеатр
                    }
                    String cinemaName = line.replace("Кинотеатр: ", "").trim();
                    currentCinema = new Cinema(cinemaName);
                    isCurrentCinema = true;
                } else if (isCurrentCinema && !line.isBlank()) {
                    currentCinema.loadHall(new Hall(line)); // Добавляем зал
                }
            }
            if (currentCinema != null) {
                cinemas.add(currentCinema);  // Добавляем последний кинотеатр
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
        System.out.println("Кинотеатры загружены из файла!");
        return cinemas;
    }

    // Создание кинотеатра
    public static void addCinema(Scanner scanner) {
        System.out.println("Введите название кинотеатра: ");
        String cinemaName = scanner.nextLine();

        // Создаем файлик с сеансами в кинотеатре
        try (PrintWriter writer = new PrintWriter(new FileWriter(SESSIONS_PATH + cinemaName + ".txt"))) {
        } catch (IOException e) {
            System.out.println("Ошибка сохранения кинотеатра: " + e.getMessage());
        }

        // Добавим кинотеатр в список кинотеатров
        File cinemasFile = new File(CINEMAS_FILE);
        insertLine2File(cinemasFile, "\nКинотеатр:" + cinemaName, Integer.MAX_VALUE); // вставляем в конец списка
        // нужно что-то сделать с созданием объекта кинотеатра
    }

    // Загрузка фильмов из списка по дате (один фильм записывается один раз)
    public static HashSet<String> loadSessionsByDate(String cinemaName, String date) {
        File file = new File(SESSIONS_PATH + cinemaName + ".txt");
        if (!file.exists()){
            System.out.println("Такого кинотеатра нет :(");
            return null;
        } 
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean dateMatches = false;
            HashSet<String> films = new HashSet<>();

            while ((line = reader.readLine()) != null) {
                if (line.equals(date)) { // нашли нужную дату, считываем записи до следующей даты
                    dateMatches = true;
                }
                else if (line.matches("\\d{4}-\\d{2}-\\d{2}") && dateMatches) { // считали следующую дату
                        return films;
                }
                else if (!line.isBlank() && !line.contains("Зал")) { // не дата, не пустая строка и не зал => это фильм
                        String[] parts = line.split(" ");
                        films.add(parts[0]);
                }
            }
            if (!dateMatches){
                System.out.println("В эту дату в кинотетре " + cinemaName + " сеансов нет :(");
                return null;
            } else {
                return films;
            }
        } catch (IOException e) {
            System.out.println("Ошибка загрузки кинотеатра: " + e.getMessage());
            return null;
        }
    }
}