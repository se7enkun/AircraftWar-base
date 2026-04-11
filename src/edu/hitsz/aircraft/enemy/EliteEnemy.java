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

}
