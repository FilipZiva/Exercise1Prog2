package at.ac.fhcampuswien.fhmdb.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@Setter
@DatabaseTable(tableName = "MovieEntity")
public class Movie {
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private String apiId;

    @DatabaseField
    private String title;

    @DatabaseField
    private String description;

    @DatabaseField
    private String genre;

    @DatabaseField
    private int releaseYear;

    @DatabaseField
    private String imgUrl;

    @DatabaseField
    private int lengthInMinutes;

    @DatabaseField
    private double rating;

    private final List<String> directors;
    private final List<String> writers;
    private final List<String> mainCast;


    public Movie(String apiId, String title, String description, String genre, int releaseYear, String imgUrl, int lengthInMinutes, List<String> directors, List<String> writers, List<String> mainCast, double rating) {
        this.apiId = apiId;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.lengthInMinutes = lengthInMinutes;
        this.imgUrl = imgUrl;
        this.rating = rating;
        this.directors = directors;
        this.writers = writers;
        this.mainCast = mainCast;
    }

    public static String genresToString(List<String> genres){
        return String.join(", ", genres);
    }
}