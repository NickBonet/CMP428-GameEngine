package nickbonet.pacmangame.entity.ghosts;

import nickbonet.gameengine.Rect;
import nickbonet.gameengine.tile.Tile;
import nickbonet.gameengine.tile.TileMap;
import nickbonet.pacmangame.entity.Pacman;

public class BlueGhost extends Ghost {
    public BlueGhost(int x, int y, int delay, int scatterTargetX, int scatterTargetY) {
        super(x, y, "blue", delay, scatterTargetX, scatterTargetY);
    }

    public void updateChaseTarget(Pacman player, Rect redGhost, TileMap map) {
        if (map.getNearbyTile(player.getBounds().getX(), player.getBounds().getY(), player.getSpriteDirection(), 2) != null) {
            Tile offsetTile = map.getNearbyTile(player.getBounds().getX(), player.getBounds().getY(),
                    player.getSpriteDirection(), 2);
            Tile currentBlinkyTile = map.getTileAtPoint(redGhost.getX(), redGhost.getY());
            int blinkyDistFromOffsetX = offsetTile.getX() - currentBlinkyTile.getX();
            int blinkyDistFromOffsetY = offsetTile.getY() - currentBlinkyTile.getY();
            if (map.getTileAtPoint(offsetTile.getX() + blinkyDistFromOffsetX, offsetTile.getY() + blinkyDistFromOffsetY) != null) {
                chaseTargetX = offsetTile.getX() + blinkyDistFromOffsetX;
                chaseTargetY = offsetTile.getY() + blinkyDistFromOffsetY;
            }
        }
    }
}
