package nickbonet.tilemapeditor.components;

import nickbonet.gameengine.tile.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MapEditorView extends JPanel {
    private ArrayList<TileButton> tilesOnScreen = new ArrayList<>();
    private JPanel tileButtonContainer;

    public void setupPanelLayout() {
        tileButtonContainer = new JPanel();
        tileButtonContainer.setLayout(new GridBagLayout());
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.add(tileButtonContainer);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setPreferredSize(new Dimension((int)(screenSize.getWidth() * 0.4), (int)(screenSize.getHeight() * 0.5)));
        this.setBackground(Color.white);
    }

    public void initMapView(int rows, int columns, List<Tile> tileArray, int[][] mapLayout) {
        GridBagConstraints constraints = new GridBagConstraints();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Tile currentTile = tileArray.get(mapLayout[row][col]);
                TileButton tileButton = new TileButton(currentTile.getTileImage());
                tilesOnScreen.add(tileButton);
                constraints.gridx = col;
                constraints.gridy = row;
                tileButtonContainer.add(tileButton, constraints);
            }
        }
    }
}
