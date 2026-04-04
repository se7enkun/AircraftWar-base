package edu.hitsz.aircraft;

import edu.hitsz.aircraft.enemy.AceEnemy;
import edu.hitsz.application.Main;

public class AceEnemyFactory implements edu.hitsz.factory.EnemyFactory {
    @Override
    public AbstractAircraft createEnemy() {
        return new AceEnemy(
                (int) (Math.random() * Main.WINDOW_WIDTH),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                2, 8, 150);
    }
}