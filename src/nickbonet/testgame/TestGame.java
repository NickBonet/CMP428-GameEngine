package nickbonet.testgame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import nickbonet.gameengine.GameApplet;
import nickbonet.gameengine.Rect;

public class TestGame extends GameApplet {
	
	Rect testRect = new Rect(10, 10, 50, 60);
	Rect testRect2 = new Rect(61, 71, 50, 60);
	
	public void mainGameLogic() {
		if(pressedKey[KeyEvent.VK_W] == true) testRect.move(0, -2);
		if(pressedKey[KeyEvent.VK_S] == true) testRect.move(0, 2);
		if(pressedKey[KeyEvent.VK_A] == true) testRect.move(-2, 0);
		if(pressedKey[KeyEvent.VK_D] == true) testRect.move(2, 0);
	}
	
	public void checkForCollision() {
		if (testRect.overlaps(testRect2)) testRect.setColliding(true);
		else testRect.setColliding(false);
		
	}
	
	public void paint(Graphics g) {
		testRect.draw(g);
		testRect2.draw(g);
	}
}
