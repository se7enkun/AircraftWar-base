package edu.hitsz.aircraft;

import edu.hitsz.aircraft.enemy.ExcellentEnemy;
import edu.hitsz.application.Main;

public class ExcellentEnemyFactory implements edu.hitsz.factory.EnemyFactory {
    @Override
    public AbstractAircraft createEnemy() {
        return new ExcellentEnemy(
                (int) (Math.random() * Main.WINDOW_WIDTH),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                0, 12, 100);
    }
}