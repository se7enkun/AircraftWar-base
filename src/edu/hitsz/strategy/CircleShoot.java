package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class CircleShoot implements ShootStrategy {
    private int shootNum; // Boss通常为20发
    private int power;

    public CircleShoot(int shootNum, int power) {
        this.shootNum = shootNum;
        this.power = power;
    }

    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        // 从飞机正中心发射
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY();

        int baseSpeed = 5; // 圆形扩散的合速度

        for (int i = 0; i < shootNum; i++) {
            // 将 360 度 (2*PI) 平分成 shootNum 份
            double angle = 2 * Math.PI * i / shootNum;

            // 利用三角函数计算 X 和 Y 方向的速度分量
            int speedX = (int) (baseSpeed * Math.cos(angle));
            int speedY = (int) (baseSpeed * Math.sin(angle));

            BaseBullet bullet;
            if (aircraft instanceof HeroAircraft) {
                bullet = new HeroBullet(x, y, speedX, speedY, power);
            } else {
                bullet = new EnemyBullet(x, y, speedX, speedY, power);
            }
            res.add(bullet);
        }
        return res;
    }
}