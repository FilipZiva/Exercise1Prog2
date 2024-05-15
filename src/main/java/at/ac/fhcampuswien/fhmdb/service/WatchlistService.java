package at.ac.fhcampuswien.fhmdb.service;

import at.ac.fhcampuswien.fhmdb.dao.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.models.WatchlistMovie;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class WatchlistService {
    private final WatchlistRepository watchlistRepository;

    public WatchlistService() {
        this.watchlistRepository = new WatchlistRepository();
    }

    public List<WatchlistMovie> getWatchlist() {
        return watchlistRepository.getWatchlist();
    }

    public int addToWatchlist(WatchlistMovie movie) {
        return watchlistRepository.addToWatchlist(movie);
    }

    public int removeFromWatchlist(String apiId) {
        return watchlistRepository.removeFromWatchlist(apiId);
    }
}