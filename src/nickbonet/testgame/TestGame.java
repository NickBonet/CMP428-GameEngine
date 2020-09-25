package nickbonet.testgame;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import nickbonet.gameengine.GameApplet;
import nickbonet.gameengine.Rectangle;

public class TestGame extends GameApplet {
	
	Rectangle testRect = new Rectangle(10, 10, 50, 60);
	
	public void gameLoopLogic() {
		if(pressedKey[KeyEvent.VK_W] == true) { testRect.move(0, -2); };
		if(pressedKey[KeyEvent.VK_S] == true) { testRect.move(0, 2); };
		if(pressedKey[KeyEvent.VK_A] == true) { testRect.move(-2, 0); };
		if(pressedKey[KeyEvent.VK_D] == true) { testRect.move(2, 0); };
	}
	
	public void paint(Graphics g) {
		testRect.draw(g);
	}
}
