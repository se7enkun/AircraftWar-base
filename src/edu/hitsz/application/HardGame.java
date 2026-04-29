
package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.factory.*;

public class HardGame extends BaseGame {
    private int currentBossHp = 600;

    @Override
    protected void initParams() {
        this.backgroundImage = ImageManager.BACKGROUND_IMAGE_HARD;
        this.enemyMaxNumber = 8;
        this.enemySpawnCycle = 15;
        this.enemyShootCycle = 15;
        this.heroShootCycle = 20; // 初始较慢
        this.bossScoreThreshold = 300;
    }

    @Override
    protected void updateDifficulty() {
        if (timeTotal % 10000 == 0) {
            enemySpawnCycle = Math.max(10, enemySpawnCycle - 2);
            enemySpeedIncrease += 2;
            enemyHpMultiplier += 0.2;
            enemyShootCycle = Math.max(10, enemyShootCycle - 1); // 敌机射击加快
            heroShootCycle = Math.min(30, heroShootCycle + 1); // 英雄机射击变慢
            System.out.printf("时间: %d, 难度提升！敌机周期: %.1f, 英雄机射击周期: %.1f\n",
                    timeTotal, enemySpawnCycle, heroShootCycle);
        }
    }

    @Override
    protected boolean bossCondition() {
        return score >= bossScoreThreshold && !bossExists;
    }

    @Override
    protected void createBoss() {
        // 音乐控制同上...
        EnemyFactory factory = new BossEnemyFactory();
        AbstractAircraft boss = factory.createEnemy();
        boss.setHp(currentBossHp);
        enemyAircrafts.add(boss);
        bossExists = true;
        bossScoreThreshold += 300;
        currentBossHp += 200; // 困难模式 Boss 每次血量增加
    }

    @Override
    protected EnemyFactory getEnemyFactoryByProb() {
        double rand = Math.random();
        if (rand < 0.20) return new AceEnemyFactory();
        else if (rand < 0.40) return new ExcellentEnemyFactory();
        else if (rand < 0.60) return new EliteEnemyFactory();
        else return new MobEnemyFactory();
    }
}