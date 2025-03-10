package models;

import java.io.*;
import java.util.*;

public class FileManager {
    private static final String MOVIES_FILE = "lab_3\\movies.txt";
    private static final String SESSIONS_FILE = "lab_3\\";

    // Чтение списка фильмов из файла
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

    // Добавление нового фильма
    public static void addMovie(List<Movie> movies, Scanner scanner) {
        System.out.println("\nДобавление нового фильма");
        System.out.println("Введите название фильма: ");
        String title = scanner.nextLine();
        System.out.println("Введите длительность фильма (чч:мм): ");
        String length = scanner.nextLine();
        try (PrintWriter writer = new PrintWriter(new FileWriter(MOVIES_FILE))) {
            movies.add(new Movie(title, length));
            updateMoviesList(movies);            
        } catch (IOException e) {
            System.out.println("Ошибка сохранения: " + e.getMessage());
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

    // Загрузка фильмов из списка по дате (один фильм записывается один раз)
    public static HashSet<String> loadSessionsByDate(String cinemaName, String date) {
        File file = new File(SESSIONS_FILE + cinemaName + ".txt");
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
                else if (!line.matches("") && !line.contains("Зал")) { // не дата, не пустая строка и не зал => это фильм
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