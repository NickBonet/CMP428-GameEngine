/**
 * Pacman - The main player sprite.
 * @author Nicholas Bonet
 *
 */
package nickbonet.testgame;

import nickbonet.gameengine.Rect;
import nickbonet.gameengine.sprite.Sprite;

public class Pacman extends Sprite {
	public Pacman(int x, int y, String spritePrefix, int delay) {
		super(x, y, spritePrefix, delay);
		this.boundsRect = new Rect(9, 17, 6, 6);
	}
	
	@Override
	protected void initAnimations() {
		/*
		 * Don't need anything here yet.
		 */
	}
}
