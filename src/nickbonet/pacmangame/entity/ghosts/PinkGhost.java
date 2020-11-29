package nickbonet.pacmangame.entity.ghosts;

public class PinkGhost extends Ghost {
    public PinkGhost(int x, int y, int delay, int scatterTargetX, int scatterTargetY) {
        super(x, y, "pink", delay, scatterTargetX, scatterTargetY);
    }

    @Override
    protected void updateChaseTarget() {

    }
}
