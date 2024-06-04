package at.ac.fhcampuswien.fhmdb.controller;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.ObservableList;

public class SortContext{
    private SortState currentState;

    public SortContext() {
        // Initialer Zustand
        this.currentState = new AscendingSortState();
    }
    public SortState getCurrentState() {
        return currentState;
    }

    public void setState(SortState state) {
        this.currentState = state;
    }

    public void sort(ObservableList<Movie> movies) {
        currentState.sort(movies);
    }
}
