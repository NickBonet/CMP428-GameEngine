package nickbonet.mapeditor.components;

import nickbonet.gameengine.tile.Tile;
import nickbonet.mapeditor.MapEditorController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class MapEditorView extends JPanel {
    private ArrayList<MapEditorTileButton> tilesOnScreen = new ArrayList<>();
    private transient MapEditorController editorController;
    private final JPanel tileButtonContainer;

    public MapEditorView(MapEditorController controller) {
        this.editorController = controller;
        tileButtonContainer = new JPanel();
        tileButtonContainer.setLayout(new GridBagLayout());
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.add(tileButtonContainer);
        this.setBackground(Color.gray);
    }

    public void loadInitialMapView(int rows, int columns, List<Tile> tileArray, int[][] mapLayout) {
        tileButtonContainer.removeAll();
        tilesOnScreen.clear();
        GridBagConstraints constraints = new GridBagConstraints();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Tile currentTile = tileArray.get(mapLayout[row][col]);
                MapEditorTileButton mapEditorTileButton = new MapEditorTileButton(currentTile, row, col);
                mapEditorTileButton.addActionListener((ActionEvent e) -> changeTile((mapEditorTileButton)));
                tilesOnScreen.add(mapEditorTileButton);
                constraints.gridx = col;
                constraints.gridy = row;
                tileButtonContainer.add(mapEditorTileButton, constraints);
            }
        }
        revalidate();
        repaint();
    }

    private void changeTile(MapEditorTileButton button) {
        button.setTile(editorController.getSelectedTile());
        editorController.updateTileInMap(button.getMapRow(), button.getMapCol());
    }
}
