package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.WatchlistMovie;
import at.ac.fhcampuswien.fhmdb.service.WatchlistService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genre = new Label();
    private final Label year = new Label();
    private final Label rating = new Label();
    private final Label directors = new Label();
    private final Label writers = new Label();
    private final Label mainCast = new Label();
    private final VBox detailsBox = new VBox(year, rating, directors, writers, mainCast);
    private final Button showDetailsButton = new Button("Show Details");
    private final Button watchlistButton = new Button();
    private final HBox buttonsBox = new HBox(showDetailsButton, watchlistButton);
    private final VBox layout = new VBox(title, detail, genre, buttonsBox);

    private final WatchlistService watchlistService;
    private final boolean isInWatchlistView;
    private boolean detailsVisible = false;

    public MovieCell(WatchlistService watchlistService, boolean isInWatchlistView) {
        this.watchlistService = watchlistService;
        this.isInWatchlistView = isInWatchlistView;
    }

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setText(null);
            setGraphic(null);
        } else {
            this.getStyleClass().add("movie-cell");
            title.setText(movie.getTitle());
            detail.setText(
                    movie.getDescription() != null
                            ? movie.getDescription()
                            : "No description available"
            );
            genre.setText("Genre: " + movie.getGenre());
            year.setText("Year: " + movie.getReleaseYear());
            rating.setText("Rating: " + String.format("%.1f", movie.getRating()));

            directors.setText("Directors: " + (movie.getDirectors() != null ? String.join(", ", movie.getDirectors()) : "N/A"));
            writers.setText("Writers: " + (movie.getWriters() != null ? String.join(", ", movie.getWriters()) : "N/A"));
            mainCast.setText("Main Cast: " + (movie.getMainCast() != null ? String.join(", ", movie.getMainCast()) : "N/A"));

            detailsBox.setVisible(detailsVisible);
            detailsBox.setSpacing(5);
            detailsBox.setAlignment(Pos.CENTER_LEFT);

            genre.setTextFill(Color.WHITE);
            year.setTextFill(Color.WHITE);
            rating.setTextFill(Color.WHITE);
            directors.setTextFill(Color.WHITE);
            writers.setTextFill(Color.WHITE);
            mainCast.setTextFill(Color.WHITE);

            showDetailsButton.setOnAction(e -> {
                detailsVisible = !detailsVisible;
                detailsBox.setVisible(detailsVisible);
                showDetailsButton.setText(detailsVisible ? "Hide Details" : "Show Details");
                if (detailsVisible) {
                    layout.getChildren().add(3, detailsBox);
                } else {
                    layout.getChildren().remove(detailsBox);
                }
            });

            if (isInWatchlistView) {
                watchlistButton.setText("Remove from Watchlist");
                watchlistButton.setOnAction(e -> {
                    watchlistService.removeFromWatchlist(movie.getApiId());
                    getListView().getItems().remove(movie);
                });
            } else {
                watchlistButton.setText("Add to Watchlist");
                WatchlistMovie watchlistMovie = new WatchlistMovie();
                watchlistMovie.setMovie(movie);
                watchlistButton.setOnAction(e -> watchlistService.addToWatchlist(watchlistMovie));
            }
            buttonsBox.setSpacing(10);
            buttonsBox.setAlignment(Pos.CENTER_RIGHT);

            title.getStyleClass().add("text-yellow");
            detail.getStyleClass().add("text-white");
            layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

            title.setFont(Font.font(20));
            detail.setMaxWidth(this.getScene().getWidth() - 30);
            detail.setWrapText(true);
            layout.setPadding(new Insets(10));
            layout.setSpacing(10);
            layout.setAlignment(Pos.CENTER_LEFT);
            setGraphic(layout);
        }
    }
}