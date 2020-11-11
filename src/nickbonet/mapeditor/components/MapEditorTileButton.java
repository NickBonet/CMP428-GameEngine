package nickbonet.mapeditor.components;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

@SuppressWarnings("java:S110")
public class MapEditorTileButton extends JLabel {
    private transient BufferedImage tileImg;
    private transient BufferedImage objectImg;
    private final int mapRow;
    private final int mapCol;

    public MapEditorTileButton(BufferedImage tileImg, BufferedImage objectImg, int row, int col) {
        this.tileImg = tileImg;
        this.objectImg = objectImg;
        this.mapRow = row;
        this.mapCol = col;
        if(tileImg != null) {
            this.drawIcon();
            this.setPreferredSize(new Dimension(this.tileImg.getWidth(), this.tileImg.getHeight()));
        } else {
            this.setIcon(new ImageIcon());
        }
    }

    public void drawIcon() {
        if(this.objectImg != null) {
            BufferedImage layeredImg = new BufferedImage(tileImg.getWidth(), tileImg.getHeight(), tileImg.getType());
            Graphics test = layeredImg.getGraphics();
            test.drawImage(this.tileImg, 0, 0, null);
            test.drawImage(this.objectImg, 0, 0, null);
            this.setIcon(new ImageIcon(layeredImg));
        }
        else this.setIcon(new ImageIcon(tileImg));
    }

    public void setTileImage(BufferedImage tileImg) {
        this.tileImg = tileImg;
        this.drawIcon();
    }

    public BufferedImage getTileImage() {
        return tileImg;
    }

    public int getMapRow() {
        return mapRow;
    }

    public int getMapCol() {
        return mapCol;
    }

    public void setObjectImg(BufferedImage objectImg) {
        if(objectImg != null) {
            this.objectImg = objectImg;
            this.drawIcon();
        }
    }
}
