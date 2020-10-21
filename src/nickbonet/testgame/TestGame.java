package nickbonet.testgame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JFrame;

import nickbonet.gameengine.GamePanel;
import nickbonet.gameengine.Rect;

@SuppressWarnings("serial")
public class TestGame extends GamePanel {
	
	private transient Pacman player = new Pacman(500, 500);
	private transient List<Rect> rectObjects = new ArrayList<>(); // keep all the rectangles in a neat list for iteration purposes
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		player.draw(g);
		g.setColor(Color.red);
		for (int i = 0; i < rectObjects.size(); i++) {
			rectObjects.get(i).draw(g);
		}
	}

	protected void initObjects() {
		Rect testRect2 = new Rect(61, 71, 50, 60);
		Rect testRect3 = new Rect(150, 150, 70, 100);
		Rect testRect4 = new Rect(250, 250, 50, 51);
		Rect testRect5 = new Rect(480, 250, 50, 51);
		rectObjects.add(testRect2);
		rectObjects.add(testRect3);
		rectObjects.add(testRect4);
		rectObjects.add(testRect5);
	}
	
	@Override
	protected void mainGameLogic() {
		playerMovement();
	}
	
	private void playerMovement() {
		int dx = 0;
		int dy = 0;
		
		if (pressedKey[KeyEvent.VK_W]) {
			dx = 0;
			dy = -1;
			player.setSpriteDirection(KeyEvent.VK_W);
		}
		
		if (pressedKey[KeyEvent.VK_S]) { 
			dx = 0;
			dy = 1;
			player.setSpriteDirection(KeyEvent.VK_S);
		}
		
		if (pressedKey[KeyEvent.VK_A]) { 
			dx = -1;
			dy = 0;
			player.setSpriteDirection(KeyEvent.VK_A);
		}
		
		if (pressedKey[KeyEvent.VK_D]) {
			dx = 1;
			dy = 0;
			player.setSpriteDirection(KeyEvent.VK_D);
		}
		checkPlayerCollision(dx, dy);
	}
	
	private void checkPlayerCollision(int dx, int dy) {
		for (int i = 0; i < rectObjects.size(); i++) {
			if (player.getBounds().overlaps(rectObjects.get(i), dx, dy)) {
				dx = 0;
				dy = 0;
				logger.log(Level.INFO, "Player collision detected!");
			}
		}
		player.move(dx, dy);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Test Game");
		TestGame game = new TestGame();
		game.setBackground(Color.black);
		frame.add(game);
		frame.setSize(1024, 768);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.runGame();
	}
}
