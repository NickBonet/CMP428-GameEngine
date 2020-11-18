package nickbonet.testgame;

import nickbonet.gameengine.GamePanel;
import nickbonet.gameengine.sprite.Sprite;
import nickbonet.gameengine.tile.Tile;
import nickbonet.gameengine.tile.TileMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SuppressWarnings({"serial", "java:S110"})
public class TestGame extends GamePanel {
    private static final int WINDOW_HEIGHT = 768;
    private static final int WINDOW_WIDTH = 672;
    private final transient Pacman player = new Pacman(3, 11);
    private final transient Ghost redGhost = new Ghost(80, 11, "red", 100);
    private final transient List<TileMap> maps = new ArrayList<>();

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

    @Override
    // Paints all the components onto a base image, then that image is upscaled 3x (very roughly for now.).
    public void paintComponent(Graphics g) {
        BufferedImage frame = new BufferedImage(224, 256, BufferedImage.TYPE_INT_RGB);
        Graphics base = frame.createGraphics();
        super.paintComponent(base);
        if (!maps.isEmpty()) {
            maps.get(0).drawMap(base);
            for (Tile tile : maps.get(0).getSurroundingTiles(player.getBounds().getX(), player.getBounds().getY(), "all"))
                tile.drawBoundsRect(base);
        }
        player.draw(base);
        redGhost.draw((base));
        base.dispose();
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
        redGhost.setSpriteDirection("left");
    }

    @Override
    protected void mainGameLogic() {
        playerMovement();
        ghostMovement();
        System.out.println("Player box X: "+ player.getBounds().getX() +" Y:" + player.getBounds().getY());
    }

    private void ghostMovement() {
        String direction = redGhost.getSpriteDirection();
        if (!isSpriteCollidingWithMap(redGhost, redGhost.getSpriteDirection())) {
            redGhost.setSpriteDirection(direction);
            redGhost.move();
        } else {
            // See which directions are available to move in at the ghost's current position.
            // BESIDES the direction it came from.
            ArrayList<String> possibleDirections = new ArrayList<>(Arrays.asList("left", "right", "up", "down"));
            switch(direction) {
            case "left":
                possibleDirections.remove("right");
                break;
            case "right":
                possibleDirections.remove("left");
                break;
            case "up":
                possibleDirections.remove("down");
                break;
            case "down":
                possibleDirections.remove("up");
                break;
            }
            possibleDirections.removeIf(d -> isSpriteCollidingWithMap(redGhost, d));
            Random random = new Random();
            int randDirIndex = random.nextInt(possibleDirections.size());
            redGhost.setSpriteDirection(possibleDirections.get(randDirIndex));
            redGhost.move();
        }
    }

    private void playerMovement() {
        String direction = player.getSpriteDirection();
        boolean allowMove = false;

        if (pressedKey[KeyEvent.VK_W] && !isSpriteCollidingWithMap(player, "up")) {
            direction = "up";
            allowMove = true;
        }

        if (pressedKey[KeyEvent.VK_S] && !isSpriteCollidingWithMap(player, "down")) {
            direction = "down";
            allowMove = true;
        }

        if (pressedKey[KeyEvent.VK_A] && !isSpriteCollidingWithMap(player, "left")) {
            direction = "left";
            allowMove = true;
        }

        if (pressedKey[KeyEvent.VK_D] && !isSpriteCollidingWithMap(player, "right")) {
            direction = "right";
            allowMove = true;
        }

        if (allowMove || !isSpriteCollidingWithMap(player, direction)) {
            player.setSpriteDirection(direction);
            player.move();
        }
    }

    // Basic collision detection based on the 8 map tiles surrounding a sprite.
    private boolean isSpriteCollidingWithMap(Sprite sprite, String direction) {
        boolean isColliding = false;
        int velocity = (int) Math.ceil(sprite.getVelocity());

        switch (direction) {
        case "right":
            isColliding = isCollidingInDirection(sprite, direction, velocity, 0);
            break;
        case "left":
            isColliding = isCollidingInDirection(sprite, direction, -velocity, 0);
            break;
        case "up":
            isColliding = isCollidingInDirection(sprite, direction, 0, -velocity);
            break;
        case "down":
            isColliding = isCollidingInDirection(sprite, direction, 0, velocity);
            break;
        default:
            break;
        }

        return isColliding;
    }

    private boolean isCollidingInDirection(Sprite sprite, String direction, int dx, int dy) {
        boolean isColliding = false;
        for (Tile tile : maps.get(0).getSurroundingTiles(sprite.getBounds().getX(), sprite.getBounds().getY(), direction))
            if (tile.isCollisionEnabled() && sprite.getBounds().overlaps(tile.getBoundsRect(), dx, dy))
                isColliding = true;
        return isColliding;
    }
}
