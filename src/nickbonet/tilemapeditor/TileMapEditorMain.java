package nickbonet.tilemapeditor;

import javax.swing.*;

public class TileMapEditorMain {
    public static void main(String[] args) {
        TileMapEditorController editorController = new TileMapEditorController();
        JFrame frame = new JFrame("Tile Map Editor");
        JScrollPane scrollMapPane = new JScrollPane(editorController.getMapEditorView());
        frame.add(scrollMapPane);
        frame.setJMenuBar(editorController.getMapEditorMenuBar());
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }
}
