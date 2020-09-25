package nickbonet.testgame;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import nickbonet.gameengine.GameApplet;
import nickbonet.gameengine.Rect;

public class TestGame extends GameApplet {
	
	Rect playerRect = new Rect(10, 10, 50, 60);
	List<Rect> rectangleObjects = new ArrayList<Rect>(); // keep all the rectangles in a neat list for iteration purposes

	public void mainGameLogic() {
		checkPlayerMovement();
	}
	
	public void paint(Graphics g) {
		playerRect.draw(g);
		for (int i = 0; i < rectangleObjects.size(); i++) {
			rectangleObjects.get(i).draw(g);
		}
	}

	public void initObjects() {
		Rect testRect2 = new Rect(61, 71, 50, 60);
		Rect testRect3 = new Rect(150, 150, 70, 100);
		Rect testRect4 = new Rect(250, 250, 50, 51);
		rectangleObjects.add(testRect2);
		rectangleObjects.add(testRect3);
		rectangleObjects.add(testRect4);
	}
	
	private void checkPlayerMovement() {
		if (pressedKey[KeyEvent.VK_W] == true) {
			playerRect.move(0, -1);
			checkPlayerCollision();
		}
		
		if (pressedKey[KeyEvent.VK_S] == true) { 
			playerRect.move(0, 1);
			checkPlayerCollision();
		}
		
		if (pressedKey[KeyEvent.VK_A] == true) { 
			playerRect.move(-1, 0);
			checkPlayerCollision();
		}
		
		if (pressedKey[KeyEvent.VK_D] == true) {
			playerRect.move(1, 0);
			checkPlayerCollision();
		}
	}
	
	private void checkPlayerCollision() {
		for (int i = 0; i < rectangleObjects.size(); i++) {
			if (playerRect.overlaps(rectangleObjects.get(i))) {
				playerRect.undoMove();
			}
		}
	}
}
