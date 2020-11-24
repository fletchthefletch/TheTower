package theTower;

import java.util.Random;
import static theTower.Engine.loadImage;

public class Hood extends Enemy {

    public Hood(int towerx, Game main)
    {
        super(towerx, main);
        this.posx = -100;
        this.posy = -100;
        this.health = 50;
        this.healthMax = health;
        this.attack = 25;
        this.speed = 4;
        this.abilSpeed = 40;
        Random r = new Random();
        this.vtionID = r.nextInt(2);    //randomly chooses male or female sprite
        if(vtionID == 0)
        {
            eFrames.add(loadImage("src/frames/Enemies/hood_run_0.png"));
            eFrames.add(loadImage("src/frames/Enemies/hood_run_1.png"));
            eFrames.add(loadImage("src/frames/Enemies/hood_run_2.png"));
            eFrames.add(loadImage("src/frames/Enemies/hood_run_3.png"));
        } else {
            eFrames.add(loadImage("src/frames/Enemies/hood2_run_0.png"));
            eFrames.add(loadImage("src/frames/Enemies/hood2_run_1.png"));
            eFrames.add(loadImage("src/frames/Enemies/hood2_run_2.png"));
            eFrames.add(loadImage("src/frames/Enemies/hood2_run_3.png"));
        }
        this.img = eFrames.get(0);
    }

    Boolean fading = true;
    Boolean waiting = false;
    int waitcount = 0;
    public void Ability()
    {
        if((alpha <= 0.03) && fading)
        {
            fading = false;
            waiting = true;
        }
        if((alpha >= 0.97) && !fading)
        {
            fading = true;
            waiting = true;
        }
        if(!waiting) {
            if (fading) {
                if (alpha > 0) {
                    alpha = alpha - (float) 0.03;
                }
            }
            if (!fading) {
                if (alpha < 1) {
                    alpha = alpha + (float) 0.03;
                }
            }
        } else {
            waitcount += 1;
            if(waitcount == 50)
            {
                waiting = false;
                waitcount = 0;
            }
        }
    }
}
