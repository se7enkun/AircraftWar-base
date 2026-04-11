package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.ScatteringShoot;

public class FireProp extends AbstractProp {
    public FireProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void active(HeroAircraft hero) {
        System.out.println("FireSupply active!");
        hero.setShootStrategy(new ScatteringShoot());
    }
}
