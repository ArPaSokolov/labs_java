import java.util.*;
import java.util.stream.Collectors;

public class Library {
    private List<Book> books; // все книги в библиотеке
    private Set<String> authors; // уникальные авторы
    private Map<String, Integer> authorStatistics; // подсчет количества книг у автора

    public Library() {
        books = new ArrayList<>();
        authors = new HashSet<>();
        authorStatistics = new HashMap<>();
    }

    public void addBook(Book book) {
        books.add(book);
        authors.add(book.getAuthor());
        authorStatistics.put(book.getAuthor(), 
            authorStatistics.getOrDefault(book.getAuthor(), 0) + 1);
    }

    public void removeBook(Book book) {
        if (books.remove(book)) {
            // Есть ли еще книги этого автора
            boolean authorHasMoreBooks = books.stream().anyMatch(b -> b.getAuthor().equals(book.getAuthor())); 
            
            if (!authorHasMoreBooks) {
                authors.remove(book.getAuthor());
            }
            
            authorStatistics.put(book.getAuthor(), 
                authorStatistics.getOrDefault(book.getAuthor(), 0) - 1);
            
            if (authorStatistics.get(book.getAuthor()) <= 0) {
                authorStatistics.remove(book.getAuthor());
            }
        }
    }

    public List<Book> findBooksByAuthor(String author) {
        return books.stream()
                .filter(book -> book.getAuthor().equals(author))
                .collect(Collectors.toList());
    }

    public List<Book> findBooksByYear(int year) {
        return books.stream()
                .filter(book -> book.getYear() == year)
                .collect(Collectors.toList());
    }

    public void printAllBooks() {;
        books.forEach(System.out::println);
    }

    public void printUniqueAuthors() {;
        authors.forEach(System.out::println);
    }

    public void printAuthorStatistics() {;
        authorStatistics.forEach((author, count) -> 
            System.out.println(author + ": " + count));
    }
}