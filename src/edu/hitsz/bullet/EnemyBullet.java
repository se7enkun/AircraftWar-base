package edu.hitsz.bullet;

/**
 * 敌机子弹
 * @Author hitsz
 */
public class EnemyBullet extends BaseBullet implements edu.hitsz.observer.EnemyObserver{

    public EnemyBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
    }

    @Override
    public void onBombActive() {
        this.vanish();
    }

    @Override
    public void onFreezeActive() {
        Runnable r = () -> {
            int originalSpeedX = this.speedX;
            int originalSpeedY = this.speedY;
            this.speedX = 0; this.speedY = 0;
            try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }
            if (!this.notValid()) { this.speedX = originalSpeedX; this.speedY = originalSpeedY; }
        };
        new Thread(r).start();
    }

}
