package at.ac.fhcampuswien.fhmdb.util;

public class MovieApiRequestBuilderImpl implements MovieApiRequestBuilder {
    private StringBuilder urlBuilder;

    public MovieApiRequestBuilderImpl(String baseUrl) {
        this.urlBuilder = new StringBuilder(baseUrl);
    }

    @Override
    public MovieApiRequestBuilder query(String query) {
        appendParameter("query", query);
        return this;
    }

    @Override
    public MovieApiRequestBuilder genre(String genre) {
        appendParameter("genre", genre);
        return this;
    }

    @Override
    public MovieApiRequestBuilder releaseYear(String releaseYear) {
        appendParameter("releaseYear", releaseYear);
        return this;
    }

    @Override
    public MovieApiRequestBuilder ratingFrom(String ratingFrom) {
        appendParameter("ratingFrom", ratingFrom);
        return this;
    }

    @Override
    public String build() {
        return urlBuilder.toString();
    }

    private void appendParameter(String key, String value) {
        if (urlBuilder.indexOf("?") > 0) {
            urlBuilder.append("&");
        } else {
            urlBuilder.append("?");
        }
        urlBuilder.append(key).append("=").append(value);
    }
}
