package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
    private final VBox layout = new VBox(title, detail, genreYearRatingBox);


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


            // color scheme
            title.getStyleClass().add("text-yellow");
            detail.getStyleClass().add("text-white");
            layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

            // layout
            title.getFont();
            title.fontProperty().set(Font.font(20));
            detail.setMaxWidth(this.getScene().getWidth() - 30);
            detail.setWrapText(true);
            layout.setPadding(new Insets(10));
            layout.spacingProperty().set(10);
            layout.alignmentProperty().set(javafx.geometry.Pos.CENTER_LEFT);
            setGraphic(layout);
        }
    }
}

