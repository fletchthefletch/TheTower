package theTower;
import java.util.ArrayList;
import java.util.Random;

public class EnemySpawner {
    Game main;
    int arenaSizeX, arenaSizeY, towerX;

    public void init(Game main, int sizeX, int sizeY)
    {
        this.main = main;
        arenaSizeX = sizeX;
        arenaSizeY = sizeY;
        towerX = arenaSizeX/2;
    }

    public void Spawn(int enemyIndex)
    {
            Random r = new Random();
            int tempx = r.nextInt(arenaSizeX - 260) + 130;
            int tempy = arenaSizeY-204;

            // Specify which enemy will be spawned
            Enemy nextEnemy;
            switch(enemyIndex) {
            	case 0:
            		// Kobold
            		nextEnemy = new Kobold(towerX, main);
            		break;
                case 1:
                    //Orc
                    nextEnemy = new Orc(towerX, main);
                    break;
                case 2:
                    //Imp
                    nextEnemy = new Imp(towerX, main);
                    break;
                case 3:
                    //Hood
                    nextEnemy = new Hood(towerX, main);
                    break;
                case 4:
                    //Ogre
                    nextEnemy = new Ogre(towerX, main);
                    break;
                case 5:
                    //Boss
                    nextEnemy = new DBug(towerX, main);
                    break;
            	default:
            		// If the index is invalid
            		nextEnemy = new Kobold(towerX, main);
            		break;
            }
            
            nextEnemy.posx = tempx;
            nextEnemy.posy = tempy;
            nextEnemy.Activate();
            main.addCurrentEnemies(nextEnemy);
        }

    public void MoveStep()
    {
        for(Enemy e : main.getCurrentEnemies())
        {
            if(e.active)        //moves all active enemies
            {
                 e.Move(main.getCurrentEnemies());
            }

        }
    }
}
