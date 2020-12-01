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
        this.boundsRect = new Rect(x + 5, y + 5, 8, 8);
    }

    @Override
    protected void initAnimations() {
        Animation diedAnim = new Animation(150, "died_", getSpriteDirectory());
        animDict.put("died", diedAnim);
    }
}
