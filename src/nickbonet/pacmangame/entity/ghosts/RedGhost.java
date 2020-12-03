package nickbonet.pacmangame.entity.ghosts;

import nickbonet.gameengine.Rect;
import nickbonet.gameengine.sprite.SpriteDir;

public class RedGhost extends Ghost {
    public RedGhost(int scatterTargetX, int scatterTargetY) {
        super(104, 107, "red", scatterTargetX, scatterTargetY);
    }

    // Blinky just chases Pac-Man directly.
    public void updateChaseTarget(Rect playerBounds) {
        chaseTargetX = playerBounds.getX();
        chaseTargetY = playerBounds.getY();
    }

    public void respawn() {
        super.respawn();
        currentDirection = SpriteDir.LEFT;
    }
}
