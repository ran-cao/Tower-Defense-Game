import acm.program.*;
import acm.graphics.*;
import java.awt.*;
/**
 * Projectile.java <p>
 *
* A class for balls shot from cannon in BouncyCannonBallGame. <p>
*/
public class Projectile extends GCompound implements Runnable{
 //constants
 private static final double DELAY = 20;
 public static double
 GRAVITY = 0.3;
 // instance variable
 private TowerDefenseGame game; // the main game
 private double size; // size of ball
 private double level;
 private double gravity;
 private double speed, angle; // speed and direction of movement
 private boolean isAlive = true; // condition of ball, is alive or not
 private GOval ball;
 
 /** the constructor, create the ball */
 public Projectile(double size, double speed, double angle, double level, double gravity,
                  TowerDefenseGame game) {
  // save the parameters in instance variables
  this.game = game;
  this.size = size;
  this.angle = angle;
  this.speed = speed;
  this.level = level;
  this.gravity = gravity;
  // create the ball centered at the local origin
  ball = new GOval(-size/2, -size/2, size, size);
  add(ball);
  ball.setFilled(true);
  if(level == 1){ //if the weapon is on level1, the projectile's color is red
     ball.setFillColor(Color.RED);
    } else if (level == 2){ //if the weapon is on level2, the projectile's color is yellow
     ball.setFillColor(Color.YELLOW);  
    } else if (level == 3){ //if the weapon is on level3, the projectile's color is blue
     ball.setFillColor(Color.BLUE);
    }
 }

 /** the run method, to animate the ball */
 public void run() {
  while (isAlive) {
   oneTimeStep();
   pause(DELAY);
  }
  explode(); // when the ball is not alive, explode and disappear
 }
 
/** return the angle of movement */
public double getAngle() {
  return angle;
}

/** change the angle of movement when bounce */
public void setAngle(double angle) {
  this.angle = angle;
}

/**return the speed of movement, since the form of new balls need old speed*/
public double getSpeed(){
    return speed;
}

/**main class calls the die method in projectile method*/
public void die(){
    isAlive = false;
}

/**main class calls the setfillcolor method in projectile method*/
public void setFillColor(Color color) {
    ball.setFillColor(color);
}

/**main class calls the setsize method in projectile method*/
public void setSize(double x, double y) {
    ball.setSize(x,y);
}

//return to the main class
public int getType() {
   if (size == 30) {
      return 2;
    }
   else {
      return 1;
    }
}

//return to the main class
public double getballSize() {
    return size;
}

//return to the main class
public double getLevel() {
    return level;
}

 // show explosion and disappear
private void explode() {  
   removeAll();// remove the ball  
} 

// in each time step, move the ball and bounce if hit the wall
private void oneTimeStep() {
  ball.movePolar(speed, angle);// move the ball
  game.checkCollision(this);// check collision to see if the balls hit anything
  applyGravity(); // apply gravity
  
  }

// apply the effect of gravity
private void applyGravity(){
    // calculate xSpeed and ySpeed
    double xSpeed = speed*GMath.cosDegrees(angle);
    double ySpeed = -speed*GMath.sinDegrees(angle);
    ySpeed += gravity; // apply gravity to ySpeed
    //calculate new speed and angle
    speed = GMath.distance(xSpeed, ySpeed);
    angle = GMath.angle(xSpeed, ySpeed);
} 
}