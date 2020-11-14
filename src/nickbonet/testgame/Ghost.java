package nickbonet.testgame;

import nickbonet.gameengine.Rect;
import nickbonet.gameengine.sprite.Sprite;

/**
 * Ghost - Little ghost enemies!
 * @author Nicholas Bonet
 *
 */
public class Ghost extends Sprite {

	public Ghost(int x, int y, String spritePrefix, int delay) {
		super(x, y, spritePrefix, delay);
		this.boundsRect = new Rect(x+4, y+5, 8, 8);
	}

	@Override
	protected void initAnimations() {
		/*
		 * Not needed currently.
		 */
	}
}
