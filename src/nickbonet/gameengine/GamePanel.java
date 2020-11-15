package nickbonet.gameengine;

import nickbonet.gameengine.tile.TileMap;
import nickbonet.gameengine.tile.TileMapModel;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * GameApplet - where all the magic is brought together!
 * NOTE: This is simply the JPanel for the game. Must create a JFrame
 * and create an instance of your extension of this class and add it to the JFrame,
 * then execute the runGame() method of the instance.
 *
 * @author Nicholas Bonet
 */
@SuppressWarnings("serial")
public abstract class GamePanel extends JPanel implements KeyListener {

    protected final transient Logger logger = Logger.getLogger("GameEngine", null);
    protected final boolean[] pressedKey = new boolean[255];
    private boolean isRunning = true;

    public GamePanel() {
        setFocusable(true);
        addKeyListener(this);
    }

    protected void runGame() {
        initObjects();

        while (isRunning) {
            mainGameLogic();
            repaint();
            try {
                Thread.sleep(16); // should result in 60FPS.
            } catch (InterruptedException ex) {
                logger.log(Level.SEVERE, ex.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    public TileMap loadTileMap(String mapFile) {
        try (FileInputStream fis = new FileInputStream(TileMapModel.MAP_FOLDER + mapFile); ObjectInputStream is = new ObjectInputStream(fis)) {
            TileMapModel mapModel = (TileMapModel) is.readObject();
            return new TileMap(mapModel);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * Main logic for the game, up to you to implement this in your game of course.
     */
    protected abstract void mainGameLogic();

    /*
     * Every game requires objects of some sort, implement that logic in this method.
     */
    protected abstract void initObjects();

    @Override
    public void keyTyped(KeyEvent e) {
        /*
         * Will be implemented later.
         */
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKey[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKey[e.getKeyCode()] = false;
    }
}
