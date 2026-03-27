package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

public class HpProp extends AbstractProp {
    public HpProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }
    public void active(HeroAircraft hero) {
        // 恢复 30 血量，但不超过最大血量
        // 仓库代码中 AbstractAircraft 有 hp 和 maxHp
        int heal = 30;
        if (hero.getHp() + heal > 1000) { // 假设初始 1000 是最大值
            hero.decreaseHp(hero.getHp() - 1000);
        } else {
            hero.decreaseHp(-heal);
        }
    }
}
