package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.service.MovieAPI;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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

    public List<Movie> allMovies;

    ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeMovies();
        initializeUi();
        initializeGenre();
        initializeYear();
        initializeRating();
        handleResetButton();
        handleSortingMechanism();
        handleSearchbarFilter();
    }

    public void initializeUi() {
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(movieListView -> new MovieCell());
    }

    public void initializeMovies() {
        allMovies = MovieAPI.getAllMovies();
        observableMovies.addAll(allMovies);
    }

    public void initializeGenre() {
        List<String> uniqueGenres = allMovies.stream()
                .flatMap(movie -> movie.getGenre().stream())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

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
        searchBtn.setOnAction(actionEvent -> applyFilters());
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

    private void applyFilters() {
        String searchQuery = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        String selectedGenre = (genreComboBox.getValue() == null) ? "" : genreComboBox.getValue().toString();
        String selectedYear = (yearComboBox.getValue() == null) ? "" : yearComboBox.getValue().toString();
        String selectedRating = (ratingComboBox.getValue() == null) ? "" : ratingComboBox.getValue().toString();


        List<Movie> filteredMovies = filterMovieByQueryOrGenre(searchQuery, selectedGenre, selectedYear, selectedRating);

        observableMovies.setAll(filteredMovies);
    }

    public List<Movie> filterMovieByQueryOrGenre(String searchQuery, String selectedGenre, String selectedYear, String selectedRating) {
        StringBuilder queryBuilder = new StringBuilder();

        if (!searchQuery.isEmpty()) {
            queryBuilder.append("query=").append(searchQuery);
        }
        if (!selectedGenre.isEmpty()) {
            if (queryBuilder.length() > 0) queryBuilder.append("&");
            queryBuilder.append("genre=").append(selectedGenre);
        }
        if (!selectedYear.isEmpty()) {
            if (queryBuilder.length() > 0) queryBuilder.append("&");
            queryBuilder.append("releaseYear=").append(selectedYear);
        }
        if (!selectedRating.isEmpty()) {
            if (queryBuilder.length() > 0) queryBuilder.append("&");
            queryBuilder.append("ratingFrom=").append(selectedRating);
        }

        if (queryBuilder.length() == 0) {
            return MovieAPI.getAllMovies();
        }

        return MovieAPI.getMoviesByQuery(queryBuilder.toString());
    }

    public void filterMovieByTitleAscDesc(boolean initialize) {
        if (!initialize) {
            FXCollections.sort(observableMovies, Comparator.comparing(Movie::getTitle));
        } else {
            FXCollections.sort(observableMovies, Comparator.comparing(Movie::getTitle).reversed());
        }
    }

    public List<Movie> getAllMovies() {
        return allMovies;
    }
}