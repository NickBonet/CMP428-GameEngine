package nickbonet.tilemapeditor.components;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

@SuppressWarnings("java:S110")
public class TileButton extends JButton {
    private transient BufferedImage tileButtonImg;

    public TileButton(BufferedImage tileButtonImg) {
        this.tileButtonImg = tileButtonImg;
        this.setIcon(new ImageIcon(this.tileButtonImg));
        this.setPreferredSize(new Dimension(tileButtonImg.getWidth(), tileButtonImg.getHeight()));
    }

    public void setTileButtonImg(BufferedImage tileButtonImg) {
        this.tileButtonImg = tileButtonImg;
    }
}
