package nickbonet.tilemapeditor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nickbonet.gameengine.tile.TileMapModel;
import nickbonet.gameengine.tile.TileSet;
import nickbonet.tilemapeditor.components.MapEditorView;
import nickbonet.tilemapeditor.components.TileMapEditorMenuBar;
import nickbonet.tilemapeditor.model.TileMapEditorModel;

import java.io.*;

public class TileMapEditorController {
    private TileMapEditorModel model;
    private MapEditorView mapEditorView;
    private TileMapEditorMenuBar mapEditorMenuBar;

    public TileMapEditorController() {
        this.model = new TileMapEditorModel();
        this.mapEditorView = new MapEditorView();
        this.mapEditorMenuBar = new TileMapEditorMenuBar(this);
        this.mapEditorView.setupPanelLayout();
        if (model.getMapModel() != null) {
            this.mapEditorView.loadInitialMapView(model.getMapModel().getMapRows(), model.getMapModel().getMapColumns(),
                    model.getTileSet().getTileArrayList(), model.getMapModel().getMapLayout());
        }
    }

    public void loadTileMapJson(String mapJson) throws FileNotFoundException {
        Gson gson = new Gson();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(mapJson));
            model.setMapModel(gson.fromJson(reader, TileMapModel.class));
            model.setTileSet(new TileSet(model.getMapModel().getPerTileWidth(), model.getMapModel().getPerTileHeight(),
                                        model.getMapModel().getTileSetFile()));
            mapEditorView.loadInitialMapView(model.getMapModel().getMapRows(), model.getMapModel().getMapColumns(),
                    model.getTileSet().getTileArrayList(), model.getMapModel().getMapLayout());
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("JSON file not found.");
        }
    }

    public void saveTileMapJson(String mapJson) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Writer writer = new FileWriter(mapJson);
        gson.toJson(model.getMapModel(), writer);
        writer.flush();
        writer.close();
    }

    public MapEditorView getMapEditorView() {
        return mapEditorView;
    }

    public TileMapEditorMenuBar getMapEditorMenuBar() {
        return mapEditorMenuBar;
    }
}
