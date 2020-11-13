package nickbonet.gameengine;

import java.awt.Graphics;

/**
 * Rectangle - simple abstraction of a rectangle for the engine.
 * @author Nicholas Bonet
 *
 */
public class Rect {
	private int x;
	private int y;
	private int width;
	private int height;
	private int diagX;
	private int diagY;

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

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
