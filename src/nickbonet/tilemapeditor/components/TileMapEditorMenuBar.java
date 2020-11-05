package nickbonet.tilemapeditor.components;

import nickbonet.tilemapeditor.TileMapEditorController;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;

public class TileMapEditorMenuBar extends JMenuBar {
    private transient TileMapEditorController editorController;

    public TileMapEditorMenuBar(TileMapEditorController controller) {
        this.editorController = controller;
        JMenu fileMenu = new JMenu("File");
        this.add(fileMenu);

        JMenuItem openFile = new JMenuItem("Open File...");
        fileMenu.add(openFile);

        openFile.addActionListener((ActionEvent event) -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON Files", "json");
            fileChooser.setFileFilter(filter);
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String jsonFile = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    editorController.loadTileMapJson(jsonFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
