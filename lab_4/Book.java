import java.util.Objects;

public class Book {
    private String title; // название книги
    private String author; // автор книги
    private int year; // год издания

    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    @Override
    public String toString() {
        return author + " '" + title + "'" + "(" + year + " г)";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return year == book.year &&
                Objects.equals(title, book.title) &&
                Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, year);
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }
}