package nickbonet.pacmangame.entity;

import nickbonet.gameengine.sprite.Animation;
import nickbonet.gameengine.sprite.Sprite;

public class PowerPellet extends Sprite {
    public PowerPellet(int x, int y) {
        super(x, y, "powerpellet", 0, "items");
        boundsOffsetX = 3;
        boundsOffsetY = 4;
        boundsWidth = 8;
        boundsHeight = 8;
        initBoundsRect();
    }

    @Override
    protected void loadBaseAnimations(String prefix, int delay) {
        /**
         * Not an animated sprite.
         */
    }

    @Override
    protected void initAnimations() {
        Animation powerPellet = new Animation(200, "powerpellet", getSpriteDirectory());
        animDict.put("image", powerPellet);
    }
}
