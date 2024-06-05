package at.ac.fhcampuswien.fhmdb.controller;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.ObservableList;

public class SortContext {
    private SortState currentState;

    public SortContext() {
        this.currentState = new NotSortedState();
    }

    public void sort(ObservableList<Movie> movies) {
        currentState.sort(movies);
    }

    public void nextState() {
        currentState = currentState.nextState();
    }

    public SortState getCurrentState() {
        return currentState;
    }
}
