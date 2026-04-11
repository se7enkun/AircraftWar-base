package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import java.util.List;

public interface ShootStrategy {
    /**
     * 具体的射击算法
     * @param aircraft 执行射击的飞机
     * @return 产生的子弹列表
     */
    List<BaseBullet> shoot(AbstractAircraft aircraft);
}