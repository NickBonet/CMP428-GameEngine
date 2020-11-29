package nickbonet.pacmangame.entity.ghosts;

public class BlueGhost extends Ghost {
    public BlueGhost(int x, int y, int delay, int scatterTargetX, int scatterTargetY) {
        super(x, y, "blue", delay, scatterTargetX, scatterTargetY);
    }

    @Override
    protected void updateChaseTarget() {

    }
}
