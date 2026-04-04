package edu.hitsz.aircraft;

import edu.hitsz.aircraft.enemy.EliteEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class EliteEnemyFactory implements edu.hitsz.factory.EnemyFactory {
    @Override
    public AbstractAircraft createEnemy() {
        return new EliteEnemy(
                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                0, 10, 60); // 假设精英机血量更高
    }
}