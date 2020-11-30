package nickbonet.pacmangame.entity.ghosts;

import nickbonet.gameengine.tile.Tile;
import nickbonet.gameengine.tile.TileMap;
import nickbonet.pacmangame.entity.Pacman;

public class PinkGhost extends Ghost {
    public PinkGhost(int x, int y, int delay, int scatterTargetX, int scatterTargetY) {
        super(x, y, "pink", delay, scatterTargetX, scatterTargetY);
    }

    // Pinky's chase target tile is always 4 tiles in front of Pac-Man. In the original game, there was a bug
    // where while Pac-Man was facing up, Pinky's target tile would be offset by 4 up and 4 away from Pac-Man's current tile.
    // Not replicating that bug in this game.
    public void updateChaseTarget(Pacman player, TileMap map) {
        if (map.getNearbyTile(player.getBounds().getX(), player.getBounds().getY(), player.getSpriteDirection(), 4) != null) {
            Tile offsetTile = map.getNearbyTile(player.getBounds().getX(), player.getBounds().getY(),
                    player.getSpriteDirection(), 4);
            chaseTargetX = offsetTile.getX();
            chaseTargetY = offsetTile.getY();
        }
    }
}
