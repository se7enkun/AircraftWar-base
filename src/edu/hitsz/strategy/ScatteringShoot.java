package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import java.util.LinkedList;
import java.util.List;

public class ScatteringShoot implements ShootStrategy {
    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY();
        int speedY = aircraft.getSpeedY();
        int direction = (aircraft instanceof HeroAircraft) ? -1 : 1;
        int shootNum = 3; // 散射通常为3枚

        for(int i = 0; i < shootNum; i++){
            // 计算分速度，实现扇形效果
            int speedX = i - shootNum / 2;
            BaseBullet bullet;
            if (aircraft instanceof HeroAircraft) {
                bullet = new HeroBullet(x, y, speedX, speedY + direction * 5, 30);
            } else {
                bullet = new EnemyBullet(x, y, speedX, speedY + direction * 5, 20);
            }
            res.add(bullet);
        }
        return res;
    }
}