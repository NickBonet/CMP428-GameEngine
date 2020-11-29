package nickbonet.pacmangame;

import nickbonet.gameengine.sprite.SpriteDir;

/**
 * Util - Utility class for misc. methods throughout the game code.
 */
public class Util {

    private Util() {

    }

    public static int directionPriority(SpriteDir direction) {
        switch (direction) {
            case UP:
                return 0;
            case LEFT:
                return 1;
            case DOWN:
                return 2;
            case RIGHT:
                return 3;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
    }
}
