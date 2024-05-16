package at.ac.fhcampuswien.fhmdb.dao;


import at.ac.fhcampuswien.fhmdb.exception.ApplicationException;
import at.ac.fhcampuswien.fhmdb.exception.ErrorCodes;
import at.ac.fhcampuswien.fhmdb.exception.ExceptionType;
import at.ac.fhcampuswien.fhmdb.models.WatchlistMovie;
import at.ac.fhcampuswien.fhmdb.util.DatabaseUtil;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class WatchlistRepository {
    private Dao<WatchlistMovie, Long> dao;

    public WatchlistRepository() throws ApplicationException {
        try {
            dao = DatabaseUtil.getDatabase().getWatchlistDao();
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.DATABASE, ErrorCodes.DATABASE_DAO_CREATION_ERROR, "Error initializing: " + e.getMessage());
        }
    }

    public List<WatchlistMovie> getWatchlist() throws ApplicationException {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new ApplicationException(ExceptionType.DATABASE, ErrorCodes.DATABASE_QUERY_ERROR, "Error fetching watchlist: " + e.getMessage());
        }
    }

    public int addToWatchlist(WatchlistMovie movie) throws ApplicationException {
        try {
            return dao.create(movie);
        } catch (SQLException e) {
            throw new ApplicationException(ExceptionType.DATABASE, ErrorCodes.DATABASE_INSERT_ERROR, "Error adding movie to watchlist: " + e.getMessage());
        }
    }

    public int removeFromWatchlist(String apiId) throws ApplicationException {
        try {
            List<WatchlistMovie> movies = dao.queryForEq("apiId", apiId);
            if (movies.isEmpty()) {
                return 0;
            }
            return dao.delete(movies);
        } catch (SQLException e) {
            throw new ApplicationException(ExceptionType.DATABASE, ErrorCodes.DATABASE_DELETE_ERROR, "Error removing movie from watchlist: " + e.getMessage());
        }
    }
}