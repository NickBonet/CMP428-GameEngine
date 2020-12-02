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
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nickbonet.gameengine.util.CollisionUtil.isSpriteCollidingWithMap;

@SuppressWarnings({"serial", "java:S110"})
public class PacmanGame extends GamePanel {
    private static final int WINDOW_HEIGHT = 864;
    private static final int WINDOW_WIDTH = 672;
    private static final int PELLETS_ON_BOARD = 240;
    private final transient Pacman player = new Pacman();
    private final transient RedGhost redGhost = new RedGhost(207, 0);
    private final transient BlueGhost blueGhost = new BlueGhost(223, 287);
    private final transient PinkGhost pinkGhost = new PinkGhost(16, 0);
    private final transient OrangeGhost orangeGhost = new OrangeGhost(0, 287);
    private final transient List<Ghost> ghostList = Arrays.asList(redGhost, blueGhost, pinkGhost, orangeGhost);
    private final transient List<TileMap> maps = new ArrayList<>();
    private final transient List<Timer> currentTimers = new ArrayList<>();
    private Font gameFont;
    private int pelletsLeft = PELLETS_ON_BOARD;
    private int score = 0;
    private int level = 0;
    private boolean enableDebugVisuals = false;
    private LevelState currentLevelState = LevelState.LEVEL_STARTING;

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
        frame.setResizable(false);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        game.runGame();
    }

    @Override
    // Paints all the components onto a base image, then that image is upscaled 3x (very roughly for now.).
    public void paintComponent(Graphics g) {
        BufferedImage frame = new BufferedImage(224, 288, BufferedImage.TYPE_INT_RGB);
        Graphics base = frame.createGraphics();
        if (!maps.isEmpty()) maps.get(0).drawMap(base);
        player.draw(base);
        for (Ghost ghost : ghostList)
            ghost.draw(base);
        // Debug info/metrics
        if (!maps.isEmpty() && enableDebugVisuals) drawDebugElements(base);
        base.dispose();
        g.drawImage(frame, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null);
        drawUIElements(g);
    }

    private void drawUIElements(Graphics g) {
        g.setColor(Color.white);
        g.setFont(gameFont);
        g.drawString("Score", 25, 35);
        g.drawString(String.valueOf(score), 25, 58);
        g.drawString("Level:" + level, 25, 848);
        g.setColor(Color.yellow);
        if (currentLevelState == LevelState.LEVEL_RUNNING && isPaused) g.drawString("Ready?", 275, 500);
        if (currentLevelState == LevelState.LEVEL_STARTING && isPaused) g.drawString("Complete!", 250, 500);
    }

    private void drawDebugElements(Graphics base) {
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
            Font uiTextFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/arcade.ttf"));
            gameFont = uiTextFont.deriveFont(20f);
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
            case LEVEL_STARTING:
                newLevelStarting();
                break;
            case LEVEL_RESTARTING:
                restartLevelInProgress();
                break;
            case LEVEL_RUNNING:
                levelInProgress();
                break;
            case LEVEL_FINISHED:
                levelFinished();
                break;
            case PAC_HIT:
                handlePacmanLifeLost();
                break;
            case PAC_DIED: // Just lets the death animation finish playing.
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

    /**
     * Preps the game for a new level to start.
     */
    private void newLevelStarting() {
        level += 1;
        maps.get(0).initializeMap();
        // TODO: refactor this into a method in TileMap
        Tile doorTile1 = maps.get(0).getTileAtPoint(104, 120);
        Tile doorTile2 = maps.get(0).getTileAtPoint(112, 120);
        doorTile1.setCollisionOverride(true);
        doorTile2.setCollisionOverride(true);
        pelletsLeft = PELLETS_ON_BOARD;
        redGhost.setInGhostHouse(false);
        restartLevelInProgress();
    }

    /**
     * Runs when there's currently a level in progress.
     */
    private void levelInProgress() {
        if (pressedKey[KeyEvent.VK_0])
            for (Ghost ghost : ghostList)
                ghost.setState(GhostState.SCATTER);
        if (pressedKey[KeyEvent.VK_9])
            for (Ghost ghost : ghostList)
                ghost.setState(GhostState.CHASE);
        if (pressedKey[KeyEvent.VK_6]) System.out.println("Pac X: " + player.getX() + " Y: " + player.getY());
        if (pressedKey[KeyEvent.VK_7]) enableDebugVisuals = true;
        if (pressedKey[KeyEvent.VK_8]) enableDebugVisuals = false;
        if (pressedKey[KeyEvent.VK_ESCAPE]) isRunning = false;

        if (pelletsLeft == 0) currentLevelState = LevelState.LEVEL_FINISHED;
        else {
            playerMovement();
            playerEntityCollisionCheck();
            playerObjectCheck();
            ghostMovement();
        }
    }

    /**
     * Runs at the end of a level.
     */
    private void levelFinished() {
        pauseGameLoop(2000);
        player.setMoving(false);
        resetAllTimers();
        currentLevelState = LevelState.LEVEL_STARTING;
    }

    /**
     * Triggered whenever Pac-Man is hit. (PAC_HIT state is active)
     */
    private void handlePacmanLifeLost() {
        for (Ghost ghost : ghostList) {
            ghost.setMoving(false);
            ghost.setVisible(false);
        }
        player.setCurrentAnimation("died");
        player.restartAnimation("died"); // makes sure the animation starts from the beginning
        player.changeNumberOfLives(-1);
        // Death animation is set to 150ms delay between each frame, 12 frames. 1800ms total
        Timer restartTimer = new Timer(1800, e -> {
            player.stopAnimation("died");
            currentLevelState = LevelState.LEVEL_RESTARTING;
            pauseGameLoop(250);
        });
        restartTimer.setRepeats(false);
        restartTimer.start();
        resetAllTimers();
        currentLevelState = LevelState.PAC_DIED;
    }

    /**
     * Runs when LEVEL_RESTARTING is the current state.
     * Prepares the game to resume after Pac-Man has died/was hit.
     */
    private void restartLevelInProgress() {
        if (player.getNumberOfLives() < 0) System.exit(0);
        for (Ghost ghost : ghostList) {
            ghost.respawn();
            ghost.setVisible(true);
            ghost.setMoving(!ghost.isInGhostHouse());
        }
        player.respawn();
        player.setSpriteDirection(SpriteDir.LEFT);
        player.setCurrentAnimation("left");
        pauseGameLoop(1500);
        player.setMoving(true);
        setupGhostHouseExit(pinkGhost, 3500);
        setupGhostHouseExit(blueGhost, 5500);
        setupGhostHouseExit(orangeGhost, 8000);
        currentLevelState = LevelState.LEVEL_RUNNING;
    }

    private void resetAllTimers() {
        currentTimers.forEach(Timer::stop);
        currentTimers.clear();
    }

    private void setupGhostHouseExit(Ghost ghost, int delay) {
        Timer leaveHouseTimer = new Timer(delay, e -> {
            ghost.setCanTraverseOverrideTiles(true);
            ghost.setMoving(true);
            Timer disableOverride = new Timer(400, f -> {
                ghost.setInGhostHouse(false);
                ghost.setCanTraverseOverrideTiles(false);
            });
            disableOverride.setRepeats(false);
            disableOverride.start();
            currentTimers.add(disableOverride);
        });
        leaveHouseTimer.setRepeats(false);
        leaveHouseTimer.start();
        currentTimers.add(leaveHouseTimer);
    }

    enum LevelState {
        LEVEL_STARTING, LEVEL_RESTARTING, LEVEL_RUNNING, LEVEL_FINISHED, PAC_HIT, PAC_DIED
    }
}
