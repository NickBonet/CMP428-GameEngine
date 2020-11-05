package nickbonet.testgame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JFrame;

import nickbonet.gameengine.GamePanel;
import nickbonet.gameengine.Rect;
import nickbonet.gameengine.tile.TileMap;

@SuppressWarnings({"serial", "java:S110"})
public class TestGame extends GamePanel {
	
	private transient Pacman player = new Pacman(500, 500, "pac", 65);
	private transient Ghost redGhost = new Ghost(400, 500, "redghost", 100);
	private transient List<Rect> rectObjects = new ArrayList<>(); // keep all the rectangles in a neat list for iteration purposes
	private final transient List<TileMap> maps = new ArrayList<>();
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		/*
		player.draw(g);
		redGhost.draw(g);
		g.setColor(Color.red);
		for (int i = 0; i < rectObjects.size(); i++) {
			rectObjects.get(i).draw(g);
		}*/
		maps.get(0).drawMap(g);
	}

	@Override
	protected void initObjects() {
		try {
			maps.add(loadTileMap("1.json"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Rect testRect2 = new Rect(61, 71, 50, 60);
		Rect testRect3 = new Rect(150, 150, 70, 100);
		Rect testRect4 = new Rect(250, 250, 50, 51);
		Rect testRect5 = new Rect(480, 250, 50, 51);
		rectObjects.add(testRect2);
		rectObjects.add(testRect3);
		rectObjects.add(testRect4);
		rectObjects.add(testRect5);
		rectObjects.add(redGhost.getBounds());
		redGhost.setSpriteAnim("up");
	}
	
	@Override
	protected void mainGameLogic() {
		playerMovement();
		if (redGhost.getX() >= 400 && redGhost.getY() > 50) {
			redGhost.move(0, -2);
		}
	}
	
	private void playerMovement() {
		int dx = 0;
		int dy = 0;
		
		if (pressedKey[KeyEvent.VK_W]) {
			dy = -1;
			player.setSpriteAnim("up");
		}
		
		if (pressedKey[KeyEvent.VK_S]) {
			dy = 1;
			player.setSpriteAnim("down");
		}
		
		if (pressedKey[KeyEvent.VK_A]) { 
			dx = -1;
			dy = 0;
			player.setSpriteAnim("left");
		}
		
		if (pressedKey[KeyEvent.VK_D]) {
			dx = 1;
			dy = 0;
			player.setSpriteAnim("right");
		}
		dx *= 2;
		dy *= 2;
		checkPlayerCollision(dx, dy);
	}
	
	private void checkPlayerCollision(int dx, int dy) {
		for (Rect rectObject : rectObjects) {
			if (player.getBounds().overlaps(rectObject, dx, dy)) {
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
		game.setPreferredSize(new Dimension(1024, 768));
		game.setBackground(Color.black);
		frame.add(game);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		game.runGame();
	}
}
