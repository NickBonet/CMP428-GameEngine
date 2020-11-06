package nickbonet.mapeditor.components;

import nickbonet.gameengine.tile.Tile;
import nickbonet.gameengine.tile.TileSet;
import nickbonet.mapeditor.MapEditorController;

import javax.swing.*;
import java.awt.*;

public class MapEditorTileSetView extends JPanel {
    private transient MapEditorController editorController;
    private JPanel tilesInSet;

    public MapEditorTileSetView() {
        tilesInSet = new JPanel();
        tilesInSet.setLayout(new GridBagLayout());
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.add(tilesInSet);
        this.setBackground(Color.gray);
    }

    public void initTileSetView(TileSet tileSet) {
        tilesInSet.removeAll();
        GridBagConstraints constraints = new GridBagConstraints();
        for (int row = 0; row < tileSet.getTileSetRows(); row++) {
            for (int col = 0; col < tileSet.getTileSetColumns(); col++) {
                int tileIndex = (row * tileSet.getTileSetColumns()) + col;
                Tile currentTile = tileSet.getTileArrayList().get(tileIndex);
                MapEditorTileButton mapEditorTileButton = new MapEditorTileButton(currentTile.getTileImage());
                constraints.gridx = col;
                constraints.gridy = row;
                tilesInSet.add(mapEditorTileButton, constraints);
            }
        }
        revalidate();
        repaint();
    }
}
