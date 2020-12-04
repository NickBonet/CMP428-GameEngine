package nickbonet.pacmangame.entity;

import nickbonet.gameengine.sprite.Animation;
import nickbonet.gameengine.sprite.Sprite;

public class Fruit extends Sprite {
    private String fruitName;
    private int pointValue;

    public Fruit(int level) {
        super(104, 156, "", 0, "items");
        boundsOffsetX = 4;
        boundsOffsetY = 5;
        boundsWidth = 8;
        boundsHeight = 8;
        initBoundsRect();
        getFruitForLevel(level);
        Animation stillAnimation = new Animation(0, fruitName, getSpriteDirectory());
        animDict.put("image", stillAnimation);
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

    private void getFruitForLevel(int level) {
        if (level == 1) {
            fruitName = "cherry";
            pointValue = 100;
        } else if (level == 2) {
            fruitName = "strawberry";
            pointValue = 300;
        } else if (level == 3 || level == 4) {
            fruitName = "peach";
            pointValue = 500;
        } else if (level == 5 || level == 6) {
            fruitName = "apple";
            pointValue = 700;
        } else if (level == 7 || level == 8) {
            fruitName = "grape";
            pointValue = 1000;
        } else if (level == 9 || level == 10) {
            fruitName = "galaxian";
            pointValue = 2000;
        } else if (level == 11 || level == 12) {
            fruitName = "bell";
            pointValue = 3000;
        } else if (level > 12) {
            fruitName = "key";
            pointValue = 5000;
        }
    }
}
