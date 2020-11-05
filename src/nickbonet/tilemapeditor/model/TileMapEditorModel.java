package nickbonet.tilemapeditor.model;

import com.google.gson.Gson;
import nickbonet.gameengine.tile.TileMapModel;
import nickbonet.gameengine.tile.TileSet;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class TileMapEditorModel {
    private TileMapModel mapModel;
    private TileSet tileSet;

    public TileMapEditorModel() throws FileNotFoundException {
        loadTileMapJson("1.json");
    }

    private void loadTileMapJson(String mapJson) throws FileNotFoundException {
        Gson gson = new Gson();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(TileMapModel.MAP_FOLDER + mapJson));
            mapModel = gson.fromJson(reader, TileMapModel.class);
            this.tileSet = new TileSet(mapModel.getPerTileWidth(), mapModel.getPerTileHeight(), mapModel.getTileSetFile());
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("JSON file not found.");
        }
    }

    public TileMapModel getMapModel() {
        return mapModel;
    }

    public TileSet getTileSet() {
        return tileSet;
    }
}
