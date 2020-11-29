package nickbonet.gameengine.util;

import nickbonet.gameengine.sprite.Sprite;
import nickbonet.gameengine.sprite.SpriteDir;
import nickbonet.gameengine.tile.Tile;
import nickbonet.gameengine.tile.TileMap;

/**
 * CollisionUtil - Utility class for collision detection between sprites and collision tiles in a tile map.
 */
public class CollisionUtil {

    private CollisionUtil() {

    }

    // Basic collision detection based on the 8 map tiles surrounding a sprite.
    public static boolean isSpriteCollidingWithMap(Sprite sprite, SpriteDir direction, TileMap map) {
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

        return isCollidingInDirection(sprite, direction, dx, dy, map);
    }

    private static boolean isCollidingInDirection(Sprite sprite, SpriteDir direction, int dx, int dy, TileMap map) {
        boolean isColliding = false;
        for (Tile tile : map.getTilesInDirection(sprite.getBounds().getX(), sprite.getBounds().getY(), direction))
            if (tile.isCollisionEnabled() && sprite.getBounds().overlaps(tile.getBoundsRect(), dx, dy))
                isColliding = true;
        return isColliding;
    }
}
