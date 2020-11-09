package nickbonet.gameengine.tile;

public class TileMapModel {
    public static final String MAP_FOLDER = "assets/maps/";
    private final String tileSetFile;
    private final int perTileWidth;
    private final int perTileHeight;
    private final int mapRows;
    private final int mapColumns;
    private final int[][] mapLayout;

    public TileMapModel(String file, int perTileWidth, int perTileHeight, int mapRows, int mapColumns) {
        this.tileSetFile = file;
        this.perTileWidth = perTileWidth;
        this.perTileHeight = perTileHeight;
        this.mapColumns = mapColumns;
        this.mapRows = mapRows;
        this.mapLayout = new int[mapRows][mapColumns];

        for(int row = 0; row < mapRows; row++) {
            for(int col =0; col < mapColumns; col++) {
                mapLayout[row][col] = -1;
            }
        }
    }

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
