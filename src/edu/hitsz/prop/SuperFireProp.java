package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.CircleShoot;

public class SuperFireProp extends AbstractProp {
    public SuperFireProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void active(HeroAircraft hero) {
        System.out.println("FirePlusSupply active!");
        hero.setShootStrategy(new CircleShoot(20, 30));
    }
}
