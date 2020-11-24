/*
 159261 - S2 2020 - Assignment 3
Fletch van Ameringen - 19023939
Sam Fleming - 19033395
Alex Fawcett - 11145744
 */


package theTower;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

import theTower.Engine.AudioClip;

public class Game extends Engine {
	private int window_width, window_height;
	public static int min_map_width, max_map_width, min_map_height, max_map_height, tileSize = 16;
	private int thickness, waveNumber = 0, playerScore = 0, towerHealth, radius = 90; 
	public boolean gameOver, gameWon; // Cannot be static!
	private boolean playerIsAlive; // playerIsAlive is false when player is against a boundary or is dead
	private int currentCastle = 0, castleWidth = 300, castleX, castleY;
	private boolean firstWave = true, runAnimation = false, isPaused = false;
	private long nextWaveTime = 5, timeBetweenEachCollectableGenerated = 15; 
	private double runningSpeed = 0.001; // Time (ms) between character's animations
	private double hitSpeed = 0.1; // Time (ms) between character's weapon swing animations
	private boolean gameStarting, beginGameAnimation = true, endGameAnimation = true, gameOverText = false;
	private double healthWidthX, showWaveTextTimer = 0;
	private int modX, scoreStartX;
	private double t, difference, collectableTimer, runningCounter, swingingCounter;
	private double es = 0, mc = 0, introOpening = 0, outroOpening = 0, rechargeTimer = 0;
	private boolean delayWeapon = false, send = false, flag = true;
	public boolean bPlayIntro = false, loopStarted = false;
	public int introFrame = 1;
	public double introTime = 0.0;
	
	// Details for health arc
	private int startAngle;
	private int arcAngle;

	// Game object arrays
	ArrayList<Image> castlePics = new ArrayList<Image>();
	ArrayList<Image> wallPics = new ArrayList<Image>();
	ArrayList<Collectable> collectables = new ArrayList<>();
		
	private int actualNumberOfCollectables = 8, numberOfPossibleCollectables = 1; // (Number of weapons)
	
	// Unlocking collectables
	private boolean[] thresholdsReached = new boolean[7];
	private long[] thresholds = new long[7];
	private int xTopCurrent, xBottomCurrent; // variables for opening scene
	
	static AudioClip introMusic, hitEnemy, pickupWeapon, heroDamage, gameOverSound, spell, healthInc, backgroundMusic, towerDamage, winMusic, newWave, ogreNoise, shockWave, dBugMusic;  
	
	Image introFrame1, introFrame2, introFrame3, introFrame4;
	Image backing, characterProfileImage, towerProfileImage, healthKitImage, goldImage, mysteryBox, introImage, gameTextImage, overTextImage, youTextImage, wonTextImage, gamePausedText;
	Image left, mid, right, top, bottom, rightEdge, leftEdge; 
	Image smallShockwavePic, largeShockwavePic;	
	Player character;
	// Colours
	Color orangey = Color.decode("#FF9933"), green = Color.decode("#00FF00"), darkgreen = Color.decode("#006600"), redy = Color.decode("#FF0000"), brown = Color.decode("#4A3728");
    Color gridGrey = Color.decode("#444444"), borderGrey = Color.decode("#3d3d3d"), darkGrey = Color.decode("333333"), healthBackingGrey = Color.decode("#aaaaaa");
    
    // Magic staff opaque field
    Color sBlue1 = Color.decode("#87CEEB");
    Color skyBlue = new Color(sBlue1.getRed(), sBlue1.getGreen(), sBlue1.getBlue(), 100);
    
    // Dark overlay for pause 
    Color dark1 = Color.decode("#000000");
    Color darkOverlay = new Color(dark1.getRed(), dark1.getGreen(), dark1.getBlue(), 160);
    
	//Dark overlay for wave banner
	Color darkOverlay2 = new Color(dark1.getRed(), dark1.getGreen(), dark1.getBlue(), 90);

	public Game(boolean bPlayIntro) {
		super();
		this.bPlayIntro = bPlayIntro;
	    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

	    int largestWidth = 1920;
	    int largestHeight = 1080;

		this.window_width = (screen.width < largestWidth) ? screen.width : largestWidth;
		this.window_height = (screen.height < largestHeight) ? screen.height : largestHeight;
	    this.setWindowSize(window_width, window_height);

		loadImagesIntoGame();
		loadAudio();
		
		gameOver = false;
		gameWon = false;
		playerIsAlive = true;
		castleX = (window_width/2)-(castleWidth/2);
		castleY = 25;
		
		// Setting collectable thresholds
		thresholds[0] = 100;
		thresholds[1] = 350;
		thresholds[2] = 800;
		thresholds[3] = 1000;
		thresholds[4] = 1500;
		thresholds[5] = 2800;
		thresholds[6] = 3000;		
		for (int i = 0; i < 7; i++) {
			thresholdsReached[i] = false;
		} 

		gameStarting = true;
		if(!bPlayIntro) {
			startAudioLoop(backgroundMusic);
			loopStarted = true;
		}
		
		
		// Spawn Player
		character = new Player(window_width/2, window_height/2);
		
		// Give player basic weapon
		collectables.add(new RustyKnife(mysteryBox, window_width/2, window_height/2));
		checkIfPlayerGetCollectable();
	}
	
	public void loadImagesIntoGame() {
		// Load Game Images/resources
		try {
			
			//Intro Frames
			introFrame1 = loadImage("src/images/frame1.png");
			introFrame2 = loadImage("src/images/frame2.png");
			introFrame3 = loadImage("src/images/frame3.png");
			introFrame4 = loadImage("src/images/frame4.png");
			
			// Load tower
			towerHealth = 1000;
			castlePics.add(loadImage("src/images/castlePrincess1.png"));
			castlePics.add(loadImage("src/images/towerNoDoor2.png"));
			
			// Load wall images
			wallPics.add(loadImage("src/frames/walls/wall_left.png"));
			wallPics.add(loadImage("src/frames/walls/wall_mid.png"));
			wallPics.add(loadImage("src/frames/walls/wall_right.png"));
			wallPics.add(loadImage("src/frames/walls/walledgetop.png"));
			wallPics.add(loadImage("src/frames/walls/walledgebottom.png"));
			wallPics.add(loadImage("src/frames/walls/walledgeright.png"));
			wallPics.add(loadImage("src/frames/walls/walledgeleft.png"));
			
			left = wallPics.get(0); mid = wallPics.get(1); right = wallPics.get(2); top = wallPics.get(3); bottom = wallPics.get(4); rightEdge = wallPics.get(5);  leftEdge = wallPics.get(6); 
			
			backing = loadImage("src/images/background1.jpg");
			introImage = loadImage("src/images/introBorder.png");
			
			// Game over text
			overTextImage = loadImage("src/images/over.png");
			gameTextImage = loadImage("src/images/game.png");
			youTextImage = loadImage("src/images/youText.png");
			wonTextImage = loadImage("src/images/wonText.png");
			
			// Game Paused text
			gamePausedText = loadImage("src/images/gamePausedText.png");
			
			// load profile images
			characterProfileImage = loadImage("src/images/profile2.png");
			towerProfileImage  = loadImage("src/images/profile3.png");
			
			// Load collectable images
			mysteryBox = loadImage("src/images/mysteryBox.png");
			healthKitImage = loadImage("src/images/healthKit.png");
			
			
			smallShockwavePic = loadImage("src/images/shockwavesmall.png");	
			largeShockwavePic = loadImage("src/images/shockwavebig.png");	

			// Load enemies
		} catch (Exception a) {
			// Could not load an image
			System.out.println("Could not load all images.");
		}
	}
	
	public void loadAudio() {	//load audio files
		try{
			introMusic = loadAudio("src/audio/intro.wav"); //https://opengameart.org/content/intro-cue
			dBugMusic = loadAudio("src/audio/idk.wav"); //https://opengameart.org/content/cc0-scraps
			backgroundMusic = loadAudio("src/audio/mainBackground.wav"); //https://opengameart.org/content/cc0-scraps
			hitEnemy = loadAudio("src/audio/attack.wav"); //https://opengameart.org/content/512-sound-effects-8-bit-style
			pickupWeapon = loadAudio("src/audio/powerup.wav"); //https://opengameart.org/content/512-sound-effects-8-bit-style
			healthInc = loadAudio("src/audio/healthInc.wav"); //made on https://www.leshylabs.com/apps/sfMaker/
			heroDamage = loadAudio("src/audio/heroDamage2.wav"); //https://opengameart.org/content/zombie-skeleton-monster-voice-effects (edited on https://twistedwave.com/online/edit)
			gameOverSound = loadAudio("src/audio/GameOver.wav"); //https://opengameart.org/content/game-over-soundold-school
			spell = loadAudio("src/audio/spell.wav"); //made on https://www.leshylabs.com/apps/sfMaker/
			towerDamage = loadAudio("src/audio/towerDamage.wav"); //https://opengameart.org/content/bell-dingschimes (edited on https://twistedwave.com/online/edit)
			winMusic = loadAudio("src/audio/achievement.wav"); // https://opengameart.org/content/8-bit-sound-effects-library
			newWave = loadAudio("src/audio/newWave.wav"); //https://opengameart.org/content/short-alarm
			ogreNoise = loadAudio("src/audio/ogreNoise.wav"); //https://www.youtube.com/watch?v=TxNeiMR5RWM&ab_channel=BrandNameAudio	
			shockWave = loadAudio("src/audio/shockwave.wav"); //made on https://www.leshylabs.com/apps/sfMaker/
		} catch (Exception a) {
			// Could not load an audio file
			System.out.println("Could not load all audio files.");
		}
	}
	
	public void drawIntro() {
		if(!loopStarted) {
			startAudioLoop(introMusic);
			loopStarted = true;
		}
		int img_width = introFrame1.getWidth(null);
		int img_height = introFrame1.getHeight(null);
		switch(introFrame) {
			case 1:
				drawImage(introFrame1, (window_width/2)-(img_width/2), (window_height/2)-(img_height/2), img_width, img_height);
				break;
			case 2:
				drawImage(introFrame2, (window_width/2)-(img_width/2), (window_height/2)-(img_height/2), img_width, img_height);
				break;
			case 3:
				drawImage(introFrame3, (window_width/2)-(img_width/2), (window_height/2)-(img_height/2), img_width, img_height);
				break;
			case 4:
				drawImage(introFrame4, (window_width/2)-(img_width/2), (window_height/2)-(img_height/2), img_width, img_height);
				break;
		}
	}
	
	private void generateNextWave() {
		showWaveTextTimer = 0.01;
		switch(waveNumber) {
			case 0:
				// Wave 1: 3 Kobolds
				for (int i = 0; i < 3; i++) {
					spawner.Spawn(0);
				}
				playAudio(newWave);
				break;
			case 1:
				// Wave 2: 5 Kobolds
				for (int i = 0; i < 5; i++) {
					spawner.Spawn(0);
				}
				playAudio(newWave);
				break;
			case 2:
				// Wave 3: 2 Kobolds, 1 Orc
				for (int i = 0; i < 2; i++) {
					spawner.Spawn(0);
				}
				spawner.Spawn(1);
				playAudio(newWave);
				break;
			case 3:
				// Wave 4: 2 Kobolds, 3 Orcs
				for (int i = 0; i < 2; i++) {
					spawner.Spawn(0);
				}
				for (int i = 0; i < 3; i++) {
					spawner.Spawn(1);
				}
				playAudio(newWave);
				break;
			case 4:
				//Wave 5: 3 Kobolds, 1 Orc, 1 Imp
				for (int i = 0; i < 3; i++) {
					spawner.Spawn(0);
				}
				spawner.Spawn(1);
				spawner.Spawn(2);
				playAudio(newWave);
				break;
			case 5:
				//Wave 6: 3 Kobolds, 3 Orcs, 2 Imps
				for (int i = 0; i < 3; i++) {
					spawner.Spawn(0);
				}
				for (int i = 0; i < 3; i++) {
					spawner.Spawn(1);
				}
				for (int i = 0; i < 2; i++) {
					spawner.Spawn(2);
				}
				playAudio(newWave);
				break;
			case 6:
				//Wave 7: 4 Imps
				for (int i = 0; i < 4; i++) {
					spawner.Spawn(2);
				}
				playAudio(newWave);
				break;
			case 7:
				//Wave 8: 4 Orcs, 4 Imps
				for (int i = 0; i < 4; i++) {
					spawner.Spawn(1);
				}
				for (int i = 0; i < 4; i++) {
					spawner.Spawn(2);
				}
				playAudio(newWave);
				break;
			case 8:
				//Wave 9: 3 Kobolds, 2 Orcs, 4 Imps, 1 Hood (Speedy fade in out?)
				for (int i = 0; i < 3; i++) {
					spawner.Spawn(0);
				}
				for (int i = 0; i < 2; i++) {
					spawner.Spawn(1);
				}
				for (int i = 0; i < 4; i++) {
					spawner.Spawn(2);
				}
				spawner.Spawn(3);
				playAudio(newWave);
				break;
			case 9:
				//Wave 10: 2 Orcs, 6 Imps, 2 Hoods
				for (int i = 0; i < 2; i++) {
					spawner.Spawn(1);
				}
				for (int i = 0; i < 6; i++) {
					spawner.Spawn(2);
				}
				for (int i = 0; i < 2; i++) {
					spawner.Spawn(3);
				}
				playAudio(newWave);
				break;
			case 10:
				//Wave 11: 6 Imps, 4 Hoods
				for (int i = 0; i < 6; i++) {
					spawner.Spawn(2);
				}
				for (int i = 0; i < 4; i++) {
					spawner.Spawn(3);
				}
				playAudio(newWave);
				break;
			case 11:
				//Wave 12: 6 Imps, 2 Hoods, 1 Ogre (Huge dude. Shockwave attacks?)
				for (int i = 0; i < 6; i++) {
					spawner.Spawn(2);
				}
				for (int i = 0; i < 2; i++) {
					spawner.Spawn(3);
				}
				spawner.Spawn(4);
				playAudio(newWave);
				break;
			case 12:
				//Wave 13: 2 Orcs, 4 Imps, 4 Hoods, 2 Ogres
				for (int i = 0; i < 2; i++) {
					spawner.Spawn(1);
				}
				for (int i = 0; i < 4; i++) {
					spawner.Spawn(2);
				}
				for (int i = 0; i < 4; i++) {
					spawner.Spawn(3);
				}
				for (int i = 0; i < 2; i++) {
					spawner.Spawn(4);
				}
				playAudio(newWave);
				break;
			case 13:
				//Wave 14: 4 Kobolds, 4 Orcs, 4 Imps, 4 Hoods, 4 Ogres
				for (int i = 0; i < 4; i++) {
					spawner.Spawn(0);
				}
				for (int i = 0; i < 4; i++) {
					spawner.Spawn(1);
				}
				for (int i = 0; i < 4; i++) {
					spawner.Spawn(2); 
				}
				for (int i = 0; i < 4; i++) {
					spawner.Spawn(3);
				}
				for (int i = 0; i < 4; i++) {
					spawner.Spawn(4);
				}
				playAudio(newWave);
				break;
			case 14:
				//Wave 15: BOSS - maybe huge kobold ruler? With a swarm of little ones
				for (int i = 0; i < 10; i++) {
					spawner.Spawn(0);
				}
				spawner.Spawn(5);
				playAudio(newWave);
				
				// Stop Current game audio and start dbug music
				stopAudioLoop(backgroundMusic);

				startAudioLoop(dBugMusic);
				break;				
		}
		if (waveNumber >= 16) {
			return;
		}
		waveNumber++;
	}
	
	static public boolean closeEnough(int y1, int y2, int tolerance) {
		if (((y1 - y2) < tolerance) && ((y1 - y2) > -tolerance)) {
			return true;
		}
		return false;
	}
	
	private void scoreThresholds() {
		if (playerScore < thresholds[0]) {
			return;
		}
		
		if ((playerScore >= thresholds[0]) && (!thresholdsReached[0])) {
			thresholdsReached[0] = true;
			increaseNumberOfWeaponsAvailable();
			return;
		} else if ((playerScore >= thresholds[1]) && (!thresholdsReached[1])) {
			thresholdsReached[1] = true;
			increaseNumberOfWeaponsAvailable();
			return;
		} else if ((playerScore >= thresholds[2]) && (!thresholdsReached[2])) {
			thresholdsReached[2] = true;
			increaseNumberOfWeaponsAvailable();
			return;
		} else if ((playerScore >= thresholds[3]) && (!thresholdsReached[3])) {
			thresholdsReached[3] = true;
			increaseNumberOfWeaponsAvailable();
			return;
		} else if ((playerScore >= thresholds[4]) && (!thresholdsReached[4])) {
			thresholdsReached[4] = true;
			increaseNumberOfWeaponsAvailable();
			return;
		} else if ((playerScore >= thresholds[5]) && (!thresholdsReached[5])) {
			thresholdsReached[5] = true;
			increaseNumberOfWeaponsAvailable();
			return;
		} else if ((playerScore >= thresholds[6]) && (!thresholdsReached[6])) {
			thresholdsReached[6] = true;
			increaseNumberOfWeaponsAvailable();
			return;
		}
	}
	
	private void increaseNumberOfWeaponsAvailable() {
		if (numberOfPossibleCollectables+1 > actualNumberOfCollectables) {
			return;
		}
		numberOfPossibleCollectables++;
	}
	
	private boolean getPythag(double Xp, double Yp, double Xc, double Yc) {
		double d, temp;
		temp = ((Xp - Xc)*(Xp - Xc)) + (Yp - Yc)*(Yp - Yc);
		d = Math.sqrt(temp);
		if (d <= radius*2) {
			// enemy is inside circle
			return true;
		} else if (d > radius*2) {
			// enemy is outside the circle
			return false;
		}
		
		
		return false;
	}
	
	private void checkIfPlayerHasHitEnemy() {
		Weapon w = character.getPlayerWeapon();
		int weaponX = w.getLocationX(), weaponY = w.getLocationY(), damage = w.getDamage();
		
		if (w instanceof MagicStaff) {
			boolean res;
			for (Enemy C : cEnemies) {
				res = getPythag(C.getLocationX(), C.getLocationY(), character.getLocationX()+8, character.getLocationY()+8);
				if (res) {
					playAudio(spell);
					C.reduceHealth(damage);
					playerScore += 25;
				}
			}
			return;
		}
		
		for (Enemy C : cEnemies) {
			if (closeEnough(weaponX, (int) C.getLocationX(), 50)) {
				if (closeEnough(weaponY, (int) C.getLocationY(), 50)) {
					// Hurt enemy
					playAudio(hitEnemy);
					C.reduceHealth(damage);
					playerScore += 25;
				}
			}
		}
	}
	
	private Color getHealthColor(double health, int upperThreshold) {
		if (health >= upperThreshold - (0.25*upperThreshold)) {
			return green;
		}
		else if ((health < upperThreshold - (0.25*upperThreshold)) && (health > upperThreshold - (0.6*upperThreshold))) {
			return orangey; 
		} else {
			return redy;
		}	
	}
	
	private Color getHealthColorEnemies(double health, int upperThreshold) {
		if (health >= upperThreshold - (0.25*upperThreshold)) {
			return darkgreen;
		}
		else if ((health < upperThreshold - (0.25*upperThreshold)) && (health > upperThreshold - (0.6*upperThreshold))) {
			return orangey; 
		} else {
			return redy;
		}	
	}
	
	private void newTargetForEnemy() {
		for (Enemy E : cEnemies) {
			boolean tx = Game.closeEnough((int)E.getLocationX(), character.getLocationX(), 300);
			boolean ty = Game.closeEnough((int)E.getLocationY(), character.getLocationY(), 300);
			if ((tx) && (ty)) {
				// change target to player
				E.newTarget(character.getLocationX(), character.getLocationY());
				E.chasingPlayer = true;
			}
			else if (E.chasingPlayer) {
				// Was chasing the player
				E.newTarget(E.towerposx, E.towerposy);
				E.chasingPlayer = false;
			}
		}
	}
	
	private void checkIfPlayerGetCollectable() {
		int x = character.getLocationX(), y = character.getLocationY();
		int id = 0;
		boolean delete = false;
		for (Collectable c : collectables) {
			if ((closeEnough(c.getLocationX(), x, 25)) && (closeEnough(c.getLocationY(), y, 25))) {
				delete = true;
				break;
			}
			id++;
		}
		if (delete) {
			if (collectables.get(id) instanceof HealthKit) {
				HealthKit t = (HealthKit) collectables.get(id);
				// apply health to the character
				character.increaseHealth(t.getPayload());
				// Remove collectable
				collectables.remove(id);
			} 
			else if (collectables.get(id) instanceof Weapon) {
				Weapon w = (Weapon) collectables.get(id);
				w.setPickedUp(true);
				// Collectable is a weapon
				if (!character.hasWeapon) {
					character.sethasWeapon(true);
				}
				
				character.setCollectable(w);
				collectables.remove(id);
				playAudio(pickupWeapon);
			}			
		}
	}
	
	private void removeDeadEnemies() {
		ArrayList<Enemy> delete = new ArrayList<>();;
		for (Enemy C : cEnemies) {
			if (C.getHealth() <= 0) {
				// Delete the enemy
				delete.add(C);	
			}
		}
		
		for (Enemy C : delete) {
			cEnemies.remove(C);
		}
	}

	private void spawnRandomCollectable() {
		// Create a new Collectable
		int minX = Game.min_map_width, maxX = Game.max_map_width-32, minY = Game.min_map_height+50, maxY = Game.max_map_height-32;
		int tempX, tempY;
		
		tempX = (int) ((Math.random() * ((maxX - minX) + 1)) + minX);
		tempY = (int) ((Math.random() * ((maxY - minY) + 1)) + minY);
		// Make sure the collectable does not have the same location as another collectable 
		for (Collectable a : collectables) {
			while ((tempX == a.getLocationX()) && (tempY == a.getLocationY())) {
				tempX = (int) ((Math.random() * ((maxX - minX) + 1)) + minX);
				tempY = (int) ((Math.random() * ((maxY - minY) + 1)) + minY);
			}
		}
		
		Weapon w = null;
		if (character.doesHaveWeapon()) {
			w = character.getPlayerWeapon();
		}
		boolean repeat = true;
		while (repeat) {
			int result = rand(numberOfPossibleCollectables);
			switch(result) {
				case 0:
					// Health kit
					collectables.add(new HealthKit(healthKitImage, tempX, tempY));
					return;
					
				case 1:
					if (!(w instanceof Katana)) {
						collectables.add(new Katana(mysteryBox, tempX, tempY));
						return;
					}
				case 2:
					if (!(w instanceof Hammer)) {
						collectables.add(new Hammer(mysteryBox, tempX, tempY));
						return;
					}
				case 3:
					if (!(w instanceof ProperSword)) {
						collectables.add(new ProperSword(mysteryBox, tempX, tempY));
						return;
					}
				case 4:
					if (!(w instanceof Mace)) {
						collectables.add(new Mace(mysteryBox, tempX, tempY));
						return;
					}
				case 5:
					if (!(w instanceof Spear)) {
						collectables.add(new Spear(mysteryBox, tempX, tempY));
						return;
					}
				case 6:
					if (!(w instanceof GoldSword)) {
						collectables.add(new GoldSword(mysteryBox, tempX, tempY));
						return;
					}
				case 7:
					if (!(w instanceof MagicStaff)) {
						collectables.add(new MagicStaff(mysteryBox, tempX, tempY));
						return;
					}
			}
		}
	}
	
	@Override
	public void init() {
		// Start main game loop
		this.gameLoop(60);
		//initialize enemy spawner
		spawner.init(this, window_width, window_height);		
	}	
	
	
	private void checkIfPlayerIsDead() {
		if (character.getHealth() <= 0) {
			playerIsAlive = false;
			setGameOver(true, false);
			return;
		} 	
	}
	
	void checkIfPlayerisInside() {
		if ((character.getLocationX() > castleX+15) && (character.getLocationX() < castleX+castleWidth-15)) {
			if ((character.getLocationY() > castleY+150) && (character.getLocationY() <= castleY+165+95)) {
				System.out.println("Congratulations - Player has won!");
				setGameOver(true, true);
				return;
			}
		}
	}
	private void checkIfPlayerHasWon() {
		if (playerIsAlive) {
			if (cEnemies.size() == 0) {
				if (flag) {
					currentCastle++;
					flag = false;
				} else {
					checkIfPlayerisInside();
				}
				
			}
		} 	
	}
	
	@Override
	public void update(double dt) { // Run this every tick
		
		if(!mFrame.isVisible() && !gameOver)
			setGameOver(true, false);

		if ((isPaused) && (!gameOver)) { // Game is paused
			return;
		}
		
		// Update outro movement
		if (gameOver) {
			outroOpening += dt;
			if ((outroOpening > 0.01) && (endGameAnimation)) {
				boolean go1 = false, go2 = false;
				if (xTopCurrent < window_height/2-32) {
					xTopCurrent += 4;
					go1 = true;
				}
				if (xBottomCurrent > xTopCurrent+32) {
					xBottomCurrent -= 4;
					go2 = true;
				}
				if (!go1 && !go2) {
					// Close game window here
					// This code block executes ONCE 
					endGameAnimation = false;
				}
				outroOpening = 0;
			}

			return;
		}

		if(gameStarting && bPlayIntro && introFrame < 5){
			introTime += dt;
			if(introTime >= 30) {
				introTime = 0;
				introFrame++;
			}

			return;
		}
		
		// Update the animation
		checkIfPlayerGetCollectable();
		
		// Check if player is within enemies range
		newTargetForEnemy();
		
		checkIfPlayerIsDead();
		
		if (waveNumber >= 16) {
			checkIfPlayerHasWon();
		}
		
		t += dt;
		runningCounter += dt;
		collectableTimer += dt;
		if (playerIsAlive) {
			// Control character's swinging animation speed
			if (runAnimation) {
				if (character.doesHaveWeapon()) {
					swingingCounter += dt;
					if (swingingCounter >= hitSpeed) {
						// Rotate animation
						Weapon s = character.getPlayerWeapon();
						s.rotateWeapon(character.getFacingDirection());
						if (s.getImageID() == 0) {
							runAnimation = false;
							checkIfPlayerHasHitEnemy();
							
							// Modify weapon's recharge
							s.setCurrentRecharge(0.0);
							delayWeapon = true;
							rechargeTimer = 0;
						}
						swingingCounter = 0;
					}
				}
			}
			
			// Control character's movement animation speed
			if (runningCounter >= runningSpeed) {
				// Move player
				character.update(t);
				
				runningCounter = 0;
			}
		}
		if (collectableTimer > timeBetweenEachCollectableGenerated) {
			// Generate a collectable
			spawnRandomCollectable();
			collectableTimer = 0;
		}
		if (!firstWave) {
			// Delay between the release of each wave
			difference = nextWaveTime - t;
			if (t > nextWaveTime) {
				// Spawn wave
				generateNextWave();
				
				difference = 0;
				t = 0;
				nextWaveTime = 9 + (2 * waveNumber);
			}
		}
		else {
			// For the first wave
			difference = nextWaveTime - t;
			if (t > nextWaveTime) {
				generateNextWave();
				difference = 0;
				t = 0;
				firstWave = false;
				nextWaveTime = 9 + (2 * waveNumber);
			}
		}

		//-------------------------Enemy stuff-------------------------//
		es += dt;   		//enemy spawn counter
		mc += dt;			//movestep counter
		
		if (mc > 0.05) {
			spawner.MoveStep();
			mc = 0;
		}
		
		// Update weapon recharge
		if (delayWeapon) {
			rechargeTimer += dt;
			// Update weapon's recharge
			Weapon w = character.getPlayerWeapon();
			if (rechargeTimer > w.getRechargeDelay()/500.0) {
				rechargeTimer = 0;
				w.increaseCurrentRecharge(5.5);
			}
			if (w.getCurrentRecharge() == 100.0) {
				delayWeapon = false;
			}
		}
		
		// Remove dead enemies
		removeDeadEnemies();
		
		// Check collectable thresholds
		scoreThresholds();
		
		// Show wave text for 2.5 seconds
		if (showWaveTextTimer > 0.0) {
			showWaveTextTimer += dt;
			if (showWaveTextTimer >= 2.5) {
				showWaveTextTimer = 0.0;
			}
		}
		
		// Update intro movement
		introOpening += dt;
		if ((introOpening > 0.01) && (gameStarting)) {
			if (xTopCurrent >= -32) {
				xTopCurrent -= 5;
			}
			if (xBottomCurrent <= window_height-63+32) {
				xBottomCurrent += 5;

			}
			else {
				gameStarting = false;
			}
			introOpening = 0;
		}
		
		if (!playerIsAlive) {
			setGameOver(true, false);
		}
	}

	public void damageTower(double damage) {
		if (towerHealth - damage <= 0) {
			towerHealth = 0;
			playAudio(gameOverSound);
			setGameOver(true, false);
			return;
		}
		towerHealth -= damage;
		playAudio(towerDamage);
	}

	@Override
	public void keyPressed(KeyEvent event) {		
		// All keyboard operations in here
		try {			
			if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
				if (isPaused) {
					isPaused = false;
				} else {
					isPaused = true;
				}
			}
			
			if ((event.getKeyCode() == KeyEvent.VK_SPACE)) { // Use weapon
				// Only use weapon when the weapon has recharged to at least 85% inclusive
				if(gameStarting && bPlayIntro && introFrame < 5) {
					introTime = 0;
					introFrame++;
				}
				else if (character.getPlayerWeapon().getCurrentRecharge() >= 85.0) {
					runAnimation = true;
					Weapon w = character.getPlayerWeapon();
				}
			} 
			
			if (event.getKeyCode() == KeyEvent.VK_W){
				// Move player up
				character.moveUp();
			} else if (event.getKeyCode() == KeyEvent.VK_S){
				// Move player down
				character.moveDown();

			} else if (event.getKeyCode() == KeyEvent.VK_D)  {
				// Move player right
				character.moveRight();

			} else if (event.getKeyCode() == KeyEvent.VK_A) {
				// Move player left
				character.moveLeft();
			}
			
		} catch (Error e) {
			System.out.println(e);
			return;
		}		
	}
	
	@Override
	public void keyReleased(KeyEvent event) {
		try {
			if (event.getKeyCode() == KeyEvent.VK_W) {
				// Move player up
				character.setVelY(0);
			} else if (event.getKeyCode() == KeyEvent.VK_S) {
				// Move player down
				character.setVelY(0);
			} else if (event.getKeyCode() == KeyEvent.VK_D) {
				// Move player right
				character.setVelX(0);
			} else if (event.getKeyCode() == KeyEvent.VK_A) {
				// Move player left
				character.setVelX(0);
			}
		} catch (Error e) {
			System.out.println(e);
			return;
		}	
	}
	
	@Override
	public void paintComponent() {
		
		// Clear window
		changeBackgroundColor(darkGrey);

		clearBackground (window_width, window_height);

		if(bPlayIntro && introFrame < 5) {
			drawIntro();
			return;
			
		} 
		if(bPlayIntro && introFrame == 5) {
			stopAudioLoop(introMusic);
			loopStarted = false;
			introFrame++;
		}
		if(!loopStarted){
			startAudioLoop(backgroundMusic);
			loopStarted = true;
		}
		
		// Draw map
		drawMap();
		
		// The below need to come after the map has been drawn for the first time (they rely on its variables)
		modX = castleX-218; // maximum x coordinate for player health bar
		healthWidthX = modX - (min_map_width + 92); // Width of the player's health bar
		
		// Draw Score
		drawScore();
		
		if (character.doesHaveWeapon()) {
			drawWeaponRecharge();
		}
		
		drawPlayerHealth();
		
		drawTowerHealth();
		// Draw castle
		drawImage(castlePics.get(currentCastle), castleX, castleY);
		
		if (!flag) {
			changeColor(skyBlue);
			drawSolidRectangle(castleX, castleY+165, castleWidth, 110);
			changeColor(yellow);
			drawRectangle(castleX, castleY+165, castleWidth, 110);
		}
		// Draw collectables
		for (Collectable a : collectables) {
			drawImage(a.getImage(), a.getLocationX(), a.getLocationY());
		}
		
		// Draw magic staff here, since the radius should be behind enemies
		if (character.hasWeapon) {
			if (character.getPlayerWeapon() instanceof MagicStaff) {
				changeColor(skyBlue);
				drawSolidCircle(character.getLocationX()+8, character.getLocationY()+8, radius*2);
			}
		}
		
		// Paint enemies with their healths
		for(Enemy e : cEnemies)
		{
			if(e.active) {
				drawEnemy(e);
			}
		}
		
		// Paint player
		drawPlayer();
		
		// Show wave text
		if ((showWaveTextTimer > 0 ) &&  (waveNumber <= 15)) {
			changeColor(darkOverlay2);
			drawSolidRectangle(0, window_height/2-200, window_width, 100);
			changeColor(white);
			drawText(window_width/2-50, window_height/2-140, "Wave " + Integer.toString(waveNumber), "Book Antiqua", 35);
		}
		
		if ((isPaused) || (gameOver)) {
			// If the game is over, pause the current screen state and, and show Game Over message
			if (gameOver) {
				isPaused = true;
				if (!send) {
					MenuState.getCurrentScore(playerScore, waveNumber-1);
					send = true;
				}
				drawOverlayOutro();
				if (gameOverText) {				
					drawImage(gameTextImage,window_width/2-200, xTopCurrent-200);
					drawImage(overTextImage,window_width/2-200, xBottomCurrent+100);
				} else if (gameWon) {
					drawImage(youTextImage,window_width/2-230, xTopCurrent-200);
					drawImage(wonTextImage,window_width/2-200, xBottomCurrent+100);					
				}
			} else {
				changeColor(darkOverlay);
				drawSolidRectangle(0, 0, window_width, window_height);
				drawImage(gamePausedText,window_width/2-160,  window_height/2 - 100);
			}
		}
		
		if (gameStarting) {
			// Show starting animation as an overlay
			drawOverlayIntro();
		} 	
	}
	
	private void setGameOver(boolean gameOverValue, boolean gameWon) {
		gameOver = gameOverValue;
		stopAudioLoop(backgroundMusic);
		stopAudioLoop(dBugMusic);
		if (!gameWon) {
			gameOverText = true;
		} else {
			playAudio(winMusic);
			this.gameWon = gameWon; 
		}
	}
	
	
	private void drawImageLine(Image i, int startingY) {
		for (int j = 0; j <= window_width-32; j+=32) {
			// Draw image line
			drawImage(i, j, startingY, 32, 32);
		}
	}
	
	private void drawOverlayIntro() {
		if (beginGameAnimation) {
			xTopCurrent = window_height/2-32;
			xBottomCurrent = xTopCurrent +32;
			beginGameAnimation = false;
		}
		changeColor(borderGrey);
		drawSolidRectangle(0, 0, window_width-1, xTopCurrent);
		drawSolidRectangle(0, xBottomCurrent, window_width-1, window_height-65);
		drawImageLine(introImage, xTopCurrent);
		drawImageLine(introImage, xBottomCurrent);
	}
	
	private void drawOverlayOutro() {
		changeColor(borderGrey);
		drawSolidRectangle(0, 0, window_width-1, xTopCurrent);
		drawSolidRectangle(0, xBottomCurrent, window_width-1, window_height-65);
		drawImageLine(introImage, xTopCurrent);
		drawImageLine(introImage, xBottomCurrent);
	}
	
	private void drawScore() {
		changeColor(yellow);

		// Show score and wave labels
		drawText(modX+30, 60, "Score:", "Book Antiqua", 27);
		drawText(modX+110, 60, Integer.toString(playerScore), "Arial", 20);
		drawText(modX+30, 90, "Wave:", "Book Antiqua", 27);
		drawText(modX+110, 90,  Integer.toString(waveNumber), "Arial", 20);
		
		// Show when next wave is
		if (waveNumber < 15) {
			drawText(min_map_width + 130, 150,  Integer.toString((int)difference), "Arial", 19);
		}
		changeColor(brown);
		drawText(min_map_width+10, 150, "Next wave in:", "Arial", 20);
		// Show character profile Image
		drawImage(characterProfileImage, min_map_width, 15, 75, 92);	
	}
	
	private void drawWeaponRecharge() {
		// Background Circle
		drawSolidCircle(max_map_width-50+tileSize, min_map_height+50, 50);
		
		// Image overlay
		Weapon w = character.getPlayerWeapon();
		drawImage(w.getImageAt(0), max_map_width-50, min_map_height+20, 32, 67);
		
		// Recharge level
		double current = w.getCurrentRecharge();
		changeColor(getHealthColor(current, 100));
		startAngle = 0;
		arcAngle = (int) ((current/100)*360);
		Stroke t = super.mGraphics.getStroke();
        super.mGraphics.setStroke(new BasicStroke(6));
		super.mGraphics.drawArc(max_map_width-77, min_map_height+7, 85, 85, startAngle, arcAngle);
		// Reset stroke
        super.mGraphics.setStroke(t);
	}
	
	private void drawPlayer() {
		int width = character.getImage().getWidth(null);	
		int height = character.getImage().getHeight(null);
		// Draw player weapon
		if (character.doesHaveWeapon()) {
			Weapon s = character.getPlayerWeapon();
			drawImage(s.getImage(), s.getLocationX(), s.getLocationY());		
		}
		
		// Draw player image
		drawImage(character.getImage(), character.getLocationX()-3, character.getLocationY()-15);
	}
	
	
	private void drawTowerHealth() {
		// Draw grey backing bar
		changeColor(healthBackingGrey);
		drawSolidRectangle(max_map_width - 410, 45, 310, 40);
		changeColor(getHealthColor(towerHealth, 1000));
		// Draw health bar
		int start = max_map_width - 405;		
		for (int i = 0; i < towerHealth/10; i++) {
			drawRectangle(start, 48, 3, 33);
			start += 3;
		}
		drawImage(towerProfileImage, max_map_width - 90, 15, 107, 92);	
	}

	private void drawPlayerHealth() {
		// Draw grey backing
		changeColor(healthBackingGrey);	
		drawSolidRectangle(min_map_width + 87, 45, healthWidthX, 40);
		changeColor(getHealthColor(character.getHealth(), 100)); // 100 represents the max health that the character can have
		
		// Draw health bar
		double end = healthWidthX - 10;	
		double start = min_map_width + 92;
		double bar = (end) / (100.0);		
		for (int i = 0; i < character.getHealth(); i++) {
			drawRectangle(start, 48, bar, 33);
			start += bar;
		}
	}
	
	private void drawEnemy(Enemy e) {	
		int width = e.img.getWidth(null);	
		int height = e.img.getHeight(null);	
		if(e.shockWaving)	
		{	
			if(e.boss)	
			{	
				drawImage(largeShockwavePic, e.posx - ((largeShockwavePic.getWidth(null)) / 2), e.posy - ((largeShockwavePic.getHeight(null)) / 2));	
			} else {	
				drawImage(smallShockwavePic, e.posx - ((smallShockwavePic.getWidth(null)) / 2), e.posy - ((smallShockwavePic.getHeight(null)) / 2));	
			}	
		}	
		if (!e.flipped) {	
			drawAlphaImage(e.img, e.posx - (width/2), e.posy - (height/2), width, height, e.alpha);	
		} else {	
			drawAlphaImage(e.img, e.posx + (width/2), e.posy - (height/2), -width, height, e.alpha);	
		}	
		int health = e.getHealth();	
		changeColor(getHealthColorEnemies(health, e.getHealthMax()));	
		if(e.boss) {	
			drawAlphaBoldText(e.getLocationX() - (width/4), e.getLocationY() + (height/1.3), Integer.toString(health), "Impact", 25, e.alpha);	
		} else {	
			drawAlphaBoldText(e.getLocationX() - (width / 2), e.getLocationY() + height, Integer.toString(health), "Book Antiqua", 17, e.alpha);	
		}	
	}
	
	private void drawMap() {
		// Create Border
		int buffer = tileSize*4;
		int minX = 0, maxX = window_width-(window_width%tileSize), minY = 0, maxY = window_height-(window_height%tileSize)-buffer;
		int multiplier = 0;
		int borderWidth = 8;
		

		for (thickness = 0; thickness < borderWidth; thickness++) { // Thickness of the border around the game
			for (int i = minX+(tileSize*multiplier); i <= maxX-(tileSize*multiplier); i += tileSize) { // Top border
				if (thickness == borderWidth-1) {
					drawImage(top, i, minY+(tileSize*multiplier), tileSize, tileSize);
				} else {
					drawImage(mid, i, minY+(tileSize*multiplier), tileSize, tileSize);
				}
			}	
			
			for (int i = minX+(multiplier * tileSize); i <= maxX-(tileSize*multiplier); i += tileSize) { // Bottom border
				if (thickness == borderWidth-1) {
					drawImage(bottom, i, maxY-(tileSize*multiplier),tileSize, tileSize);

				} else {
					drawImage(mid, i, maxY-(tileSize*multiplier), tileSize, tileSize);	
				}
			}	
			for (int i = minY+(tileSize*multiplier); i <= maxY - (tileSize*multiplier); i += tileSize) { // Left border
				if (thickness == borderWidth-1) {
					drawImage(rightEdge, minX+(tileSize*multiplier), i, tileSize, tileSize);		

				} else {
					drawImage(mid, minX+(tileSize*multiplier), i, tileSize, tileSize);		
				}
			} for (int i = minY+(tileSize*multiplier); i <= maxY-(tileSize*multiplier); i += tileSize) { // Right border				
				if (thickness == borderWidth-1) {
					drawImage(leftEdge, maxX-(tileSize*multiplier), i, tileSize, tileSize);		

				} else {
					drawImage(mid, maxX-(tileSize*multiplier), i, tileSize, tileSize);		
				}
			}
			multiplier += 1;
		}	
	
		// Set new size
		min_map_width = minX+(tileSize*multiplier);
		max_map_width = maxX-(tileSize*(multiplier));
		min_map_height = minY+(tileSize*multiplier);
		max_map_height  = maxY - (tileSize*(multiplier));
		

		scoreStartX = modX + 40;

		
		// Set backdrop for gameplay arena		
		drawImage(backing, min_map_width, min_map_height, (max_map_width-min_map_width) +tileSize, (max_map_height-min_map_height) + tileSize);
	}
	
	//-------------------ENEMIES SECTION-------------------//
	EnemySpawner spawner = new EnemySpawner();
	ArrayList<Enemy> cEnemies = new ArrayList<>();

	public ArrayList<Enemy> getCurrentEnemies()
	{
		return cEnemies;
	}

	public void addCurrentEnemies(Enemy n)
	{
		cEnemies.add(n);
	}
}
