package edu.hitsz.aircraft.enemy;

import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

import java.util.LinkedList;
import java.util.List;

public class AceEnemy extends MobEnemy {
    public AceEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.speedX = 3;
    }

    @Override
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + 2;
        // 扇形散射：左偏、直下、右偏
        res.add(new EnemyBullet(x, y, -2, this.getSpeedY() + 5, 20));
        res.add(new EnemyBullet(x, y, 0, this.getSpeedY() + 5, 20));
        res.add(new EnemyBullet(x, y, 2, this.getSpeedY() + 5, 20));
        return res;
    }
}
