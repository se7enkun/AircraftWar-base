
package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.factory.*;

public class NormalGame extends BaseGame {
    private int currentBossHp = 600;

    @Override
    protected void initParams() {
        this.backgroundImage = ImageManager.BACKGROUND_IMAGE_NORMAL;
        this.enemyMaxNumber = 6;
        this.enemySpawnCycle = 20;
        this.enemyShootCycle = 18;
        this.heroShootCycle = 18;
        this.bossScoreThreshold = 400;
    }

    @Override
    protected void updateDifficulty() {
        // 普通难度递增逻辑：周期、速度和血量
        if (timeTotal % 10000 == 0) { // 每10秒
            enemySpawnCycle = Math.max(12, enemySpawnCycle - 1);
            enemySpeedIncrease += 1;
            enemyHpMultiplier += 0.1;
            System.out.printf("时间: %d, 难度提升！敌机周期: %.1f, 敌机速度加成: %d, 敌机血量倍率: %.2f\n",
                    timeTotal, enemySpawnCycle, enemySpeedIncrease, enemyHpMultiplier);
        }
    }

    @Override
    protected boolean bossCondition() {
        return score >= bossScoreThreshold && !bossExists;
    }

    @Override
    protected void createBoss() {
        if (bgmThread != null) { bgmThread.stopMusic(); }
        bossBgmThread = new MusicThread(videoPath + "bgm_boss.wav", true);
        bossBgmThread.start();

        EnemyFactory factory = new BossEnemyFactory();
        AbstractAircraft boss = factory.createEnemy();
        boss.setHp(currentBossHp); // 普通难度 Boss 血量不增加
        enemyAircrafts.add(boss);
        bossExists = true;
        bossScoreThreshold += 400;
    }

    @Override
    protected EnemyFactory getEnemyFactoryByProb() {
        double rand = Math.random();
        if (rand < 0.10) return new AceEnemyFactory();
        else if (rand < 0.20) return new ExcellentEnemyFactory();
        else if (rand < 0.50) return new EliteEnemyFactory();
        else return new MobEnemyFactory();
    }
}