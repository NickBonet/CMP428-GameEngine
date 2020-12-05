package nickbonet.gameengine;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Rectangle - simple abstraction of a rectangle for the engine.
 *
 * @author Nicholas Bonet
 */
public class Rect {
    private final int width;
    private final int height;
    private final Rectangle2D bounds;
    private double x;
    private double y;

    public Rect(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        bounds = new Rectangle2D.Double(x, y, width, height);
    }

    public void move(double dx, double dy) {
        x += dx;
        y += dy;
        bounds.setRect(x, y, width, height);
    }

    public boolean overlaps(Rect r, double dx, double dy) {
        return r.getBounds().intersects(((int) x + dx), ((int) y + dy), width, height);
    }

    public void draw(Graphics g) {
        g.drawRect((int) x, (int) y, width, height);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Rectangle2D getBounds() {
        return bounds;
    }
}
