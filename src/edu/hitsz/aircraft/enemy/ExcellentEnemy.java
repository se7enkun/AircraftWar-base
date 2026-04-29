package edu.hitsz.aircraft.enemy;

import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.strategy.StraightShoot;

import java.util.LinkedList;
import java.util.List;

public class ExcellentEnemy extends MobEnemy {
    public ExcellentEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.speedX = 2;
        this.shootStrategy = new StraightShoot(2, 1, 20);
    }

    @Override
    public void onFreezeActive() {
        Runnable r = () -> {
            int originalSpeedX = this.speedX;
            int originalSpeedY = this.speedY;
            this.speedX = 0; this.speedY = 0;
            try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
            if (!this.notValid()) { this.speedX = originalSpeedX; this.speedY = originalSpeedY; }
        };
        new Thread(r).start();
    }

}
