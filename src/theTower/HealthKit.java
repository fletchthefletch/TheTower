package theTower;

import java.awt.Image;

public class HealthKit extends Collectable {
	private int healthAmount = 15;
	public HealthKit(Image i, int lx, int ly) {
		super(i, lx, ly);
		
	}
	public int getPayload() {
		return healthAmount;
	}
}
