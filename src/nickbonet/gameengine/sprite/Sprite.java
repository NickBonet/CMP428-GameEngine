/**
 * Sprite - Basic abstraction of a 2D game sprite.
 * @author Nicholas Bonet
 *
 */
package nickbonet.gameengine.sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import nickbonet.gameengine.Rect;

public abstract class Sprite {
	private static final String spriteDir = "assets/sprites/";
	protected int x;
	protected int y;
	protected Logger logger = Logger.getLogger("GameEngine", null);
	protected String spriteCurrentAnim;
	protected HashMap<String, Animation> animDict = new HashMap<>();
	protected Rect boundsRect;
	
	public Sprite(int x, int y, String spritePrefix, int delay) {
		this.x = x;
		this.y = y;
		loadBaseAnimations(spritePrefix, delay);
		initAnimations();
		this.boundsRect = new Rect(this.x, this.y, 
				getFirstAnimation().getCurrentFrame().getWidth(), 
				getFirstAnimation().getCurrentFrame().getHeight());
	}
	
	protected abstract void initAnimations();
	
	private void loadBaseAnimations(String prefix, int delay) {
		String[] directions = {"up", "down", "left", "right"};
		for (int i = 0; i < directions.length; i++) {
			Animation anim = new Animation(delay, String.join("_", prefix, directions[i]), getSpriteDir());
			animDict.put(directions[i], anim);
		}
	}
	
	public void draw(Graphics g) {
		if (animDict.containsKey(spriteCurrentAnim)) {
			g.drawImage(animDict.get(spriteCurrentAnim).getCurrentFrame(), x, y, null);
		} else {
			Animation firstAnim = getFirstAnimation();
			g.drawImage(firstAnim.getCurrentFrame(), x, y, null);
		}
		
		// For debug purposes.
		g.setColor(Color.blue);
		boundsRect.draw(g);
	}
	
	protected Animation getFirstAnimation() {
		Optional<Animation> firstAnim = animDict.values().stream().findFirst();
		if (firstAnim.isPresent()) {
			return firstAnim.get();
		} else {
			logger.log(Level.SEVERE, "No animations created for {0}!", this.getClass().getSimpleName());
			throw new NoSuchElementException();
		}
	}
	
	public void move(int dx, int dy) {
		x += dx;
		y += dy;
		boundsRect.move(dx, dy);
	}
	
	public Rect getBounds() {
		return boundsRect;
	}
	
	public String getSpriteDir() {
		return spriteDir + this.getClass().getSimpleName().toLowerCase();
	}

	public void setSpriteAnim(String animIndex) {
		this.spriteCurrentAnim = animIndex;
	}
}
