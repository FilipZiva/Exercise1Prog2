package at.ac.fhcampuswien.fhmdb.dao;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieRepositoryTest {
    private MovieRepository repository;
    List<Movie> dummyMovies = Arrays.asList(
            new Movie("movie_1_id", "abcdefghijklm", "...", "Action", 2012, 120,"...", 3.5),
            new Movie("movie_2_id", "Movie 23333", "...", "Comedy", 2022,100, "...", 4.2),
            new Movie("movie_3_id", "Movie 322", "...", "Drama", 2004, 90,"...", 2.8)
    );

    @BeforeEach
    void setUp() {
        repository = new MovieRepository();
        repository.addAllMovies(dummyMovies);
    }
    @AfterEach
    void tearDown() {
        repository.removeAllMovies();
    }

    @Test
    void testGetAllMovies() {
        // Act
        List<Movie> movies = repository.getAllMovies();
        // Assert
        assertNotNull(movies);
        assertEquals(3, movies.size());
    }

    @Test
    void testRemoveAllMovies() {
        // Act
        int removedCount = repository.removeAllMovies();
        // Assert
        assertEquals(3, removedCount);
        assertTrue(repository.getAllMovies().isEmpty());
    }

    @Test
    void testGetMovie() {
        // Act
        Movie movie = repository.getMovie(dummyMovies.get(1));
        // Assert
        assertNotNull(movie);
        assertEquals("Movie 23333", movie.getTitle());
    }

    @Test
    void testAddAllMovies() {
        // Arrange
        assertEquals(3, repository.getAllMovies().size());
        List<Movie> newMovies = Arrays.asList(
                new Movie("movie_4_id", "New Film", "...", "Sci-Fi", 2020, 130, "...", 4.5)
        );
        // Act
        repository.addAllMovies(newMovies);
        // Assert
        assertEquals(4, repository.getAllMovies().size());
    }
}