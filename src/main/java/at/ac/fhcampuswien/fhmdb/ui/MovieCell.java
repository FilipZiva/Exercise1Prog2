package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.models.Movie;
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
    private final HBox genreYearRatingBox = new HBox(genre, year, rating);
    private final Button showDetailsButton = new Button("Show Details");
    private final Button watchlistButton = new Button("Add to Watchlist");
    private final HBox buttonsBox = new HBox(showDetailsButton, watchlistButton);
    private final VBox layout = new VBox(title, detail, genreYearRatingBox, buttonsBox);

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
            genre.setText(movie.getGenre().toString());
            year.setText("Year: " + movie.getReleaseYear());
            rating.setText("Rating: " + String.format("%.1f", movie.getRating()));

            genreYearRatingBox.setSpacing(10);
            genreYearRatingBox.setAlignment(Pos.CENTER_LEFT);
            genre.setTextFill(Color.WHITE);
            year.setTextFill(Color.WHITE);
            rating.setTextFill(Color.WHITE);

            // Button styles and actions
            showDetailsButton.setOnAction(e -> {});
            watchlistButton.setOnAction(e -> {});
            buttonsBox.setSpacing(10);
            buttonsBox.setAlignment(Pos.CENTER_RIGHT);

            // Color scheme and layout
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
