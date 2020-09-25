package nickbonet.gameengine;
/**
 * Rectangle - simple abstraction of a rectangle for the engine.
 * @author Nicholas Bonet
 * 
 */

import java.awt.Graphics;

public class Rect {
	int x, y, width, height;
	int diagX, diagY;
	private boolean colliding;
	
	public boolean isColliding() {
		return colliding;
	}

	public void setColliding(boolean colliding) {
		this.colliding = colliding;
	}

	public Rect(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.diagX = x + width;
		this.diagY = y + height;
	}
	
	public void move(int dx, int dy) {
		if (!colliding) {
			x += dx;
			y += dy;
			diagX += dx;
			diagY += dy;
		}
	}
	
	public boolean overlaps(Rect r) {
		return !(this.x > r.diagX || this.y > r.diagY || 
				r.x > this.diagX || r.y > this.diagY); 
	}
	
	public void draw(Graphics g) {
		g.drawRect(x, y, width, height);
	}
}
