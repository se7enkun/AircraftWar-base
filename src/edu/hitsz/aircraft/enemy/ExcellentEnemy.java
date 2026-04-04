package edu.hitsz.aircraft.enemy;

import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

import java.util.LinkedList;
import java.util.List;

public class ExcellentEnemy extends MobEnemy {
    public ExcellentEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.speedX = 2;
    }

    @Override
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + 2;
        // 双排子弹：一颗在左-20，一颗在右+20
        res.add(new EnemyBullet(x - 20, y, 0, this.getSpeedY() + 5, 20));
        res.add(new EnemyBullet(x + 20, y, 0, this.getSpeedY() + 5, 20));
        return res;
    }
}
