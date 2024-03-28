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
import java.util.*;
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
        genreComboBox.setPromptText("Filter by Genre");

         Set<String> uniqueGenres = allMovies.stream()
             .flatMap(movie -> movie.getGenre().stream())
             .collect(Collectors.toSet());
        genreComboBox.getItems().addAll(uniqueGenres);
        List<String> sortedGenres = uniqueGenres.stream().sorted().toList();
        genreComboBox.getItems().setAll(sortedGenres);
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
            observableMovies.setAll(allMovies);
        });
    }

    private void applyFilters() {
        String searchQuery = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        String selectedGenre = (genreComboBox.getValue() == null) ? "" : genreComboBox.getValue().toString().toUpperCase();

        List<Movie> filteredMovies = filterMovieByQueryOrGenre(searchQuery, selectedGenre);

        observableMovies.setAll(filteredMovies);
    }

    public List<Movie> filterMovieByQueryOrGenre(String searchQuery, String selectedGenre) {
        return allMovies.stream()
                .filter(movie -> (searchQuery.isEmpty() || movie.getTitle().toLowerCase().contains(searchQuery) || movie.getDescription().toLowerCase().contains(searchQuery)))
                .filter(movie -> selectedGenre.isEmpty() || movie.getGenre().stream().anyMatch(genre -> genre.equalsIgnoreCase(selectedGenre)))
                .collect(Collectors.toList());
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