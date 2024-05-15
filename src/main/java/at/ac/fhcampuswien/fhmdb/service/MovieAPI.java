package at.ac.fhcampuswien.fhmdb.service;

import at.ac.fhcampuswien.fhmdb.config.EndpointConfig;
import at.ac.fhcampuswien.fhmdb.dao.MovieRepository;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieAPI {

    private MovieRepository movieRepository;

    public MovieAPI() {
        this.movieRepository = new MovieRepository();
    }

    public ResponseEntity<JsonNode> getResponseAsJson(String url) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(url, JsonNode.class);
    }

    public List<Movie> getAllMovies() {
        ResponseEntity<JsonNode> responseEntity = getResponseAsJson(EndpointConfig.API_ENDPOINT);
        JsonNode jsonNode = responseEntity.getBody();
        return getMovieList(jsonNode);
    }


    public List<Movie> getMoviesByQuery(String query) {
        ResponseEntity<JsonNode> responseEntity = getResponseAsJson(EndpointConfig.API_ENDPOINT + "?" + query);
        JsonNode jsonNode = responseEntity.getBody();
        return getMovieList(jsonNode);
    }

    private List<Movie> getMovieList(JsonNode jsonNode) {
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
                double rating = node.get("rating").asDouble();

                movies.add(new Movie(id, title, description, genreString, releaseYear, lengthInMinutes, imgUrl, rating));
            }
        }

        handleMoviesInDatabase(movies);
        return movies;
    }

    private void handleMoviesInDatabase(List<Movie> movies) {
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