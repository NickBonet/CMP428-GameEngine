package nickbonet.gameengine.tile;

import nickbonet.gameengine.sprite.SpriteDir;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TileMap {
    private final TileMapModel mapModel;
    private final TileSet tileSet;
    private final int mapWidth;
    private final int mapHeight;
    private final Tile[][] mainLayerTiles;
    private final Tile[][] objectLayerTiles;

    public TileMap(TileMapModel mapModel) {
        this.tileSet = new TileSet(mapModel.getPerTileWidth(), mapModel.getPerTileHeight(), mapModel.getTileSetFile());
        this.mapWidth = mapModel.getPerTileWidth() * mapModel.getMapColumns();
        this.mapHeight = mapModel.getPerTileHeight() * mapModel.getMapRows();
        this.mapModel = mapModel;
        this.mainLayerTiles = new Tile[mapModel.getMapRows()][mapModel.getMapColumns()];
        this.objectLayerTiles = new Tile[mapModel.getMapRows()][mapModel.getMapColumns()];
    }

    // Calculates the distance between two tiles via the Euclidean distance formula. (from their center points)
    public static int euclideanDistanceBetweenTiles(Tile firstTile, Tile secondTile) {
        int x1 = (firstTile.getX() + (firstTile.getWidth() / 2));
        int y1 = (firstTile.getY() + (firstTile.getHeight() / 2));
        int x2 = (secondTile.getX() + (secondTile.getWidth() / 2));
        int y2 = (secondTile.getY() + (secondTile.getHeight() / 2));

        return (int) Math.sqrt(Math.pow((double) x2 - x1, 2) + Math.pow((double) y2 - y1, 2));
    }

    public void initializeMap() {
        for (int row = 0; row < mapModel.getMapRows(); row++) {
            for (int col = 0; col < mapModel.getMapColumns(); col++) {
                BufferedImage tileImage = tileSet.getTileImageList().get(mapModel.getMapLayout()[row][col]);
                Tile currentTile = new Tile(tileImage);
                currentTile.setCollisionEnabled(mapModel.getCollisionMap()[row][col]);
                if (mapModel.getObjectMap()[row][col] != -1) {
                    Tile objectTile = new Tile(tileSet.getTileImageList().get(mapModel.getObjectMap()[row][col]));
                    objectTile.setX(mapModel.getPerTileWidth() * col);
                    objectTile.setY(mapModel.getPerTileHeight() * row);
                    objectLayerTiles[row][col] = objectTile;
                }
                currentTile.setX(mapModel.getPerTileWidth() * col);
                currentTile.setY(mapModel.getPerTileHeight() * row);
                currentTile.initBoundsRect();
                mainLayerTiles[row][col] = currentTile;
            }
        }
    }

    public void drawMap(Graphics g) {
        BufferedImage map = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_RGB);
        Graphics gMap = map.getGraphics();
        for (int row = 0; row < mapModel.getMapRows(); row++) {
            for (int col = 0; col < mapModel.getMapColumns(); col++) {
                Tile currentTile = mainLayerTiles[row][col];
                currentTile.draw(gMap);
                if (objectLayerTiles[row][col] != null) objectLayerTiles[row][col].draw(gMap);
            }
        }
        g.drawImage(map, 0, 0, null);
    }

    // Returns the adjacent and corner tiles in a given direction, based on the tile at a given point.
    public List<Tile> getTilesInDirection(int x, int y, SpriteDir direction) {
        int row = y / mapModel.getPerTileHeight();
        int col = x / mapModel.getPerTileWidth();
        List<Tile> tiles = new ArrayList<>();
        switch (direction) {
            case UP:
                for (int i : new int[]{col, col + 1, col - 1})
                    tiles.add(getMainLayerTileAt(row - 1, i));
                break;
            case DOWN:
                for (int i : new int[]{col, col + 1, col - 1})
                    tiles.add(getMainLayerTileAt(row + 1, i));
                break;
            case LEFT:
                for (int i : new int[]{row - 1, row, row + 1})
                    tiles.add(getMainLayerTileAt(i, col - 1));
                break;
            case RIGHT:
                for (int i : new int[]{row - 1, row, row + 1})
                    tiles.add(getMainLayerTileAt(i, col + 1));
                break;
            case ALL:
                for (int i : new int[]{row - 1, row + 1}) {
                    tiles.add(getMainLayerTileAt(i, col));
                    tiles.add(getMainLayerTileAt(i, col + 1));
                    tiles.add(getMainLayerTileAt(i, col - 1));
                }
                for (int i : new int[]{col - 1, col + 1})
                    tiles.add(getMainLayerTileAt(row, i));
                break;
            default:
                break;
        }
        return tiles.parallelStream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    // Returns a nearby tile in any of the 4 directions, with a provided offset for how far to grab a tile from the given point.
    public Tile getNearbyTile(int x, int y, SpriteDir direction, int tileOffset) {
        int row = y / mapModel.getPerTileHeight();
        int col = x / mapModel.getPerTileWidth();
        switch (direction) {
            case UP:
                return getMainLayerTileAt(row - tileOffset, col);
            case DOWN:
                return getMainLayerTileAt(row + tileOffset, col);
            case LEFT:
                return getMainLayerTileAt(row, col - tileOffset);
            case RIGHT:
                return getMainLayerTileAt(row, col + tileOffset);
            default:
                return null;
        }
    }

    public Tile getTileAtPoint(int x, int y) {
        int row = y / mapModel.getPerTileHeight();
        int col = x / mapModel.getPerTileWidth();
        return getMainLayerTileAt(row, col);
    }

    private Tile getMainLayerTileAt(int row, int col) {
        try {
            return mainLayerTiles[row][col];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
}
