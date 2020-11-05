package nickbonet.tilemapeditor;

import nickbonet.tilemapeditor.components.MapEditorView;
import nickbonet.tilemapeditor.model.TileMapEditorModel;

public class TileMapEditorController {
    private TileMapEditorModel model;
    private MapEditorView editView;

    public TileMapEditorController(TileMapEditorModel model, MapEditorView editView) {
        this.model = model;
        this.editView = editView;
        this.editView.setupPanelLayout();
        if (model.getMapModel() != null) {
            this.editView.initMapView(model.getMapModel().getMapRows(), model.getMapModel().getMapColumns(),
                    model.getTileSet().getTileArrayList(), model.getMapModel().getMapLayout());
        }
    }
}
