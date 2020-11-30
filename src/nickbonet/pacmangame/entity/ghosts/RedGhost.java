package nickbonet.pacmangame.entity.ghosts;

import nickbonet.gameengine.Rect;

public class RedGhost extends Ghost {
    public RedGhost(int x, int y, int delay, int scatterTargetX, int scatterTargetY) {
        super(x, y, "red", delay, scatterTargetX, scatterTargetY);
    }

    public void updateChaseTarget(Rect playerBounds) {
        chaseTargetX = playerBounds.getX();
        chaseTargetY = playerBounds.getY();
    }
}
