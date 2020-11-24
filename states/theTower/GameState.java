package theTower;

import javax.swing.JFrame;

public class GameState extends JFrame {	   
	public GameState(boolean bPlayIntro) {	
	    System.out.println("GameState has been opened.");
    	Game towerGame = new Game(bPlayIntro);
    	Engine.createGame(towerGame);
	}
}
