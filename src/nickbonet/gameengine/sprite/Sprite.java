package nickbonet.gameengine.sprite;

import nickbonet.gameengine.Rect;

import java.awt.*;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sprite - Basic abstraction of a 2D game sprite.
 *
 * @author Nicholas Bonet
 */
public abstract class Sprite {
    private static final String SPRITE_FOLDER = "assets/sprites/";
    protected int x;
    protected int y;
    protected Logger logger = Logger.getLogger("GameEngine", null);
    protected SpriteDir currentDirection;
    protected String currentAnimation = "";
    protected HashMap<String, Animation> animDict = new HashMap<>();
    protected Rect boundsRect;
    protected int velocity;

    public Sprite(int x, int y, String spritePrefix, int delay) {
        this.x = x;
        this.y = y;
        velocity = 1;
        loadBaseAnimations(spritePrefix, delay);
        initAnimations();
        this.boundsRect = new Rect(this.x, this.y,
                getFirstAnimation().getCurrentFrame().getWidth(),
                getFirstAnimation().getCurrentFrame().getHeight());
    }

    // Takes care of initializing animations for the 4 basic directions the sprite would face.
    // Can always override this to fit the needs of your sprite.
    protected void loadBaseAnimations(String prefix, int delay) {
        String[] directions = {"up", "down", "left", "right"};
        for (String direction : directions) {
            Animation anim = new Animation(delay, String.join("_", prefix, direction), getSpriteDirectory());
            animDict.put(direction, anim);
        }
    }

    // For initializing anymore animations besides 4 basic ones for the sprite.
    protected abstract void initAnimations();

    private Animation getFirstAnimation() {
        Optional<Animation> firstAnim = animDict.values().stream().findFirst();
        if (firstAnim.isPresent()) {
            return firstAnim.get();
        } else {
            logger.log(Level.SEVERE, "No animations created for {0}!", this.getClass().getSimpleName());
            throw new NoSuchElementException();
        }
    }

    public String getSpriteDirectory() {
        return SPRITE_FOLDER + this.getClass().getSimpleName().toLowerCase();
    }

    // Draws the sprite's current image based on its current state.
    public void draw(Graphics g) {
        if (animDict.containsKey(currentAnimation)) {
            g.drawImage(animDict.get(currentAnimation).getCurrentFrame(), x, y, null);
        } else {
            Animation firstAnim = getFirstAnimation();
            g.drawImage(firstAnim.getCurrentFrame(), x, y, null);
        }

        // For debug purposes, draw the bounding box of the sprite.
        g.setColor(Color.blue);
        boundsRect.draw(g);
    }

    public void move() {
        switch (currentDirection) {
            case UP:
                y -= velocity;
                boundsRect.move(0, -velocity);
                break;
            case DOWN:
                y += velocity;
                boundsRect.move(0, +velocity);
                break;
            case LEFT:
                x -= velocity;
                boundsRect.move(-velocity, 0);
                break;
            case RIGHT:
                x += velocity;
                boundsRect.move(velocity, 0);
                break;
            default:
                break;
        }
    }

    public Rect getBounds() {
        return boundsRect;
    }

    public SpriteDir getSpriteDirection() {
        return currentDirection;
    }

    public void setSpriteDirection(SpriteDir currentDirection) {
        this.currentDirection = currentDirection;
    }

    public int getVelocity() {
        return velocity;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setCurrentAnimation(String currentAnimation) {
        this.currentAnimation = currentAnimation;
    }
}
