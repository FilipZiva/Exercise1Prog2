package at.ac.fhcampuswien.fhmdb.dao;

public interface Observer<T> {
    void update(T data);
}
