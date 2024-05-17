package at.ac.fhcampuswien.fhmdb.service;

import at.ac.fhcampuswien.fhmdb.config.ApplicationConfig;
import at.ac.fhcampuswien.fhmdb.exception.ApplicationException;
import at.ac.fhcampuswien.fhmdb.exception.ErrorCodes;
import at.ac.fhcampuswien.fhmdb.exception.ExceptionType;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieApiServiceTest {
    private static final String INVALID_API_ENDPOINT = "http://invalid-endpoint";
    private static final String MALFORMED_JSON = "[{\"id\":\"1\",\"title\":\"Movie 1\",\"description\":\"Description 1\"}]";
    private MovieApiService movieApiService;

    @BeforeEach
    void setUp() throws ApplicationException {
        movieApiService = new MovieApiService();
    }

    @Test
    void testGetAllMoviesIntegration() throws ApplicationException {
        // Act
        List<Movie> movies = movieApiService.getAllMovies(ApplicationConfig.API_ENDPOINT);

        // Assert
        assertNotNull(movies);
        assertFalse(movies.isEmpty());
        movies.forEach(movie -> {
            assertNotNull(movie.getApiId());
            assertNotNull(movie.getTitle());
        });
    }

    @Test
    void testGetMoviesByQueryInvalidEndpoint() {
        // Act & Assert
        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            movieApiService.getMoviesByQuery("query=test", INVALID_API_ENDPOINT);
        });

        assertEquals(ExceptionType.MOVIE_API_EXCEPTION, exception.getType());
        assertEquals(ErrorCodes.API_RESPONSE_ERROR.getCode(), exception.getErrorCode());
    }

    @Test
    void testGetMovieList() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode validJsonNode = mapper.readTree("[{" +
                "\"id\":\"1\"," +
                "\"title\":\"Movie 1\"," +
                "\"description\":\"Description 1\"," +
                "\"genres\":[\"Action\",\"Drama\"]," +
                "\"releaseYear\":2020," +
                "\"imgUrl\":\"http://example.com/img1.jpg\"," +
                "\"lengthInMinutes\":120," +
                "\"directors\":[\"Director 1\"]," +
                "\"writers\":[\"Writer 1\"]," +
                "\"mainCast\":[\"Actor 1\"]," +
                "\"rating\":8.5" +
                "}]");

        // Act
        List<Movie> movies = movieApiService.getMovieList(validJsonNode);

        // Assert
        assertNotNull(movies);
        assertEquals(1, movies.size());
        Movie movie = movies.get(0);
        assertEquals("1", movie.getApiId());
        assertEquals("Movie 1", movie.getTitle());
        assertEquals("Description 1", movie.getDescription());
        assertEquals("Action, Drama", movie.getGenre());
        assertEquals(2020, movie.getReleaseYear());
        assertEquals("http://example.com/img1.jpg", movie.getImgUrl());
        assertEquals(120, movie.getLengthInMinutes());
        assertEquals(List.of("Director 1"), movie.getDirectors());
        assertEquals(List.of("Writer 1"), movie.getWriters());
        assertEquals(List.of("Actor 1"), movie.getMainCast());
        assertEquals(8.5, movie.getRating(), 0.01);
    }

    @Test
    void testGetMovieListExceptionHandling() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode malformedJsonNode;
        try {
            malformedJsonNode = mapper.readTree(MALFORMED_JSON);
        } catch (Exception e) {
            malformedJsonNode = null; // Simulate malformed JSON
        }

        // Act & Assert
        JsonNode finalMalformedJsonNode = malformedJsonNode;
        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            movieApiService.getMovieList(finalMalformedJsonNode);
        });

        assertEquals(ExceptionType.MOVIE_API_EXCEPTION, exception.getType());
        assertEquals(ErrorCodes.JSON_PROCESSING_ERROR.getCode(), exception.getErrorCode());
    }
}
