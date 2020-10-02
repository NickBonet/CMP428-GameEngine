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

	public Rect(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.diagX = x + width;
		this.diagY = y + height;
	}
	
	public void move(int dx, int dy) {
		x += dx;
		y += dy;
		diagX += dx;
		diagY += dy;
	}
	
	public boolean overlaps(Rect r, int dx, int dy) {
		return !(this.x + dx > r.diagX || this.y + dy > r.diagY || 
				r.x > this.diagX + dx || r.y > this.diagY + dy); 
	}
	
	public void draw(Graphics g) {
		g.drawRect(x, y, width, height);
	}
}
