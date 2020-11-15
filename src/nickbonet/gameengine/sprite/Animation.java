/**
 * Animation - Basic animation handling via array list and timer.
 * @author Nicholas Bonet
 *
 */
package nickbonet.gameengine.sprite;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Animation {
	private static final Logger logger = Logger.getLogger("GameEngine", null);
	private final List<BufferedImage> frames = new ArrayList<>();
	private final Timer timer;
	private final int delay;
	private int currentFrame = 0;

	public Animation(int delay, String prefix, String directory) {
		this.delay = delay;
		loadFrames(prefix, directory);
		timer = new Timer();
		startAnimation();
	}
	
	public void loadFrames(String prefix, String folder) {
		File directory = new File(folder);
		File[] frameFiles = directory.listFiles(file->file.getName().startsWith(prefix));
		if (frameFiles.length == 0) {
			logger.log(Level.INFO, "No images file found for {0}.", prefix);
		}
		for (File f: frameFiles) {
			this.addFrame(f);
		}
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
	    timer.cancel();
    }

    public void startAnimation() {
        AnimateTask task = new AnimateTask();
        timer.scheduleAtFixedRate(task, 0, this.delay);
    }
	
	public BufferedImage getCurrentFrame() {
		try {
			return frames.get(currentFrame);
		} catch (IndexOutOfBoundsException e) {
			logger.log(Level.SEVERE, "No image files loaded for the current animation!");
			return null;
		}
	}
	
	private class AnimateTask extends TimerTask {
		@Override
		public void run() {
			if (currentFrame < frames.size() - 1) { 
				currentFrame += 1;
			} else { 
				currentFrame = 0;
			}
		}
	}
}
