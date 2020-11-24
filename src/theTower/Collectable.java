package theTower;

import java.awt.Image;

public abstract class Collectable {
	private int locationX, locationY;
	protected Image pickupImage;
	protected boolean pickedUp = false;
	
	public Collectable(Image i, int lx, int ly) {
		this.locationX = lx;
		this.locationY = ly;
		this.pickupImage = i;
	}
	
	public void setLocationX(int x) {
		locationX = x;
	}
	public void setLocationY(int y) {
		locationY = y;
	}
	public int getLocationX() {
		return locationX;
	}
	public int getLocationY() {
		return locationY;
	}
	public Image getImage() {
		return pickupImage;
	}
	public void setPickedUp(boolean value) {
		pickedUp = value;
	}
}
