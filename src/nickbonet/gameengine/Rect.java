package nickbonet.gameengine;

import java.awt.*;

/**
 * Rectangle - simple abstraction of a rectangle for the engine.
 *
 * @author Nicholas Bonet
 */
public class Rect {
    private int x;
    private int y;
    private final int width;
    private final int height;
    private int diagonalX;
    private int diagonalY;

    public Rect(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.diagonalX = x + (width - 1);
        this.diagonalY = y + (height - 1);
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
        diagonalX += dx;
        diagonalY += dy;
    }

    public boolean overlaps(Rect r, int dx, int dy) {
        return !(this.x + dx > r.diagonalX || this.y + dy > r.diagonalY ||
                r.x > this.diagonalX + dx || r.y > this.diagonalY + dy);
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
