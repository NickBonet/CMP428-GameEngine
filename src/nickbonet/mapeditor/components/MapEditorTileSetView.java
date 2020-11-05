package nickbonet.mapeditor.components;

import nickbonet.mapeditor.MapEditorController;

import javax.swing.*;
import java.awt.*;

public class MapEditorTileSetView extends JPanel {
    private transient MapEditorController editorController;
    private JPanel tilesInSet;

    public MapEditorTileSetView() {
        tilesInSet = new JPanel();
        tilesInSet.setLayout(new GridBagLayout());
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.add(tilesInSet);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setPreferredSize(new Dimension((int)(screenSize.getWidth() * 0.1), (int)(screenSize.getHeight() * 0.1)));
        this.setBackground(Color.gray);
    }
}
