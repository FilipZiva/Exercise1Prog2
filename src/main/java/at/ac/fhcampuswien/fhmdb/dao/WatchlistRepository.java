package at.ac.fhcampuswien.fhmdb.dao;

import at.ac.fhcampuswien.fhmdb.models.WatchlistMovie;
import at.ac.fhcampuswien.fhmdb.util.DatabaseUtil;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class WatchlistRepository {
    private Dao<WatchlistMovie, Long> dao;

    public WatchlistRepository() {
        dao = DatabaseUtil.getDatabase().getWatchlistDao();
    }

    public List<WatchlistMovie> getWatchlist() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            System.err.println("Error fetching watchlist: " + e.getMessage());
            return null;
        }
    }

    public int addToWatchlist(WatchlistMovie movie) {
        try {
            return dao.create(movie);
        } catch (SQLException e) {
            System.err.println("Error adding movie to watchlist: " + e.getMessage());
            return 0;
        }
    }

    public int removeFromWatchlist(String apiId) {
        try {
            List<WatchlistMovie> movies = dao.queryForEq("apiId", apiId);
            if (movies.isEmpty()) {
                return 0;
            }
            return dao.delete(movies);
        } catch (SQLException e) {
            System.err.println("Error removing movie from watchlist: " + e.getMessage());
            return 0;
        }
    }
}