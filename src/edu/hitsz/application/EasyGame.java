// edu.hitsz.application.HardGame.java
package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.factory.*;

public class EasyGame extends BaseGame {


    @Override
    protected void initParams() {
        this.backgroundImage = ImageManager.BACKGROUND_IMAGE_EASY;
        this.enemyMaxNumber = 5;
        this.enemySpawnCycle = 25;
        this.enemyShootCycle = 20;
        this.heroShootCycle = 25;
        this.bossScoreThreshold = 300;
    }

    @Override
    protected void updateDifficulty() {

    }


    @Override
    protected boolean bossCondition() {
        return false;
    }

    @Override
    protected void createBoss() {

    }


    @Override
    protected EnemyFactory getEnemyFactoryByProb() {
        double rand = Math.random();
        if (rand < 0.05) return new AceEnemyFactory();
        else if (rand < 0.20) return new ExcellentEnemyFactory();
        else if (rand < 0.50) return new EliteEnemyFactory();
        else return new MobEnemyFactory();
    }
}
