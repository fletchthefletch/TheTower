package theTower;

import java.awt.*;
import java.util.ArrayList;

public abstract class Enemy {
    public double posx, posy;
    public Image img;
    protected int health, healthMax;
    protected double speed;
    protected int attack;
    protected double abilSpeed;
    protected float alpha = 1;
    protected int vtionID;  //used to determine variations in sprite
    public Boolean active = false, flipped = false, moving = true, boss = false;
    public int towerposx, towerposy;
    protected Game main;

    protected ArrayList<Image> eFrames = new ArrayList<>();

    public double targetx, targety;

    public void Activate()
    {
        active = true;
    }

    public Enemy(int towerx, Game main /*player*/)
    {
        this.main = main;
        towerposx = towerx - 5; //Offset a bit so it looks like they go to sprite door
        towerposy = 150;
        targetx = this.towerposx;
        targety = this.towerposy;
    }
    public double getLocationX() {
    	return posx;
    }
    public double getLocationY() {
    	return posy;
    }

    public boolean chasingPlayer = false;
    public boolean shockWaving = false;

    public void newTarget(int targetX, int targetY) {
        if(!attackingtower) {
            targetx = targetX;
            targety = targetY;
        }
    }

    double count = 0, hits = 0.45, abilCount = 0;
    public boolean attackingtower = false;
    public int Move(ArrayList<Enemy> otherEnemies)
    {
        abilCount = abilCount+0.05;
        if(abilCount*abilSpeed > 2)
        {
            Ability();
        }
        if(!attackingtower) {
            if (!chasingPlayer) {
                targetx = this.towerposx;
                targety = this.towerposy;
            } else {
                targetx = main.character.getLocationX();
                targety = main.character.getLocationY();
            }
        }
        count = count + 0.05;
        if(count >= 0.5/speed)
        {
            Animate();
            count = 0;
        }
		 if(targetx < posx)
    	{
    		flipped = true;
    	} else {
		     flipped = false;
         }
        double dx, dy;
        //get direction vector to target.
        dx = targetx - posx;
        dy = targety - posy;
        //check that the enemy isn't in range of a target
        if((targetx == towerposx) && (targety == towerposy)) {  //if theyre stopping to attack tower
            if ((Game.closeEnough((int)posx, (int)targetx, 100)) && (Game.closeEnough((int)posy, (int)targety, 30))) //makes it look more like theyre stopping at the horizontal wall
            {
                hits = hits + 0.05;
                if(hits > 0.5)
                {
                    main.damageTower(attack);
                    hits = 0;
                }
                attackingtower = true;
                moving = false;
                return 0;
            }
        } else {        //if theyre stopping because theyre near hero
            if((Game.closeEnough((int)posx, (int)targetx, 25)) && (Game.closeEnough((int)posy, (int)targety, 25)))
            {
                hits = hits + 0.05;
                int res = 0;
                if(hits > 1)
                {
                    res = main.character.damagePlayer(attack);
                    hits = 0;
                }
                System.out.println("Attacking player");
                moving = false;
                if (res == -1) {
                	return -1;
                }
                return 0;
            }
        }
        //normalize vector.
        double mag = Math.sqrt(((targetx - posx) * (targetx - posx)) + ((targety - posy) * (targety - posy)));
        dx = dx / mag;
        dy = dy / mag;
        //multiply vector by speed.
        dx = dx * speed;
        dy = dy * speed;
        //move by result vector amount
        hits = 0.45;
        moving = true;
        posx = posx + dx;
        posy = posy + dy;
		  for(Enemy e : otherEnemies)
        {
        	if(e != this)
        	{
	        	if((Game.closeEnough((int)posx, (int)e.posx, 10)) && (Game.closeEnough((int)posy, (int)e.posy, 10)))
	        	{
	        		//push this enemy away by -2 * unit direction vector to other enemy
	        		dx = e.posx - posx;
	        		dy = e.posy - posy;
	        		mag = Math.sqrt(((e.posx - posx) * (e.posx - posx)) + ((e.posy - posy) * (e.posy - posy)));
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

    int index = 0;
    protected void Animate()
    {
        index++;
        if(index > 3)
        {
            index = 0;
        }
        img = eFrames.get(index);
    }

    public int getHealth() {
        return health;
    }
    public int getHealthMax() {
        return healthMax;
    }

    public void reduceHealth(int amount) {
        if (health - amount <= 0) {
            health = 0;
            return;
        }
        health -= amount;
    }

    public void Ability()
    {
        return;
    }
}
