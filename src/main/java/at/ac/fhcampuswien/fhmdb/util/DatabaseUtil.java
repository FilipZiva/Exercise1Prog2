package at.ac.fhcampuswien.fhmdb.util;

import at.ac.fhcampuswien.fhmdb.config.ApplicationConfig;
import at.ac.fhcampuswien.fhmdb.exception.ApplicationException;
import at.ac.fhcampuswien.fhmdb.exception.ErrorCodes;
import at.ac.fhcampuswien.fhmdb.exception.ExceptionType;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.WatchlistMovie;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.Getter;

import java.sql.SQLException;

@Getter
public class DatabaseUtil {

    private static ConnectionSource connectionSource;

    private Dao<Movie, Long> movieDao;
    private Dao<WatchlistMovie, Long> watchlistDao;

    private static DatabaseUtil instance;

    private DatabaseUtil() throws ApplicationException {
        try {
            createConnectionSource();
            movieDao = DaoManager.createDao(connectionSource, Movie.class);
            watchlistDao = DaoManager.createDao(connectionSource, WatchlistMovie.class);
            createTables();
        } catch (SQLException e) {
            throw new ApplicationException(ExceptionType.DATABASE, ErrorCodes.DATABASE_DAO_CREATION_ERROR, e.getMessage());
        }
    }

    public static DatabaseUtil getDatabase() throws ApplicationException {
        if (instance == null) {
            instance = new DatabaseUtil();
        }
        return instance;
    }

    private static void createConnectionSource() throws ApplicationException {
        try {
            connectionSource = new JdbcConnectionSource(ApplicationConfig.DB_URL, ApplicationConfig.DB_USER, ApplicationConfig.DB_PASSWORD);
        } catch (SQLException e) {
            throw new ApplicationException(ExceptionType.DATABASE, ErrorCodes.DATABASE_CONNECTION_ERROR, e.getMessage());
        }
    }

    private static void createTables() throws ApplicationException {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Movie.class);
            TableUtils.createTableIfNotExists(connectionSource, WatchlistMovie.class);
        } catch (SQLException e) {
            throw new ApplicationException(ExceptionType.DATABASE, ErrorCodes.DATABASE_TABLE_CREATION_ERROR, e.getMessage());
        }
    }

}