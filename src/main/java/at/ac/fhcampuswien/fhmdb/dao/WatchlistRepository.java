package at.ac.fhcampuswien.fhmdb.dao;

import at.ac.fhcampuswien.fhmdb.exception.ApplicationException;
import at.ac.fhcampuswien.fhmdb.exception.ErrorCodes;
import at.ac.fhcampuswien.fhmdb.exception.ExceptionType;
import at.ac.fhcampuswien.fhmdb.models.WatchlistMovie;
import at.ac.fhcampuswien.fhmdb.util.DatabaseUtil;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WatchlistRepository implements Observable<String> {
    private final Dao<WatchlistMovie, Long> dao;
    private List<Observer<String>> observers = new ArrayList<>();

    private static WatchlistRepository instance;

    public static WatchlistRepository getInstance() throws ApplicationException {
        if (instance == null) {
            instance = new WatchlistRepository();
        }
        return instance;
    }

    private WatchlistRepository() throws ApplicationException {
        try {
            dao = DatabaseUtil.getDatabase().getWatchlistDao();
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.DATABASE_EXCEPTION, ErrorCodes.DATABASE_DAO_CREATION_ERROR, "Error initializing: " + e.getMessage());
        }
    }

    public List<WatchlistMovie> getWatchlist() throws ApplicationException {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new ApplicationException(ExceptionType.DATABASE_EXCEPTION, ErrorCodes.DATABASE_QUERY_ERROR, "Error fetching watchlist: " + e.getMessage());
        }
    }

    public int addToWatchlist(WatchlistMovie movie) throws ApplicationException {
        try {
            List<WatchlistMovie> existingMovies = dao.queryForEq("apiId", movie.getMovie().getApiId());
            if (!existingMovies.isEmpty()) {
                notifyObservers("Movie already in watchlist.");
                return 0;
            }
            int result = dao.create(movie);
            if (result > 0) {
                notifyObservers("Movie successfully added to watchlist!");
            } else {
                notifyObservers("An error occurred. Movie not added.");
            }
            return result;
        } catch (SQLException e) {
            throw new ApplicationException(ExceptionType.DATABASE_EXCEPTION, ErrorCodes.DATABASE_INSERT_ERROR, "Error adding movie to watchlist: " + e.getMessage());
        }
    }

    public int removeFromWatchlist(String apiId) throws ApplicationException {
        try {
            List<WatchlistMovie> movies = dao.queryForEq("apiId", apiId);
            if (movies.isEmpty()) {
                return 0;
            }
            int result = dao.delete(movies);
            if (result > 0) {
                notifyObservers("Movie successfully removed from watchlist!");
            } else {
                notifyObservers("An error occurred. Movie not removed.");
            }
            return result;
        } catch (SQLException e) {
            throw new ApplicationException(ExceptionType.DATABASE_EXCEPTION, ErrorCodes.DATABASE_DELETE_ERROR, "Error removing movie from watchlist: " + e.getMessage());
        }
    }

    @Override
    public void registerObserver(Observer<String> observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer<String> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String data) {
        for (Observer<String> observer : observers) {
            observer.update(data);
        }
    }
}