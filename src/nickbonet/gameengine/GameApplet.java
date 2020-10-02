package nickbonet.gameengine;
/**
 * GameApplet - where all the magic is brought together!
 * @author Nicholas Bonet
 *
 */

import java.applet.Applet;
import java.awt.event.*;
import java.util.logging.*;

@SuppressWarnings("serial")
public class GameApplet extends Applet implements Runnable, KeyListener {
	
	private transient Thread mainGameThread = new Thread(this);
	private boolean isRunning = true;
	protected final transient Logger logger = Logger.getLogger(GameApplet.class.getName(), null);
	protected final boolean[] pressedKey = new boolean[255];

	@Override
	public void init() {
		requestFocus();
		addKeyListener(this);
		mainGameThread.start();
	}

	@Override
	public void run() {
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
