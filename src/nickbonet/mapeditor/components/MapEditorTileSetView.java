package nickbonet.mapeditor.components;

import nickbonet.gameengine.tile.Tile;
import nickbonet.mapeditor.MapEditorController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

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

    public void initTileSetView(List<Tile> tileArray) {
        tilesInSet.removeAll();
        GridBagConstraints constraints = new GridBagConstraints();
        for (int row = 0; row < tileArray.size() / 8; row++) {
            for (int col = 0; col < 8; col++) {
                Tile currentTile = tileArray.get((row*8)+col);
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
