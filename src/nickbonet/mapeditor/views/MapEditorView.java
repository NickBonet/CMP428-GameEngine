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
    private final ArrayList<MapEditorTileButton> tilesOnScreen = new ArrayList<>();
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

    public void loadInitialMapView(int rows, int columns, List<BufferedImage> tileArray, int[][] mapLayout, boolean[][] collisionMap) {
        tileButtonContainer.removeAll();
        tilesOnScreen.clear();
        GridBagConstraints constraints = new GridBagConstraints();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Tile currentTile = null;
                if(mapLayout[row][col] != -1) currentTile = new Tile(tileArray.get(mapLayout[row][col]));
                MapEditorTileButton mapEditorTileButton = new MapEditorTileButton(currentTile, row, col);

                if(currentTile == null) createEmptyTileButton(tileArray.get(0).getWidth(), tileArray.get(0).getHeight(), mapEditorTileButton);

                if (collisionMap[row][col]) mapEditorTileButton.setBorder(BorderFactory.createLineBorder(Color.red));
                else mapEditorTileButton.setBorder(UIManager.getBorder("Label.border"));

                mapEditorTileButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        editorTileClicked(mapEditorTileButton);
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

    private void createEmptyTileButton(int iconWidth, int iconHeight, MapEditorTileButton mapEditorTileButton) {
        BufferedImage image = new BufferedImage(iconWidth, iconHeight, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.createGraphics();
        g.setColor(Color.lightGray);
        g.fillRect (0, 0, iconWidth, iconHeight);
        Dimension tileSize = new Dimension(iconWidth, iconHeight);
        mapEditorTileButton.setPreferredSize(tileSize);
        mapEditorTileButton.setIcon(new ImageIcon(image));
    }

    private void editorTileClicked(MapEditorTileButton button) {
        switch(editorController.getEditorMode()) {
        case PAINT:
            if(editorController.getSelectedPaintModeTile() != null) {
                button.setTile(editorController.getSelectedPaintModeTile());
                editorController.updateTileInMap(button.getMapRow(), button.getMapCol());
            }
            break;
        case COLLISION:
            boolean currentVal = editorController.updateCollisionTileInMap(button.getMapRow(), button.getMapCol());
            if (currentVal) button.setBorder(BorderFactory.createLineBorder(Color.red));
            else button.setBorder(UIManager.getBorder("Label.border"));
            break;
        case OBJECT:
        }
    }
}
