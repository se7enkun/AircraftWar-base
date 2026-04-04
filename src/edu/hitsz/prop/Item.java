package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;


public interface Item {
    /**
     * 道具生效方法
     * @param hero 传入英雄机引用，以便修改英雄机状态
     */
    void active(HeroAircraft hero);
}