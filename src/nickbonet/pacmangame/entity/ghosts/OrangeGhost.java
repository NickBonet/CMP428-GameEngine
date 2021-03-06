package nickbonet.pacmangame.entity.ghosts;

import nickbonet.gameengine.Rect;
import nickbonet.gameengine.sprite.SpriteDir;
import nickbonet.gameengine.tile.Tile;
import nickbonet.gameengine.tile.TileMap;

public class OrangeGhost extends Ghost {
    public OrangeGhost(int scatterTargetX, int scatterTargetY) {
        super(120, 131, "orange", scatterTargetX, scatterTargetY);
    }

    public void updateChaseTarget(Rect player, TileMap map) {
        Tile currentPlayerTile = map.getTileAtPoint(player.getX(), player.getY());
        Tile currentTile = map.getTileAtPoint(boundsRect.getX(), boundsRect.getY());
        // Clyde effectively checks if he's inside an 8 tile radius with Pac-Man's location
        // based as the center of the circle. If he's in the circle, he chases his scatter mode tile.
        // If he's not in the circle, he chases Pac-Man directly.
        int radius = 64;
        if (currentPlayerTile != null && currentTile != null) {
            int distBetweenGhostAndPac = TileMap.euclideanDistanceBetweenTiles(currentTile, currentPlayerTile);
            if (distBetweenGhostAndPac <= radius) {
                chaseTargetX = scatterTargetX;
                chaseTargetY = scatterTargetY;
            } else {
                chaseTargetX = player.getX();
                chaseTargetY = player.getY();
            }
        }
    }

    @Override
    public void respawn() {
        super.respawn();
        currentDirection = SpriteDir.LEFT;
        inGhostHouse = true;
    }
}
