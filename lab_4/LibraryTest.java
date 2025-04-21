import java.util.*;

public class LibraryTest {
    public static void main(String[] args) {
        Library library = new Library();

        // Создаем книги
        Book book1 = new Book("Морожены волки", "Писахов Степан Григорьевич", 1938);
        Book book2 = new Book("Реквием каравану PQ-17", "Пикуль Валентин Саввич", 1970);
        Book book3 = new Book("Степан Разин", "Чапыгин Алексей Павлович",  1927);
        Book book4 = new Book("Как я делал эту лабу", "Соколов Арсений Павлович", 2025);
        Book book5 = new Book("Незнайка на Луне", "Носов Николай Николаевич", 1964);
        Book book6 = new Book("Волшебник Изумрудного города", "Волков Александр Мелентьевич", 1939);
        Book book7 = new Book("Архангельские сказки", "Писахов Степан Григорьевич", 1924);


        // Добавляем книги в библиотеку
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);
        library.addBook(book4);
        library.addBook(book5);
        library.addBook(book6);
        library.addBook(book7);

        // Тестируем методы
        System.out.println("================ Коллекция книг ===============");
        library.printAllBooks();
        System.out.println();

        System.out.println("===================  Авторы  ==================");
        library.printUniqueAuthors();
        System.out.println();

        System.out.println("=== Статистика по количеству книг у автора  ===");
        library.printAuthorStatistics();
        System.out.println();

        System.out.println("============== Произведения Писахова ==========");
        List<Book> authorBooks = library.findBooksByAuthor("Писахов Степан Григорьевич");
        authorBooks.forEach(System.out::println);
        System.out.println();

        System.out.println("============== Книги вышедшие с 1950 ==========");
        List<Book> booksFrom1950 = new ArrayList<>();

        for (int year = 1950; year <= 2025; year++) {
            booksFrom1950.addAll(library.findBooksByYear(year));
        }

        if (booksFrom1950.isEmpty()) {
            System.out.println("Книг, выпущенных с 1950 года, не найдено.");
        } else {
            booksFrom1950.forEach(book -> System.out.println(book));
        }
        System.out.println();

        // Удаляем книгу и проверяем изменения
        System.out.println("======= Удаляем 'Как я делал эту лабу' ========");
        library.removeBook(book4);
        library.printAllBooks();
        System.out.println();
        library.printAuthorStatistics();
    }
}