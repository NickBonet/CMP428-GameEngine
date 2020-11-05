package nickbonet.mapeditor;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        MapEditorController editorController = new MapEditorController();
        JFrame frame = new JFrame("Tile Map Editor");
        JScrollPane scrollMapPane = new JScrollPane(editorController.getMapEditorView());
        JScrollPane tileSetPane = new JScrollPane(editorController.getMapEditorTileSetView());
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollMapPane, tileSetPane);
        frame.add(splitPane);
        frame.setJMenuBar(editorController.getMapEditorMenuBar());
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }
}
