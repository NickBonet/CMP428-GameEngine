package nickbonet.pacmangame.entity.ghosts;

import nickbonet.gameengine.Rect;

public class RedGhost extends Ghost {
    public RedGhost(int scatterTargetX, int scatterTargetY) {
        super(104, 107, "red", scatterTargetX, scatterTargetY);
    }

    // Blinky just chases Pac-Man directly.
    public void updateChaseTarget(Rect playerBounds) {
        chaseTargetX = playerBounds.getX();
        chaseTargetY = playerBounds.getY();
    }
}
