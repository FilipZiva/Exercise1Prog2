package at.ac.fhcampuswien.fhmdb.controller;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.ObservableList;

public interface SortState {
    void sort(ObservableList<Movie> movies);
    SortState nextState();
}
