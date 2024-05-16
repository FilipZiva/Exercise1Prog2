package at.ac.fhcampuswien.fhmdb.util;

import at.ac.fhcampuswien.fhmdb.config.ApplicationConfig;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.WatchlistMovie;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseUtil {
    private static ConnectionSource connectionSource;

    private Dao<Movie, Long> movieDao;
    private Dao<WatchlistMovie, Long> watchlistDao;

    private static DatabaseUtil instance;

    private DatabaseUtil() {
        try {
            createConnectionSource();
            movieDao = DaoManager.createDao(connectionSource, Movie.class);
            watchlistDao = DaoManager.createDao(connectionSource, WatchlistMovie.class);
            createTables();
        } catch (SQLException e) {
            System.err.println("Error while initializing database: " + e.getMessage());
        }
    }

    public static DatabaseUtil getDatabase() {
        if (instance == null) {
            instance = new DatabaseUtil();
        }
        return instance;
    }

    private static void createConnectionSource() throws SQLException {
        connectionSource = new JdbcConnectionSource(ApplicationConfig.DB_URL, ApplicationConfig.DB_USER, ApplicationConfig.DB_PASSWORD);
    }

    private static void createTables() throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, Movie.class);
        TableUtils.createTableIfNotExists(connectionSource, WatchlistMovie.class);
    }

    public Dao<Movie, Long> getMovieDao() {
        return movieDao;
    }

    public Dao<WatchlistMovie, Long> getWatchlistDao() {
        return watchlistDao;
    }
}
