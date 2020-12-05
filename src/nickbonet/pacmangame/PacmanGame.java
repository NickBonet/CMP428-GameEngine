package nickbonet.pacmangame;

import nickbonet.gameengine.GamePanel;
import nickbonet.gameengine.sprite.Sprite;
import nickbonet.gameengine.sprite.SpriteDir;
import nickbonet.gameengine.tile.Tile;
import nickbonet.gameengine.tile.TileMap;
import nickbonet.pacmangame.entity.Fruit;
import nickbonet.pacmangame.entity.Pacman;
import nickbonet.pacmangame.entity.PowerPellet;
import nickbonet.pacmangame.entity.ghosts.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static nickbonet.gameengine.util.CollisionUtil.isSpriteCollidingWithMap;

@SuppressWarnings({"serial", "java:S110"})
public class PacmanGame extends GamePanel {
    // Some constants used for the game.
    private static final int WINDOW_HEIGHT = 864;
    private static final int WINDOW_WIDTH = 672;
    private static final int PELLETS_ON_BOARD = 244;

    private final transient Pacman player = new Pacman();
    private final transient RedGhost redGhost = new RedGhost(207, 0);
    private final transient BlueGhost blueGhost = new BlueGhost(223, 287);
    private final transient PinkGhost pinkGhost = new PinkGhost(16, 0);
    private final transient OrangeGhost orangeGhost = new OrangeGhost(0, 287);

    private final transient List<Ghost> ghostList = Arrays.asList(redGhost, blueGhost, pinkGhost, orangeGhost);
    private final transient List<TileMap> maps = new ArrayList<>();
    private final transient List<PowerPellet> powerPelletsActive = new ArrayList<>();
    private final transient List<Timer> currentTimers = new ArrayList<>();
    private Font gameFont;
    private int score = 0;
    private int level = 0;
    private LevelState currentLevelState = LevelState.LEVEL_STARTING;
    private int pelletsLeft = PELLETS_ON_BOARD;
    private transient Fruit currentFruit = null;
    private boolean firstFruitSpawned = false;
    private boolean secondFruitSpawned = false;

    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        System.setProperty(DEBUG_PROPERTY_NAME, "false");
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
        if (currentFruit != null) currentFruit.draw(base);
        for (PowerPellet pellet : powerPelletsActive) pellet.draw(base);
        player.draw(base);
        for (Ghost ghost : ghostList) ghost.draw(base);
        // Debug info/metrics
        if (!maps.isEmpty() && System.getProperty(DEBUG_PROPERTY_NAME).equals("true")) drawDebugElements(base);
        base.dispose();
        g.drawImage(frame, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null);
        drawUIElements(g);
    }

    private void drawUIElements(Graphics g) {
        g.setColor(Color.white);
        g.setFont(gameFont);
        g.drawString("Score", 50, 35);
        g.drawString(String.valueOf(score), 50, 58);
        g.drawString("Level:" + level, 268, 848);
        g.setColor(Color.yellow);
        if (currentLevelState == LevelState.LEVEL_RUNNING && isPaused) g.drawString("Ready?", 275, 500);
        if (currentLevelState == LevelState.LEVEL_STARTING && isPaused) g.drawString("Complete!", 250, 500);
        try {
            for (int i = 0; i < player.getNumberOfLives(); i++)
                g.drawImage(ImageIO.read(new File("assets/sprites/pacman/pac_left_1.gif")), 50 + (i * 46), 815, 48, 48, null);
            g.drawImage(ImageIO.read(new File("assets/sprites/items/" + Fruit.getFruitName() + ".png")), 600, 815, 48, 48, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawDebugElements(Graphics base) {
        for (Tile tile : maps.get(0).getTilesInDirection(player.getBounds().getX(), player.getBounds().getY(), SpriteDir.ALL))
            tile.drawBoundsRect(base);
        base.setColor(Color.cyan);
        base.drawOval(((int) player.getBounds().getX()) + 4 - 64, ((int) player.getBounds().getY()) + 4 - 64, 128, 128);
        base.drawRect(maps.get(0).getTileAtPoint(blueGhost.getChaseTargetX(), blueGhost.getChaseTargetY()).getX(),
                maps.get(0).getTileAtPoint(blueGhost.getChaseTargetX(), blueGhost.getChaseTargetY()).getY(), 8, 8);
        base.setColor(Color.MAGENTA);
        base.drawRect((int) redGhost.getChaseTargetX(), (int) redGhost.getChaseTargetY(), 8, 8);
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
            checkIfInTunnel(ghost);
            ghost.calculateNextMove(maps.get(0), player.getSpriteDirection());
        }
    }

    private void playerMovement() {
        checkIfInTunnel(player);

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

    private void checkIfInTunnel(Sprite sprite) {
        if (maps.get(0).getTileAtPoint(sprite.getBounds().getX(), sprite.getBounds().getY()) == null) {
            if (sprite.getX() < 0 && sprite.getSpriteDirection() == SpriteDir.LEFT) sprite.setNewLocation(225, 131);
            else if (sprite.getX() > 223 && sprite.getSpriteDirection() == SpriteDir.RIGHT)
                sprite.setNewLocation(-8, 131);
        }

        if (sprite instanceof Ghost) {
            if ((sprite.getX() > 186 || sprite.getX() < 30) && (sprite.getY() < 146 && sprite.getY() > 129))
                sprite.setVelocity(0.6);
            else
                ((Ghost) sprite).resetVelocity(level);
        }
    }

    private void playerObjectCheck() {
        if (maps.get(0).getObjectTileAtPoint(player.getBounds().getX(), player.getBounds().getY()) != null) {
            Tile objectTile = maps.get(0).getObjectTileAtPoint(player.getBounds().getX(), player.getBounds().getY());
            if (objectTile.getTileID() == 404) {
                maps.get(0).removeObjectTile(objectTile.getX(), objectTile.getY());
                pelletsLeft -= 1;
                score += 10;
                player.dotEatenVelocity();
            }
        } else player.resetVelocity(level);
    }

    private void playerEntityCollisionCheck() {
        for (Ghost ghost : ghostList) {
            if (ghost.getBounds().overlaps(player.getBounds(), 0, 0)) {
                currentLevelState = LevelState.PAC_HIT;
                pauseGameLoop(250);
                break;
            }
        }

        if (currentFruit != null && currentFruit.getBounds().overlaps(player.getBounds(), 0, 0)) {
            score += currentFruit.getPointValue();
            currentFruit = null;
        }

        Iterator<PowerPellet> i = powerPelletsActive.iterator();
        while (i.hasNext()) {
            PowerPellet pellet = i.next();
            if (pellet.getBounds().overlaps(player.getBounds(), 0, 0)) {
                score += 500;
                pelletsLeft -= 1;
                i.remove();
            }
        }
    }

    /**
     * Preps the game for a new level to start.
     */
    private void newLevelStarting() {
        level += 1;
        Fruit.setFruitForLevel(level);
        maps.get(0).initializeMap();
        maps.get(0).setCollisionOverrideOnTile(104, 120);
        maps.get(0).setCollisionOverrideOnTile(112, 120);
        pelletsLeft = PELLETS_ON_BOARD;
        redGhost.setInGhostHouse(false);
        restartLevelInProgress();
        powerPelletsActive.add(new PowerPellet(204, 203));
        powerPelletsActive.add(new PowerPellet(4, 203));
        powerPelletsActive.add(new PowerPellet(4, 44));
        powerPelletsActive.add(new PowerPellet(204, 44));
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
        if (pressedKey[KeyEvent.VK_6]) logger.info("Pac X: " + player.getX() + " Y: " + player.getY());
        if (pressedKey[KeyEvent.VK_7]) System.setProperty(DEBUG_PROPERTY_NAME, "true");
        if (pressedKey[KeyEvent.VK_8]) System.setProperty(DEBUG_PROPERTY_NAME, "false");
        if (pressedKey[KeyEvent.VK_ESCAPE]) isRunning = false;

        switch (pelletsLeft) {
            case 0:
                currentLevelState = LevelState.LEVEL_FINISHED;
                break;
            case 70:
                if (!secondFruitSpawned) {
                    spawnFruit();
                    secondFruitSpawned = true;
                }
                break;
            case 170:
                if (!firstFruitSpawned) {
                    spawnFruit();
                    firstFruitSpawned = true;
                }
                break;
            default:
                break;
        }
        if (pelletsLeft != 0) {
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
        currentFruit = null;
        firstFruitSpawned = false;
        secondFruitSpawned = false;
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
        currentFruit = null;
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
            ghost.resetVelocity(level);
        }
        currentFruit = null;
        player.respawn();
        player.resetVelocity(level);
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

    private void spawnFruit() {
        if (currentFruit == null) {
            currentFruit = new Fruit();
            Timer fruitTimer = new Timer(7500, e -> currentFruit = null);
            fruitTimer.setRepeats(false);
            fruitTimer.start();
            currentTimers.add(fruitTimer);
        }
    }

    private void setupGhostHouseExit(Ghost ghost, int delay) {
        Timer leaveHouseTimer = new Timer(delay, e -> {
            ghost.setCanTraverseOverrideTiles(true);
            ghost.setMoving(true);
            Timer disableOverride = new Timer(550, f -> {
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
