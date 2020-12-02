package nickbonet.gameengine.sprite;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Animation - Basic animation handling via array list and timer.
 *
 * @author Nicholas Bonet
 */
public class Animation {
    private static final Logger logger = Logger.getLogger("GameEngine", null);
    private final List<BufferedImage> frames = new ArrayList<>();
    private final int delay;
    private Timer timer;
    private int currentFrame = 0;

    public Animation(int delay, String prefix, String directory) {
        this.delay = delay;
        loadFrames(prefix, directory);
        startAnimation();
    }

    public void loadFrames(String prefix, String folder) {
        File directory = new File(folder);
        File[] frameFiles = directory.listFiles(file -> file.getName().startsWith(prefix));
        if (frameFiles == null || frameFiles.length == 0) {
            logger.log(Level.INFO, "No images file found for {0}.", prefix);
        } else {
            for (File f : frameFiles) {
                this.addFrame(f);
            }
        }
    }

    public void startAnimation() {
        currentFrame = 0;
        timer = new Timer(delay, e -> {
            if (currentFrame < frames.size() - 1) {
                currentFrame += 1;
            } else {
                currentFrame = 0;
            }
        });
        timer.setRepeats(true);
        timer.start();
    }

    private void addFrame(File frame) {
        try {
            frames.add(ImageIO.read(frame));
            logger.log(Level.INFO, "Loaded image file {0}.", frame.getName());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading image for animation frame.");
        }
    }

    public void stopAnimation() {
        timer.stop();
    }

    public BufferedImage getCurrentFrame() {
        try {
            return frames.get(currentFrame);
        } catch (IndexOutOfBoundsException e) {
            logger.log(Level.SEVERE, "No image files loaded for the current animation!");
            return null;
        }
    }

    public BufferedImage getFirstFrame() {
        try {
            return frames.get(0);
        } catch (IndexOutOfBoundsException e) {
            logger.log(Level.SEVERE, "No image files loaded for the current animation!");
            return null;
        }
    }
}
