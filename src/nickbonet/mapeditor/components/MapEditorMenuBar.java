package nickbonet.mapeditor.components;

import nickbonet.mapeditor.MapEditorController;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MapEditorMenuBar extends JMenuBar {
    private transient MapEditorController editorController;

    public MapEditorMenuBar(MapEditorController controller) {
        this.editorController = controller;
        JMenu fileMenu = new JMenu("File");
        this.add(fileMenu);
        // TODO: Create UI component for creating new map, add action listener to item afterwards
        JMenuItem newFile = new JMenuItem("Create Map");
        JMenuItem openFile = new JMenuItem("Open Map..");
        JMenuItem saveFile = new JMenuItem("Save Map..");
        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        setupActionListeners(openFile, saveFile);
    }

    private void setupActionListeners(JMenuItem openFile, JMenuItem saveFile) {
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

        saveFile.addActionListener((ActionEvent event) -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON Files", "json");
            fileChooser.setFileFilter(filter);
            int returnVal = fileChooser.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String jsonFile = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    editorController.saveTileMapJson(jsonFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
