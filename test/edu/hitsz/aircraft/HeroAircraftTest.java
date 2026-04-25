package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("HeroAircraft 单元测试")
class HeroAircraftTest {

    private HeroAircraft heroAircraft;

    @BeforeEach
    void setUp() {
        // 由于 HeroAircraft 是单例，每次测试前获取实例
        // 注意：单例模式下，不同测试用例可能会相互影响 HP 等状态
        heroAircraft = HeroAircraft.getInstance(256, 600, 0, 0, 1000);
        // 手动重置 HP，保证每个测试用例的初始状态一致
        // 如果没有 setHp 方法，可以利用 decreaseHp 补满或重新设计
    }

    @Test
    @DisplayName("测试单例模式：验证获取的实例是否唯一")
    void testSingleton() {
        HeroAircraft anotherHero = HeroAircraft.getInstance(0, 0, 0, 0, 100);
        assertSame(heroAircraft, anotherHero, "多次获取单例应指向同一内存地址");
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 100, 500})
    @DisplayName("测试减血逻辑：正常扣除生命值")
    void testDecreaseHp(int damage) {
        int initialHp = heroAircraft.getHp();
        heroAircraft.decreaseHp(damage);
        int expectedHp = Math.max(0, initialHp - damage);
        assertEquals(expectedHp, heroAircraft.getHp(), "血量扣除计算或边界处理错误");
    }

    @Test
    @DisplayName("测试减血边界：血量不会低于 0 且会消失")
    void testDecreaseHpToZero() {
        heroAircraft.decreaseHp(2000); // 远超初始 1000
        assertEquals(0, heroAircraft.getHp(), "血量扣除后最低应为 0");
        assertTrue(heroAircraft.notValid(), "血量为 0 时飞机应标记为无效(消失)");
    }

    @Test
    @DisplayName("测试射击功能：验证是否产生子弹")
    void testShoot() {
        List<BaseBullet> bullets = heroAircraft.shoot();
        assertNotNull(bullets, "射击产生的子弹列表不应为 null");
        assertFalse(bullets.isEmpty(), "英雄机射击应至少产生一颗子弹");
        // 进一步验证子弹威力（根据当前代码，初始威力应为 30）
        assertEquals(30, bullets.get(0).getPower(), "子弹威力与预期不符");
    }

    @AfterEach
    void tearDown() {
        heroAircraft = null;
    }
}