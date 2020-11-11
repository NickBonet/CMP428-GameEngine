package nickbonet.mapeditor.components;

import nickbonet.mapeditor.MapEditorController;
import nickbonet.mapeditor.model.EditorMode;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MapEditorMenuBar extends JMenuBar {
    private final transient MapEditorController editorController;

    public MapEditorMenuBar(MapEditorController controller) {
        this.editorController = controller;
        setupFileMenu();
        setupEditorModeMenu();
        setupEditMenu();
    }

    private void setupEditMenu() {
        JMenu editMenu = new JMenu("Edit");
        JMenuItem fillOption = new JMenuItem("Fill All Tiles");
        KeyStroke fillShortcut = KeyStroke.getKeyStroke(KeyEvent.VK_A,  Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        fillOption.setAccelerator(fillShortcut);

        JMenuItem fillRow = new JMenuItem("Fill Current Row");
        KeyStroke fillRowShortcut = KeyStroke.getKeyStroke(KeyEvent.VK_R,  Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        fillRow.setAccelerator(fillRowShortcut);

        JMenuItem fillCol = new JMenuItem("Fill Current Column");
        KeyStroke fillColShortcut = KeyStroke.getKeyStroke(KeyEvent.VK_C,  Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        fillCol.setAccelerator(fillColShortcut);

        fillOption.addActionListener((ActionEvent event) -> editorController.fillMapWithSelectedTile());
        fillRow.addActionListener((ActionEvent event) -> editorController.fillRowBasedOnEditorMode());
        fillCol.addActionListener((ActionEvent event) -> editorController.fillColumnBasedOnEditorMode());

        editMenu.add(fillOption);
        editMenu.add(fillRow);
        editMenu.add(fillCol);
        this.add(editMenu);
    }

    private void setupEditorModeMenu() {
        JMenu editorMode = new JMenu("Editor Mode");
        JMenuItem paintMode = new JMenuItem("Paint Mode");
        JMenuItem collisionMode = new JMenuItem("Collision Mode");
        JMenuItem objectMode = new JMenuItem("Object Mode");
        editorMode.add(paintMode);
        editorMode.add(collisionMode);
        editorMode.add(objectMode);
        this.add(editorMode);

        KeyStroke paintShortcut = KeyStroke.getKeyStroke(KeyEvent.VK_P,  Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        paintMode.setAccelerator(paintShortcut);
        KeyStroke collisionShortcut = KeyStroke.getKeyStroke(KeyEvent.VK_L,  Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        collisionMode.setAccelerator(collisionShortcut);
        KeyStroke objectShortcut = KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD,  Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        objectMode.setAccelerator(objectShortcut);
        paintMode.addActionListener((ActionEvent event) -> editorController.setEditorMode(EditorMode.PAINT));
        collisionMode.addActionListener((ActionEvent event) -> editorController.setEditorMode(EditorMode.COLLISION));
        objectMode.addActionListener((ActionEvent event) -> editorController.setEditorMode(EditorMode.OBJECT));
    }

    private void setupFileMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem newFile = new JMenuItem("Create Map..");
        JMenuItem openFile = new JMenuItem("Open Map..");
        JMenuItem saveFile = new JMenuItem("Save Map..");
        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        this.add(fileMenu);

        KeyStroke newMapShortcut = KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        KeyStroke openFileShortcut = KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        KeyStroke saveFileShortcut = KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        newFile.setAccelerator(newMapShortcut);
        openFile.setAccelerator(openFileShortcut);
        saveFile.setAccelerator(saveFileShortcut);

        newFile.addActionListener((ActionEvent event) -> new NewMapDialog(editorController));

        openFile.addActionListener((ActionEvent event) -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON Files (.json)", "json");
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
            FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON Files (.json)", "json");
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
