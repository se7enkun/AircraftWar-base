package edu.hitsz.aircraft.enemy;

import edu.hitsz.aircraft.MobEnemy;
import java.util.List;
import java.util.LinkedList;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.strategy.StraightShoot;

public class EliteEnemy extends MobEnemy {
    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.shootStrategy = new StraightShoot(1, 1, 20);
    }

    @Override
    public void onFreezeActive() {
        Runnable r = () -> {
            int originalSpeedX = this.speedX;
            int originalSpeedY = this.speedY;
            this.speedX = 0; this.speedY = 0;
            try { Thread.sleep(4000); } catch (InterruptedException e) { e.printStackTrace(); }
            if (!this.notValid()) { this.speedX = originalSpeedX; this.speedY = originalSpeedY; }
        };
        new Thread(r).start();
    }

}
