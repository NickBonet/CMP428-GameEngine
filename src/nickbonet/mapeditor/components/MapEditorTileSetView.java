package nickbonet.mapeditor.components;

import nickbonet.gameengine.tile.Tile;
import nickbonet.gameengine.tile.TileSet;
import nickbonet.mapeditor.MapEditorController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MapEditorTileSetView extends JPanel {
    private final transient MapEditorController editorController;
    private MapEditorTileButton selectedTileButton;
    private final JPanel tilesInSet;

    public MapEditorTileSetView(MapEditorController controller) {
        this.editorController = controller;
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
                MapEditorTileButton mapEditorTileButton = new MapEditorTileButton(currentTile, row, col);
                mapEditorTileButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        setTileSelected(mapEditorTileButton);
                    }
                });
                constraints.gridx = col;
                constraints.gridy = row;
                tilesInSet.add(mapEditorTileButton, constraints);
            }
        }
        revalidate();
        repaint();
    }

    private void setTileSelected(MapEditorTileButton button) {
        if(this.selectedTileButton != null) {
            this.selectedTileButton.setBorder(UIManager.getBorder("Label.border"));
        }
        this.selectedTileButton = button;
        button.setBorder(BorderFactory.createLineBorder(Color.blue));
        this.editorController.setSelectedTile(button.getTile());
    }
}
