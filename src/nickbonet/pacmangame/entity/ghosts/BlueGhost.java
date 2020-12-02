package nickbonet.pacmangame.entity.ghosts;

import nickbonet.gameengine.Rect;
import nickbonet.gameengine.tile.Tile;
import nickbonet.gameengine.tile.TileMap;
import nickbonet.pacmangame.entity.Pacman;

public class BlueGhost extends Ghost {
    public BlueGhost(int scatterTargetX, int scatterTargetY) {
        super(192, 27, "blue", scatterTargetX, scatterTargetY);
    }

    // For Inky's chase target tile, we start off with a tile 2 away from Pac-Man's current direction and position.
    // Next, we check where Blinky is, and the distance between Blinky and the tile selected above. (in terms of X/Y)
    // Then, we create coordinates for a new tile by adding the resulting distances found above to the first offset tile's
    // X and Y coordinates. We then check if the position is a valid/existing tile, and if it is, set the ghost's target
    // to the final X and Y calculations. (experimental) If not, use the first offset tile's X and Y.
    public void updateChaseTarget(Pacman player, Rect redGhost, TileMap map) {
        if (map.getNearbyTile(player.getBounds().getX(), player.getBounds().getY(), player.getSpriteDirection(), 2) != null) {
            Tile offsetTile = map.getNearbyTile(player.getBounds().getX(), player.getBounds().getY(),
                    player.getSpriteDirection(), 2);
            Tile currentBlinkyTile = map.getTileAtPoint(redGhost.getX(), redGhost.getY());
            int blinkyDistFromOffsetX = offsetTile.getX() - currentBlinkyTile.getX();
            int blinkyDistFromOffsetY = offsetTile.getY() - currentBlinkyTile.getY();
            int finalTileX = offsetTile.getX() + blinkyDistFromOffsetX;
            int finalTileY = offsetTile.getY() + blinkyDistFromOffsetY;
            if (map.getTileAtPoint(finalTileX, finalTileY) != null) {
                chaseTargetX = finalTileX;
                chaseTargetY = finalTileY;
            } else {
                chaseTargetX = offsetTile.getX();
                chaseTargetY = offsetTile.getY();
            }
        }
    }
}
