package nickbonet.gameengine.tile;

public class TileMapModel {
    public static final String MAP_FOLDER = "assets/maps/";
    private String tileSetFile;
    private int perTileWidth;
    private int perTileHeight;
    private int mapRows;
    private int mapColumns;
    private int[][] mapLayout;

    public String getTileSetFile() {
        return tileSetFile;
    }

    public int getPerTileWidth() {
        return perTileWidth;
    }

    public int getPerTileHeight() {
        return perTileHeight;
    }

    public int getMapRows() {
        return mapRows;
    }

    public int getMapColumns() {
        return mapColumns;
    }

    public int[][] getMapLayout() {
        return mapLayout;
    }
}
