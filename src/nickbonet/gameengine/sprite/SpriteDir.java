package nickbonet.gameengine.sprite;

public enum SpriteDir {
    LEFT("left"),
    RIGHT("right"),
    DOWN("down"),
    UP("up"),
    ALL("all");

    private final String direction;

    SpriteDir(String dir) {
        this.direction = dir;
    }

    @Override
    public String toString() {
        return direction;
    }
}
