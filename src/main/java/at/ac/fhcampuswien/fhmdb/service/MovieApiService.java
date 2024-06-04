package at.ac.fhcampuswien.fhmdb.service;


import at.ac.fhcampuswien.fhmdb.dao.MovieRepository;
import at.ac.fhcampuswien.fhmdb.exception.ApplicationException;
import at.ac.fhcampuswien.fhmdb.exception.ErrorCodes;
import at.ac.fhcampuswien.fhmdb.exception.ExceptionType;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.util.MovieApiRequestBuilderImpl;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static at.ac.fhcampuswien.fhmdb.util.PopupUtil.showPopup;

@Service
public class MovieApiService {
    private final MovieRepository movieRepository;

    public MovieApiService() throws ApplicationException {
        this.movieRepository = MovieRepository.getInstance();
    }

    public ResponseEntity<JsonNode> getResponseAsJson(String url) throws ApplicationException {
        try {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForEntity(url, JsonNode.class);
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.MOVIE_API_EXCEPTION, ErrorCodes.API_REQUEST_ERROR, "Error sending request to API: " + e.getMessage());
        }
    }

    public List<Movie> getAllMovies(String apiEndpoint) throws ApplicationException {
        try {
            ResponseEntity<JsonNode> responseEntity = getResponseAsJson(apiEndpoint);
            JsonNode jsonNode = responseEntity.getBody();
            return getMovieList(jsonNode);
        } catch (Exception e) {
            showPopup(new ApplicationException(ExceptionType.MOVIE_API_EXCEPTION, ErrorCodes.API_RESPONSE_ERROR, "Error processing API response: " + e.getMessage()));
            return movieRepository.getAllMovies();
        }
    }

    public List<Movie> getMoviesByQuery(String query, String apiEndpoint) throws ApplicationException {
        try {
            String url = new MovieApiRequestBuilderImpl(apiEndpoint)
                    .query(query)
                    .build();

            ResponseEntity<JsonNode> responseEntity = getResponseAsJson(url);
            JsonNode jsonNode = responseEntity.getBody();
            return getMovieList(jsonNode);
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.MOVIE_API_EXCEPTION, ErrorCodes.API_RESPONSE_ERROR, "Error processing API response for query: " + e.getMessage());
        }
    }

    public List<Movie> getMoviesByQueryParams(String apiEndpoint, String query, String genre, String releaseYear, String ratingFrom) throws ApplicationException {
        try {
            String url = new MovieApiRequestBuilderImpl(apiEndpoint)
                    .query(query)
                    .genre(genre)
                    .releaseYear(releaseYear)
                    .ratingFrom(ratingFrom)
                    .build();

            ResponseEntity<JsonNode> responseEntity = getResponseAsJson(url);
            JsonNode jsonNode = responseEntity.getBody();
            return getMovieList(jsonNode);
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.MOVIE_API_EXCEPTION, ErrorCodes.API_RESPONSE_ERROR, "Error processing API response: " + e.getMessage());
        }
    }

    public List<Movie> getMovieList(JsonNode jsonNode) throws ApplicationException {
        try {
            List<Movie> movies = new ArrayList<>();
            if (jsonNode != null) {
                for (JsonNode node : jsonNode) {
                    String id = node.get("id").asText();
                    String title = node.get("title").asText();
                    String description = node.get("description").asText();
                    List<String> genres = new ArrayList<>();
                    node.get("genres").forEach(genreNode -> genres.add(genreNode.asText()));
                    String genreString = Movie.genresToString(genres);
                    int releaseYear = node.get("releaseYear").asInt();
                    String imgUrl = node.get("imgUrl").asText();
                    int lengthInMinutes = node.get("lengthInMinutes").asInt();
                    List<String> directors = new ArrayList<>();
                    node.get("directors").forEach(directorNode -> directors.add(directorNode.asText()));
                    List<String> writers = new ArrayList<>();
                    node.get("writers").forEach(writerNode -> writers.add(writerNode.asText()));
                    List<String> mainCast = new ArrayList<>();
                    node.get("mainCast").forEach(castNode -> mainCast.add(castNode.asText()));
                    double rating = node.get("rating").asDouble();

                    movies.add(new Movie(id, title, description, genreString, releaseYear, imgUrl, lengthInMinutes, directors, writers, mainCast, rating));
                }
            }

            handleMoviesInDatabase(movies);
            return movies;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.MOVIE_API_EXCEPTION, ErrorCodes.JSON_PROCESSING_ERROR, "Error processing JSON response: " + e.getMessage());
        }
    }

    private void handleMoviesInDatabase(List<Movie> movies) throws ApplicationException {
        List<Movie> existingMovies = movieRepository.getAllMovies();

        Set<String> existingMovieTitles = existingMovies.stream()
                .map(Movie::getTitle)
                .collect(Collectors.toSet());

        List<Movie> newMovies = movies.stream()
                .filter(movie -> !existingMovieTitles.contains(movie.getTitle()))
                .collect(Collectors.toList());

        if (!newMovies.isEmpty()) {
            movieRepository.addAllMovies(newMovies);
        }
    }
}
