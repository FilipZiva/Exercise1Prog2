package at.ac.fhcampuswien.fhmdb.service;

import at.ac.fhcampuswien.fhmdb.dao.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.exception.ApplicationException;
import at.ac.fhcampuswien.fhmdb.models.WatchlistMovie;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class WatchlistService {
    private final WatchlistRepository watchlistRepository;

    public WatchlistService() throws ApplicationException {
        this.watchlistRepository = WatchlistRepository.getInstance();
    }

    public List<WatchlistMovie> getWatchlist() throws ApplicationException {
        return watchlistRepository.getWatchlist();
    }

    public int addToWatchlist(WatchlistMovie movie) throws ApplicationException {
        return watchlistRepository.addToWatchlist(movie);
    }

    public int removeFromWatchlist(String apiId) throws ApplicationException {
        return watchlistRepository.removeFromWatchlist(apiId);
    }
}