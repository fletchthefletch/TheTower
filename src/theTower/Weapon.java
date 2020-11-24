package theTower;

import java.awt.Image;
import java.util.ArrayList;

public abstract class Weapon extends Collectable {
	protected ArrayList<Image> anims = new ArrayList<>();
	protected int currentImage = 0;
	protected int damageToInflict, numberOfFrames = 2; // recharge stands for recharge delay
	protected double recharge, currentRecharge;
	
	public Weapon(Image i, int lx, int ly) {
		super(i, lx, ly);
		currentRecharge = 100.0;
	}
	
	public void rotateWeapon(boolean facingDirection) {
		if (facingDirection) {
			switch(currentImage) {
			case 0:
				currentImage = 2;
				return;
			case 2:
				currentImage = 0;
				return;
			}
		}
		else {
			switch(currentImage) {
				case 0:
					currentImage = 1;
					return;
				case 1:
					currentImage = 0;
					return;
			}
		}
	}
	
	protected abstract void loadImages();
	
	public int getImageID() {
		return currentImage;
	}
	public int getDamage() {
		return damageToInflict;
	}
	public void setImageID(int id) {
		if ((id < numberOfFrames) && (id >= 0)) {
			currentImage = id;

		}
	}
	@Override
	public Image getImage() {
		if (!pickedUp) {
			return pickupImage;
		}
		return anims.get(currentImage);
	}
	
	public Image getImageAt(int id) {
		if ((id >= 0) && (id <= anims.size())) {
			return anims.get(id);
		}
		return null;
	}
	
	public double getRechargeDelay() {
		return recharge;
	}
	
	public double getCurrentRecharge() {
		return currentRecharge;
	}
	
	public void increaseCurrentRecharge(double value) {
		if (currentRecharge + value <= 100.0) {
			currentRecharge += value;
		} else {
			currentRecharge = 100.0;
		}
	}
	public void setCurrentRecharge(double value) {
		currentRecharge = value;
	}
}
