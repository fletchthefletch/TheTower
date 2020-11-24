package theTower;

import java.awt.Image;

public class GoldSword extends Weapon {
	public GoldSword(Image i, int lx, int ly) {
		super(i, lx, ly);
		damageToInflict = 50;
		recharge = 0.0;
		
		loadImages();
	}
	@Override
	protected void loadImages() {
		try {
			for (int i = 0; i < 3; i++) {
				String str = "src/frames/weapons/goldsword/" + Integer.toString(i) + ".png";
				anims.add(Engine.loadImage(str));
			}
		}
		catch (Exception e) {
			System.out.println("Could not retrieve images.");
			return;
		}
	}
}
