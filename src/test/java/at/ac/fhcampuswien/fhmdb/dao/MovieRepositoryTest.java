package at.ac.fhcampuswien.fhmdb.dao;

import at.ac.fhcampuswien.fhmdb.exception.ApplicationException;
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
            new Movie("movie_1_id", "abcdefghijklm", "...", String.valueOf(List.of("Action")), 2012, "...", 120,
                    List.of("Director 1"), Arrays.asList("Writer 1", "Writer 2"),
                    Arrays.asList("Actor 1", "Actor 2"), 3.5),
            new Movie("movie_2_id", "Movie 23333", "...", String.valueOf(List.of("Comedy")), 2022, "...", 100,
                    Arrays.asList("Director 2","Director 1"), List.of("Writer 3"),
                    Arrays.asList("Actor 2", "Actor 3", "Actor 2"), 4.2),
            new Movie("movie_3_id", "Movie 322", "...", String.valueOf(List.of("Drama")), 2004, "...", 90,
                    List.of("Director 3"), List.of("Writer 4"),
                    List.of("Actor 1"), 2.8)
    );

    @BeforeEach
    void setUp() throws ApplicationException {
        repository = new MovieRepository();
        repository.removeAllMovies();
        repository.addAllMovies(dummyMovies);
    }
    @AfterEach
    void tearDown() throws ApplicationException {
        MovieRepository.instance = null;
        repository.removeAllMovies();
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
    void testGetAllMovies() throws ApplicationException {
        // Act
        List<Movie> movies = repository.getAllMovies();
        // Assert
        assertNotNull(movies);
        assertEquals(3, movies.size());
    }

    @Test
    void testRemoveAllMovies() throws ApplicationException {
        // Act
        int removedCount = repository.removeAllMovies();
        // Assert
        assertEquals(3, removedCount);
        assertTrue(repository.getAllMovies().isEmpty());
    }

    @Test
    void testGetMovie() throws ApplicationException {
        // Act
        Movie movie = repository.getMovie(dummyMovies.get(1));
        // Assert
        assertNotNull(movie);
        assertEquals("Movie 23333", movie.getTitle());
    }

    @Test
    void testAddAllMovies() throws ApplicationException {
        // Arrange
        assertEquals(3, repository.getAllMovies().size());
        List<Movie> newMovies = List.of(
                new Movie("movie_4_id", "Movie 322", "...", String.valueOf(List.of("Drama")), 2004, "...", 90,
                        List.of("Director 3"), List.of("Writer 4"),
                        List.of("Actor 1"), 2.8)
        );
        // Act
        repository.addAllMovies(newMovies);
        // Assert
        assertEquals(4, repository.getAllMovies().size());
    }
}