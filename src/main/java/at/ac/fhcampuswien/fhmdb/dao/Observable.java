package at.ac.fhcampuswien.fhmdb.dao;

public interface Observable<T> {
    void registerObserver(Observer<T> observer);
    void unregisterObserver(Observer<T> observer);
    void notifyObservers(T data);
}

