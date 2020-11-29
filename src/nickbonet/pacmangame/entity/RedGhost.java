package nickbonet.pacmangame.entity;

public class RedGhost extends Ghost {
    public RedGhost(int x, int y, int delay, int scatterTargetX, int scatterTargetY) {
        super(x, y, "red", delay, scatterTargetX, scatterTargetY);
    }

    @Override
    protected void updateChaseTarget() {

    }
}
