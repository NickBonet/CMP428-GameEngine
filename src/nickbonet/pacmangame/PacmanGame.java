package nickbonet.pacmangame;

import nickbonet.gameengine.GamePanel;
import nickbonet.gameengine.sprite.Sprite;
import nickbonet.gameengine.sprite.SpriteDir;
import nickbonet.gameengine.tile.Tile;
import nickbonet.gameengine.tile.TileMap;
import nickbonet.pacmangame.entity.Ghost;
import nickbonet.pacmangame.entity.Pacman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"serial", "java:S110"})
public class PacmanGame extends GamePanel {
    private static final int WINDOW_HEIGHT = 864;
    private static final int WINDOW_WIDTH = 672;
    private final transient Pacman player = new Pacman(3, 27);
    private final transient Ghost redGhost = new Ghost(80, 27, "red", 100, 207, 0);
    private final transient Ghost blueGhost = new Ghost(64, 27, "blue", 100, 223, 287);
    private final transient Ghost pinkGhost = new Ghost(48, 27, "pink", 100, 16, 0);
    private final transient Ghost orangeGhost = new Ghost(32, 27, "orange", 100, 0, 287);
    private final transient List<Ghost> ghostList = Arrays.asList(redGhost, blueGhost, pinkGhost, orangeGhost);
    private final transient List<TileMap> maps = new ArrayList<>();

    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        JFrame frame = new JFrame("Pac-Man");
        PacmanGame game = new PacmanGame();
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
            for (Tile tile : maps.get(0).getTilesInDirection(player.getBounds().getX(), player.getBounds().getY(), SpriteDir.ALL))
                tile.drawBoundsRect(base);
        }
        player.draw(base);
        for (Ghost ghost : ghostList)
            ghost.draw(base);
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
        for (Ghost ghost : ghostList) {
            ghost.setSpriteDirection(SpriteDir.LEFT);
            ghost.setCurrentAnimation(SpriteDir.LEFT.toString());
        }
        player.setSpriteDirection(SpriteDir.LEFT);
    }

    @Override
    protected void mainGameLogic() {
        playerMovement();
        ghostMovement();
    }

    private static int directionPriority(SpriteDir direction) {
        switch (direction) {
            case UP:
                return 0;
            case LEFT:
                return 1;
            case DOWN:
                return 2;
            case RIGHT:
                return 3;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
    }

    private void ghostMovement() {
        for (Ghost ghost : ghostList) {
            SpriteDir currentDirection = ghost.getSpriteDirection();
            // See which directions are available to move in at the ghost's current position.
            // BESIDES the direction it came from.
            ArrayList<SpriteDir> possibleDirections = new ArrayList<>(Arrays.asList(SpriteDir.values()));
            possibleDirections.remove(currentDirection.getOpposite());
            possibleDirections.remove(SpriteDir.ALL); // remove the ALL enum constant
            possibleDirections.removeIf(d -> isSpriteCollidingWithMap(ghost, d));

            SpriteDir directionToMove = null;
            int prevDistanceChecked = -1;
            int prevDirectionPriority = -1;
            Tile targetTile = maps.get(0).getTileAtPoint(ghost.getTargetX(), ghost.getTargetY());

            for (SpriteDir d : possibleDirections) {
                Tile inspectingTile;
                switch (d) {
                    case LEFT:
                        inspectingTile = maps.get(0).getTileAtPoint(ghost.getBounds().getX() - 8, ghost.getBounds().getY());
                        break;
                    case RIGHT:
                        inspectingTile = maps.get(0).getTileAtPoint(ghost.getBounds().getX() + 8, ghost.getBounds().getY());
                        break;
                    case UP:
                        inspectingTile = maps.get(0).getTileAtPoint(ghost.getBounds().getX(), ghost.getBounds().getY() - 8);
                        break;
                    case DOWN:
                        inspectingTile = maps.get(0).getTileAtPoint(ghost.getBounds().getX(), ghost.getBounds().getY() + 8);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + d);
                }

                if ((prevDistanceChecked == -1 && directionToMove == null) || (prevDistanceChecked > TileMap.euclideanDistanceBetweenTiles(inspectingTile, targetTile)) ||
                        ((prevDistanceChecked == TileMap.euclideanDistanceBetweenTiles(inspectingTile, targetTile)) && (prevDirectionPriority > directionPriority(d)))) {
                    directionToMove = d;
                    prevDistanceChecked = TileMap.euclideanDistanceBetweenTiles(inspectingTile, targetTile);
                    prevDirectionPriority = directionPriority(d);
                }
            }

            if (directionToMove != null) {
                ghost.setSpriteDirection(directionToMove);
                if (!ghost.isScared()) ghost.setCurrentAnimation(directionToMove.toString());
                ghost.move();
            }
        }
    }

    private void playerMovement() {
        SpriteDir direction = player.getSpriteDirection();
        boolean allowMove = false;

        if (pressedKey[KeyEvent.VK_W] && !isSpriteCollidingWithMap(player, SpriteDir.UP)) {
            direction = SpriteDir.UP;
            allowMove = true;
        }

        if (pressedKey[KeyEvent.VK_S] && !isSpriteCollidingWithMap(player, SpriteDir.DOWN)) {
            direction = SpriteDir.DOWN;
            allowMove = true;
        }

        if (pressedKey[KeyEvent.VK_A] && !isSpriteCollidingWithMap(player, SpriteDir.LEFT)) {
            direction = SpriteDir.LEFT;
            allowMove = true;
        }

        if (pressedKey[KeyEvent.VK_D] && !isSpriteCollidingWithMap(player, SpriteDir.RIGHT)) {
            direction = SpriteDir.RIGHT;
            allowMove = true;
        }

        if (allowMove || !isSpriteCollidingWithMap(player, direction)) {
            player.setSpriteDirection(direction);
            player.setCurrentAnimation(direction.toString());
            player.move();
        }
    }

    // Basic collision detection based on the 8 map tiles surrounding a sprite.
    private boolean isSpriteCollidingWithMap(Sprite sprite, SpriteDir direction) {
        int velocity = sprite.getVelocity();
        int dx = 0;
        int dy = 0;

        switch (direction) {
            case RIGHT:
                dx = velocity;
                break;
            case LEFT:
                dx = -velocity;
                break;
            case UP:
                dy = -velocity;
                break;
            case DOWN:
                dy = velocity;
                break;
            default:
                break;
        }

        return isCollidingInDirection(sprite, direction, dx, dy);
    }

    private boolean isCollidingInDirection(Sprite sprite, SpriteDir direction, int dx, int dy) {
        boolean isColliding = false;
        for (Tile tile : maps.get(0).getTilesInDirection(sprite.getBounds().getX(), sprite.getBounds().getY(), direction))
            if (tile.isCollisionEnabled() && sprite.getBounds().overlaps(tile.getBoundsRect(), dx, dy))
                isColliding = true;
        return isColliding;
    }
}
