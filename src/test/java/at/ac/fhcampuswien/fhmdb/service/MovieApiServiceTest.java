package at.ac.fhcampuswien.fhmdb.service;

import at.ac.fhcampuswien.fhmdb.exception.ApplicationException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieApiServiceTest {
    MovieApiService movieApiService;
    @BeforeEach
    void setUp() throws ApplicationException {
        movieApiService = new MovieApiService();
    }

    @Test
    void testGetAllMoviesIntegration() throws ApplicationException {
        // Act
        List<Movie> movies = movieApiService.getAllMovies();

        // Assert
        assertNotNull(movies);
        assertFalse(movies.isEmpty());
        movies.forEach(movie -> {
            assertNotNull(movie.getApiId());
            assertNotNull(movie.getTitle());
        });
    }
}