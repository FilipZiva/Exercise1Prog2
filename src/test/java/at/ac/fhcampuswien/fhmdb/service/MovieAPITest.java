package at.ac.fhcampuswien.fhmdb.service;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieAPITest {

    @Test
    void testGetAllMoviesIntegration() {
        // Act
        List<Movie> movies = MovieAPI.getAllMovies();

        // Assert
        assertNotNull(movies);
        assertFalse(movies.isEmpty());
        movies.forEach(movie -> {
            assertNotNull(movie.getId());
            assertNotNull(movie.getTitle());
        });
    }
}