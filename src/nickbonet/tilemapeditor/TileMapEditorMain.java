package nickbonet.tilemapeditor;

import nickbonet.tilemapeditor.components.MapEditorView;
import nickbonet.tilemapeditor.model.TileMapEditorModel;

import javax.swing.*;
import java.io.FileNotFoundException;

public class TileMapEditorMain {
    public static void main(String[] args) throws FileNotFoundException {
        MapEditorView mapView = new MapEditorView();
        TileMapEditorModel editorModel = new TileMapEditorModel();
        TileMapEditorController editorController = new TileMapEditorController(editorModel, mapView);

        JFrame frame = new JFrame("Tile Map Editor");
        JScrollPane scrollMapPane = new JScrollPane(mapView);
        frame.add(scrollMapPane);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }
}
