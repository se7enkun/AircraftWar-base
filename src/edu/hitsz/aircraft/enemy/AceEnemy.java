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

}
