package at.ac.fhcampuswien.fhmdb.dao;

import at.ac.fhcampuswien.fhmdb.exception.ApplicationException;
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
    void setUp() throws ApplicationException {
        List<Movie> dummyMovies = Arrays.asList(
                new Movie("movie_1_id", "abcdefghijklm", "...", String.valueOf(List.of("Action")), 2012, "...", 120,
                        List.of("Director 1"), Arrays.asList("Writer 1", "Writer 2"),
                        Arrays.asList("Actor 1", "Actor 2"), 3.5),
                new Movie("movie_2_id", "Movie 23333", "...", String.valueOf(List.of("Comedy")), 2022, "...", 100,
                        Arrays.asList("Director 2", "Director 1"), List.of("Writer 3"),
                        Arrays.asList("Actor 2", "Actor 3", "Actor 2"), 4.2),
                new Movie("movie_3_id", "Movie 322", "...", String.valueOf(List.of("Drama")), 2004, "...", 90,
                        List.of("Director 3"), List.of("Writer 4"),
                        List.of("Actor 1"), 2.8)
        );

        movieRepository = new MovieRepository();
        watchlistRepository = new WatchlistRepository();

        movieRepository.addAllMovies(dummyMovies);

        for (Movie movie : dummyMovies) {
            WatchlistMovie watchlistMovie = new WatchlistMovie();
            watchlistMovie.setMovie(movie);
            watchlistRepository.addToWatchlist(watchlistMovie);
        }
    }

    @AfterEach
    void tearDown() throws ApplicationException {
        MovieRepository.instance = null;

        List<WatchlistMovie> watchlistMovies = watchlistRepository.getWatchlist();
        if (watchlistMovies != null) {
            for (WatchlistMovie movie : watchlistMovies) {
                watchlistRepository.removeFromWatchlist(movie.getMovie().getApiId());
            }
        }
        movieRepository.removeAllMovies();
    }


    @Test
    void testSingletonInstance() throws ApplicationException {
        // Arrange
        MovieRepository firstInstance = MovieRepository.getInstance();

        // Act
        MovieRepository secondInstance = MovieRepository.getInstance();

        // Assert
        assertSame(firstInstance, secondInstance);
    }


    @Test
    void testGetWatchlist() throws ApplicationException {
        // Act
        List<WatchlistMovie> movies = watchlistRepository.getWatchlist();
        // Assert
        assertNotNull(movies);
        assertEquals(3, movies.size());
    }

    @Test
    void testAddToWatchlist() throws ApplicationException {
        // Arrange
        Movie newMovie = new Movie("movie_4_id", "Movie 322", "...", String.valueOf(List.of("Drama")), 2004, "...", 90,
                List.of("Director 3"), List.of("Writer 4"),
                List.of("Actor 1"), 2.8);
        movieRepository.addAllMovies(List.of(newMovie));

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
    void testRemoveFromWatchlist() throws ApplicationException {
        // Act
        int removedCount = watchlistRepository.removeFromWatchlist("movie_2_id");
        // Assert
        assertEquals(1, removedCount);
        assertEquals(2, watchlistRepository.getWatchlist().size());
    }
}