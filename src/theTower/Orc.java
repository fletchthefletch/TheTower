package theTower;

import java.util.Random;
import static theTower.Engine.loadImage;

public class Orc extends Enemy {

    public Orc(int towerx, Game main)
    {
        super(towerx, main);
        this.posx = -100;
        this.posy = -100;
        this.health = 75;
        this.healthMax = health;
        this.attack = 20;
        this.speed = 2;
        Random r = new Random();
        this.vtionID = r.nextInt(3);    //randomly chooses sprite variation
        if(vtionID == 0) {
            eFrames.add(loadImage("src/frames/Enemies/orc_run_0.png"));
            eFrames.add(loadImage("src/frames/Enemies/orc_run_1.png"));
            eFrames.add(loadImage("src/frames/Enemies/orc_run_2.png"));
            eFrames.add(loadImage("src/frames/Enemies/orc_run_3.png"));
        } else if (vtionID == 1)
        {
            eFrames.add(loadImage("src/frames/Enemies/maskorc_run_0.png"));
            eFrames.add(loadImage("src/frames/Enemies/maskorc_run_1.png"));
            eFrames.add(loadImage("src/frames/Enemies/maskorc_run_2.png"));
            eFrames.add(loadImage("src/frames/Enemies/maskorc_run_3.png"));
        } else {
            eFrames.add(loadImage("src/frames/Enemies/shamanorc_run_0.png"));
            eFrames.add(loadImage("src/frames/Enemies/shamanorc_run_1.png"));
            eFrames.add(loadImage("src/frames/Enemies/shamanorc_run_2.png"));
            eFrames.add(loadImage("src/frames/Enemies/shamanorc_run_3.png"));
        }
        this.img = eFrames.get(0);
    }
}
