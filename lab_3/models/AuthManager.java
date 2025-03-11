package models;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AuthManager {
    private static final Map<String, String> users = new HashMap<>(); // логин + пароль

    // БД пользователей
    static {
        users.put("admin", "admin");  

        users.put("sesha", "12345");

        users.put("yssslay", "67890");
    }

    // Вход
    public static String login() {
        Scanner scanner = new Scanner(System.in);
        String username;

        while (true) { 
            System.out.print("Введите логин: ");
            username = scanner.nextLine();

            System.out.print("Введите пароль: ");
            String password = scanner.nextLine();

            // Проверяем пользователя в БД
            if (users.containsKey(username) && users.get(username).equals(password)) {
                return username;
            } else {
                System.out.println("Неверный логин или пароль! Попробуйте снова.");
            }
        }
    }
}