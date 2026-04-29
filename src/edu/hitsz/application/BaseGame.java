package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.aircraft.enemy.AceEnemy;
import edu.hitsz.aircraft.enemy.BossEnemy;
import edu.hitsz.aircraft.enemy.EliteEnemy;
import edu.hitsz.aircraft.enemy.ExcellentEnemy;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.EnemyFactory;
import edu.hitsz.observer.EnemyObserver;
import edu.hitsz.observer.PropSubject;
import edu.hitsz.prop.*;
import edu.hitsz.record.Record;
import edu.hitsz.record.RecordDao;
import edu.hitsz.record.RecordDaoImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * 游戏主面板模板基类
 * 采用模板方法模式定义游戏主循环骨架，提供不同的难度扩展挂载点
 */
public abstract class BaseGame extends JPanel {

    protected int backGroundTop = 0;
    protected BufferedImage backgroundImage;
    protected final Timer timer;
    protected final int timeInterval = 40;
    protected int timeTotal = 0;

    protected final HeroAircraft heroAircraft;
    protected final List<AbstractAircraft> enemyAircrafts;
    protected final List<BaseBullet> heroBullets;
    protected final List<BaseBullet> enemyBullets;
    protected List<AbstractProp> props = new LinkedList<>();

    // --- 难度挂载参数 ---
    protected int enemyMaxNumber;
    protected double enemySpawnCycle;
    protected double heroShootCycle = 20;  // 英雄机射击周期
    protected double enemyShootCycle = 20; // 敌机射击周期
    protected double enemyHpMultiplier = 1.0;
    protected int enemySpeedIncrease = 0;
    protected int bossScoreThreshold;
    protected boolean bossExists = false;

    protected int enemySpawnCounter = 0;
    protected int heroShootCounter = 0;
    protected int enemyShootCounter = 0;
    protected int score = 0;
    protected boolean gameOverFlag = false;

    protected MusicThread bgmThread;
    protected MusicThread bossBgmThread;
    protected final String videoPath = "src/videos/";

    public BaseGame() {
        heroAircraft = HeroAircraft.getInstance(
                Main.WINDOW_WIDTH / 2,
                Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                0, 0, 1000);

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();

        // 启动英雄机鼠标监听
        new HeroController(this, heroAircraft);
        this.timer = new Timer("game-action-timer", true);

        // 调用钩子方法初始化各难度特有参数
        initParams();
    }

    protected void playSound(String fileName) {
        new MusicThread(videoPath + fileName, false).start();
    }

    /**
     * 模板方法：游戏启动入口，执行固定的游戏逻辑骨架
     */
    public final void action() {
        // 游戏开始，循环播放常规 BGM
        bgmThread = new MusicThread(videoPath + "bgm.wav", true);
        bgmThread.start();

        // 定时任务：绘制、对象产生、碰撞判定、及结束判定
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                timeTotal += timeInterval;
                updateDifficulty();     // 钩子：随着时间更新难度

                spawnEnemy();           // 步骤 1：产生敌机
                shootAction();          // 步骤 2：射击
                bulletsMoveAction();    // 步骤 3：子弹移动
                aircraftsMoveAction();  // 步骤 4：飞行物（飞机、道具）移动
                crashCheckAction();     // 步骤 5：撞击检测
                postProcessAction();    // 步骤 6：后处理（清理无效实体）
                repaint();              // 步骤 7：重绘界面
                checkResultAction();    // 步骤 8：游戏结束检查
            }
        };
        // 以固定延迟时间进行执行
        timer.schedule(task, 0, timeInterval);
    }

    // ==========================================
    // 抽象方法与钩子方法（交由不同难度子类实现）
    // ==========================================

    protected abstract void initParams();
    protected abstract void updateDifficulty();
    protected abstract boolean bossCondition();
    protected abstract void createBoss();
    protected abstract EnemyFactory getEnemyFactoryByProb();

    // ==========================================
    // 具体步骤实现
    // ==========================================

    protected void spawnEnemy() {
        enemySpawnCounter++;
        if (enemySpawnCounter >= enemySpawnCycle) {
            enemySpawnCounter = 0;
            if (enemyAircrafts.size() < enemyMaxNumber) {
                if (bossCondition()) {
                    createBoss();
                } else {
                    EnemyFactory factory = getEnemyFactoryByProb();
                    AbstractAircraft enemy = factory.createEnemy();
                    // 应用难度加成
                    enemy.setHp((int) (enemy.getHp() * enemyHpMultiplier));
                    enemy.setSpeedY(enemy.getSpeedY() + enemySpeedIncrease);
                    enemyAircrafts.add(enemy);
                }
            }
        }
    }

    protected void shootAction() {
        // 英雄机射击
        heroShootCounter++;
        if (heroShootCounter >= heroShootCycle) {
            heroShootCounter = 0;
            heroBullets.addAll(heroAircraft.shoot());
        }

        // 敌机射击
        enemyShootCounter++;
        if (enemyShootCounter >= enemyShootCycle) {
            enemyShootCounter = 0;
            for (AbstractAircraft enemy : enemyAircrafts) {
                enemyBullets.addAll(enemy.shoot());
            }
        }
    }

    protected void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    protected void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
        for (AbstractProp prop : props) {
            prop.forward();
        }
    }

    protected void crashCheckAction() {
        // 敌机子弹攻击英雄机
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) continue;
            if (heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) continue;
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) continue;

                if (enemyAircraft.crash(bullet)) {
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        playSound("bullet.wav");
                        score += 10;
                        handleEnemyDestroyed(enemyAircraft);
                    }
                }

                // 英雄机与敌机相撞
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // 我方获得道具，道具生效
        propHitAction();
    }

    /**
     * 敌机坠毁时处理 Boss 死亡或普通敌机掉落逻辑
     */
    private void handleEnemyDestroyed(AbstractAircraft enemyAircraft) {
        if (enemyAircraft instanceof BossEnemy) {
            if (bossBgmThread != null) {
                bossBgmThread.stopMusic();
                bossBgmThread = null;
            }
            bgmThread = new MusicThread(videoPath + "bgm.wav", true);
            bgmThread.start();
            bossExists = false;

            // Boss 掉落 3 个随机道具
            String[] allTypes = {"HP", "FIRE", "SUPER_FIRE", "BOMB", "FREEZE"};
            Random r = new Random();
            for (int i = 0; i < 3; i++) {
                String randomType = allTypes[r.nextInt(allTypes.length)];
                AbstractProp prop = (AbstractProp) propFactory.createProp(
                        randomType,
                        enemyAircraft.getLocationX() + (i - 1) * 30,
                        enemyAircraft.getLocationY(),
                        0, 2);
                props.add(prop);
            }
        } else {
            String randomType = null;
            double propRand = Math.random();

            if (enemyAircraft instanceof AceEnemy) {
                if (propRand < 0.6) { // 60% 掉落率
                    String[] types = {"HP", "FIRE", "SUPER_FIRE", "BOMB", "FREEZE"};
                    randomType = types[(int) (Math.random() * types.length)];
                }
            } else if (enemyAircraft instanceof ExcellentEnemy || enemyAircraft instanceof EliteEnemy) {
                if (propRand < 0.3) { // 30% 掉落率
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

    /**
     * 道具碰撞检测与观察者模式注册
     */
    protected void propHitAction() {
        for (AbstractProp prop : props) {
            if (prop.notValid()) continue;

            if (heroAircraft.crash(prop)) {
                // 如果是支持观察者模式的道具（炸弹、冰冻）
                if (prop instanceof PropSubject) {
                    PropSubject subject = (PropSubject) prop;

                    // 将当前存活的敌机注册为观察者
                    for (AbstractAircraft enemy : enemyAircrafts) {
                        if (!enemy.notValid() && enemy instanceof EnemyObserver) {
                            subject.addObserver((EnemyObserver) enemy);

                            // 炸弹额外计分逻辑：记录当前在场能被炸毁的普通敌机分数
                            if (prop instanceof BombProp && !(enemy instanceof BossEnemy)) {
                                score += 10;
                            }
                        }
                    }
                    // 将当前的敌机子弹注册为观察者
                    for (BaseBullet bullet : enemyBullets) {
                        if (!bullet.notValid() && bullet instanceof EnemyObserver) {
                            subject.addObserver((EnemyObserver) bullet);
                        }
                    }
                }

                prop.active(heroAircraft);

                if (prop instanceof BombProp) {
                    playSound("bomb_explosion.wav");
                } else {
                    playSound("get_supply.wav");
                }

                prop.vanish();
            }
        }
    }

    protected void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);
    }

    protected void checkResultAction() {
        if (heroAircraft.getHp() <= 0) {
            if (bgmThread != null) bgmThread.stopMusic();
            if (bossBgmThread != null) bossBgmThread.stopMusic();

            playSound("game_over.wav");

            timer.cancel();
            gameOverFlag = true;
            System.out.println("Game Over!");

            String userName = JOptionPane.showInputDialog(this, "游戏结束，你的得分为 " + score + "。\n请输入玩家姓名：");
            if (userName == null || userName.trim().isEmpty()) {
                userName = "Anonymous";
            }

            RecordDao recordDao = new RecordDaoImpl(Main.difficulty);
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
            Record newRecord = new Record(userName, this.score, now.format(formatter));
            recordDao.doAdd(newRecord);

            ScoreTable scoreTable = new ScoreTable(recordDao);
            Main.cardPanel.add(scoreTable.getMainPanel(), "score");
            Main.cardLayout.show(Main.cardPanel, "score");
        }
    }

    // ==========================================
    // 界面绘制机制
    // ==========================================

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 使用不同难度对应的背景图
        if (this.backgroundImage != null) {
            g.drawImage(this.backgroundImage, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
            g.drawImage(this.backgroundImage, 0, this.backGroundTop, null);
        }

        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);
        paintImageWithPositionRevised(g, enemyAircrafts);
        paintImageWithPositionRevised(g, props);

        g.drawImage(ImageManager.HERO_IMAGE,
                heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

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