package edu.hitsz.aircraft.enemy;

import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.strategy.ScatteringShoot;

import java.util.LinkedList;
import java.util.List;

public class AceEnemy extends MobEnemy {
    public AceEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.speedX = 3;
        this.shootStrategy = new ScatteringShoot();
    }

    @Override
    public void onBombActive() {
        this.decreaseHp(50); // 掉血
    }

    @Override
    public void onFreezeActive() {
        Runnable r = () -> {
            int originalSpeedX = this.speedX;
            int originalSpeedY = this.speedY;
            this.speedX /= 2; this.speedY /= 2; // 减速
            try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }
            if (!this.notValid()) { this.speedX = originalSpeedX; this.speedY = originalSpeedY; }
        };
        new Thread(r).start();
    }

}
