package nickbonet.mapeditor;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Initialize main controller for the map editor.
            MapEditorController editorController = new MapEditorController();

            // Initialize all of the components for the editor.
            JFrame frame = new JFrame("Tile Map Editor");
            JScrollPane mapEditPane = new JScrollPane(editorController.getMapEditorView(),
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            JScrollPane tileSetPane = new JScrollPane(editorController.getMapEditorTileSetView(),
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            mapEditPane.getVerticalScrollBar().setUnitIncrement(16);
            tileSetPane.getVerticalScrollBar().setUnitIncrement(16);
            mapEditPane.getHorizontalScrollBar().setUnitIncrement(16);
            tileSetPane.getHorizontalScrollBar().setUnitIncrement(16);

            // Setup sizing for each component in the main window, and the split pane view.
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            mapEditPane.getViewport().setPreferredSize(new Dimension((int) (screenSize.getWidth() * 0.6),
                    (int) (screenSize.getHeight() * 0.75)));
            tileSetPane.getViewport().setPreferredSize(new Dimension((int) (mapEditPane.getViewport().getPreferredSize().getWidth() * 0.4),
                    (int) (mapEditPane.getViewport().getPreferredSize().getHeight() * 0.5)));
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mapEditPane, tileSetPane);
            splitPane.setResizeWeight(1.0);

            // Finally, put it all together into the JFrame.
            frame.add(splitPane);
            frame.setJMenuBar(editorController.getMapEditorMenuBar());
            frame.pack();
            frame.setVisible(true);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        });
    }
}
