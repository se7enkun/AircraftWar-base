package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.basic.AbstractFlyingObject;

/**
 * 道具基类
 */
public abstract class AbstractProp extends AbstractFlyingObject implements Item {

    public AbstractProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    // 这里不再实现具体逻辑，留给子类实现
    @Override
    public abstract void active(HeroAircraft hero);
}