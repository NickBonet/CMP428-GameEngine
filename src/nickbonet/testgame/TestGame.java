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
    private static final int WINDOW_HEIGHT = 864;
    private static final int WINDOW_WIDTH = 672;
    private static final Random random = new Random();
    private final transient Pacman player = new Pacman(3, 11);
    private final transient Ghost redGhost = new Ghost(80, 11, "red", 100);
    private final transient Ghost blueGhost = new Ghost(64, 11, "blue", 100);
    private final transient Ghost pinkGhost = new Ghost(48, 11, "pink", 100);
    private final transient Ghost orangeGhost = new Ghost(32, 11, "orange", 100);
    private final transient List<Ghost> ghostList = new ArrayList<>();
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
        BufferedImage frame = new BufferedImage(224, 288, BufferedImage.TYPE_INT_RGB);
        Graphics base = frame.createGraphics();
        super.paintComponent(base);
        if (!maps.isEmpty()) {
            maps.get(0).drawMap(base);
            for (Tile tile : maps.get(0).getSurroundingTiles(player.getBounds().getX(), player.getBounds().getY(), "all"))
                tile.drawBoundsRect(base);
        }
        player.draw(base);
        redGhost.draw(base);
        blueGhost.draw(base);
        pinkGhost.draw(base);
        orangeGhost.draw(base);
        base.dispose();
        g.drawImage(frame, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null);
    }

    @Override
    protected void initObjects() {
        try {
            maps.add(loadTileMap("filledmap.tilemap"));
            maps.get(0).initializeMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ghostList.add(redGhost);
        ghostList.add(blueGhost);
        ghostList.add(pinkGhost);
        ghostList.add(orangeGhost);
        for (Ghost ghost : ghostList) {
            ghost.setSpriteDirection("left");
            ghost.setCurrentAnimation("left");
        }
    }

    @Override
    protected void mainGameLogic() {
        playerMovement();
        ghostMovement();
    }

    private void ghostMovement() {
        for (Ghost ghost : ghostList) {
            String direction = ghost.getSpriteDirection();
            if (!isSpriteCollidingWithMap(ghost, ghost.getSpriteDirection())) {
                ghost.setSpriteDirection(direction);
            } else {
                // See which directions are available to move in at the ghost's current position.
                // BESIDES the direction it came from.
                ArrayList<String> possibleDirections = new ArrayList<>(Arrays.asList("left", "right", "up", "down"));
                switch (direction) {
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
                possibleDirections.removeIf(d -> isSpriteCollidingWithMap(ghost, d));
                int randDirIndex = random.nextInt(possibleDirections.size());
                ghost.setSpriteDirection(possibleDirections.get(randDirIndex));
                if (!ghost.isScared()) ghost.setCurrentAnimation(possibleDirections.get(randDirIndex));
            }
            ghost.move();
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
            player.setCurrentAnimation(direction);
            player.move();
        }
    }

    // Basic collision detection based on the 8 map tiles surrounding a sprite.
    private boolean isSpriteCollidingWithMap(Sprite sprite, String direction) {
        boolean isColliding = false;
        int velocity = sprite.getVelocity();

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
