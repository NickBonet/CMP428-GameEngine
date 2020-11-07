package nickbonet.mapeditor.components;

import nickbonet.gameengine.tile.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("java:S110")
public class MapEditorTileButton extends JLabel {
    private transient Tile tile;
    private boolean isSelected;
    private int mapRow;
    private int mapCol;

    public MapEditorTileButton(Tile tile, int row, int col) {
        this.tile = tile;
        this.mapRow = row;
        this.mapCol = col;
        if(tile != null) {
            this.setIcon(new ImageIcon(this.tile.getTileImage()));
            this.setPreferredSize(new Dimension(this.tile.getTileImage().getWidth(), this.tile.getTileImage().getHeight()));
        } else {
            this.setIcon(new ImageIcon());
        }
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                MapEditorTileButton button = (MapEditorTileButton) e.getSource();
                if(!button.isSelected) button.setBorder(BorderFactory.createLineBorder(Color.white));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                MapEditorTileButton button = (MapEditorTileButton) e.getSource();
                if(!button.isSelected) button.setBorder(UIManager.getBorder("Label.border"));
            }
        });
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

    public void setSelected(boolean state) {
        this.isSelected = state;
    }
}
