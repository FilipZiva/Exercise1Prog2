package at.ac.fhcampuswien.fhmdb.dao;

import at.ac.fhcampuswien.fhmdb.exception.ApplicationException;
import at.ac.fhcampuswien.fhmdb.exception.ErrorCodes;
import at.ac.fhcampuswien.fhmdb.exception.ExceptionType;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.util.DatabaseUtil;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class MovieRepository {
    private final Dao<Movie, Long> dao;

    public MovieRepository() throws ApplicationException {
        try {
            dao = DatabaseUtil.getDatabase().getMovieDao();
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.DATABASE_EXCEPTION, ErrorCodes.DATABASE_DAO_CREATION_ERROR, "Error initializing: " + e.getMessage());
        }
    }

    public List<Movie> getAllMovies() throws ApplicationException {
        try {
            return dao.queryForAll();
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.DATABASE_EXCEPTION, ErrorCodes.DATABASE_QUERY_ERROR, "Error fetching all movies: " + e.getMessage());
        }
    }

    public int removeAllMovies() throws ApplicationException {
        try {
            return dao.deleteBuilder().delete();
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.DATABASE_EXCEPTION, ErrorCodes.DATABASE_DELETE_ERROR, "Error removing all movies: " + e.getMessage());
        }
    }

    public Movie getMovie(Movie id) throws ApplicationException {
        try {
            return dao.queryForSameId(id);
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.DATABASE_EXCEPTION, ErrorCodes.DATABASE_QUERY_BY_ID_ERROR, "Error fetching movie by ID: " + e.getMessage());
        }
    }

    public int addAllMovies(List<Movie> movies) throws ApplicationException {
        try {
            int addedCount = 0;
            for (Movie movie : movies) {
                dao.create(movie);
                addedCount++;
            }
            return addedCount;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.DATABASE_EXCEPTION, ErrorCodes.DATABASE_INSERT_ERROR, "Error adding movies: " + e.getMessage());
        }
    }
}