package edu.hitsz.aircraft.enemy;

import edu.hitsz.aircraft.MobEnemy;
import java.util.List;
import java.util.LinkedList;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

public class EliteEnemy extends MobEnemy {
    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + 2; // 向下偏移
        int speedX = 0;
        int speedY = this.getSpeedY() + 5; // 向下直射
        res.add(new EnemyBullet(x, y, speedX, speedY, 20)); // 设定威力
        return res;
    }
}
