/**
 * GameApplet - where all the magic is brought together!
 * @author Nicholas Bonet
 *
 */
package nickbonet.gameengine;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import java.util.logging.*;

@SuppressWarnings("serial")
public class GameApplet extends Applet implements Runnable, KeyListener {
	
	private transient Thread mainGameThread = new Thread(this);
	private boolean isRunning = true;
	protected transient Image bufferImage;
	protected transient Graphics bufferGraphics;
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
		initDoubleBuffer();
		
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
	
	// Initialize our bufferImage/Graphics objects for use. Adjusted for size of applet.
	public void initDoubleBuffer() {
		if (bufferImage == null) {
			bufferImage = createImage(getWidth(), getHeight());
			bufferGraphics = bufferImage.getGraphics();
			bufferGraphics.setColor(Color.white);
			bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
		}
	}
	
	@Override
	// Decided to override update to abstract the bufferGraphics clearing away from the actual game applet.
	public void update(Graphics g) {
		bufferGraphics.setColor(Color.white);
		bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
		paint(g);
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
