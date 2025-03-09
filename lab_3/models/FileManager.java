package models;

import java.io.*;
import java.util.*;

public class FileManager {
    private static final String MOVIES_FILE = "C:\\Programming\\Github\\labs_java\\lab_3\\movies.txt";

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
}