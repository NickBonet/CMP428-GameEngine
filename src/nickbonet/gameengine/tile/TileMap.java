package nickbonet.gameengine.tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TileMap {
    private final TileMapModel mapModel;
    private final TileSet tileSet;
    private final int mapWidth;
    private final int mapHeight;
    private Tile[][] tiles;

    public TileMap(TileMapModel mapModel) {
        this.tileSet = new TileSet(mapModel.getPerTileWidth(), mapModel.getPerTileHeight(), mapModel.getTileSetFile());
        this.mapWidth = mapModel.getPerTileWidth() * mapModel.getMapColumns();
        this.mapHeight = mapModel.getPerTileHeight() * mapModel.getMapRows();
        this.mapModel = mapModel;
        this.tiles = new Tile[mapModel.getMapRows()][mapModel.getMapColumns()];
    }

    public void initializeMap() {
        for (int row = 0; row < mapModel.getMapRows(); row++) {
            for (int col = 0; col < mapModel.getMapColumns(); col++) {
                BufferedImage tileImg = tileSet.getTileImageList().get(mapModel.getMapLayout()[row][col]);
                Tile currentTile = new Tile(tileImg);
                currentTile.setCollisionEnabled(mapModel.getCollisionMap()[row][col]);
                currentTile.setX(mapModel.getPerTileWidth()*col);
                currentTile.setY(mapModel.getPerTileHeight()*row);
                currentTile.initBoundsRect();
                tiles[row][col] = currentTile;
            }
        }
    }

    public void drawMap(Graphics g) {
        BufferedImage map = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_RGB);
        Graphics gMap = map.getGraphics();
        for (int row = 0; row < mapModel.getMapRows(); row++) {
            for (int col = 0; col < mapModel.getMapColumns(); col++) {
                Tile currentTile = tiles[row][col];
                currentTile.draw(gMap);
            }
        }
        g.drawImage(map, 0, 0, null);
    }

    public static BufferedImage resizeBufferedImage(BufferedImage image, int width, int height) {
        Image tempImg = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = resizedImg.createGraphics();
        g.drawImage(tempImg, 0, 0, null);
        g.dispose();
        return resizedImg;
    }
}
