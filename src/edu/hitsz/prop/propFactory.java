package edu.hitsz.prop;

/**
 * 简单工厂：负责道具的创建
 */
public class propFactory {
    public static Item createProp(String type, int x, int y, int speedX, int speedY) {
        switch (type) {
            case "HP":
                return new HpProp(x, y, speedX, speedY);
            case "BOMB":
                return new BombProp(x, y, speedX, speedY);
            case "FIRE":
                return new FireProp(x, y, speedX, speedY);
            case "SUPER_FIRE":
                return new SuperFireProp(x, y, speedX, speedY);
            default:
                throw new IllegalArgumentException("Unknown Prop Type: " + type);
        }
    }
}