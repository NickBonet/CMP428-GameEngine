package nickbonet.gameengine;
/**
 * GameApplet - where all the magic is brought together!
 * @author Nicholas Bonet
 *
 */

import java.applet.Applet;
import java.awt.event.*;

public class GameApplet extends Applet implements Runnable, KeyListener {
	
	private Thread mainGameThread = new Thread(this);
	public boolean[] pressedKey = new boolean[255];

	public void init() {
		requestFocus();
		addKeyListener(this);
		mainGameThread.start();
	}

	@Override
	public void run() {
		while (true) {
			initObjects();
			mainGameLogic();
			repaint();
			try {
				Thread.sleep(16); // should result in 60FPS.
			} catch (InterruptedException ex) {};
		}
	}
	
	/*
	 * Method that can be overridden, will run in the engine's thread loop.
	 */
	public void mainGameLogic() { }
	
	/*
	 * Method that can be overridden, takes care of any necessary object initialization.
	 */
	public void initObjects() { }
	
	@Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void keyPressed(KeyEvent e) {
		pressedKey[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		pressedKey[e.getKeyCode()] = false;
	}
}
