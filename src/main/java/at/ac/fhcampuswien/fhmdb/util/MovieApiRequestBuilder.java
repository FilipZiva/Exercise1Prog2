package at.ac.fhcampuswien.fhmdb.util;

public interface MovieApiRequestBuilder {
    MovieApiRequestBuilder query(String query);
    MovieApiRequestBuilder genre(String genre);
    MovieApiRequestBuilder releaseYear(String releaseYear);
    MovieApiRequestBuilder ratingFrom(String ratingFrom);
    String build();
}
