package nickbonet.mapeditor.components;

import nickbonet.gameengine.tile.Tile;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("java:S110")
public class MapEditorTileButton extends JLabel {
    private transient Tile tile;
    private int mapRow;
    private int mapCol;

    public MapEditorTileButton(Tile tile, int row, int col) {
        this.tile = tile;
        this.mapRow = row;
        this.mapCol = col;
        this.setIcon(new ImageIcon(this.tile.getTileImage()));
        this.setPreferredSize(new Dimension(this.tile.getTileImage().getWidth(), this.tile.getTileImage().getHeight()));
    }

    public void setTile(Tile tile) {
        if (tile != null) {
            this.tile = tile;
            this.setIcon(new ImageIcon((this.tile.getTileImage())));
        }
    }

    public Tile getTile() {
        return tile;
    }

    public int getMapRow() {
        return mapRow;
    }

    public int getMapCol() {
        return mapCol;
    }
}
