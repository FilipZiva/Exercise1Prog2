package at.ac.fhcampuswien.fhmdb.service;

import at.ac.fhcampuswien.fhmdb.config.EndpointConfig;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieAPI {

    public static ResponseEntity<JsonNode> getResponseAsJson(String url) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(url, JsonNode.class);
    }

    public static List<Movie> getAllMovies() {
        ResponseEntity<JsonNode> responseEntity = getResponseAsJson(EndpointConfig.API_ENDPOINT);
        JsonNode jsonNode = responseEntity.getBody();

        List<Movie> movies = new ArrayList<>();
        if (jsonNode != null) {
            for (JsonNode node : jsonNode) {
                String id = node.get("id").asText();
                String title = node.get("title").asText();
                String description = node.get("description").asText();
                List<String> genres = new ArrayList<>();
                node.get("genres").forEach(genreNode -> genres.add(genreNode.asText()));
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

                movies.add(new Movie(id, title, description, genres, releaseYear, imgUrl, lengthInMinutes, directors, writers, mainCast, rating));
            }
        }
        return movies;
    }



}
