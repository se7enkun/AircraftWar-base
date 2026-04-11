package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.aircraft.enemy.AceEnemy;
import edu.hitsz.aircraft.enemy.BossEnemy;
import edu.hitsz.aircraft.enemy.ExcellentEnemy;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.aircraft.enemy.EliteEnemy;
import edu.hitsz.prop.*;


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.*;

/**
 * 游戏主面板，游戏启动
 * @author hitsz
 */
public class Game extends JPanel {

    private int backGroundTop = 0;
    private List<AbstractProp> props = new LinkedList<>();
    //private List<Item> props = new LinkedList<>(); // 使用接口类型作为容器
    //调度器, 用于定时任务调度
    private final Timer timer;
    //时间间隔(ms)，控制刷新频率
    private final int timeInterval = 40;

    private final HeroAircraft heroAircraft;
    private final List<AbstractAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;

    //屏幕中出现的敌机最大数量
    private final int enemyMaxNumber = 5;

    //敌机生成周期
    protected double enemySpawnCycle  =  20;
    private int enemySpawnCounter = 0;

    //英雄机和敌机射击周期
    protected double shootCycle = 20;
    private int shootCounter = 0;

    //当前玩家分数
    private int score = 0;

    private boolean bossExists = false;
    private int bossScoreThreshold = 200;

    //游戏结束标志
    private boolean gameOverFlag = false;

    public Game() {
        heroAircraft =HeroAircraft.getInstance(Main.WINDOW_WIDTH / 2,
                Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                0, 0, 1000);

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

        this.timer = new Timer("game-action-timer", true);

    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {

        // 定时任务：绘制、对象产生、碰撞判定、及结束判定
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                enemySpawnCounter++;
                if (enemySpawnCounter >=enemySpawnCycle) {
                    enemySpawnCounter = 0;
                    // 产生敌机
                    if (enemyAircrafts.size() < enemyMaxNumber) {
                        edu.hitsz.factory.EnemyFactory enemyFactory; // 声明抽象工厂接口
                        if (score >= bossScoreThreshold && !bossExists) {
                            enemyFactory = new BossEnemyFactory();
                            bossExists = true;
                            bossScoreThreshold += 500;
                        }
                        else{
                            double rand = Math.random();

                        // 根据概率决定使用哪个具体工厂
                            if (rand < 0.15) {
                                enemyFactory = new AceEnemyFactory();
                            } else if (rand < 0.25) {
                                enemyFactory = new ExcellentEnemyFactory();
                            } else if (rand < 0.45) {
                                enemyFactory = new EliteEnemyFactory();
                            } else {
                                enemyFactory = new MobEnemyFactory();
                            }
                        }

                        // 使用工厂生产敌机，不再直接 new 具体类
                        enemyAircrafts.add(enemyFactory.createEnemy());
                    }
                }


                // 飞机发射子弹
                shootAction();
                // 子弹移动
                bulletsMoveAction();
                // 飞机移动
                aircraftsMoveAction();
                // 撞击检测
                crashCheckAction();
                // 后处理
                postProcessAction();
                // 重绘界面
                repaint();
                // 游戏结束检查
                checkResultAction();
            }
        };
        // 以固定延迟时间进行执行：本次任务执行完成后，延迟 timeInterval 再执行下一次
        timer.schedule(task,0,timeInterval);

    }

    //***********************
    //      Action 各部分
    //***********************



    private void shootAction() {
        shootCounter++;
        if (shootCounter >= shootCycle) {
            shootCounter = 0;
            //英雄机射击
            heroBullets.addAll(heroAircraft.shoot());
            // TODO 敌机射击
            for (AbstractAircraft enemy : enemyAircrafts) {
                enemyBullets.addAll(enemy.shoot());
            }
        }
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
        // 新增：道具移动
        for (AbstractProp prop : props) {
            prop.forward();
        }
    }


    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // TODO 敌机子弹攻击英雄机
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                // 英雄机损毁一定生命值
                heroAircraft.decreaseHp(bullet.getPower());
                // 子弹消失
                bullet.vanish();
            }
        }
        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        score += 10;
                        // --- 新增 Boss 死亡逻辑 ---
                        if (enemyAircraft instanceof BossEnemy) {
                            bossExists = false;

                            // Boss 掉落 3 个随机道具
                            String[] allTypes = {"HP", "FIRE", "SUPER_FIRE", "BOMB", "FREEZE"};
                            Random r = new Random();
                            for (int i = 0; i < 3; i++) {
                                String randomType = allTypes[r.nextInt(allTypes.length)];
                                // locationX 做轻微偏移 (+30, 0, -30)，防止三个道具重叠在一起
                                AbstractProp prop = (AbstractProp) propFactory.createProp(
                                        randomType,
                                        enemyAircraft.getLocationX() + (i - 1) * 30,
                                        enemyAircraft.getLocationY(),
                                        0, 2);
                                props.add(prop);
                            }
                        }
                        else{
                            String randomType = null;
                            double propRand = Math.random();

                            if (enemyAircraft instanceof AceEnemy) {
                                // 王牌机：全 5 种道具 (假设增加了 FREEZE)
                                if (propRand < 0.9) { // 60% 掉落率
                                    String[] types = {"HP", "FIRE", "SUPER_FIRE", "BOMB", "FREEZE"};
                                    randomType = types[(int) (Math.random() * types.length)];
                                }
                            } else if (enemyAircraft instanceof ExcellentEnemy || enemyAircraft instanceof EliteEnemy) {
                                // 精锐/精英机：4 种道具 (不含 FREEZE)
                                if (propRand < 0.8) { // 30% 掉落率
                                    String[] types = {"HP", "FIRE", "SUPER_FIRE", "BOMB"};
                                    randomType = types[(int) (Math.random() * types.length)];
                                }
                            }

                            if (randomType != null) {
                                AbstractProp prop = (AbstractProp) propFactory.createProp(
                                        randomType, enemyAircraft.getLocationX(), enemyAircraft.getLocationY(), 0, 2);
                                props.add(prop);
                            }
                        }
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // Todo: 我方获得道具，道具生效
        //道具碰撞检测
        propHitAction();
    }
    private void propHitAction() {
        for (AbstractProp prop : props) {
            if (prop.notValid()) { continue; }
            if (heroAircraft.crash(prop)) {
                prop.active(heroAircraft); // 触发效果
                prop.vanish();            // 道具消失
            }
        }
    }
    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 删除无效的道具
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        // Todo: 删除无效道具
        props.removeIf(AbstractFlyingObject::notValid);
    }

    /**
     * 检查游戏是否结束，若结束：关闭线程池
     */
    private void checkResultAction(){
        // 游戏结束检查英雄机是否存活
        if (heroAircraft.getHp() <= 0) {
            timer.cancel(); // 取消定时器并终止所有调度任务
            gameOverFlag = true;
            System.out.println("Game Over!");
        }
    }

    //***********************
    //      Paint 各部分
    //***********************
    /**
     * 重写 paint方法
     * 通过重复调用paint方法，实现游戏动画
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景,图片滚动
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);
        paintImageWithPositionRevised(g, enemyAircrafts);

        // Todo: 绘制道具
        // 在绘制飞机之前绘制道具
        paintImageWithPositionRevised(g, props);
        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.isEmpty()) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE: " + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE: " + this.heroAircraft.getHp(), x, y);
    }

}
