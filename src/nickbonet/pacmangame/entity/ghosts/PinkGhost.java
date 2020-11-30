package nickbonet.pacmangame.entity.ghosts;

import nickbonet.gameengine.tile.Tile;
import nickbonet.gameengine.tile.TileMap;
import nickbonet.pacmangame.entity.Pacman;

public class PinkGhost extends Ghost {
    public PinkGhost(int x, int y, int delay, int scatterTargetX, int scatterTargetY) {
        super(x, y, "pink", delay, scatterTargetX, scatterTargetY);
    }

    public void updateChaseTarget(Pacman player, TileMap map) {
        if (map.getNearbyTile(player.getBounds().getX(), player.getBounds().getY(), player.getSpriteDirection(), 4) != null) {
            Tile offsetTile = map.getNearbyTile(player.getBounds().getX(), player.getBounds().getY(),
                    player.getSpriteDirection(), 4);
            chaseTargetX = offsetTile.getX() + (offsetTile.getWidth() / 2);
            chaseTargetY = offsetTile.getY() + (offsetTile.getHeight() / 2);
        }
    }
}
