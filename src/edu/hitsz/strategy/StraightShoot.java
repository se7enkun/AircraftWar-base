package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class StraightShoot implements ShootStrategy {
    private int shootNum; // 子弹数量
    private int direction; // 射击方向：英雄机为 -1 (向上)，敌机为 1 (向下)
    private int power; // 子弹威力

    public StraightShoot(int shootNum, int direction, int power) {
        this.shootNum = shootNum;
        this.direction = direction;
        this.power = power;
    }

    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + direction * 2;
        int speedX = 0;
        int speedY = aircraft.getSpeedY() + direction * 5; // 基础速度加成

        for (int i = 0; i < shootNum; i++) {
            // 计算多排子弹的水平偏移 (如果是单发，offset 为 0)
            int xOffset = (i * 2 - shootNum + 1) * 10;

            BaseBullet bullet;
            if (aircraft instanceof HeroAircraft) {
                bullet = new HeroBullet(x + xOffset, y, speedX, speedY, power);
            } else {
                bullet = new EnemyBullet(x + xOffset, y, speedX, speedY, power);
            }
            res.add(bullet);
        }
        return res;
    }
}