/**
 * Sprite - Basic abstraction of a 2D game sprite.
 * @author Nicholas Bonet
 *
 */
package nickbonet.gameengine.sprite;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import nickbonet.gameengine.Rect;

public abstract class Sprite {
	private int x, y;
	protected Logger logger = Logger.getLogger("GameEngine", null);
	protected String spriteCurrentAnim;
	protected HashMap<String, Animation> animDict = new HashMap<>();
	protected Rect boundsRect;
	
	public Sprite(int x, int y) {
		this.x = x;
		this.y = y;
		initAnimations();
		Animation firstAnim = getFirstAnimation();
		this.boundsRect = new Rect(this.x, this.y, 
				firstAnim.getCurrentFrame().getWidth(), firstAnim.getCurrentFrame().getHeight());
	}
	
	protected abstract void initAnimations();
	
	public void draw(Graphics g) {
		if (animDict.containsKey(spriteCurrentAnim)) {
			g.drawImage(animDict.get(spriteCurrentAnim).getCurrentFrame(), x, y, null);
		} else {
			Animation firstAnim = getFirstAnimation();
			g.drawImage(firstAnim.getCurrentFrame(), x, y, null);
		}
	}
	
	private Animation getFirstAnimation() {
		Optional<Animation> firstAnim = animDict.values().stream().findFirst();
		if (firstAnim.isPresent()) {
			return firstAnim.get();
		} else {
			logger.log(Level.SEVERE, "No animations/images loaded for this sprite!");
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
	
	public String getSpriteAnim() {
		return spriteCurrentAnim;
	}

	public void setSpriteAnim(String animIndex) {
		this.spriteCurrentAnim = animIndex;
	}
}
