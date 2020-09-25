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
	int prevX, prevY;

	public Rect(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.diagX = x + width;
		this.diagY = y + height;
	}
	
	public void move(int dx, int dy) {
		prevX = x;
		prevY = y;
		x += dx;
		y += dy;
		diagX += dx;
		diagY += dy;
	}
	
	/**
	 * Restores x and y to previous values before a move() call was made.
	 */
	public void undoMove() {
		x = prevX;
		y = prevY;
		diagX = x + width;
		diagY = y + height;
	}
	
	public boolean overlaps(Rect r) {
		return !(this.x > r.diagX || this.y > r.diagY || 
				r.x > this.diagX || r.y > this.diagY); 
	}
	
	public void draw(Graphics g) {
		g.drawRect(x, y, width, height);
	}
}
