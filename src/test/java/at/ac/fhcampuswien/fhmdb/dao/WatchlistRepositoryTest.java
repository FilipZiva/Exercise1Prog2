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
    private WatchlistRepository repository;

    @BeforeEach
    void setUp() {
        List<Movie> dummyMovies = Arrays.asList(
                new Movie("api_1_id", "Movie 1", "...", "Action", 2012, 120, "...", 3.5),
                new Movie("api_2_id", "Movie 2", "...", "Comedy", 2022, 100, "...", 4.2),
                new Movie("api_3_id", "Movie 3", "...", "Drama", 2004, 90, "...", 2.8)
        );
        List<WatchlistMovie> dummyWatchlistMovies = Arrays.asList(
                new WatchlistMovie(1, "api_1_id", dummyMovies.get(0)),
                new WatchlistMovie(2, "api_2_id", dummyMovies.get(1)),
                new WatchlistMovie(3, "api_3_id", dummyMovies.get(2))
        );

        repository = new WatchlistRepository();
        for (WatchlistMovie movie : dummyWatchlistMovies) {
            repository.addToWatchlist(movie);
        }
    }

    @AfterEach
    void tearDown() {
        List<WatchlistMovie> watchlistMovies = repository.getWatchlist();
        for (WatchlistMovie movie : watchlistMovies) {
            repository.removeFromWatchlist(movie.getApiId());
        }
    }

    @Test
    void testGetWatchlist() {
        // Act
        List<WatchlistMovie> movies = repository.getWatchlist();
        // Assert
        assertNotNull(movies);
        assertEquals(3, movies.size());
    }

    @Test
    void testAddToWatchlist() {
        // Arrange
        Movie newMovie = new Movie("api_4_id", "New Movie", "...", "Sci-Fi", 2020, 130, "...", 4.5);
        WatchlistMovie newWatchlistMovie = new WatchlistMovie(4, "api_4_id", newMovie);
        // Act
        int result = repository.addToWatchlist(newWatchlistMovie);
        List<WatchlistMovie> movies = repository.getWatchlist();
        // Assert
        assertEquals(1, result);
        assertEquals(4, movies.size());
    }

    @Test
    void testRemoveFromWatchlist() {
        // Act
        int removedCount = repository.removeFromWatchlist("api_2_id");
        // Assert
        assertEquals(1, removedCount);
        assertEquals(2, repository.getWatchlist().size());
    }
}