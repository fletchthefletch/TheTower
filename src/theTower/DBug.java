package theTower;

import java.util.ArrayList;

import static theTower.Engine.loadImage;

public class DBug extends Enemy {

    Boolean charged = false, dashing = false;
    double oldSpeed;

    public DBug(int towerx, Game main)
    {
        super(towerx, main);
        this.boss = true;
        this.posx = -100;
        this.posy = -100;
        this.health = 500;
        this.healthMax = health;
        this.attack = 45;
        this.speed = 2;
        oldSpeed = this.speed;
        this.abilSpeed = 0.8;
        eFrames.add(loadImage("src/frames/Boss/d'bug_run_0.png"));
        eFrames.add(loadImage("src/frames/Boss/d'bug_run_1.png"));
        eFrames.add(loadImage("src/frames/Boss/d'bug_run_2.png"));
        eFrames.add(loadImage("src/frames/Boss/d'bug_run_3.png"));
        this.img = eFrames.get(0);
    }

    public int Move(ArrayList<Enemy> otherEnemies) {
        moving = true;
        abilCount = abilCount + 0.05;
        if (abilCount * abilSpeed > 2) {
            Ability();
        }
        if (!attackingtower) {
            if (!chasingPlayer) {
                targetx = this.towerposx;
                targety = this.towerposy;
            } else {
                targetx = main.character.getLocationX();
                targety = main.character.getLocationY();
            }
        }
        count = count + 0.05;
        if (count >= 0.5 / speed) {
            Animate();
            count = 0;
        }
        if (targetx < posx) {
            flipped = true;
        } else {
            flipped = false;
        }
        double dx, dy;
        //get direction vector to target.
        dx = targetx - posx;
        dy = targety - posy;
        if (attackDelay() == -1) {
            return -1;
        }
        //check that the enemy isn't in range of a target
        if ((targetx == towerposx) && (targety == towerposy)) {  //if theyre stopping to attack tower
            if ((Game.closeEnough((int) posx, (int) targetx, 100)) && (Game.closeEnough((int) posy, (int) targety, 30))) //makes it look more like theyre stopping at the horizontal wall
            {
                hits = hits + 0.05;
                if (hits > 0.5) {
                    main.damageTower(attack);
                    hits = 0;
                }
                attackingtower = true;
                moving = false;
                return 0;
            }
        } else {        //if theyre stopping because theyre near hero
            if ((Game.closeEnough((int) posx, (int) targetx, 50)) && (Game.closeEnough((int) posy, (int) targety, 50))) {
                hits = hits + 0.05;
                int res = 0;
                if (hits > 1) {
                    res = main.character.damagePlayer(attack);
                    hits = 0;
                }
                System.out.println("Attacking player");
                moving = false;
                if (res == -1) {
                    return -1;
                }
                return 0;
            } else {
                if (charged && ((Game.closeEnough((int) posx, (int) targetx, 120)) && (Game.closeEnough((int) posy, (int) targety, 120)))) {   //Shockwave attack
                    aCount = 0;
                    charged = false;
                    hits = 0;
                    Engine.playAudio(main.ogreNoise);
                    Engine.playAudio(main.shockWave);
                    moving = false;
                    return 0;
                }
            }
        }
        if (moving){
            //normalize vector.
            double mag = Math.sqrt(((targetx - posx) * (targetx - posx)) + ((targety - posy) * (targety - posy)));
            dx = dx / mag;
            dy = dy / mag;
            //multiply vector by speed.
            dx = dx * speed;
            dy = dy * speed;
            //move by result vector amount
            hits = 0.45;
            posx = posx + dx;
            posy = posy + dy;
        }
        for(Enemy e : otherEnemies)
        {
            if(e != this)
            {
                if((Game.closeEnough((int)posx, (int)e.posx, 10)) && (Game.closeEnough((int)posy, (int)e.posy, 10)))
                {
                    //push this enemy away by -2 * unit direction vector to other enemy
                    dx = e.posx - posx;
                    dy = e.posy - posy;
                    double mag = Math.sqrt(((e.posx - posx) * (e.posx - posx)) + ((e.posy - posy) * (e.posy - posy)));
                    dx = dx / mag;
                    dy = dy / mag;
                    dx = dx * -2;
                    dy = dy * -2;
                    posx = posx + dx;
                    posy = posy + dy;
                }
            }
        }
        return 0;
    }

    public void Ability()
    {
        System.out.println("D'Bug shockwave charged");
        charged = true;
        this.abilCount = 0;

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

    int aCount = 51;
    private int attackDelay()
    {
        if(aCount <= 20)
        {
            moving = false;
        }
        aCount = aCount+1;
        if((aCount == 10) && ((Game.closeEnough((int) posx, (int) targetx, 125)) && (Game.closeEnough((int) posy, (int) targety, 125)))) {
            int res = main.character.damagePlayer(attack);
            System.out.println("Shockwaving Player");
            if (res == -1) {
                return -1;
            }
        }
        if((aCount >= 10) && (aCount <= 20))
        {
            shockWaving = true;
        } else {
            shockWaving = false;
        }
        return 0;
    }
}
