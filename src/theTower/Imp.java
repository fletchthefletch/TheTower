package theTower;

import java.util.Random;
import static theTower.Engine.loadImage;

public class Imp extends Enemy {
    Boolean dashing = false;
    double oldSpeed;

    public Imp(int towerx, Game main)
    {
        super(towerx, main);
        this.posx = -100;
        this.posy = -100;
        this.health = 40;
        this.healthMax = health;
        this.attack = 20;
        this.speed = 4;
        oldSpeed = this.speed;
        this.abilSpeed = 1;
        Random r = new Random();
        this.vtionID = r.nextInt(2);    //randomly chooses male or female sprite
        if(vtionID == 0)
        {
            eFrames.add(loadImage("src/frames/Enemies/impr_run_0.png"));
            eFrames.add(loadImage("src/frames/Enemies/impr_run_1.png"));
            eFrames.add(loadImage("src/frames/Enemies/impr_run_2.png"));
            eFrames.add(loadImage("src/frames/Enemies/impr_run_3.png"));
        } else {
            eFrames.add(loadImage("src/frames/Enemies/impp_run_0.png"));
            eFrames.add(loadImage("src/frames/Enemies/impp_run_1.png"));
            eFrames.add(loadImage("src/frames/Enemies/impp_run_2.png"));
            eFrames.add(loadImage("src/frames/Enemies/impp_run_3.png"));
        }
        this.img = eFrames.get(0);
    }

    public void Ability()
    {
        if(!dashing)
        {
            this.speed *= 3;
            this.abilCount = abilCount - (abilCount / 3);
        } else {
            this.speed = oldSpeed;
            this.abilCount = 0;
        }
        dashing = !dashing;
    }
}
