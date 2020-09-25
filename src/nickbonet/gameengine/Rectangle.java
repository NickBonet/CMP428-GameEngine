package nickbonet.gameengine;
/**
 * Rectangle - simple abstraction of a rectangle for the engine.
 * @author Nicholas Bonet
 * 
 */

import java.awt.Graphics;

public class Rectangle {
	int x, y, width, height;
	
	public Rectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void move(int dx, int dy) {
		x += dx;
		y += dy;
	}
	
	public void draw(Graphics g) {
		g.drawRect(x, y, width, height);
	}
}
