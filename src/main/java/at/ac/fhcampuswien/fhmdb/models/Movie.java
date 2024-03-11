package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Movie {
    private final String title;
    private final String description;
    private final List<Genre> genre;
    public Movie(String title, String description, List<Genre> genre) {
        this.title = title;
        this.description = description;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Genre> getGenre() {
        return genre;
    }

    public static List<Movie> initializeMovies() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Inception", "A dream-stealing thief attempts a risky mind-plant.", Arrays.asList(Genre.ACTION, Genre.SCIENCE_FICTION, Genre.THRILLER)));
        movies.add(new Movie("The Grand Budapest Hotel", "A concierge and a lobby boy solve a murder mystery.", Arrays.asList(Genre.COMEDY, Genre.ADVENTURE, Genre.CRIME)));
        movies.add(new Movie("Interstellar", "Astronauts venture through a wormhole to find a new home for humanity.", Arrays.asList(Genre.ADVENTURE, Genre.DRAMA, Genre.SCIENCE_FICTION)));
        movies.add(new Movie("The Silence of the Lambs", "An FBI trainee seeks help from an imprisoned cannibal to catch a killer.", Arrays.asList(Genre.CRIME, Genre.DRAMA, Genre.THRILLER)));
        movies.add(new Movie("La La Land", "Two artists fall in love while pursuing their dreams in Los Angeles.", Arrays.asList(Genre.DRAMA, Genre.MUSICAL, Genre.ROMANCE)));

        return movies;
    }

}
