package nickbonet.pacmangame.entity.ghosts;

import nickbonet.gameengine.sprite.Animation;
import nickbonet.gameengine.sprite.Sprite;
import nickbonet.gameengine.sprite.SpriteDir;
import nickbonet.gameengine.tile.Tile;
import nickbonet.gameengine.tile.TileMap;
import nickbonet.pacmangame.GhostState;

import java.util.ArrayList;
import java.util.Arrays;

import static nickbonet.gameengine.util.CollisionUtil.isSpriteCollidingWithMap;
import static nickbonet.pacmangame.Util.directionPriority;

/**
 * Ghost - Little ghost enemies!
 *
 * @author Nicholas Bonet
 */
public class Ghost extends Sprite {
    protected final int scatterTargetX;
    protected final int scatterTargetY;
    protected double chaseTargetX = 0;
    protected double chaseTargetY = 0;
    protected int ghostHouseExitX = 104;
    protected int ghostHouseExitY = 107;
    protected boolean inGhostHouse = true;
    private GhostState currentState = GhostState.SCATTER;

    public Ghost(int x, int y, String spritePrefix, int scatterTargetX, int scatterTargetY) {
        super(x, y, spritePrefix, 100, "ghost");
        boundsOffsetX = 4;
        boundsOffsetY = 5;
        boundsWidth = 8;
        boundsHeight = 8;
        initBoundsRect();
        this.scatterTargetX = scatterTargetX;
        this.scatterTargetY = scatterTargetY;
        velocity = 0.81;
    }

    // Used to calculate the ghost's next move towards its currently set target tile.
    public void calculateNextMove(TileMap map) {
        // See which directions are available to move in at the ghost's current position.
        // BESIDES the direction it came from.
        ArrayList<SpriteDir> possibleDirections = new ArrayList<>(Arrays.asList(SpriteDir.values()));
        possibleDirections.remove(currentDirection.getOpposite());
        possibleDirections.remove(SpriteDir.ALL); // remove the ALL enum constant
        possibleDirections.removeIf(d -> isSpriteCollidingWithMap(this, d, map));

        SpriteDir directionToMove = null;
        int prevDistanceChecked = -1;
        int prevDirectionPriority = -1;
        Tile targetTile;
        if (!inGhostHouse) targetTile = currentTargetTile(map);
        else targetTile = map.getTileAtPoint(ghostHouseExitX, ghostHouseExitY);

        for (SpriteDir d : possibleDirections) {
            Tile inspectingTile = map.getNearbyTile(boundsRect.getX(), boundsRect.getY(), d, 1);
            int distanceFromTileToTarget = TileMap.euclideanDistanceBetweenTiles(inspectingTile, targetTile);
            // If there's no current direction set, just set the direction to that of the current tile being checked.
            // If there is a direction set, check if the tile being checked has a shorter distance to the target tile.
            // If it does, set direction to that of the new tile.
            // If the distance from the tile being checked to the target tile is tied with another tile, select a direction based on
            // direction priority. (in Pac-Man, up > left > down > right, up being most preferred).
            if ((prevDistanceChecked == -1 && directionToMove == null) || (prevDistanceChecked > distanceFromTileToTarget) ||
                    ((prevDistanceChecked == distanceFromTileToTarget) && (prevDirectionPriority > directionPriority(d)))) {
                directionToMove = d;
                prevDistanceChecked = distanceFromTileToTarget;
                prevDirectionPriority = directionPriority(d);
            }
        }

        if (directionToMove != null) {
            setSpriteDirection(directionToMove);
            if (currentState != GhostState.FRIGHTENED) setCurrentAnimation(directionToMove.toString());
            move();
        }
    }

    public void setState(GhostState state) {
        currentState = state;
        if (!inGhostHouse) currentDirection = currentDirection.getOpposite();
    }

    public double getChaseTargetX() {
        return chaseTargetX;
    }

    public double getChaseTargetY() {
        return chaseTargetY;
    }

    public boolean isInGhostHouse() {
        return inGhostHouse;
    }

    public void setInGhostHouse(boolean inGhostHouse) {
        this.inGhostHouse = inGhostHouse;
    }

    @Override
    protected void initAnimations() {
        Animation scaredAnim = new Animation(100, "scared_", getSpriteDirectory());
        animDict.put("scared", scaredAnim);
    }

    // Returns the correct target tile, based on the current ghost state.
    private Tile currentTargetTile(TileMap map) {
        switch (currentState) {
            case CHASE:
                return map.getTileAtPoint(chaseTargetX, chaseTargetY);
            case SCATTER:
                return map.getTileAtPoint(scatterTargetX, scatterTargetY);
            case FRIGHTENED: // TODO: implement this
            default:
                return null;
        }
    }
}
