package at.ac.fhcampuswien.fhmdb.dao;


import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.util.DatabaseUtil;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class MovieRepository {
    private Dao<Movie, Long> dao;

    public MovieRepository() {
        dao = DatabaseUtil.getDatabase().getMovieDao();
    }

    public List<Movie> getAllMovies() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            System.err.println("Error fetching all movies: " + e.getMessage());
            return null;
        }
    }

    public int removeAllMovies() {
        try {
            return dao.deleteBuilder().delete();
        } catch (SQLException e) {
            System.err.println("Error removing all movies: " + e.getMessage());
            return 0;
        }
    }

    public Movie getMovie(Movie id) {
        try {
            return dao.queryForSameId(id);
        } catch (SQLException e) {
            System.err.println("Error fetching movie by ID: " + e.getMessage());
            return null;
        }
    }

    public int addAllMovies(List<Movie> movies) {
        try {
            int addedCount = 0;
            for (Movie movie : movies) {
                dao.create(movie);
                addedCount++;
            }
            return addedCount;
        } catch (SQLException e) {
            System.err.println("Error adding movies: " + e.getMessage());
            return 0;
        }
    }
}