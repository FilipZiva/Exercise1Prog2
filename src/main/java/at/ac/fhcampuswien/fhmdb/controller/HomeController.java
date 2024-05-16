package at.ac.fhcampuswien.fhmdb.controller;

import at.ac.fhcampuswien.fhmdb.exception.ApplicationException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.WatchlistMovie;
import at.ac.fhcampuswien.fhmdb.service.MovieApiService;
import at.ac.fhcampuswien.fhmdb.service.WatchlistService;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static at.ac.fhcampuswien.fhmdb.util.PopupUtil.showPopup;

@Getter
@Setter
public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox<Object> genreComboBox;
    @FXML
    public JFXComboBox<Object> yearComboBox;
    @FXML
    public JFXComboBox<Object> ratingComboBox;

    @FXML
    public JFXButton sortBtn;
    @FXML
    public JFXButton resetBtn;

    @FXML
    public Label homeLabel;

    @FXML
    public Label watchlistLabel;

    @Getter
    public List<Movie> allMovies;
    public MovieApiService movieApiService;
    private WatchlistService watchlistService;
    private boolean isInWatchlistView = false;

    ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initializeMovies();
            initializeUi();
            initializeGenre();
            initializeYear();
            initializeRating();
            handleResetButton();
            handleSortingMechanism();
            handleSearchbarFilter();
            handleNavigation();
        } catch (ApplicationException e) {
            showPopup(e);
        }
    }

    private void handleNavigation() throws ApplicationException {
        watchlistService = new WatchlistService();
        homeLabel.setOnMouseClicked(event -> {
            if (isInWatchlistView) {
                isInWatchlistView = false;
                try {
                    initializeMovies();
                    initializeUi();
                    initializeGenre();
                    initializeYear();
                    initializeRating();
                } catch (ApplicationException e) {
                    showPopup(e);
                }
            }
        });

        watchlistLabel.setOnMouseClicked(event -> {
            if (!isInWatchlistView) {
                isInWatchlistView = true;
                try {
                    loadWatchlist();
                } catch (ApplicationException e) {
                    showPopup(e);
                }
            }
        });
    }

    public void initializeUi() {
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(movieListView -> new MovieCell(watchlistService, isInWatchlistView));
    }

    public void initializeMovies() throws ApplicationException {
        observableMovies.clear();
        movieApiService = new MovieApiService();
        allMovies = movieApiService.getAllMovies();
        observableMovies.addAll(allMovies);
    }

    public void initializeGenre() {
        List<String> uniqueGenres = allMovies.stream()
                .flatMap(movie -> Arrays.stream(movie.getGenre().split(", ")))
                .distinct()
                .sorted()
                .toList();

        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().addAll(uniqueGenres);
    }

    public void initializeYear() {
        List<Integer> uniqueYears = allMovies.stream()
                .map(Movie::getReleaseYear)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        yearComboBox.setPromptText("Filter by Release Year");
        yearComboBox.getItems().setAll(uniqueYears);
    }

    public void initializeRating() {
        List<Double> uniqueRatings = allMovies.stream()
                .map(Movie::getRating)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        ratingComboBox.setPromptText("Filter by Rating");
        ratingComboBox.getItems().setAll(uniqueRatings);
    }

    private void handleSearchbarFilter() {
        searchBtn.setOnAction(actionEvent -> {
            try {
                applyFilters();
            } catch (ApplicationException e) {
                showPopup(e);
            }
        });
    }

    private void handleSortingMechanism() {
        sortBtn.setOnAction(actionEvent -> {
            if (sortBtn.getText().equals("Sort (asc)")) {
                filterMovieByTitleAscDesc(false);
                sortBtn.setText("Sort (desc)");
            } else {
                filterMovieByTitleAscDesc(true);
                sortBtn.setText("Sort (asc)");
            }
        });
    }

    private void handleResetButton() {
        resetBtn.setOnAction(actionEvent -> {
            searchField.setText("");
            genreComboBox.getSelectionModel().select(null);
            yearComboBox.getSelectionModel().select(null);
            ratingComboBox.getSelectionModel().select(null);
            observableMovies.setAll(allMovies);
        });
    }

    private void applyFilters() throws ApplicationException {
        String searchQuery = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        String selectedGenre = (genreComboBox.getValue() == null) ? "" : genreComboBox.getValue().toString();
        String selectedYear = (yearComboBox.getValue() == null) ? "" : yearComboBox.getValue().toString();
        String selectedRating = (ratingComboBox.getValue() == null) ? "" : ratingComboBox.getValue().toString();

        List<Movie> filteredMovies = filterMovieByQueryOrGenre(searchQuery, selectedGenre, selectedYear, selectedRating);
        observableMovies.setAll(filteredMovies);
    }

    public List<Movie> filterMovieByQueryOrGenre(String searchQuery, String selectedGenre, String selectedYear, String selectedRating) throws ApplicationException {
        StringBuilder queryBuilder = new StringBuilder();

        if (!searchQuery.isEmpty()) {
            queryBuilder.append("query=").append(searchQuery);
        }
        if (!selectedGenre.isEmpty()) {
            if (!queryBuilder.isEmpty()) {
                queryBuilder.append("&");
            }
            queryBuilder.append("genre=").append(selectedGenre);
        }
        if (!selectedYear.isEmpty()) {
            if (!queryBuilder.isEmpty()) {
                queryBuilder.append("&");
            }
            queryBuilder.append("releaseYear=").append(selectedYear);
        }
        if (!selectedRating.isEmpty()) {
            if (!queryBuilder.isEmpty()) {
                queryBuilder.append("&");
            }
            queryBuilder.append("ratingFrom=").append(selectedRating);
        }

        if (queryBuilder.isEmpty()) {
            return movieApiService.getAllMovies();
        }

        return movieApiService.getMoviesByQuery(queryBuilder.toString());
    }

    public void filterMovieByTitleAscDesc(boolean initialize) {
        if (!initialize) {
            FXCollections.sort(observableMovies, Comparator.comparing(Movie::getTitle));
        } else {
            FXCollections.sort(observableMovies, Comparator.comparing(Movie::getTitle).reversed());
        }
    }

    public String getMostPopularActor(List<Movie> movies) {
        Map<String, Long> actorFrequency = movies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        long maxFrequency = actorFrequency.values().stream()
                .max(Long::compare)
                .orElse(0L);

        return actorFrequency.entrySet().stream()
                .filter(entry -> entry.getValue() == maxFrequency)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining());
    }

    public int getLongestMovieTitle(List<Movie> movies) {
        return movies.stream()
                .mapToInt(movie -> movie.getTitle().length())
                .max()
                .orElse(0);
    }

    public long countMoviesFrom(List<Movie> movies, String director) {
        return movies.stream()
                .filter(movie -> movie.getDirectors().stream().anyMatch(d -> d.equals(director)))
                .count();
    }

    public List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        return movies.stream()
                .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                .collect(Collectors.toList());
    }

    private void loadWatchlist() throws ApplicationException {
        observableMovies.clear();
        List<WatchlistMovie> watchlistMovies = watchlistService.getWatchlist();
        List<Movie> movies = watchlistMovies.stream()
                .map(WatchlistMovie::getMovie)
                .collect(Collectors.toList());
        observableMovies.setAll(movies);
    }

}