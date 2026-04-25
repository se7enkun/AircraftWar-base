package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.CircleShoot;
import edu.hitsz.strategy.StraightShoot;

public class SuperFireProp extends AbstractProp {
    public SuperFireProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void active(HeroAircraft hero) {
        System.out.println("FirePlusSupply active!");
        Runnable r = () -> {
            hero.setShootStrategy(new CircleShoot(20, 30));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            hero.setShootStrategy(new StraightShoot(1, -1, 30));
        };
        new Thread(r, "SuperFireProp-Thread").start();
    }
}
