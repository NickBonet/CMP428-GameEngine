package nickbonet.testgame;

import java.io.File;

import nickbonet.gameengine.Rect;
import nickbonet.gameengine.sprite.Animation;
import nickbonet.gameengine.sprite.Sprite;

public class Pacman extends Sprite {
	public Pacman(int x, int y) {
		super(x, y);
		this.boundsRect = new Rect(this.x, this.y, 45, 41);
	}
	
	protected void initAnimations() {
		File restFrame = new File("C:\\Users\\nbonet\\Desktop\\pacman sprites\\pac_closed.png");
		Animation leftAnim = new Animation(65);
		leftAnim.addFrame(restFrame);
		leftAnim.addFrame(new File("C:\\Users\\nbonet\\Desktop\\pacman sprites\\pac_left_1.png"));
		leftAnim.addFrame(new File("C:\\Users\\nbonet\\Desktop\\pacman sprites\\pac_left_2.png"));
		leftAnim.addFrame(new File("C:\\Users\\nbonet\\Desktop\\pacman sprites\\pac_left_1.png"));
		animDict.put("left", leftAnim);
		
		Animation upAnim = new Animation(65);
		upAnim.addFrame(restFrame);
		upAnim.addFrame(new File("C:\\Users\\nbonet\\Desktop\\pacman sprites\\pac_up_1.png"));
		upAnim.addFrame(new File("C:\\Users\\nbonet\\Desktop\\pacman sprites\\pac_up_2.png"));
		upAnim.addFrame(new File("C:\\Users\\nbonet\\Desktop\\pacman sprites\\pac_up_1.png"));
		animDict.put("up", upAnim);
		
		Animation downAnim = new Animation(65);
		downAnim.addFrame(restFrame);
		downAnim.addFrame(new File("C:\\Users\\nbonet\\Desktop\\pacman sprites\\pac_down_1.png"));
		downAnim.addFrame(new File("C:\\Users\\nbonet\\Desktop\\pacman sprites\\pac_down_2.png"));
		downAnim.addFrame(new File("C:\\Users\\nbonet\\Desktop\\pacman sprites\\pac_down_1.png"));
		animDict.put("down", downAnim);
		
		Animation rightAnim = new Animation(65);
		rightAnim.addFrame(restFrame);
		rightAnim.addFrame(new File("C:\\Users\\nbonet\\Desktop\\pacman sprites\\pac_right_1.png"));
		rightAnim.addFrame(new File("C:\\Users\\nbonet\\Desktop\\pacman sprites\\pac_right_2.png"));
		rightAnim.addFrame(new File("C:\\Users\\nbonet\\Desktop\\pacman sprites\\pac_right_1.png"));
		animDict.put("right", rightAnim);
	}
}
