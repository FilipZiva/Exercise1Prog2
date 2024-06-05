package at.ac.fhcampuswien.fhmdb.controller;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SortContextTest {

    private SortContext sortContext;
    private ObservableList<Movie> movies;

    @BeforeEach
    void setUp() {
        sortContext = new SortContext();
        movies = FXCollections.observableArrayList(
                new Movie("movie_1_id", "abcdefghijklm", "...", String.valueOf(List.of("Action")), 2012, "...", 120,
                        List.of("Director 1"), Arrays.asList("Writer 1", "Writer 2"),
                        Arrays.asList("Actor 1", "Actor 2"), 3.5),
                new Movie("movie_2_id", "Movie 23333", "...", String.valueOf(List.of("Comedy")), 2022, "...", 100,
                        Arrays.asList("Director 2","Director 1"), List.of("Writer 3"),
                        Arrays.asList("Actor 2", "Actor 3", "Actor 2"), 4.2),
                new Movie("movie_3_id", "Movie 322", "...", String.valueOf(List.of("Drama")), 2004, "...", 90,
                        List.of("Director 3"), List.of("Writer 4"),
                        List.of("Actor 1"), 2.8)
        );
    }

    @Test
    void initialState_isNotSortedState() {
        assertInstanceOf(NotSortedState.class, sortContext.getCurrentState());
    }

    @Test
    void stateTransitions() {
        // Initial state should be NotSortedState
        assertInstanceOf(NotSortedState.class, sortContext.getCurrentState());

        // Transition to AscendingSortState
        sortContext.nextState();
        assertInstanceOf(AscendingSortState.class, sortContext.getCurrentState());

        // Transition to DescendingSortState
        sortContext.nextState();
        assertInstanceOf(DescendingSortState.class, sortContext.getCurrentState());

        // Transition back to NotSortedState
        sortContext.nextState();
        assertInstanceOf(NotSortedState.class, sortContext.getCurrentState());
    }

    @Test
    void sort_notSortedState() {
        // Act
        sortContext.sort(movies);

        // Assert
        assertEquals("abcdefghijklm", movies.get(0).getTitle());
        assertEquals("Movie 23333", movies.get(1).getTitle());
        assertEquals("Movie 322", movies.get(2).getTitle());
    }

    @Test
    void sort_ascendingSortState() {
        // Arrange
        sortContext.nextState();

        // Act
        sortContext.sort(movies);

        // Assert
        assertEquals("Movie 23333", movies.get(0).getTitle());
        assertEquals("Movie 322", movies.get(1).getTitle());
        assertEquals("abcdefghijklm", movies.get(2).getTitle());
    }

    @Test
    void sort_descendingSortState() {
        // Arrange
        sortContext.nextState();
        sortContext.nextState();

        // Act
        sortContext.sort(movies);

        // Assert
        assertEquals("abcdefghijklm", movies.get(0).getTitle());
        assertEquals("Movie 322", movies.get(1).getTitle());
        assertEquals("Movie 23333", movies.get(2).getTitle());
    }
}
