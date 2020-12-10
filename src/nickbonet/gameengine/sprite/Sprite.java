package nickbonet.gameengine.sprite;

import nickbonet.gameengine.GamePanel;
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
    private static final String BASE_SPRITE_FOLDER = "assets/sprites/";
    protected Logger logger = Logger.getLogger("GameEngine", null);
    protected HashMap<String, Animation> animDict = new HashMap<>();
    protected String spriteFolder;
    protected int spawnX;
    protected int spawnY;
    protected double x;
    protected double y;
    protected double velocity;
    protected int boundsOffsetX = 0;
    protected int boundsOffsetY = 0;
    protected int boundsWidth;
    protected int boundsHeight;
    protected SpriteDir currentDirection;
    protected String currentAnimation = "";
    protected boolean isMoving;
    protected boolean canTraverseOverrideTiles = false;
    protected boolean isVisible = true;
    protected Rect boundsRect;

    protected Sprite(double x, double y, String spritePrefix, int delay, String spriteFolder) {
        this.x = x;
        this.y = y;
        this.spawnX = (int) x;
        this.spawnY = (int) y;
        this.spriteFolder = spriteFolder;
        velocity = 1;
        loadBaseAnimations(spritePrefix, delay);
        initAnimations();
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

    protected void initBoundsRect() {
        this.boundsRect = new Rect(this.x + boundsOffsetX, this.y + boundsOffsetY, boundsWidth, boundsHeight);
    }

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
        return BASE_SPRITE_FOLDER + spriteFolder;
    }

    // Draws the sprite's current image based on its current state.
    public void draw(Graphics g) {
        if (isVisible) {
            if (animDict.containsKey(currentAnimation)) {
                if (isMoving) g.drawImage(animDict.get(currentAnimation).getCurrentFrame(), (int) x, (int) y, null);
                else g.drawImage(animDict.get(currentAnimation).getFirstFrame(), (int) x, (int) y, null);
            } else {
                Animation firstAnim = getFirstAnimation();
                g.drawImage(firstAnim.getCurrentFrame(), (int) x, (int) y, null);
            }

            // For debug purposes, draw the bounding box of the sprite.
            if (System.getProperty(GamePanel.DEBUG_PROPERTY_NAME).equals("true")) {
                g.setColor(Color.blue);
                boundsRect.draw(g);
            }
        }
    }

    public void move() {
        if (isMoving)
            switch (currentDirection) {
                case UP:
                    y -= velocity;
                    boundsRect.move(0, -velocity);
                    break;
                case DOWN:
                    y += velocity;
                    boundsRect.move(0, velocity);
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

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public void setNewLocation(double x, double y) {
        this.x = x;
        this.y = y;
        initBoundsRect();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setCurrentAnimation(String currentAnimation) {
        this.currentAnimation = currentAnimation;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public void restartAnimation(String animation) {
        animDict.get(animation).stopAnimation();
        animDict.get(animation).startAnimation();
    }

    public void stopAnimation(String animation) {
        animDict.get(animation).stopAnimation();
    }

    public void respawn() {
        setNewLocation(spawnX, spawnY);
    }

    public boolean canTraverseOverrideTiles() {
        return canTraverseOverrideTiles;
    }

    public void setCanTraverseOverrideTiles(boolean canTraverseOverrideTiles) {
        this.canTraverseOverrideTiles = canTraverseOverrideTiles;
    }
}
