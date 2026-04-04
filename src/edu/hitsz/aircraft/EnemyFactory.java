package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;

public interface EnemyFactory {
    /**
     * 创建敌机的工厂方法
     * @return 具体敌机对象
     */
    AbstractAircraft createEnemy();
}