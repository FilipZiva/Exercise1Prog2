package at.ac.fhcampuswien.fhmdb.dao;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.WatchlistMovie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WatchlistRepositoryTest {
    private WatchlistRepository watchlistRepository;
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        List<Movie> dummyMovies = Arrays.asList(
                new Movie("movie_1_id", "abcdefghijklm", "...", String.valueOf(Arrays.asList("Action")), 2012, "...", 120,
                        Arrays.asList("Director 1"), Arrays.asList("Writer 1", "Writer 2"),
                        Arrays.asList("Actor 1", "Actor 2"), 3.5),
                new Movie("movie_2_id", "Movie 23333", "...", String.valueOf(Arrays.asList("Comedy")), 2022, "...", 100,
                        Arrays.asList("Director 2","Director 1"), Arrays.asList("Writer 3"),
                        Arrays.asList("Actor 2", "Actor 3", "Actor 2"), 4.2),
                new Movie("movie_3_id", "Movie 322", "...", String.valueOf(Arrays.asList("Drama")), 2004, "...", 90,
                        Arrays.asList("Director 3"), Arrays.asList("Writer 4"),
                        Arrays.asList("Actor 1"), 2.8)
        );

        movieRepository = new MovieRepository();
        watchlistRepository = new WatchlistRepository();

        // Alle Filme hinzufügen
        movieRepository.addAllMovies(dummyMovies);

        // WatchlistMovie Einträge erstellen und hinzufügen
        for (Movie movie : dummyMovies) {
            WatchlistMovie watchlistMovie = new WatchlistMovie();
            watchlistMovie.setMovie(movie);
            watchlistRepository.addToWatchlist(watchlistMovie);
        }
    }

    @AfterEach
    void tearDown() {
        // Alle WatchlistMovie Einträge entfernen
        List<WatchlistMovie> watchlistMovies = watchlistRepository.getWatchlist();
        if (watchlistMovies != null) {
            for (WatchlistMovie movie : watchlistMovies) {
                watchlistRepository.removeFromWatchlist(movie.getMovie().getApiId());
            }
        }

        // Alle Movie Einträge entfernen
        movieRepository.removeAllMovies();
    }

    @Test
    void testGetWatchlist() {
        // Act
        List<WatchlistMovie> movies = watchlistRepository.getWatchlist();
        // Assert
        assertNotNull(movies);
        assertEquals(3, movies.size());
    }

    @Test
    void testAddToWatchlist() {
        // Arrange
        Movie newMovie = new Movie("movie_4_id", "Movie 322", "...", String.valueOf(Arrays.asList("Drama")), 2004, "...", 90,
                Arrays.asList("Director 3"), Arrays.asList("Writer 4"),
                Arrays.asList("Actor 1"), 2.8);
        movieRepository.addAllMovies(Arrays.asList(newMovie));

        WatchlistMovie newWatchlistMovie = new WatchlistMovie();
        newWatchlistMovie.setMovie(newMovie);
        // Act
        int result = watchlistRepository.addToWatchlist(newWatchlistMovie);
        List<WatchlistMovie> movies = watchlistRepository.getWatchlist();
        // Assert
        assertEquals(1, result);
        assertEquals(4, movies.size());
    }

    @Test
    void testRemoveFromWatchlist() {
        // Act
        int removedCount = watchlistRepository.removeFromWatchlist("movie_2_id");
        // Assert
        assertEquals(1, removedCount);
        assertEquals(2, watchlistRepository.getWatchlist().size());
    }
}