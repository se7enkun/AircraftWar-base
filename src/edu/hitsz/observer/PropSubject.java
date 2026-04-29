package edu.hitsz.observer;

public interface PropSubject {
    void addObserver(EnemyObserver observer);
    void removeObserver(EnemyObserver observer);
    void notifyObservers();
}
