package nickbonet.gameengine.sprite;

/**
 * SpriteDir - represents the possible movement directions of a sprite.
 */
public enum SpriteDir {
    LEFT("left"), RIGHT("right"), DOWN("down"), UP("up"), ALL("all");
    private final String direction;
    private SpriteDir opposite;

    static {
        LEFT.opposite = RIGHT;
        RIGHT.opposite = LEFT;
        UP.opposite = DOWN;
        DOWN.opposite = UP;
    }

    SpriteDir(String dir) {
        this.direction = dir;
    }

    @Override
    public String toString() {
        return direction;
    }

    public SpriteDir getOpposite() {
        return opposite;
    }
}
