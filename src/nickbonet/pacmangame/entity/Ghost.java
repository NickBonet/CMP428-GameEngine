package nickbonet.pacmangame.entity;

import nickbonet.gameengine.Rect;
import nickbonet.gameengine.sprite.Animation;
import nickbonet.gameengine.sprite.Sprite;
import nickbonet.gameengine.sprite.SpriteDir;
import nickbonet.gameengine.tile.Tile;
import nickbonet.gameengine.tile.TileMap;

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
    private boolean isScared = false;
    private int targetX;
    private int targetY;

    public Ghost(int x, int y, String spritePrefix, int delay, int targetX, int targetY) {
        super(x, y, spritePrefix, delay);
        this.boundsRect = new Rect(x + 4, y + 5, 8, 8);
        this.targetX = targetX;
        this.targetY = targetY;
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
        Tile targetTile = map.getTileAtPoint(targetX, targetY);

        for (SpriteDir d : possibleDirections) {
            Tile inspectingTile = map.getAdjacentTile(boundsRect.getX(), boundsRect.getY(), d);
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
            if (!isScared()) setCurrentAnimation(directionToMove.toString());
            move();
        }
    }

    public boolean isScared() {
        return isScared;
    }

    public void setScared(boolean scared) {
        isScared = scared;
    }

    public int getTargetX() {
        return targetX;
    }

    public void setTargetX(int targetX) {
        this.targetX = targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    public void setTargetY(int targetY) {
        this.targetY = targetY;
    }
}
