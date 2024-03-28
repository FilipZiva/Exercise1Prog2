package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HomeControllerTest {

    private HomeController homeController;
    private List<Movie> dummyMovies;

    @BeforeEach
    public void setUp() {
        ObservableList<Movie> observableMovies = FXCollections.observableArrayList();
        homeController = new HomeController();
        homeController.observableMovies = observableMovies;
        homeController.initializeMovies();


        dummyMovies = Arrays.asList(
                new Movie("movie_1_id", "abcdefghijklm", "...", Arrays.asList("Action"), 2012, "...", 120,
                        Arrays.asList("Director 1"), Arrays.asList("Writer 1", "Writer 2"),
                        Arrays.asList("Actor 1", "Actor 2"), 3.5),
                new Movie("movie_2_id", "Movie 23333", "...", Arrays.asList("Comedy"), 2022, "...", 100,
                        Arrays.asList("Director 2","Director 1"), Arrays.asList("Writer 3"),
                        Arrays.asList("Actor 2", "Actor 3", "Actor 2"), 4.2),
                new Movie("movie_3_id", "Movie 322", "...", Arrays.asList("Drama"), 2004, "...", 90,
                        Arrays.asList("Director 3"), Arrays.asList("Writer 4"),
                        Arrays.asList("Actor 1"), 2.8)
        );

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
        List<Movie> result = homeController.filterMovieByQueryOrGenre("", genre, "", "");

        // Assert
        List<Movie> expectedMovies = homeController.allMovies.stream()
                .filter(movie -> movie.getGenre().contains(genre))
                .collect(Collectors.toList());
        assertThat(result).hasSameSizeAs(expectedMovies);
    }

    @Test
    void testFilterByEmptyParameters_shouldReturnAllMovies() {
        // Act
        List<Movie> result = homeController.filterMovieByQueryOrGenre("", "", "", "");

        // Assert
        assertThat(result).hasSameSizeAs(homeController.allMovies);
    }

    @Test
    void testFilterByTitle_shouldReturnMatchingMovie_fullTitle() {
        // Arrange
        String title = "Inception";

        // Act
        List<Movie> result = homeController.filterMovieByQueryOrGenre(title, "", "", "");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).contains(title);
    }

    void testFilterByDescription_shouldReturnMatchingMovie_fullDescription() {
        // Arrange
        String description = "dream-sharing";

        // Act
        List<Movie> result = homeController.filterMovieByQueryOrGenre(description.toLowerCase(), "", "", "");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDescription()).contains(description);
    }

    @Test
    void testFilterByTitle_shouldReturnMatchingMovies_partialTitle() {
        // Arrange
        String title = "Inc";

        // Act
        List<Movie> result = homeController.filterMovieByQueryOrGenre(title, "", "", "");

        // Assert
        assertThat(result).hasSize(1);
    }


    @Test
    void testFilterByNonexistentQuery_shouldReturnEmptyList() {
        // Act
        List<Movie> result = homeController.filterMovieByQueryOrGenre("NotFound", "", "", "");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void testFilterByPartialTitleAndGenre_shouldReturnMatchingMovie() {
        // Arrange
        String title = "Inc";
        String genre = "ACTION";

        // Act
        List<Movie> result = homeController.filterMovieByQueryOrGenre(title.toLowerCase(), genre, "", "");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).contains(title);
        assertThat(result.get(0).getGenre()).contains(genre);
    }

    void testFilterByDescriptionAndGenre_shouldReturnMatchingMovie() {
        // Arrange
        String description = "The aging patriarch";
        String genre = "DRAMA";

        // Act
        List<Movie> result = homeController.filterMovieByQueryOrGenre(description.toLowerCase(), genre, "", "");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDescription()).contains(description);
        assertThat(result.get(0).getGenre()).contains(genre);
    }

    @Test
    void testFilterByYearOnly_shouldReturnMoviesFromThatYear() {
        // Arrange
        int year = 1999;

        // Act
        List<Movie> result = homeController.filterMovieByQueryOrGenre("", "", String.valueOf(year), "");

        // Assert
        assertThat(result).isNotEmpty();
        result.forEach(movie -> assertThat(movie.getReleaseYear()).isEqualTo(year));
    }

    @Test
    void testFilterByRatingOnly_shouldReturnMoviesWithEqualOrHigherRating() {
        // Arrange
        double rating = 8.0;

        // Act
        List<Movie> result = homeController.filterMovieByQueryOrGenre("", "", "", String.valueOf(rating));

        // Assert
        assertThat(result).isNotEmpty();
        result.forEach(movie -> assertThat(movie.getRating()).isGreaterThanOrEqualTo(rating));
    }

    @Test
    void testFilterWithAllParameters_shouldReturnMatchingMovies() {
        // Arrange
        String title = "The Godfather";
        String genre = "DRAMA";
        int year = 1972;
        double rating = 9.2;

        // Act
        List<Movie> result = homeController.filterMovieByQueryOrGenre(title.toLowerCase(), genre, String.valueOf(year), String.valueOf(rating));

        // Assert
        assertThat(result).hasSize(1);
        Movie movie = result.get(0);
        assertThat(movie.getTitle()).containsIgnoringCase(title);
        assertThat(movie.getGenre()).contains(genre);
        assertThat(movie.getReleaseYear()).isEqualTo(year);
        assertThat(movie.getRating()).isGreaterThanOrEqualTo(rating);
    }


    @Test
    void getMostPopularActor_ShouldReturnEmpty_WhenMoviesListIsEmpty() {
        // Act
        String mostPopularActor = homeController.getMostPopularActor(Collections.emptyList());

        // Assert
        assertEquals("", mostPopularActor);
    }

    @Test
    void getMostPopularActor_ShouldReturnCorrectActor_WhenListIsNotEmpty() {
        // Arrange
        String expectedPopularActor = "Actor 2";
        // Act
        String mostPopularActor = homeController.getMostPopularActor(dummyMovies);
        // Assert
        assertEquals(expectedPopularActor, mostPopularActor);
    }


    @Test
    void getLongestMovieTitle_ShouldReturnZero_WhenMoviesListIsEmpty() {
        // Arrange
        int expectedResult = 0;
        // Act
        int result = homeController.getLongestMovieTitle(Collections.emptyList());
        // Assert
        assertEquals(expectedResult, result);
    }
    @Test
    void getLongestMovieTitle_ShouldReturnLengthOfLongestTitle() {
        assertEquals(dummyMovies.get(0).getTitle().length(), homeController.getLongestMovieTitle(dummyMovies));
    }

    @Test
    void countMoviesFrom_ShouldReturnCorrectCount_WhenDirectorIsSpecified() {
        assertEquals(2, homeController.countMoviesFrom(dummyMovies,"Director 1"));
    }

    @Test
    void getMoviesBetweenYears() {
        // Arrange
        int startyear = 2000;
        int endyear = 2015;

        // Act
        List<Movie> result = homeController.getMoviesBetweenYears(dummyMovies,startyear,endyear);

        // Assert
        assertThat(result).hasSize(2);
    }
}


