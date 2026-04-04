package edu.hitsz.aircraft;

import edu.hitsz.aircraft.enemy.BossEnemy;
import edu.hitsz.application.Main;

public class BossEnemyFactory implements edu.hitsz.factory.EnemyFactory {
    @Override
    public AbstractAircraft createEnemy() {
        return new BossEnemy(
                Main.WINDOW_WIDTH / 2,
                (int) (Main.WINDOW_HEIGHT * 0.1),
                2, 0, 600);
    }
}