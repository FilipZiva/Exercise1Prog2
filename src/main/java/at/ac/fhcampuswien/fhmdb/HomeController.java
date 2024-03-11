package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

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

    public List<Movie> allMovies;

    ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeMovies();
        initializeUi();
        handleGenreFilter();
        handleSortingMechanism();
        handleSearchbarFilter();
    }

    public void initializeUi() {
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(movieListView -> new MovieCell());
    }

    public void initializeMovies() {
        allMovies = Movie.initializeMovies();
        observableMovies.addAll(allMovies);
    }

    public void handleGenreFilter() {
        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().addAll(Arrays.stream(Genre.values()).map(Enum::name).toList());

        genreComboBox.valueProperty().addListener((observable, oldVal, newVal) -> filterMoviesByGenre(String.valueOf(newVal)));
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

    private void handleSearchbarFilter() {

    }

    public void filterMovieByTitleAscDesc(boolean initialize) {
        if (!initialize) {
            FXCollections.sort(observableMovies, Comparator.comparing(Movie::getTitle));
        } else {
            FXCollections.sort(observableMovies, Comparator.comparing(Movie::getTitle).reversed());
        }
    }

    public void filterMoviesByGenre(String genre) {
        observableMovies.clear();
        if (genre == null || genre.isEmpty()) {
            observableMovies.addAll(allMovies);
        } else {
            List<Movie> filteredMovies = allMovies.stream().filter(movie -> movie.getGenre().contains(Genre.valueOf(genre))).toList();
            observableMovies.addAll(filteredMovies);
        }
    }


    public List<Movie> getAllMovies() {
        return allMovies;
    }
}