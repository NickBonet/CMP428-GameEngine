package nickbonet.gameengine.tile;

import nickbonet.gameengine.Rect;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {
    private int width;
    private int height;
    private boolean collisionEnabled = false;
    private boolean isObject = false;
    private BufferedImage tileImage;

    public Tile(int width, int height, BufferedImage image) {
        this.width = width;
        this.height = height;
        this.tileImage = image;
    }

    public void draw(Graphics g, int x, int y) {
        g.drawImage(tileImage, x, y, null);
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

    public boolean isObject() {
        return isObject;
    }

    public void setObject(boolean object) {
        isObject = object;
    }

    public BufferedImage getTileImage() {
        return tileImage;
    }
}
