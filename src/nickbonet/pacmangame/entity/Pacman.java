package nickbonet.pacmangame.entity;

import nickbonet.gameengine.Rect;
import nickbonet.gameengine.sprite.Animation;
import nickbonet.gameengine.sprite.Sprite;

/**
 * Pacman - The main player sprite.
 *
 * @author Nicholas Bonet
 */
public class Pacman extends Sprite {
    public Pacman(int x, int y) {
        super(x, y, "pac", 65, "pacman");
        boundsOffsetX = 4;
        boundsOffsetY = 5;
        boundsWidth = 8;
        boundsHeight = 8;
        initBoundsRect();
    }

    @Override
    protected void initAnimations() {
        Animation diedAnim = new Animation(150, "died_", getSpriteDirectory());
        animDict.put("died", diedAnim);
    }
}
