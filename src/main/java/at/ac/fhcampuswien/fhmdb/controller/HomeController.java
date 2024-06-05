package at.ac.fhcampuswien.fhmdb.controller;

import at.ac.fhcampuswien.fhmdb.config.ApplicationConfig;
import at.ac.fhcampuswien.fhmdb.dao.Observer;
import at.ac.fhcampuswien.fhmdb.dao.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.exception.ApplicationException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.WatchlistMovie;
import at.ac.fhcampuswien.fhmdb.service.MovieApiService;
import at.ac.fhcampuswien.fhmdb.service.WatchlistService;
import at.ac.fhcampuswien.fhmdb.ui.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import at.ac.fhcampuswien.fhmdb.util.PopupUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
public class HomeController implements Initializable, Observer<String> {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView<Movie> movieListView;

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

    @FXML
    public HBox filterContainer;

    public List<Movie> allMovies;
    public MovieApiService movieApiService;
    private WatchlistService watchlistService;
    private boolean isInWatchlistView = false;

    ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    private SortContext sortContext;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sortContext = new SortContext();
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

            WatchlistRepository.getInstance().registerObserver(this);
        } catch (ApplicationException e) {
            PopupUtil.showPopup(e);
        }
    }

    @Override
    public void update(String message) {
        PopupUtil.showPopup(message);
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
                    setFilterVisibility(true);
                } catch (ApplicationException e) {
                    PopupUtil.showPopup(e);
                }
            }
        });

        watchlistLabel.setOnMouseClicked(event -> {
            if (!isInWatchlistView) {
                isInWatchlistView = true;
                try {
                    loadWatchlist();
                    setFilterVisibility(false);
                } catch (ApplicationException e) {
                    PopupUtil.showPopup(e);
                }
            }
        });
    }

    public void initializeUi() {
        movieListView.setItems(observableMovies);

        ClickEventHandler<Movie> addToWatchlistClicked = clickedMovie -> {
            try {
                WatchlistMovie watchlistMovie = new WatchlistMovie();
                watchlistMovie.setMovie(clickedMovie);
                WatchlistRepository.getInstance().addToWatchlist(watchlistMovie);
            } catch (ApplicationException ex) {
                PopupUtil.showPopup(ex);
            }
        };

        ClickEventHandler<Movie> removeFromWatchlistClicked = clickedMovie -> {
            try {
                watchlistService.removeFromWatchlist(clickedMovie.getApiId());
                observableMovies.remove(clickedMovie);
            } catch (ApplicationException ex) {
                PopupUtil.showPopup(ex);
            }
        };

        movieListView.setCellFactory(movieListView -> new MovieCell(isInWatchlistView, addToWatchlistClicked, removeFromWatchlistClicked));
    }

    public void initializeMovies() throws ApplicationException {
        observableMovies.clear();
        movieApiService = new MovieApiService();
        allMovies = movieApiService.getAllMovies(ApplicationConfig.API_ENDPOINT);
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
                PopupUtil.showPopup(e);
            }
        });
    }

    private void handleSortingMechanism() {
        sortBtn.setOnAction(actionEvent -> {
            sortContext.nextState();
            updateSortButtonText();
            sortContext.sort(observableMovies);
        });
    }

    private void updateSortButtonText() {
        if (sortContext.getCurrentState() instanceof NotSortedState) {
            sortBtn.setText("Not sorted");
        } else if (sortContext.getCurrentState() instanceof AscendingSortState) {
            sortBtn.setText("Sort (asc)");
        } else if (sortContext.getCurrentState() instanceof DescendingSortState) {
            sortBtn.setText("Sort (desc)");
        }
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
        sortContext.sort(observableMovies);
    }

    public List<Movie> filterMovieByQueryOrGenre(String searchQuery, String selectedGenre, String selectedYear, String selectedRating) throws ApplicationException {
        if (searchQuery.isEmpty() && selectedGenre.isEmpty() && selectedYear.isEmpty() && selectedRating.isEmpty()) {
            return movieApiService.getAllMovies(ApplicationConfig.API_ENDPOINT);
        }
        return movieApiService.getMoviesByQueryParams(ApplicationConfig.API_ENDPOINT, searchQuery, selectedGenre, selectedYear, selectedRating);
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

    private void setFilterVisibility(boolean isVisible) {
        filterContainer.setManaged(isVisible);
        filterContainer.setVisible(isVisible);
    }
}