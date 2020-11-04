/**
 * GameApplet - where all the magic is brought together!
 * NOTE: This is simply the JPanel for the game. Must create a JFrame
 * and create an instance of your extension of this class and add it to the JFrame,
 * then execute the runGame() method of the instance.
 * @author Nicholas Bonet
 *
 */
package nickbonet.gameengine;

import com.google.gson.Gson;
import nickbonet.gameengine.tile.Tile;
import nickbonet.gameengine.tile.TileMap;
import nickbonet.gameengine.tile.TileMapModel;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.*;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class GamePanel extends JPanel implements KeyListener {
	
	private boolean isRunning = true;
	protected final transient Logger logger = Logger.getLogger("GameEngine", null);
	protected final boolean[] pressedKey = new boolean[255];

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

	public TileMap loadTileMap(String mapJson) throws FileNotFoundException {
		Gson gson = new Gson();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(TileMapModel.MAP_FOLDER + mapJson));
			TileMapModel mapModel = gson.fromJson(reader, TileMapModel.class);
			return new TileMap(mapModel);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("JSON file not found.");
		}
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
