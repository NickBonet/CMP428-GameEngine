package nickbonet.testgame;

import nickbonet.gameengine.GamePanel;
import nickbonet.gameengine.sprite.Sprite;
import nickbonet.gameengine.tile.Tile;
import nickbonet.gameengine.tile.TileMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

@SuppressWarnings({"serial", "java:S110"})
public class TestGame extends GamePanel {
    private static final int WINDOW_HEIGHT = 864;
    private static final int WINDOW_WIDTH = 672;
    private final transient Pacman player = new Pacman(3, 27);
    private final transient Ghost redGhost = new Ghost(80, 27, "red", 100, 207, 0);
    private final transient Ghost blueGhost = new Ghost(180, 27, "blue", 100, 223, 287);
    private final transient Ghost pinkGhost = new Ghost(48, 27, "pink", 100, 16, 0);
    private final transient Ghost orangeGhost = new Ghost(32, 27, "orange", 100, 0, 287);
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

    private int directionPriority(String direction) {
        switch (direction) {
            case "up":
                return 0;
            case "left":
                return 1;
            case "down":
                return 2;
            case "right":
                return 3;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
    }

    private int taxiCabDistance(Tile firstTile, Tile secondTile) {
        int x1 = (firstTile.getX() + (firstTile.getWidth() / 2));
        int y1 = (firstTile.getY() + (firstTile.getHeight() / 2));
        int x2 = (secondTile.getX() + (secondTile.getWidth() / 2));
        int y2 = (secondTile.getY() + (secondTile.getHeight() / 2));

        return Math.abs((x2 - x1)) + Math.abs((y2 - y1));
    }

    private void ghostMovement() {
        for (Ghost ghost : ghostList) {
            String currentDirection = ghost.getSpriteDirection();
            // See which directions are available to move in at the ghost's current position.
            // BESIDES the direction it came from.
            ArrayList<String> possibleDirections = new ArrayList<>(Arrays.asList("left", "right", "up", "down"));
            switch (currentDirection) {
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

            String directionToMove = null;
            int prevDistanceChecked = -1;
            int prevDirectionPriority = -1;
            Tile targetTile = maps.get(0).getTileAtPoint(ghost.getTargetX(), ghost.getTargetY());

            for (String d : possibleDirections) {
                Tile inspectingTile;
                switch (d) {
                    case "left":
                        inspectingTile = maps.get(0).getTileAtPoint(ghost.getBounds().getX() - 8, ghost.getBounds().getY());
                        break;
                    case "right":
                        inspectingTile = maps.get(0).getTileAtPoint(ghost.getBounds().getX() + 8, ghost.getBounds().getY());
                        break;
                    case "up":
                        inspectingTile = maps.get(0).getTileAtPoint(ghost.getBounds().getX(), ghost.getBounds().getY() - 8);
                        break;
                    case "down":
                        inspectingTile = maps.get(0).getTileAtPoint(ghost.getBounds().getX(), ghost.getBounds().getY() + 8);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + d);
                }

                if ((prevDistanceChecked == -1 && directionToMove == null) || (prevDistanceChecked > taxiCabDistance(inspectingTile, targetTile))) {
                    directionToMove = d;
                    prevDistanceChecked = taxiCabDistance(inspectingTile, targetTile);
                    prevDirectionPriority = directionPriority(d);
                } else if (prevDistanceChecked == taxiCabDistance(inspectingTile, targetTile)) {
                    if (prevDirectionPriority > directionPriority(d)) {
                        directionToMove = d;
                        prevDistanceChecked = taxiCabDistance(inspectingTile, targetTile);
                        prevDirectionPriority = directionPriority(d);
                    }
                }
            }

            ghost.setSpriteDirection(directionToMove);
            if (!ghost.isScared()) ghost.setCurrentAnimation(directionToMove);
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
