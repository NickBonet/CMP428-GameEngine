package nickbonet.testgame;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import nickbonet.gameengine.GameApplet;
import nickbonet.gameengine.Rect;

@SuppressWarnings("serial")
public class TestGame extends GameApplet {
	
	private transient Rect playerRect = new Rect(10, 10, 50, 60);
	private transient List<Rect> rectangleObjects = new ArrayList<>(); // keep all the rectangles in a neat list for iteration purposes

	@Override
	public void mainGameLogic() {
		checkPlayerMovement();
	}
	
	@Override
	public void paint(Graphics g) {
		playerRect.draw(g);
		for (int i = 0; i < rectangleObjects.size(); i++) {
			rectangleObjects.get(i).draw(g);
		}
	}

	@Override
	public void initObjects() {
		Rect testRect2 = new Rect(61, 71, 50, 60);
		Rect testRect3 = new Rect(150, 150, 70, 100);
		Rect testRect4 = new Rect(250, 250, 50, 51);
		rectangleObjects.add(testRect2);
		rectangleObjects.add(testRect3);
		rectangleObjects.add(testRect4);
	}
	
	private void checkPlayerMovement() {
		int dx = 0;
		int dy = 0;
		
		if (pressedKey[KeyEvent.VK_W]) {
			dx = 0;
			dy = -1;
		}
		
		if (pressedKey[KeyEvent.VK_S]) { 
			dx = 0;
			dy = 1;
		}
		
		if (pressedKey[KeyEvent.VK_A]) { 
			dx = -1;
			dy = 0;
		}
		
		if (pressedKey[KeyEvent.VK_D]) {
			dx = 1;
			dy = 0;
		}
		checkPlayerCollision(dx, dy);
	}
	
	private void checkPlayerCollision(int dx, int dy) {
		for (int i = 0; i < rectangleObjects.size(); i++) {
			if (playerRect.overlaps(rectangleObjects.get(i), dx, dy)) {
				dx = 0;
				dy = 0;
			}
		}
		playerRect.move(dx, dy);
	}
}
