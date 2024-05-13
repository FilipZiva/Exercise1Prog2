package at.ac.fhcampuswien.fhmdb.service;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieAPITest {
    MovieAPI movieAPI;
    @BeforeEach
    void setUp() {
        movieAPI = new MovieAPI();
    }

    @Test
    void testGetAllMoviesIntegration() {
        // Act
        List<Movie> movies = movieAPI.getAllMovies();

        // Assert
        assertNotNull(movies);
        assertFalse(movies.isEmpty());
        movies.forEach(movie -> {
            assertNotNull(movie.getApiId());
            assertNotNull(movie.getTitle());
        });
    }
}