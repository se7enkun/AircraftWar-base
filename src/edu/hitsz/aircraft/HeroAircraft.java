package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.strategy.ShootStrategy;
import edu.hitsz.strategy.StraightShoot;

import java.util.LinkedList;
import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {

    //每次射击发射子弹数量
    private int shootNum = 1;

    //子弹威力
    private int power = 30;

    //子弹射击方向 (向上发射：-1，向下发射：1)
    private int direction = -1;

    // 1. 添加私有静态变量，保存唯一实例 [cite: 345]
    private volatile static HeroAircraft instance;

    /**
     * 2. 私有化构造方法，防止外部 new 实例
     */
    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.shootStrategy = new StraightShoot(1, -1, 30);
    }

    /**
     * 3. 提供全局公有的静态方法获取实例
     */
    public static HeroAircraft getInstance(int locationX, int locationY, int speedX, int speedY, int hp) {
        if (instance == null) {
            synchronized (HeroAircraft.class) {
                if (instance == null) {
                    // 根据 Main 中的初始设置来初始化
                    instance = new HeroAircraft(locationX, locationY, speedX, speedY, hp);
                }
            }
        }
        return instance;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }


    public ShootStrategy getShootStrategy() {
        return this.shootStrategy;
    }
}
