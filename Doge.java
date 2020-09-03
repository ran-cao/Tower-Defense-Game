import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.awt.*;
import java.awt.event.*;
/**
 * Write a description of class Turtle here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Doge extends GCompound implements Runnable
{
    // constants
    private static final double DELAY = 20;    
    // instance variables
    private double speed;
    private GImage doge;
    private GRect indicator;
    private TowerDefenseGame game;
    private boolean isAlive = true;
    public boolean isBigBoss;
    private RandomGenerator rand = RandomGenerator.getInstance();
    private double lifeSpan;
    
   /*** Constructor for objects of class Doge*/
    public Doge(GImage doge, boolean isBigBoss, double speed, TowerDefenseGame game)
    {
        // save the primaters
        this.doge = doge ;
        this.isBigBoss = isBigBoss;
        this.speed = speed;
        this.game = game;
        add(doge, -doge.getWidth() / 2, -doge.getHeight() / 2);
        // add the life indicator above
        indicator();
    }

   public void run(){      
       while (isAlive && !game.gameOver) { // when the doge is alive and the main game is not over
        OneTimeStep();
        pause(DELAY);
    }      
    dead(); // if doge is not alive, call dead method
    }
    
   // return isAlive to the main class
   public boolean isAlive(){
       return isAlive;
    }
    
   // return  to the main class
   public boolean isBigBoss(){
       return isBigBoss;
    }
    
   // return to the main class
   public double lifeSpan(){
       return lifeSpan;
    }
   
    // let the main class use setlifespan method
   public void setlifeSpan(double x) {
       lifeSpan = x;
    }

    // let the main class call the die method
   public void die(){
       isAlive = false;
    }
    
      // in each time step,
   private void OneTimeStep(){
      if(game.gameWin)return;
       if (!isBigBoss) {// if it is not the big boss
        move(-speed,0); // move the doge
        game.checkCollision(this); // check if hit anything
        indicator.setSize(lifeSpan,10);
      }
      if (isBigBoss) { // if it is big boss
        if (game.isBoss) {   // in main class, when the time big boss coming
        move(-speed,0); // move the doge
        game.checkCollision(this); // check if hit anything
        indicator.setSize(lifeSpan/6,10); // set the indicator of bigboss
       }
        }
    }
    
    // set the life of doge
   private void indicator(){
        // set different life span for big boss and normal doge 
        if(isBigBoss){
            lifeSpan = 900;
        }  else{
            lifeSpan = 20*(rand.nextInt(3)+1); // random lifespan for normal doge
        }
        indicator = new GRect(lifeSpan,10);
        indicator.setFilled(true);
        if(isBigBoss){ 
             indicator = new GRect(lifeSpan/6,10);
             indicator.setFilled(true);
             indicator.setFillColor(Color.BLUE); // if it is big boss, the indicator is blue 
        }  else{
            indicator = new GRect(lifeSpan,10);
            indicator.setFilled(true);
            indicator.setFillColor(Color.RED); // if it is normal doge, the indicator is red  
        }
        add(indicator, doge.getX(),doge.getY()-8);
   }
   
    // remove the doge and change the pic
   private void dead() {    
    removeAll(); // remove the previous doge pic    
    // draw an cry pic
    if (!isBigBoss) {
     GImage cryingDoge = new GImage("7.gif");
     add(cryingDoge,-doge.getWidth() / 2, -doge.getHeight() / 2);
     pause(500);
     removeAll();
    }
   }
   
}
