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
    protected int chaseTargetX = 0;
    protected int chaseTargetY = 0;
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
    }

    @Override
    protected void initAnimations() {
        Animation scaredAnim = new Animation(100, "scared_", getSpriteDirectory());
        animDict.put("scared", scaredAnim);
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
        Tile targetTile = currentTargetTile(map);

        for (SpriteDir d : possibleDirections) {
            Tile inspectingTile = map.getNearbyTile(boundsRect.getX(), boundsRect.getY(), d, 1);
            // If there's no current direction set, just set the direction to that of the current tile being checked.
            // If there is a direction set, check if the tile being checked has a shorter distance to the target tile.
            // If it does, set direction to that of the new tile.
            // If the distance from the tile being checked to the target tile is tied with another tile, select a direction based on
            // direction priority. (in Pac-Man, up > left > down > right, up being most preferred).
            if ((prevDistanceChecked == -1 && directionToMove == null) ||
                    (prevDistanceChecked > TileMap.euclideanDistanceBetweenTiles(inspectingTile, targetTile)) ||
                    ((prevDistanceChecked == TileMap.euclideanDistanceBetweenTiles(inspectingTile, targetTile)) &&
                            (prevDirectionPriority > directionPriority(d)))) {
                directionToMove = d;
                prevDistanceChecked = TileMap.euclideanDistanceBetweenTiles(inspectingTile, targetTile);
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
        currentDirection = currentDirection.getOpposite();
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

    public int getChaseTargetX() {
        return chaseTargetX;
    }

    public int getChaseTargetY() {
        return chaseTargetY;
    }

    public boolean isInGhostHouse() {
        return inGhostHouse;
    }

    public void setInGhostHouse(boolean inGhostHouse) {
        this.inGhostHouse = inGhostHouse;
    }
}
