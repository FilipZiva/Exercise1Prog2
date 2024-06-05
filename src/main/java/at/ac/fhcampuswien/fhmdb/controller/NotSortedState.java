package at.ac.fhcampuswien.fhmdb.controller;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.ObservableList;

public class NotSortedState implements SortState {
    @Override
    public void sort(ObservableList<Movie> movies) {
    }

    @Override
    public SortState nextState() {
        return new AscendingSortState();
    }
}
