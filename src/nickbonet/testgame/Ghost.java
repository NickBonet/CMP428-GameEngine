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
    private int targetX;
    private int targetY;

    public Ghost(int x, int y, String spritePrefix, int delay, int targetX, int targetY) {
        super(x, y, spritePrefix, delay);
        this.boundsRect = new Rect(x + 4, y + 5, 8, 8);
        this.targetX = targetX;
        this.targetY = targetY;
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

    public int getTargetX() {
        return targetX;
    }

    public void setTargetX(int targetX) {
        this.targetX = targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    public void setTargetY(int targetY) {
        this.targetY = targetY;
    }
}
