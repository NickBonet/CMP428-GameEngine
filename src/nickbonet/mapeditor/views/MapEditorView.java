package nickbonet.mapeditor.views;

import nickbonet.gameengine.tile.Tile;
import nickbonet.mapeditor.MapEditorController;
import nickbonet.mapeditor.components.MapEditorTileButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MapEditorView extends JPanel {
    private ArrayList<MapEditorTileButton> tilesOnScreen = new ArrayList<>();
    private final transient MapEditorController editorController;
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
                Tile currentTile = null;
                if(mapLayout[row][col] != -1) currentTile = tileArray.get(mapLayout[row][col]);
                MapEditorTileButton mapEditorTileButton = new MapEditorTileButton(currentTile, row, col);
                if(currentTile == null) {
                    int iconWidth = tileArray.get(0).getWidth();
                    int iconHeight = tileArray.get(0).getHeight();
                    BufferedImage image = new BufferedImage(iconWidth, iconHeight, BufferedImage.TYPE_INT_RGB);
                    Graphics g = image.createGraphics();
                    g.setColor(Color.lightGray);
                    g.fillRect (0, 0, iconWidth, iconHeight);
                    Dimension tileSize = new Dimension(iconWidth, iconHeight);
                    mapEditorTileButton.setPreferredSize(tileSize);
                    mapEditorTileButton.setIcon(new ImageIcon(image));
                }

                mapEditorTileButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        changeTile(mapEditorTileButton);
                    }
                });
                tilesOnScreen.add(mapEditorTileButton);
                constraints.gridx = col;
                constraints.gridy = row;
                constraints.insets = new Insets(0, 0, 1 , 1);
                tileButtonContainer.add(mapEditorTileButton, constraints);
            }
        }
        revalidate();
        repaint();
    }

    private void changeTile(MapEditorTileButton button) {
        button.setTile(editorController.getSelectedPaintModeTile());
        editorController.updateTileInMap(button.getMapRow(), button.getMapCol());
    }
}
