package nickbonet.gameengine.tile;

import nickbonet.gameengine.Rect;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {
    private final int width;
    private final int height;
    private final BufferedImage tileImage;
    private final int tileID;
    private int x;
    private int y;
    private Rect boundsRect;
    private boolean collisionOverride = false;
    private boolean collisionEnabled = false;

    public Tile(BufferedImage image, int tileID) {
        this.tileImage = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.tileID = tileID;
    }

    public void draw(Graphics g) {
        g.drawImage(tileImage, x, y, null);
    }

    public void drawBoundsRect(Graphics g) {
        if (collisionEnabled) g.setColor(Color.red);
        else g.setColor(Color.green);
        boundsRect.draw(g);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isCollisionEnabled() {
        return collisionEnabled;
    }

    public void setCollisionEnabled(boolean collisionEnabled) {
        this.collisionEnabled = collisionEnabled;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rect getBoundsRect() {
        return boundsRect;
    }

    public void initBoundsRect() {
        this.boundsRect = new Rect(x, y, width, height);
    }

    public int getTileID() {
        return tileID;
    }

    public boolean isCollisionOverride() {
        return collisionOverride;
    }

    public void setCollisionOverride(boolean collisionOverride) {
        this.collisionOverride = collisionOverride;
    }
}
