package at.ac.fhcampuswien.fhmdb.util;

import at.ac.fhcampuswien.fhmdb.config.ApplicationConfig;
import at.ac.fhcampuswien.fhmdb.exception.ApplicationException;
import at.ac.fhcampuswien.fhmdb.exception.ErrorCodes;
import at.ac.fhcampuswien.fhmdb.exception.ExceptionType;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.WatchlistMovie;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseUtilTest {

    @AfterEach
    void tearDown() {
        DatabaseUtil.connectionSource = null;
        DatabaseUtil.instance = null;
    }

    @Test
    void testSingletonInstance() throws ApplicationException {
        // Arrange
        DatabaseUtil firstInstance = DatabaseUtil.getDatabase();

        // Act
        DatabaseUtil secondInstance = DatabaseUtil.getDatabase();

        // Assert
        assertSame(firstInstance, secondInstance, "The getDatabase method should return the same instance");
    }

    @Test
    void testCreateConnectionSourceSuccess() throws ApplicationException {
        // Arrange & Act
        DatabaseUtil.createConnectionSource();

        // Assert
        assertNotNull(DatabaseUtil.connectionSource, "ConnectionSource should be created successfully");
    }

    @Test
    void testCreateConnectionSourceFailure() {
        // Arrange
        String originalDbUrl = ApplicationConfig.DB_URL;
        ApplicationConfig.DB_URL = "invalid_url"; // Set an invalid URL to cause failure

        // Act
        ApplicationException exception = assertThrows(ApplicationException.class, DatabaseUtil::createConnectionSource);

        // Assert
        assertEquals(ExceptionType.DATABASE_EXCEPTION, exception.getType());
        assertEquals(ErrorCodes.DATABASE_CONNECTION_ERROR.getCode(), exception.getErrorCode());

        // Restore the original DB_URL
        ApplicationConfig.DB_URL = originalDbUrl;
    }

    @Test
    void testCreateTablesSuccess() throws ApplicationException, SQLException {
        // Arrange
        DatabaseUtil.createConnectionSource();

        // Act
        DatabaseUtil.createTables();
        ConnectionSource connectionSource = DatabaseUtil.connectionSource;

        // Assert
        assertNotNull(connectionSource, "ConnectionSource should not be null");

        // Check if tables are created
        Dao<Movie, Long> movieDao = DaoManager.createDao(connectionSource, Movie.class);
        Dao<WatchlistMovie, Long> watchlistDao = DaoManager.createDao(connectionSource, WatchlistMovie.class);

        assertNotNull(movieDao, "Movie DAO should be created successfully");
        assertNotNull(watchlistDao, "WatchlistMovie DAO should be created successfully");
    }

    @Test
    void testDatabaseUtilConstructorFailure() {
        // Arrange
        String originalDbUrl = ApplicationConfig.DB_URL;
        ApplicationConfig.DB_URL = "invalid_url";

        // Act
        ApplicationException exception = assertThrows(ApplicationException.class, DatabaseUtil::getDatabase);

        // Assert
        assertEquals(ExceptionType.DATABASE_EXCEPTION, exception.getType());
        assertEquals(ErrorCodes.DATABASE_DAO_CREATION_ERROR.getCode(), exception.getErrorCode());

        // Restore the original DB_URL
        ApplicationConfig.DB_URL = originalDbUrl;
    }
}
