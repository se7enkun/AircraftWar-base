package edu.hitsz.aircraft.enemy;

import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.strategy.CircleShoot;

public class BossEnemy extends MobEnemy {
    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.speedX = 5;
        this.speedY = 0;
        this.shootStrategy = new CircleShoot(20, 30);
    }

    @Override
    public void forward() {
        // 仅执行左右移动，不向下移动
        locationX += speedX;

        // 判定左右越界反向
        if (locationX <= 0 || locationX >= edu.hitsz.application.Main.WINDOW_WIDTH) {
            speedX = -speedX;
        }
        // y 坐标保持不变，不执行 locationY += speedY;
    }

    @Override
    public void onBombActive() {
        /* 不受影响 */
    }

    @Override
    public void onFreezeActive() {
        /* 不受影响 */
    }
}
