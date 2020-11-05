package nickbonet.gameengine.tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TileMap {
    private TileMapModel mapStruct;
    private TileSet tileSet;
    private int mapWidth;
    private int mapHeight;

    public TileMap(TileMapModel mapStruct) {
        this.tileSet = new TileSet(mapStruct.getPerTileWidth(), mapStruct.getPerTileHeight(), mapStruct.getTileSetFile());
        this.mapWidth = mapStruct.getPerTileWidth() * mapStruct.getMapColumns();
        this.mapHeight = mapStruct.getPerTileHeight() * mapStruct.getMapRows();
        this.mapStruct = mapStruct;
    }

    public void drawMap(Graphics g) {
        BufferedImage map = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_RGB);
        Graphics gMap = map.getGraphics();
        for (int row = 0; row < mapStruct.getMapRows(); row++) {
            for (int col = 0; col < mapStruct.getMapColumns(); col++) {
                Tile currentTile = tileSet.getTileArrayList().get(mapStruct.getMapLayout()[row][col]);
                currentTile.draw(gMap, col*mapStruct.getPerTileWidth(), row*mapStruct.getPerTileHeight());
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
