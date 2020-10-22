/**
 * Ghost - Little ghost enemies!
 * @author Nicholas Bonet
 *
 */
package nickbonet.testgame;

import nickbonet.gameengine.Rect;
import nickbonet.gameengine.sprite.Sprite;

public class Ghost extends Sprite {

	public Ghost(int x, int y, String spritePrefix, int delay) {
		super(x, y, spritePrefix, delay);
		this.boundsRect = new Rect(this.x, this.y, 40, 40);
	}

	@Override
	protected void initAnimations() {
		/*
		 * Not needed currently.
		 */
	}
}
