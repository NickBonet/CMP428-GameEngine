package nickbonet.pacmangame.entity;

import nickbonet.gameengine.sprite.Animation;
import nickbonet.gameengine.sprite.Sprite;

import java.util.HashMap;

public class Fruit extends Sprite {
    private static final HashMap<String, Integer> POINT_VALUES;
    private static String fruitName;

    static {
        POINT_VALUES = new HashMap<>();
        POINT_VALUES.put("cherry", 100);
        POINT_VALUES.put("strawberry", 300);
        POINT_VALUES.put("peach", 500);
        POINT_VALUES.put("apple", 700);
        POINT_VALUES.put("grape", 1000);
        POINT_VALUES.put("galaxian", 2000);
        POINT_VALUES.put("bell", 3000);
        POINT_VALUES.put("key", 5000);
    }

    private final int pointValue;

    public Fruit() {
        super(104, 156, "", 0, "items");
        boundsOffsetX = 4;
        boundsOffsetY = 5;
        boundsWidth = 8;
        boundsHeight = 8;
        initBoundsRect();
        pointValue = POINT_VALUES.get(fruitName);
        Animation stillAnimation = new Animation(0, fruitName, getSpriteDirectory());
        animDict.put("image", stillAnimation);
    }

    public static String setFruitForLevel(int level) {
        if (level == 1) {
            fruitName = "cherry";
        } else if (level == 2) {
            fruitName = "strawberry";
        } else if (level == 3 || level == 4) {
            fruitName = "peach";
        } else if (level == 5 || level == 6) {
            fruitName = "apple";
        } else if (level == 7 || level == 8) {
            fruitName = "grape";
        } else if (level == 9 || level == 10) {
            fruitName = "galaxian";
        } else if (level == 11 || level == 12) {
            fruitName = "bell";
        } else if (level > 12) {
            fruitName = "key";
        }
        return fruitName;
    }

    public static String getFruitName() {
        return fruitName;
    }

    @Override
    protected void loadBaseAnimations(String prefix, int delay) {
        /**
         * Not an animated sprite.
         */
    }

    @Override
    protected void initAnimations() {
        /**
         * Executed too early for this purpose.
         */
    }

    public int getPointValue() {
        return pointValue;
    }
}
