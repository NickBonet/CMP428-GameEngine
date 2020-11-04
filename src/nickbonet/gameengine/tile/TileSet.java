package nickbonet.gameengine.tile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TileSet {
    private static final String TILE_FOLDER = "assets/tiles/";
    private ArrayList<Tile> tileArrayList = new ArrayList<>();
    private int perTileHeight;
    private int perTileWidth;
    private String tileSetFile;
    private BufferedImage tileSetImage;

    public TileSet(int perTileWidth, int perTileHeight, String tileSetFile) {
        this.perTileHeight = perTileHeight;
        this.perTileWidth = perTileWidth;
        this.tileSetFile = tileSetFile;
        File tileSet = new File(TILE_FOLDER + tileSetFile);
        try {
            this.tileSetImage = ImageIO.read(tileSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initTileSet();
    }

    private void initTileSet() {
        int rows = tileSetImage.getHeight() / perTileHeight;
        int columns = tileSetImage.getWidth() / perTileWidth;
        for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
            for (int columnIndex = 0; columnIndex < columns; columnIndex++) {
                BufferedImage newTileImage = tileSetImage.getSubimage(columnIndex*perTileWidth, rowIndex*perTileHeight, perTileWidth, perTileHeight);
                Tile newTile = new Tile(perTileWidth, perTileHeight, newTileImage);
                tileArrayList.add(newTile);
            }
        }
    }

    public ArrayList<Tile> getTileArrayList() {
        return tileArrayList;
    }
}
