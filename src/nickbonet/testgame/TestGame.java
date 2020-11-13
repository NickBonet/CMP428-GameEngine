package nickbonet.testgame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

import nickbonet.gameengine.GamePanel;
import nickbonet.gameengine.tile.Tile;
import nickbonet.gameengine.tile.TileMap;

@SuppressWarnings({"serial", "java:S110"})
public class TestGame extends GamePanel {
	private static final int WINDOW_HEIGHT = 768;
	private static final int WINDOW_WIDTH = 672;
	private transient Pacman player = new Pacman(9, 17, "pac", 65);
	//private transient Ghost redGhost = new Ghost(400, 500, "redghost", 100);
	private final transient List<TileMap> maps = new ArrayList<>();
	
	@Override
	// Paints all the components onto a base image, then that image is upscaled 3x (very roughly for now.).
	public void paintComponent(Graphics g) {
		BufferedImage frame = new BufferedImage(224, 256, BufferedImage.TYPE_INT_RGB);
		Graphics test = frame.createGraphics();
		super.paintComponent(test);
		if (maps.size() != 0) {
			maps.get(0).drawMap(test);
			for (int i = 0; i < 3; i++) {
				maps.get(0).getSurroundingTiles(player.getBounds().getX(), player.getBounds().getY(), "right")[i].drawBoundsRect(test);
				maps.get(0).getSurroundingTiles(player.getBounds().getX(), player.getBounds().getY(), "down")[i].drawBoundsRect(test);
				maps.get(0).getSurroundingTiles(player.getBounds().getX(), player.getBounds().getY(), "up")[i].drawBoundsRect(test);
				maps.get(0).getSurroundingTiles(player.getBounds().getX(), player.getBounds().getY(), "left")[i].drawBoundsRect(test);
			}
		}
		player.draw(test);
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
	}
	
	private void playerMovement() {
		String direction = player.getSpriteDirection();
		boolean allowMove = false;

		if (pressedKey[KeyEvent.VK_W]) {
			if(!isPlayerColliding("up")) {
				direction = "up";
				allowMove = true;
			}
		}

		if (pressedKey[KeyEvent.VK_S]) {
			if(!isPlayerColliding("down")) {
				direction = "down";
				allowMove = true;
			}
		}

		if (pressedKey[KeyEvent.VK_A]) {
			if(!isPlayerColliding("left")) {
				direction = "left";
				allowMove = true;
			}
		}
		
		if (pressedKey[KeyEvent.VK_D]) {
			if(!isPlayerColliding("right")) {
				direction = "right";
				allowMove = true;
			}
		}

		if(allowMove || !isPlayerColliding(direction)) {
			player.setSpriteDirection(direction);
			player.move();
		}
	}
	
	private boolean isPlayerColliding(String direction) {
		boolean isColliding = false;
		int velocity = player.getVelocity();

		switch(direction) {
		case "right":
			for (Tile tile : maps.get(0).getSurroundingTiles(player.getBounds().getX(), player.getBounds().getY(), "right"))
				if(tile.isCollisionEnabled() && player.getBounds().overlaps(tile.getBoundsRect(), velocity, 0))
					isColliding = true;
			break;
		case "left":
			for (Tile tile : maps.get(0).getSurroundingTiles(player.getBounds().getX(), player.getBounds().getY(), "left"))
				if(tile.isCollisionEnabled() && player.getBounds().overlaps(tile.getBoundsRect(), -velocity, 0))
					isColliding = true;
			break;
		case "up":
			for (Tile tile : maps.get(0).getSurroundingTiles(player.getBounds().getX(), player.getBounds().getY(), "up"))
				if(tile.isCollisionEnabled() && player.getBounds().overlaps(tile.getBoundsRect(), 0, -velocity))
					isColliding = true;
			break;
		case "down":
			for (Tile tile : maps.get(0).getSurroundingTiles(player.getBounds().getX(), player.getBounds().getY(), "down"))
				if(tile.isCollisionEnabled() && player.getBounds().overlaps(tile.getBoundsRect(), 0, velocity))
					isColliding = true;
			break;
		}

		return isColliding;
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
