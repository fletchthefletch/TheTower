package theTower;

import java.util.Random;
import static theTower.Engine.loadImage;

public class Kobold extends Enemy {

    public Kobold(int towerx, Game main)
    {
        super(towerx, main);
        this.posx = -100;
        this.posy = -100;
        this.health = 25;
        this.healthMax = health;
        this.attack = 10;
        this.speed = 5;
        Random r = new Random();
        this.vtionID = r.nextInt(2);    //randomly chooses male or female sprite
        if(vtionID == 0)
        {
            eFrames.add(loadImage("src/frames/Enemies/Koboldf_run_0.png"));
            eFrames.add(loadImage("src/frames/Enemies/Koboldf_run_1.png"));
            eFrames.add(loadImage("src/frames/Enemies/Koboldf_run_2.png"));
            eFrames.add(loadImage("src/frames/Enemies/Koboldf_run_3.png"));
        } else {
            eFrames.add(loadImage("src/frames/Enemies/Koboldm_run_0.png"));
            eFrames.add(loadImage("src/frames/Enemies/Koboldm_run_1.png"));
            eFrames.add(loadImage("src/frames/Enemies/Koboldm_run_2.png"));
            eFrames.add(loadImage("src/frames/Enemies/Koboldm_run_3.png"));
        }
        this.img = eFrames.get(0);
    }
}
