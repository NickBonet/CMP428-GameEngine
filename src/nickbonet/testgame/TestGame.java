package nickbonet.testgame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JFrame;

import nickbonet.gameengine.GamePanel;
import nickbonet.gameengine.Rect;
import nickbonet.gameengine.tile.Tile;
import nickbonet.gameengine.tile.TileMap;

@SuppressWarnings({"serial", "java:S110"})
public class TestGame extends GamePanel {
	private static final int WINDOW_HEIGHT = 768;
	private static final int WINDOW_WIDTH = 672;
	private transient Pacman player = new Pacman(0, 0, "pac", 65);
	private transient Ghost redGhost = new Ghost(400, 500, "redghost", 100);
	private final transient List<TileMap> maps = new ArrayList<>();
	
	@Override
	// Paints all the components onto a base image, then that image is upscaled 3x (very roughly for now.).
	public void paintComponent(Graphics g) {
		BufferedImage frame = new BufferedImage(224, 256, BufferedImage.TYPE_INT_RGB);
		Graphics test = frame.createGraphics();
		super.paintComponent(test);
		maps.get(0).drawMap(test);
		player.draw(test);
		for(Tile tile : maps.get(0).getSurroundingTiles(player.getBounds().getX(), player.getBounds().getY()))
			tile.drawBoundsRect(test);
		test.dispose();
		g.drawImage(frame, 0, 0, 672, 768, null);
	}

	@Override
	protected void initObjects() {
		try {
			maps.add(loadTileMap("test.tilemap"));
			maps.get(0).initializeMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			dx = 0;
			player.setSpriteAnim("up");
		}
		
		if (pressedKey[KeyEvent.VK_S]) {
			dy = 1;
			dx = 0;
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
		checkPlayerCollision(dx, dy);
	}
	
	private void checkPlayerCollision(int dx, int dy) {
		Tile[] collisionTiles = maps.get(0).getSurroundingTiles(player.getBounds().getX(), player.getBounds().getY());
		for (Tile tile : collisionTiles) {
			if (tile.isCollisionEnabled() && player.getBounds().overlaps(tile.getBoundsRect(), dx, dy)) {
				dx = 0;
				dy = 0;
				logger.log(Level.INFO, "Player collision detected!");
			}
		}
		player.move(dx, dy);
	}
	
	public static void main(String[] args) {
		System.setProperty("sun.java2d.opengl", "true");
		JFrame frame = new JFrame("Test Game");
		TestGame game = new TestGame();
		game.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		game.setBackground(Color.black);
		frame.add(game);
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		game.runGame();
	}
}
