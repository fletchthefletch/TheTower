package theTower;

import java.awt.Image;

public class Player {
	private int health; // Health as a percent
	private int speed = 4, currentLeftFrameImage = 0, currentRightFrameImage = 0;
	private int locationX, locationY;
	private double velocityX, velocityY;
	boolean facingDirection = false, hasWeapon = false;
	private boolean cantGoUp = false, cantGoLeft = false, cantGoRight = false, cantGoDown = false;
	private int weaponX, weaponY, weaponMarginLeft = -6, weaponMarginRight = 21; // Weapon margin is the space between the place and the weapon

	private int towerX, towerY = 150;

	Image[][] playerImages = new Image[2][20]; // Each animation must have less than 20 images 

	Weapon C; // Player's current weapon

	public Player(int spawnX, int spawnY) {
		this.locationX = spawnX;
		towerX = spawnX;
		this.locationY = spawnY;
		this.health = 100;
		loadImages();
	}

	public Weapon getPlayerWeapon() {
		if (hasWeapon) {
			return C;
		}
		return null;
	}
	public boolean doesHaveWeapon() {
		return hasWeapon;
	}
	public void sethasWeapon(boolean value) {
		hasWeapon = value;
	}

	private void setPlacement() {
		// Move weapon with player
		if (facingDirection) {
			this.C.setLocationX(getLocationX()+weaponMarginRight);
			this.C.setLocationY(getLocationY()+10);
			return;
		}

		this.C.setLocationY(getLocationY()+10);
		if (this.C.getImageID() == 1) {
			this.C.setLocationX(getLocationX()-25);
			return;
		}
		this.C.setLocationX(getLocationX()+weaponMarginLeft);
	}
	public void setCollectable(Weapon C) {
		this.C = C;
		if (facingDirection) {
			this.C.setLocationX(this.getLocationX()+15); //15
			this.C.setLocationY(this.getLocationY()+10);
			return;
		}

		this.C.setLocationY(this.getLocationY());
		this.C.setLocationX(this.getLocationX()-20);
		return;
	}

	public boolean getFacingDirection() {
		return facingDirection;
	}
	public int damagePlayer(double damage) {
		if (health - damage <= 0) {
			health = 0;
			Game.playAudio(Game.gameOverSound);
			return -1;
		}
		Game.playAudio(Game.heroDamage);
		health -= damage;
		return 0;
	}
	public void increaseHealth(int value) {
		Game.playAudio(Game.healthInc);
		if (health + value > 100) {
			health = 100;
			return;
		}
		health += value;
	}
	private void loadImages() {
		try {
			// Get player Images
			for (int i = 0; i < 7; i++) {
				String str = "src/frames/player/right/" + Integer.toString(i) + ".png";
				playerImages[1][i] = Engine.loadImage(str);
			}

			for (int i = 0; i < 7; i++) {
				String str = "src/frames/player/left/" + Integer.toString(i) + ".png";
				playerImages[0][i] = Engine.loadImage(str);
			}
		}
		catch (Exception e) {
			System.out.println("Could not retrieve images.");
			return;
		}
	}
	public void moveUp() {
		this.setVelY(-speed);
	}
	public void moveDown() {
		this.setVelY(speed);
	}
	public void moveRight() {
		if (!facingDirection) {
			facingDirection = true;
			if (hasWeapon) {
				C.setImageID(0);
			}
		}
		this.setVelX(speed);
	}
	public void moveLeft() {
		if (facingDirection)  {
			facingDirection = false;
			if (hasWeapon) {
				C.setImageID(0);
			}
		}
		this.setVelX(-speed);
	}
	private boolean checkBoundary() {
		if (locationY <= Game.min_map_height -32) {
			cantGoUp = true;
		} else {
			cantGoUp = false;
		}
		if (locationY >= Game.max_map_height-32) {
			cantGoDown = true;
		} else {
			cantGoDown = false;
		}
		if (this.getLocationX() <= Game.min_map_width) {
			cantGoLeft = true;
		} else {
			cantGoLeft = false;
		}
		if (this.getLocationX() >= Game.max_map_width) {
			cantGoRight = true;
		} else {
			cantGoRight = false;
		}

		if((Game.closeEnough(this.locationX, towerX-10, 155)) && (Game.closeEnough(this.locationY, towerY-25, 45)))
		{
			if((Game.closeEnough(this.locationX, towerX-10, 150)) && (Game.closeEnough(this.locationY, towerY-25, 42)))
			{
				cantGoUp = true;
			}
			if((towerX > this.locationX) && (Game.closeEnough(this.locationY, towerY-25, 39)))
			{
				cantGoRight = true;
			}
			if((towerX < this.locationX) && (Game.closeEnough(this.locationY, towerY-25, 39)))
			{
				cantGoLeft = true;
			}
		}


		if ((velocityX > 0) && (cantGoRight)) {
			// Don't go right
			return true;
		} else if ((velocityX < 0) && (cantGoLeft)) {
			return true;
		} else if ((velocityY > 0) && (cantGoDown)) {
			return true;
		} else if ((velocityY < 0) && (cantGoUp)) {
			return true;
		}
		return false;
	}
	private void move() {
		locationX += velocityX;
		locationY += velocityY;
	}
	private void changeFacingAnimation() {
		// Going right or left
		if ((facingDirection) && (velocityX > 0)) {
			currentRightFrameImage = (currentRightFrameImage+1) % 6;
		} else if ((!facingDirection) && (velocityX < 0)) {
			currentLeftFrameImage = (currentLeftFrameImage+1) % 6;
		}

		// Going down
		else if ((facingDirection) && (velocityY > 0)) {
			currentRightFrameImage = (currentRightFrameImage+1) % 6;
		}
		else if ((facingDirection) && (velocityY < 0)) {
			currentRightFrameImage = (currentRightFrameImage+1) % 6;
		}

		// Going up
		else if ((!facingDirection) && (velocityY < 0)) {
			currentLeftFrameImage = (currentLeftFrameImage+1) % 6;
		}
		else if ((!facingDirection) && (velocityY > 0)) {
			currentLeftFrameImage = (currentLeftFrameImage+1) % 6;
		}
	}
	public void update(double t) {
		// Check if player has reached boundary
		if (checkBoundary()) {
			return;
		}
		// Move player
		move();

		// change directional animation
		changeFacingAnimation();

		if (!hasWeapon) {
			return;
		}
		setPlacement();
	}

	public void setVelX(double x) {
		this.velocityX = x;
	}
	public void setVelY(double y) {
		this.velocityY = y;
	}
	public int getLocationX() {
		return locationX;
	}
	public int getLocationY() {
		return locationY;
	}
	public Image getImage() {
		if (facingDirection) {
			return playerImages[1][currentRightFrameImage];
		}

		return playerImages[0][currentLeftFrameImage];
	}
	public int getHealth() {
		return health;
	}
}
