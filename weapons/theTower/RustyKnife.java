package theTower;

import java.awt.Image;

public class RustyKnife extends Weapon {
	public RustyKnife(Image i, int lx, int ly) {
		super(i, lx, ly);
		damageToInflict = 10;
		recharge = 5; // take bback to 0
		
		loadImages();
	}

	@Override
	protected void loadImages() {
		try {
			for (int i = 0; i < 3; i++) {
				String str = "src/frames/weapons/rustysword/" + Integer.toString(i) + ".png";
				anims.add(Engine.loadImage(str));
			}
		}
		catch (Exception e) {
			System.out.println("Could not retrieve images.");
			return;
		}
	}
}
