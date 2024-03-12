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

    @ParameterizedTest
    @ValueSource(strings = {"ACTION", "COMEDY"})
    void testFilterByGenre_shouldReturnMoviesForSelectedGenre(String genre) {
        // Act
        List<Movie> result = homeController.filterMovieByQueryOrGenre("", genre);

        // Assert
        List<Movie> expectedMovies = homeController.allMovies.stream()
                .filter(movie -> movie.getGenre().contains(Genre.valueOf(genre)))
                .collect(Collectors.toList());
        assertThat(result).containsExactlyInAnyOrderElementsOf(expectedMovies);
    }

    @Test
    void testFilterByEmptyParameters_shouldReturnAllMovies() {
        // Act
        List<Movie> result = homeController.filterMovieByQueryOrGenre("", "");

        // Assert
        assertThat(result).hasSameSizeAs(homeController.allMovies);
    }

    @Test
    void testFilterByTitle_shouldReturnMatchingMovie_fullTitle() {
        // Arrange
        String title = "Inception";

        // Act
        List<Movie> result = homeController.filterMovieByQueryOrGenre(title.toLowerCase(), "");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).contains(title);
    }
    @Test
    void testFilterByDescription_shouldReturnMatchingMovie_fullDescription() {
        // Arrange
        String description = "dream-stealing";

        // Act
        List<Movie> result = homeController.filterMovieByQueryOrGenre(description.toLowerCase(), "");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDescription()).contains(description);
    }

    @Test
    void testFilterByTitle_shouldReturnMatchingMovies_partialTitle() {
        // Arrange
        String title = "In";

        // Act
        List<Movie> result = homeController.filterMovieByQueryOrGenre(title.toLowerCase(), "");

        // Assert
        assertThat(result).hasSize(4);
    }


    @Test
    void testFilterByNonexistentQuery_shouldReturnEmptyList() {
        // Act
        List<Movie> result = homeController.filterMovieByQueryOrGenre("NotFound", "");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void testFilterByPartialTitleAndGenre_shouldReturnMatchingMovie() {
        // Arrange
        String title = "In";
        Genre genre = Genre.ACTION;

        // Act
        List<Movie> result = homeController.filterMovieByQueryOrGenre(title.toLowerCase(), genre.toString());

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).contains(title);
        assertThat(result.get(0).getGenre()).contains(genre);
    }

    @Test
    void testFilterByDescriptionAndGenre_shouldReturnMatchingMovie() {
        // Arrange
        String description = "dream-stealing";
        Genre genre = Genre.SCIENCE_FICTION;

        // Act
        List<Movie> result = homeController.filterMovieByQueryOrGenre(description, genre.toString());

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDescription()).contains(description);
        assertThat(result.get(0).getGenre()).contains(genre);
    }


}


