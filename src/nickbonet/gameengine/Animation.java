/**
 * Animation - Basic animation handling via array list and timer.
 * @author Nicholas Bonet
 *
 */
package nickbonet.gameengine;

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
	private Logger logger = Logger.getLogger("GameEngine", null);
	private List<BufferedImage> frames = new ArrayList<>();
	private Timer timer;
	private int delay = 500; 
	private int currentFrame = 0;
	
	public BufferedImage getCurrentFrame() {
		return frames.get(currentFrame);
	}

	public Animation(int delay) {
		this.delay = delay;
		timer = new Timer();
		AnimateTask task = new AnimateTask();
		timer.scheduleAtFixedRate(task, 0, this.delay);
	}
	
	public void addFrame(File frame) {
		try {
			frames.add(ImageIO.read(frame));
		} catch (IOException e) {
			logger.log(Level.WARNING, "Error loading image for animation frame.");
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
