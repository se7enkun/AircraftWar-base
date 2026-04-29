package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.observer.EnemyObserver;
import edu.hitsz.observer.PropSubject;

import java.util.ArrayList;
import java.util.List;

public class BombProp extends AbstractProp implements PropSubject {
    private List<EnemyObserver> observers = new ArrayList<>();

    public BombProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void addObserver(EnemyObserver observer) { observers.add(observer); }

    @Override
    public void removeObserver(EnemyObserver observer) { observers.remove(observer); }

    @Override
    public void notifyObservers() {
        for (EnemyObserver observer : observers) { observer.onBombActive(); }
    }

    @Override
    public void active(HeroAircraft hero) {
        System.out.println("Bomb active!");
        notifyObservers();
    }
}
