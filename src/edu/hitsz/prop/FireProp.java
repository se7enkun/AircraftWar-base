package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.ScatteringShoot;
import edu.hitsz.strategy.StraightShoot;

public class FireProp extends AbstractProp {
    public FireProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void active(HeroAircraft hero) {
        System.out.println("FireSupply active!");
        Runnable r = () -> {
            hero.setShootStrategy(new ScatteringShoot());
            try {
                // 道具持续生效 5 秒
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 恢复默认的直射弹道
            hero.setShootStrategy(new StraightShoot(1, -1, 30));
        };
        new Thread(r, "FireProp-Thread").start();
    }
}
