package nickbonet.pacmangame.entity.ghosts;

import nickbonet.gameengine.tile.Tile;
import nickbonet.gameengine.tile.TileMap;
import nickbonet.pacmangame.entity.Pacman;

public class OrangeGhost extends Ghost {
    public OrangeGhost(int x, int y, int delay, int scatterTargetX, int scatterTargetY) {
        super(x, y, "orange", delay, scatterTargetX, scatterTargetY);
    }

    public void updateChaseTarget(Pacman player, TileMap map) {
        Tile currentPlayerTile = map.getTileAtPoint(player.getBounds().getX(), player.getBounds().getY());
        Tile currentTile = map.getTileAtPoint(boundsRect.getX(), boundsRect.getY());
        // Clyde effectively checks if he's inside an 8 tile radius with Pac-Man's location
        // based as the center of the circle. If he's in the circle, he chases his scatter mode tile.
        // If he's not in the circle, he chases Pac-Man directly.
        int radius = 8 * currentPlayerTile.getWidth();
        int distBetweenGhostAndPac = TileMap.euclideanDistanceBetweenTiles(currentTile, currentPlayerTile);
        if (distBetweenGhostAndPac <= radius) {
            chaseTargetX = scatterTargetX;
            chaseTargetY = scatterTargetY;
        } else {
            chaseTargetX = player.getBounds().getX();
            chaseTargetY = player.getBounds().getY();
        }
    }
}
