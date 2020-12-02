package nickbonet.pacmangame;

import nickbonet.gameengine.GamePanel;
import nickbonet.gameengine.sprite.SpriteDir;
import nickbonet.gameengine.tile.Tile;
import nickbonet.gameengine.tile.TileMap;
import nickbonet.pacmangame.entity.Pacman;
import nickbonet.pacmangame.entity.ghosts.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nickbonet.gameengine.util.CollisionUtil.isSpriteCollidingWithMap;

@SuppressWarnings({"serial", "java:S110"})
public class PacmanGame extends GamePanel {
    private static final int WINDOW_HEIGHT = 864;
    private static final int WINDOW_WIDTH = 672;
    private final transient Pacman player = new Pacman(192, 27);
    private final transient RedGhost redGhost = new RedGhost(80, 27, 100, 207, 0);
    private final transient BlueGhost blueGhost = new BlueGhost(64, 27, 100, 223, 287);
    private final transient PinkGhost pinkGhost = new PinkGhost(48, 27, 100, 16, 0);
    private final transient OrangeGhost orangeGhost = new OrangeGhost(32, 27, 100, 0, 287);
    private final transient List<Ghost> ghostList = Arrays.asList(redGhost, blueGhost, pinkGhost, orangeGhost);
    private final transient List<TileMap> maps = new ArrayList<>();
    private int pelletsLeft = 240;
    private int score = 0;
    private LevelState currentLevelState = LevelState.LEVEL_RUNNING;

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
        if (!maps.isEmpty()) maps.get(0).drawMap(base);
        player.draw(base);
        for (Ghost ghost : ghostList)
            ghost.draw(base);
        // Debug info/metrics
        //if (!maps.isEmpty()) {
        //    paintDebugVisuals(base);
        //}
        base.dispose();
        g.drawImage(frame, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null);
    }

    private void paintDebugVisuals(Graphics base) {
        for (Tile tile : maps.get(0).getTilesInDirection(player.getBounds().getX(), player.getBounds().getY(), SpriteDir.ALL))
            tile.drawBoundsRect(base);
        base.setColor(Color.cyan);
        base.drawOval(player.getBounds().getX() + 4 - 64, player.getBounds().getY() + 4 - 64, 128, 128);
        base.drawRect(maps.get(0).getTileAtPoint(blueGhost.getChaseTargetX(), blueGhost.getChaseTargetY()).getX(),
                maps.get(0).getTileAtPoint(blueGhost.getChaseTargetX(), blueGhost.getChaseTargetY()).getY(), 8, 8);
        base.setColor(Color.MAGENTA);
        base.drawRect(redGhost.getChaseTargetX(), redGhost.getChaseTargetY(), 8, 8);
        base.setColor(Color.PINK);
        base.drawRect(maps.get(0).getTileAtPoint(pinkGhost.getChaseTargetX(), pinkGhost.getChaseTargetY()).getX(),
                maps.get(0).getTileAtPoint(pinkGhost.getChaseTargetX(), pinkGhost.getChaseTargetY()).getY(), 8, 8);
        base.setColor(Color.ORANGE);
        base.drawRect(maps.get(0).getTileAtPoint(orangeGhost.getChaseTargetX(), orangeGhost.getChaseTargetY()).getX(),
                maps.get(0).getTileAtPoint(orangeGhost.getChaseTargetX(), orangeGhost.getChaseTargetY()).getY(), 8, 8);
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
            ghost.setMoving(true);
        }
        player.setSpriteDirection(SpriteDir.LEFT);
        player.setMoving(true);
    }

    @Override
    protected void mainGameLogic() {
        switch (currentLevelState) {
            case LEVEL_RESTARTING:
                player.setMoving(false);
                for (Ghost ghost : ghostList) {
                    ghost.setVisible(true);
                    ghost.setNewLocation(80, 27);
                    ghost.setMoving(true);
                }
                player.setNewLocation(192, 27);
                player.setSpriteDirection(SpriteDir.LEFT);
                player.setCurrentAnimation("left");
                pauseGameLoop(500);
                currentLevelState = LevelState.LEVEL_RUNNING;
                player.setMoving(true);
                break;
            case LEVEL_RUNNING:
                if (pressedKey[KeyEvent.VK_0])
                    for (Ghost ghost : ghostList)
                        ghost.setState(GhostState.SCATTER);
                if (pressedKey[KeyEvent.VK_9])
                    for (Ghost ghost : ghostList)
                        ghost.setState(GhostState.CHASE);
                playerMovement();
                playerEntityCollisionCheck();
                playerObjectCheck();
                ghostMovement();
                break;
            case PAC_HIT:
                handlePacmanLifeLost();
                break;
            case PAC_DIED:
            default:
                break;
        }
    }

    private void ghostMovement() {
        redGhost.updateChaseTarget(player.getBounds());
        pinkGhost.updateChaseTarget(player, maps.get(0));
        orangeGhost.updateChaseTarget(player.getBounds(), maps.get(0));
        blueGhost.updateChaseTarget(player, redGhost.getBounds(), maps.get(0));
        for (Ghost ghost : ghostList) {
            ghost.calculateNextMove(maps.get(0));
        }
    }

    private void playerMovement() {
        SpriteDir direction = player.getSpriteDirection();
        boolean allowMove = false;

        if (pressedKey[KeyEvent.VK_W] && !isSpriteCollidingWithMap(player, SpriteDir.UP, maps.get(0))) {
            direction = SpriteDir.UP;
            allowMove = true;
        }

        if (pressedKey[KeyEvent.VK_S] && !isSpriteCollidingWithMap(player, SpriteDir.DOWN, maps.get(0))) {
            direction = SpriteDir.DOWN;
            allowMove = true;
        }

        if (pressedKey[KeyEvent.VK_A] && !isSpriteCollidingWithMap(player, SpriteDir.LEFT, maps.get(0))) {
            direction = SpriteDir.LEFT;
            allowMove = true;
        }

        if (pressedKey[KeyEvent.VK_D] && !isSpriteCollidingWithMap(player, SpriteDir.RIGHT, maps.get(0))) {
            direction = SpriteDir.RIGHT;
            allowMove = true;
        }

        if (allowMove || !isSpriteCollidingWithMap(player, direction, maps.get(0))) {
            player.setSpriteDirection(direction);
            player.setCurrentAnimation(direction.toString());
            player.move();
        }
    }

    private void playerObjectCheck() {
        if (maps.get(0).getObjectTileAtPoint(player.getBounds().getX(), player.getBounds().getY()) != null) {
            Tile objectTile = maps.get(0).getObjectTileAtPoint(player.getBounds().getX(), player.getBounds().getY());
            if (objectTile.getTileID() == 404) {
                maps.get(0).removeObjectTile(objectTile.getX(), objectTile.getY());
                pelletsLeft -= 1;
                score += 10;
                System.out.println("Score: " + score);
            }
        }
    }

    private void playerEntityCollisionCheck() {
        for (Ghost ghost : ghostList) {
            if (ghost.getBounds().overlaps(player.getBounds(), 0, 0)) {
                currentLevelState = LevelState.PAC_HIT;
                pauseGameLoop(250);
                break;
            }
        }
    }

    private void handlePacmanLifeLost() {
        for (Ghost ghost : ghostList) {
            ghost.setMoving(false);
            ghost.setVisible(false);
        }
        player.setCurrentAnimation("died");
        player.setMoving(true);
        player.restartAnimation("died");
        // Death animation is set to 150ms delay between each frame, 12 frames. 1800ms total
        Timer restartTimer = new Timer(1800, e -> {
            player.stopAnimation("died");
            currentLevelState = LevelState.LEVEL_RESTARTING;
            pauseGameLoop(250);
        });
        restartTimer.setRepeats(false);
        restartTimer.start();
        currentLevelState = LevelState.PAC_DIED;
    }

    enum LevelState {
        LEVEL_STARTING, LEVEL_RESTARTING, LEVEL_RUNNING, LEVEL_FINISHED, PAC_HIT, PAC_DIED;
    }
}
