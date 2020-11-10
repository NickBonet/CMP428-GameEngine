package nickbonet.mapeditor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nickbonet.gameengine.tile.Tile;
import nickbonet.gameengine.tile.TileMapModel;
import nickbonet.gameengine.tile.TileSet;
import nickbonet.mapeditor.model.EditorMode;
import nickbonet.mapeditor.views.MapEditorView;
import nickbonet.mapeditor.components.MapEditorMenuBar;
import nickbonet.mapeditor.views.MapEditorTileSetView;
import nickbonet.mapeditor.model.MapEditorModel;

import java.io.*;

public class MapEditorController {
    private final MapEditorModel model;
    private final MapEditorView mapEditorView;
    private final MapEditorMenuBar mapEditorMenuBar;
    private final MapEditorTileSetView mapEditorTileSetView;
    private Tile selectedPaintModeTile;
    private EditorMode editorMode;

    public MapEditorController() {
        this.model = new MapEditorModel();
        this.mapEditorView = new MapEditorView(this);
        this.mapEditorTileSetView = new MapEditorTileSetView(this);
        this.mapEditorMenuBar = new MapEditorMenuBar(this);
        this.editorMode = EditorMode.PAINT;
    }

    public void loadTileMapJson(String mapJson) throws FileNotFoundException {
        Gson gson = new Gson();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(mapJson));
            model.setMapModel(gson.fromJson(reader, TileMapModel.class));
            initializeViews();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("JSON file not found.");
        }
    }

    public void createTileMap(String file, int perTileWidth, int perTileHeight, int mapRows, int mapColumns) {
        model.setMapModel(new TileMapModel(file, perTileWidth, perTileHeight, mapRows, mapColumns));
        initializeViews();
    }

    public void saveTileMapJson(String mapJson) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if(!mapJson.endsWith(".json")) {
            mapJson += ".json";
        }
        Writer writer = new FileWriter(mapJson);
        gson.toJson(model.getMapModel(), writer);
        writer.flush();
        writer.close();
    }

    private void initializeViews() {
        model.setTileSet(new TileSet(model.getMapModel().getPerTileWidth(), model.getMapModel().getPerTileHeight(),
                model.getMapModel().getTileSetFile()));
        mapEditorView.loadInitialMapView(model.getMapModel().getMapRows(), model.getMapModel().getMapColumns(),
                model.getTileSet().getTileImageList(), model.getMapModel().getMapLayout(), model.getMapModel().getCollisionMap());
        mapEditorTileSetView.initTileSetView(model.getTileSet());
        this.editorMode = EditorMode.PAINT;
        this.selectedPaintModeTile = null;
    }

    public void updateTileInMap(int row, int col) {
        model.getMapModel().getMapLayout()[row][col] = model.getTileSet().getTileImageList().indexOf(selectedPaintModeTile.getTileImage());
    }

    public boolean updateCollisionTileInMap(int row, int col) {
        boolean currentValue = model.getMapModel().getCollisionMap()[row][col];
        model.getMapModel().getCollisionMap()[row][col] = !currentValue;
        return !currentValue;
    }

    public void fillMapWithSelectedTile() {
        for(int row = 0; row < model.getMapModel().getMapRows(); row++) {
            for(int col = 0; col < model.getMapModel().getMapColumns(); col++) {
                updateTileInMap(row, col);
            }
        }
        mapEditorView.loadInitialMapView(model.getMapModel().getMapRows(), model.getMapModel().getMapColumns(),
                model.getTileSet().getTileImageList(), model.getMapModel().getMapLayout(), model.getMapModel().getCollisionMap());
    }

    public MapEditorView getMapEditorView() {
        return mapEditorView;
    }

    public MapEditorMenuBar getMapEditorMenuBar() {
        return mapEditorMenuBar;
    }

    public MapEditorTileSetView getMapEditorTileSetView() {
        return mapEditorTileSetView;
    }

    public Tile getSelectedPaintModeTile() {
        return selectedPaintModeTile;
    }

    public void setSelectedPaintModeTile(Tile selectedPaintModeTile) {
        this.selectedPaintModeTile = selectedPaintModeTile;
    }

    public EditorMode getEditorMode() {
        return editorMode;
    }

    public void setEditorMode(EditorMode editorMode) {
        this.editorMode = editorMode;
    }
}
