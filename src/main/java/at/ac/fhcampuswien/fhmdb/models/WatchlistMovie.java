package at.ac.fhcampuswien.fhmdb.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@DatabaseTable(tableName = "WatchlistMovieEntity")
public class WatchlistMovie {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private String apiId;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Movie movie;
}
