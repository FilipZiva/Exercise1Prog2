package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class HomeControllerTest {

    private HomeController homeController;

    @BeforeEach
    public void setUp() {
        ObservableList<Movie> observableMovies = FXCollections.observableArrayList();
        homeController = new HomeController();
        homeController.observableMovies = observableMovies;
        homeController.initializeMovies();

    }

    @ParameterizedTest
    @ValueSource(strings = {"ACTION", "COMEDY"})
    void testFilterMoviesByGenre(String val) {
        // Act
        homeController.filterMoviesByGenre(val);

        // Assert
        List<Movie> expectedMovies = homeController.allMovies.stream()
                .filter(movie -> movie.getGenre().contains(Genre.valueOf(val)))
                .collect(Collectors.toList());
        assertThat(homeController.observableMovies).containsExactlyElementsOf(expectedMovies);
    }

    @Test
    void testFilterMoviesByGenreWithNullGenre() {
        // Act
        homeController.filterMoviesByGenre(null);

        // Assert
        assertThat(homeController.observableMovies).hasSameSizeAs(homeController.allMovies);
    }


    @Test
    void testInitializeMovies() {
        // Assert
        assertThat(homeController.observableMovies).hasSize(5);
    }


    @Test
    void testSortMoviesAscending() {
        // Act
        homeController.filterMovieByTitleAscDesc(false);

        // Assert
        List<Movie> sortedMovies = homeController.getAllMovies().stream()
                .sorted(Comparator.comparing(Movie::getTitle))
                .collect(Collectors.toList());
        assertThat(homeController.observableMovies).containsExactlyElementsOf(sortedMovies);
    }

    @Test
    void testSortMoviesDescending() {
        // Arrange
        homeController.filterMovieByTitleAscDesc(false);

        // Act
        homeController.filterMovieByTitleAscDesc(true);

        // Assert
        List<Movie> sortedMovies = homeController.getAllMovies().stream()
                .sorted(Comparator.comparing(Movie::getTitle).reversed())
                .collect(Collectors.toList());
        assertThat(homeController.observableMovies).containsExactlyElementsOf(sortedMovies);
    }


}