package models;

public class Movie {
    private String movieTitle;
    private String length;

    public Movie(String movieTitle, String length) {
        this.movieTitle = movieTitle;
        this.length = length;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getLength() {
        return length;
    }

    @Override
    public String toString() {
        return movieTitle + " (" + length + ")";
    }
}