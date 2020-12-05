package nickbonet.pacmangame.entity;

import nickbonet.gameengine.sprite.Animation;
import nickbonet.gameengine.sprite.Sprite;

/**
 * Pacman - The main player sprite.
 *
 * @author Nicholas Bonet
 */
public class Pacman extends Sprite {
    private int numberOfLives = 3;

    public Pacman() {
        super(104, 203, "pac", 65, "pacman");
        boundsOffsetX = 4;
        boundsOffsetY = 5;
        boundsWidth = 8;
        boundsHeight = 8;
        initBoundsRect();
        velocity = 0.90;
    }

    @Override
    protected void initAnimations() {
        Animation diedAnim = new Animation(150, "died_", getSpriteDirectory());
        animDict.put("died", diedAnim);
    }

    public int getNumberOfLives() {
        return numberOfLives;
    }

    public void changeNumberOfLives(int numberOfLives) {
        this.numberOfLives += numberOfLives;
    }
}
