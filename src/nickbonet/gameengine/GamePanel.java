/**
 * GameApplet - where all the magic is brought together!
 * NOTE: This is simply the JPanel for the game. Must create a JFrame
 * and create an instance of your extension of this class and add it to the JFrame,
 * then execute the runGame() method of the instance.
 * @author Nicholas Bonet
 *
 */
package nickbonet.gameengine;

import java.awt.event.*;
import java.util.logging.*;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements KeyListener {
	
	private boolean isRunning = true;
	protected final transient Logger logger = Logger.getLogger(GamePanel.class.getName(), null);
	protected final boolean[] pressedKey = new boolean[255];

	public GamePanel() {
		setFocusable(true);
		addKeyListener(this);
	}

	public void runGame() {
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
	
	public void mainGameLogic() {
		/*
		 * Main logic for the game, should override in own game implementation.
		 */
	}
	
	public void initObjects() {
		/*
		 * Every game requires objects of some sort, override this to handle those. 
		 */
	}
	
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
