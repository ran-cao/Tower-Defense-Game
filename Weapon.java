import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Projectile.java <p>
 *
* A class for balls shot from cannon in BouncyCannonBallGame. <p>
*/
public class Weapon extends GCompound{
 //constants
 private static final double DELAY = 20;
 public static double
 GRAVITY = 0.5;
 // instance variable
 private TowerDefenseGame game; // the main game
 private double size; // size of ball
 private double speed, angle; // speed and direction of movement
 private boolean isAlive = true; // condition of ball, is alive or not
 private GImage shotting;
 private GImage throwing;
 private int x; // type of weapon
 private int y; // level of upgraded weapon
 public GRect level1;
 public GRect level2;
 public GRect level3;
 private RandomGenerator rand = RandomGenerator.getInstance();

 /** the constructor, create the ball */
 public Weapon(double size, int x, int y,
                  TowerDefenseGame game) {
  // save the parameters in instance variables
  this.game = game;
  this.size = size;
  this.x = x;
  this.y = y;
  // create the shotting weapon
     shotting = new GImage("3.gif"); 
     shotting.setSize(size,size);
     add(shotting,-size/2+10,-size/2);
  // create throwing weapon
     throwing = new GImage("4.gif");
     throwing.setSize(size*1.5,size*1.2);
     add(throwing,-size*1.5/2+10,-size*1.2/2+15);
  // create the level
   level1 = new GRect(15,15);
   add(level1,-45,-23);
   level1.setFilled(true);
   level1.setFillColor(Color.RED);
   level2 = new GRect(15,15);
   add(level2,-45,-8);
   level2.setFilled(true);
   level2.setFillColor(Color.GRAY);
   level3 = new GRect(15,15);
   add(level3,-45,7);
   level3.setFilled(true);
   level3.setFillColor(Color.GRAY);
   if (x == 0) {
     throwing.setVisible(false);
   }
   if (x == 1) {
     shotting.setVisible(false);
   } 
 }
 
 public int getLevel() {
     return y;
    }
    
 public void setLevel(int newy) {
     y = newy;
    }
 
 public void ball() {
     if(game.gameWin)return;
     double size = 30;
     double speed = 10;
     double level = 1;
     int time = 10;
     if (x == 0 ) {
         size = 15;
         time = 2;
        }
     if (y == 1) {
         level = 1;
        }
     else if (y == 2) {
         level = 2;
        } 
     else {
         level = 3;
        }
      // create projectile
      
     if (rand.nextInt(time) < 1.5 && isAlive && x==1) {
      // if it is throwing weapon, create a probability of shotting
      Projectile ball = new Projectile(size,speed,20,level,0.25,game);
      game.add(ball,getX(),getY());
      new Thread(ball).start();
     } else if(isAlive && x==0){
      // if it is shotting weapon, let it shot constantly
      Projectile ball = new Projectile(size,speed,20,level,0.4,game);
      game.add(ball,getX(),getY());
      new Thread(ball).start();
        }
    }
    
  public void weaponExplosion(){
    for (int i = 4; i >-1; i--) {
      for (int j = 1; j > -1; j--) {
              
     }
   }
   GStar star = new GStar(30, 8);
   star.setFilled(true);
   star.setFillColor(Color.BLUE);
   add(star,-size/2+50,-size/2+50);
   pause(100);
   remove(star);
   removeAll();
   isAlive= false;
 }

}