package nickbonet.testgame;

import nickbonet.gameengine.Rect;
import nickbonet.gameengine.sprite.Animation;
import nickbonet.gameengine.sprite.Sprite;

/**
 * Ghost - Little ghost enemies!
 *
 * @author Nicholas Bonet
 */
public class Ghost extends Sprite {
    private boolean isScared = false;

    public Ghost(int x, int y, String spritePrefix, int delay) {
        super(x, y, spritePrefix, delay);
        this.boundsRect = new Rect(x + 4, y + 5, 8, 8);
    }

    @Override
    protected void initAnimations() {
        Animation scaredAnim = new Animation(100, "scared_", getSpriteDirectory());
        animDict.put("scared", scaredAnim);
    }

    public boolean isScared() {
        return isScared;
    }

    public void setScared(boolean scared) {
        isScared = scared;
    }
}
