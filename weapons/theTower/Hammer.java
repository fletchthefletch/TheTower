package theTower;

import java.awt.Image;

public class Hammer extends Weapon {
	public Hammer(Image i, int lx, int ly) {
		super(i, lx, ly);
		damageToInflict = 30;
		recharge = 25;
		
		loadImages();
	}

	@Override
	protected void loadImages() {
		try {
			for (int i = 0; i < 3; i++) {
				String str = "src/frames/weapons/hammer/" + Integer.toString(i) + ".png";
				anims.add(Engine.loadImage(str));
			}
		}
		catch (Exception e) {
			System.out.println("Could not retrieve images.");
			return;
		}
	}
}
