package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

public class Freeze extends AbstractProp {
    public Freeze(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void active(HeroAircraft hero) {
        System.out.println("Freeze active!");
    }
}