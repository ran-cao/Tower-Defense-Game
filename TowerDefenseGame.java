import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.awt.*;
import java.awt.event.*;
/**
 * TowerDefenseGame.java <p>.
 * 
 * The player defends the tower by buying defensive weapons and adding on the tower. 
 * Initially, there are two weapons: a shotting one and a throwing one. The weapons can be made by gaining the scores, 
 * which were earned by hitting the enemies. Once added, the weapons can be upgraded with the credits.
 * If the weapons are on level 2, weapons shot yellow projectiles. When the projectile hits the doge, it splits into three smaller red
 * balls; If the weapon are on level 3, the weapons shot blue projectiles. When the projectile hits the doge, it splits into three smaller
 * yellow balls and those yellow balls further split into three red balls.
 * Once the doge reaches the tower, the number of weapon will decreased by one from bottom to the top. 
 * The aim is to defend the base and kill the boss: if no weapon left and doge reaches the terminal point, game lost; if the big boss (which 
 * shows up when the score excesses 1100) is killed, game win.
 * plus:every time when the weapon is created, the fire cost doubled. But the fire cost cannot excess 1200.
 *      every time when the weapon is upgraded, the upgrade cost doubled. But the upgrade cost cannot excess 1200.
 * 
 *                        
 * Ran Cao
 */

public class TowerDefenseGame extends GraphicsProgram {
  // constant
  public static final int
    APPLICATION_WIDTH = 1000,
    APPLICATION_HEIGHT = 600,
    ROOM_WIDTH = 100,
    ROOM_HEIGHT = 100,
    TOP_GAP = 50;
    
  public static final double
    speed = 0.3;
    
  // instance variable
  private GRect [][] room = new GRect[5][2]; // array of rooms
  private Doge[] doge = new Doge[101];
  private GRect[] level = new GRect[3];
  private Weapon[][] weapon = new Weapon[5][2];
  private GImage shotting;
  private GImage throwing;
  private GImage shot;
  private GImage cast;
  private GImage draggedWeapon;
  private GRect throwingLabel;
  private GRect shottingLabel;
  private GRect lawn;
  private GLabel start;
  private GLabel credit;
  private GLabel upgradeCost;
  private GLabel weaponCost;
  private GLabel lose;
  private GLabel win;
  private GLabel instruction;
  private GLabel approaching;
  private GPoint lastPoint;
  private SoundClip begin, process,fail;
  public boolean gameOver;
  public boolean gameWin = false;
  private boolean isDragging;
  private boolean isShotting; // if the type is shotting 
  private boolean isThrowing;
  public boolean isBoss = false;
  private boolean isLevel1;
  private boolean isLevel2;
  private boolean isLevel3;
  private boolean dogeWithBoss=false;
  private boolean[][] fireWeapon = new boolean[5][2]; // whether the room contains weapon or not 
  private double angle;
  private int fireCost = 200;
  private int upgrade = 300;
  private int hurt;
  private int score = 0;
  private int life = 1000;
  private RandomGenerator rand = RandomGenerator.getInstance();
  
  // intially draw the method
  public void init() {
      addMouseListeners();
      // use a two-dimentional array to draw the weapon
      for (int i = 0; i < 5; i++) {
          for (int j = 0; j < 2; j++) { 
           fireWeapon[i][j] = false;
          }
        }
      drawGraphics();
      // add the start sound initially 
      begin = new SoundClip("crazy-dave-in-game.wav");
      begin.setVolume(1);
      begin.play();
    }
  
  public void run(){
      // if game is not win
     if (!gameWin||!gameOver) {
     waitForClick();
     begin.stop();//stop the initial music     
     start.setVisible(false);// set the visibility of start label
     createDoge(); 
     createBigBoss();
     // add the processing music
     process = new SoundClip("graze-the-roof.wav");
     process.setVolume(1);
     process.loop(); // use a loop for creating the music 
     // use a while loop to create weapon
     while (true) {  
      for (int i = 0; i < 5; i++) {
          for (int j = 0; j < 2; j++) { 
              // if there is a weapon, create projectile along with it
           if (fireWeapon[i][j]) {
           weapon[i][j].ball(); 
           pause(50);
          }
        }       
      }
    }
    }
  }
  
  public void mousePressed(GPoint point) {
    if (shot.contains(point)) { // when the shotting weapon is pressed on 
      isDragging = true;
      lastPoint = point;
      draggedWeapon = shot;
      isShotting = true;
    } else if (cast.contains(point)) { // when the throwing weapon is pressed on
      isDragging = true;
      lastPoint = point;
      draggedWeapon = cast;
    } 
    // within the tower base
    for (int i = 0; i < 5; i++) {
          for (int j = 0; j < 2; j++) {
        // if the room contains weapon
       if (fireWeapon[i][j]) { 
          // if the score excesses upgrade cost and when weapon contains mousepoint
        if(score>upgrade&&weapon[i][j].contains(point)){
          // when the weapon is on level 1
         if(weapon[i][j].getLevel() == 1) {
            weapon[i][j].setLevel(2); //change it to level 2
            upgrade = upgrade*2; // double the upgrade cost
            weapon[i][j].level2.setFillColor(Color.RED); // change the level bar's color from green to red 
            score = score - upgrade; // change the score after minoring the upgrade cost
         } else if (weapon[i][j].getLevel()==2){
            score = score - upgrade;// change the score after minoring the upgrade cost
            weapon[i][j].setLevel(3);//change it to level 3
            upgrade = upgrade*2;// double the upgrade cost
            weapon[i][j].level3.setFillColor(Color.RED);// change the level bar's color from green to red 
          }
        }
       }
      }
     }
     updateScore(); // update the score
    }
  
    
  public void mouseDragged(GPoint point) {
      // if the score is smaller than the fire cost, will not go futher 
    if(score<fireCost) return;
    if (isDragging){ // drag the chosen draggedweapon
        draggedWeapon.move(point.getX()-lastPoint.getX(),
                       point.getY()-lastPoint.getY());
        lastPoint = point; // change the last point of the weapon
    }     
  }
  
  /** fire the cannon */
  public void mouseReleased(GPoint point) {
    if(isDragging){ // for the chosen draggedweapon
        if(score<fireCost) return; // if the score is smaller than the fire cost, will not go futher 
       for (int i = 0; i < 5; i++) {
          for (int j = 0; j < 2; j++) {
           // if the room contains mouse point 
           if (room[i][j].contains(point)) {
             // if the room does not contain mouse point 
            if(!fireWeapon[i][j]){
                // if the dragged weapon is shotting weapon
              if (draggedWeapon.equals(shot)){
                createWeapon(0, 1, i, j); // create a shotting weapon
                fireCost = fireCost*2; // double the fire cost 
                fireWeapon[i][j] = true; // set the room contains a weapon that cannot create other weapon inside
                score = score - fireCost; // update the score 
             }
             // if the dragged weapon is throwing weapon
             if (draggedWeapon.equals(cast)) {
                createWeapon(1, 1, i, j);//create a throwing weapon
                fireCost = fireCost*2;// double the fire cost
                fireWeapon[i][j] = true; // set the room contains a weapon that cannot create other weapon inside
                score = score - fireCost; // update the score 
             }         
           }   
        }
       }
    }
  }      
  shot.setLocation(665,15); // set the instruction label
  cast.setLocation(550,17); // set the instruction label
  isDragging = false; // after mouse released, stop dragging
  updateScore(); // update the score
 }
    
  public void checkCollision(Doge doge){
     // when the doge reaches terminal point, game lose
     if(doge.getX()<0){
             gameLose();
       }    
       // check the collision between doge and tower 
       // decrease the number of weapon from bottom to the top
          for (int i = 4; i > -1; i--) {
          for (int j = 0; j < 2; j++) { 
          if(doge.getX()<260){ // when doge reaches tower 
              if (fireWeapon[i][j]) {
              fireWeapon[i][j] = false; // the room no longer contains weapon
              weapon[i][j].weaponExplosion(); // decrease number of weapon by 1
              doge.die(); // call doge die in doge class
              fireCost = fireCost/2; // change the fire cost 
              if(weapon[i][j].getLevel() == 3){ // if the level of exploed weapon is 3
                  upgrade = fireCost/4; 
                } else if(weapon[i][j].getLevel() == 2){ // if the level of exploed weapon is 2
                    upgrade = upgrade/2;
                }
              updateScore(); // update score
              return; // for only decrease the number of weapon once
            }
            }
        }
     }
   
 }

 public void checkCollision(Projectile ball) { 
     // doge[0,100]is small doge and doge[101]is big boss
    for(int b = 0; b<101 ;b++) {
        // calculate the distance between doge and ball
     double dist = GMath.distance(doge[b].getX(), 
                                 doge[b].getY(), 
                                 ball.getX(), 
                                 ball.getY());
       // calculate the angle between doge and ball
     double mirrorAngle = GMath.angle(doge[b].getX(),
                                        doge[b].getY(),
                                        ball.getX(),ball.getY())+180;
     if(doge[b].isAlive() && ball.getBounds().intersects(doge[b].getBounds())){
        ball.die(); // kill the projectile
        //ball.explode(doge[b].getX(),doge[b].getY());
        ball.setAngle(2*mirrorAngle-ball.getAngle());  // projectile bounces
        // decrease the life span differently when different weapons shot on the doge
        double lifeSpan = doge[b].lifeSpan();
       if (ball.getType() == 1){
         hurt = 5; // if the shotting weapon hits the doge, decrease the life span by 5
        } else if (ball.getType() == 2) {
         hurt = 20;// if the throwing weapon hits the doge, decrease the life span by 5
       }
       lifeSpan = lifeSpan - hurt; // update the life span
       doge[b].setlifeSpan(lifeSpan); 
       if(lifeSpan<0){ 
           if(!doge[b].isBigBoss){ // if the doge is not big boss
           doge[b].die(); 
        } else if(doge[b].isBigBoss){// if the doge is big boss
            doge[b].die();
            gameWin(); 
        }
        }
        updateScore(); // increase the credit score
        
        // initially the height is decreased by 50 from the doge's y coordination
        int a = 50;
        // create three smaller balls when the weapon is on level 2 or 3
       if (ball.getLevel() == 2) {  // create three red balls with different angle
           if (doge[b].isBigBoss) {
               a = 90;    // if it is the bigboss, the height is decreased by 90 from the doge's y coordination
            }
           Projectile ball1 = new Projectile(ball.getballSize()/2,2,30,1,0.3,this);
           add(ball1,doge[b].getX(),doge[b].getY()-a);
           new Thread(ball1).start();
           Projectile ball2 = new Projectile(ball.getballSize()/2,2,150,1,0.3,this);
           add(ball2,doge[b].getX(),doge[b].getY()-a);
           new Thread(ball2).start();
           Projectile ball5 = new Projectile(ball.getballSize()/2,2,90,1,0.3,this);
           add(ball5,doge[b].getX(),doge[b].getY()-a);
           new Thread(ball5).start();
        }
       if (ball.getLevel() == 3) {   // create three yellow balls with different angle
           if (doge[b].isBigBoss) {
               a = 90;
            }
           Projectile ball3 = new Projectile(ball.getballSize()*2/3,1.5,30,2,0.3,this);
           ball3.setFillColor(Color.YELLOW);
           add(ball3,doge[b].getX(),doge[b].getY()-a);
           new Thread(ball3).start();
           Projectile ball4 = new Projectile(ball.getballSize()*2/3,1.5,150,2,0.3,this);
           ball4.setFillColor(Color.YELLOW);
           add(ball4,doge[b].getX(),doge[b].getY()-a);
           new Thread(ball4).start();
           Projectile ball6 = new Projectile(ball.getballSize()*2/3,1.5,90,2,0.3,this);
           ball6.setFillColor(Color.YELLOW);
           add(ball6,doge[b].getX(),doge[b].getY()-a);
           new Thread(ball6).start();
        }
      }
     
      }
     // when the ball reaches the lawn, remove it
      if (ball.getBounds().intersects(lawn.getBounds())) {
          ball.die();
        }
  }
  
 
  // draw graphic
  private void drawGraphics(){
    double width = getWidth();
    double height = getHeight();
    // draw the lawn
    lawn = new GRect(getWidth(),80);
    lawn.setFilled(true);
    lawn.setColor(Color.GREEN);
    add(lawn,0,550);
    // draw the grass
    double bladeX = 0;
    while (bladeX < 1000) {
     GLine blade = new GLine(bladeX, 535, bladeX, 550);
     add(blade);
     blade.setColor(Color.green);
     bladeX += 3;
    } 
    // draw four trees
    Tree tree1 = new Tree(180, 90, new Color(0,0,0), rand);
    add(tree1, 550, 550);    
    Tree tree2 = new Tree(320, 90, new Color(0,0,0), rand);
    add(tree2, 720, 550);    
    Tree tree3 = new Tree(160, 90, new Color(0,0,0), rand);
    add(tree3, 900, 550);   
    Tree tree4 = new Tree(500, 90, new Color(0,0,0), rand);
    add(tree4, 1100, 550);    
    // draw the start label
    start = new GLabel("Click to start.");
    start.setFont(new Font("Sanserif", Font.BOLD, 30));
    start.setColor(Color.RED);
    add(start, getWidth()/2, height/2-start.getHeight()/2);
    // draw the lose label
    lose = new GLabel("You Lose!");
    lose.setFont(new Font("Sanserif", Font.BOLD, 40));
    lose.setColor(Color.RED);
    add(lose, getWidth()/2+50, getHeight()/2-100);
    lose.setVisible(false);
    // draw the won label
    win = new GLabel("You Win!");
    win.setFont(new Font("Sanserif", Font.BOLD, 40));
    win.setColor(Color.RED);
    add(win, getWidth()/2, height/2-win.getHeight()/2);
    win.setVisible(false); 
    // draw the credit label
    credit = new GLabel("Credit:"+score);
    credit.setFont(new Font("Sanserif", Font.BOLD, 10));
    credit.setColor(Color.BLACK);
    add(credit, 800, credit.getHeight()*2);
    // draw the upgrade cost label
    upgradeCost = new GLabel("Upgrade cost:"+upgrade);
    upgradeCost.setFont(new Font("Sanserif", Font.BOLD, 10));
    upgradeCost.setColor(Color.BLACK);
    add(upgradeCost, 800, upgradeCost.getHeight()*4);
    // draw the upgrade cost label
    weaponCost = new GLabel("Weapon cost:"+fireCost);
    weaponCost.setFont(new Font("Sanserif", Font.BOLD, 10));
    weaponCost.setColor(Color.BLACK);
    add(weaponCost, 800, weaponCost.getHeight()*6); 
    //instruction
    instruction = new GLabel("When available, drag weapon to empty base or click weapon to upgrade");
    instruction.setFont(new Font("Sanserif",Font.BOLD,10));
    instruction.setColor(Color.RED);
    add(instruction, 0, instruction.getHeight()*2);
    instruction.setVisible(false);
    // draw the boss approaching label
    approaching = new GLabel("WARNING BOSS APPROACHING!");
    approaching.setFont(new Font("Sanserif", Font.BOLD, 30));
    approaching.setColor(Color.RED);
    add(approaching, getWidth()/2, height/2-approaching.getHeight()/2);
    approaching.setVisible(false);
    // create room
    createRoom();
    createWeapon(0, 1, 2, 1);
    createWeapon(1, 1, 3, 1);
    createLabel();
    // create weapons image on the label
    shot = new GImage("3.gif"); 
    shot.setSize(80,80);
    add(shot,665,15);
    cast = new GImage("4.gif");
    cast.setSize(130,90);
    add(cast,550,17);
 } 
    
    // draw the weapon labels which show on the top right 
    private void createLabel(){
    throwingLabel = new GRect(80,70);
    throwingLabel.setFilled(true);
    throwingLabel.setColor(Color.RED);
    add(throwingLabel,580,13);
    shottingLabel = new GRect(80,70);
    shottingLabel.setFilled(true);
    shottingLabel.setColor(Color.RED);
    add(shottingLabel,670,13);    
 }

  // create ten rooms to put each weapon
  private void createRoom(){
   for (int i = 0; i < 5; i++) {
   for (int j = 0; j < 2; j++) {
    double x = 0+j*160; // gap between two column
    double y = TOP_GAP + i*ROOM_HEIGHT; 
    room[i][j] = new GRect(ROOM_WIDTH, ROOM_HEIGHT);
    room[i][j].setFilled(true);
    room[i][j].setFillColor(this.rand.nextColor());
    add(room[i][j],x,y);
   }
  }
 }
 
 //create weapons 
 private void createWeapon(int x, int y, int i, int j){
     fireWeapon[i][j] = true;
     weapon[i][j] = new Weapon(100, x, y, this);
     add(weapon[i][j]);
     weapon[i][j].setLocation(room[i][j].getX()+ROOM_WIDTH/2, room[i][j].getY()+ROOM_HEIGHT/2);
    }   

 //update the scores
 private void updateScore(){
    // change the score everytime ball hits the doge
    score = score + hurt;
    credit.setLabel("Credit:"+score); // update credit label
     if(score>1100 && !gameWin){
      approaching.setVisible(true);
      isBoss = true;
    }
    upgradeCost.setLabel("Upgrade cost:" + upgrade);// update upgrade label
    weaponCost.setLabel("Weapon cost:" + fireCost);// update weapon cost label
    
    // when the firecost excesses 1000, make it unchanged
    if(fireCost>1200){
      fireCost = 1200;
    }
    // when the upgrade excesses 1000, make it unchanged
    if(upgrade>1200){
        upgrade = 1200;
    }
    // when the score is smaller than 0, make it equals to 0
    if(score<0){
        score = 0;
    }
    
    // when the score excesses firecost, set those two top right labels to be green ; if not, remain in red color
    if(score>fireCost){
         shottingLabel.setFillColor(Color.GREEN);
         throwingLabel.setFillColor(Color.GREEN);
        } else{
         shottingLabel.setFillColor(Color.RED);
         throwingLabel.setFillColor(Color.RED);
        }
    for (int i = 0; i < 5; i++) {
          for (int j = 0; j < 2; j++) { 
      if (fireWeapon[i][j]) { // if the room contains weapon
        // when the score excesses upgrade cost and the weapon is on level 1, change the color to be green
      if(score>upgrade&&weapon[i][j].getLevel() == 1){
        weapon[i][j].level2.setFillColor(Color.GREEN);
       } else if (weapon[i][j].getLevel() != 2 && weapon[i][j].getLevel() != 3) {  // after clicking, change to gray color
        weapon[i][j].level2.setFillColor(Color.GRAY);
       }
       // when the score excesses upgrade cost and the weapon is on level 2, change the color to be green
      if(score>upgrade&&weapon[i][j].getLevel() == 2){
        weapon[i][j].level3.setFillColor(Color.GREEN);
       } else if (weapon[i][j].getLevel() != 3) {  // after clicking, change to gray color
        weapon[i][j].level3.setFillColor(Color.GRAY);
       }
     }
     } 
    }
    }
    
 // if game is lost
  private void gameLose(){
    //  if game is not lost, return it 
    if (!gameOver){
    gameOver = true; // set the status to be true
    lose.setVisible(true); // change lose label visibility to be true
    instruction.setVisible(false);// change instruction label visibility to be true
    process.stop(); // stop the processing music
    // gamelose music
    fail = new SoundClip("choose-your-seeds.wav");
    fail.setVolume(1);
    fail.play();
    // add the happy doge pic
    GImage happy1 = new GImage("11.gif");
    add(happy1,350,200);
    GImage happy2 = new GImage("2.gif");
    add(happy2,450,200);
   }
 }
   
 // game win
 private void gameWin(){
     if(!gameWin) {
     win.setVisible(true);
     approaching.setVisible(false);
     gameWin = true;
    }
 }
    
   // create doge
 private void createDoge(){
   for(int b = 0; b<100; b++){
    doge[b] = new Doge(new GImage("doge.gif"), false, speed*rand.nextInt(1,12), this);
    doge[b].setLocation(1100+b*(rand.nextInt(1000)+1), 550 - doge[b].getHeight() / 2);
    add(doge[b]);
    new Thread(doge[b]).start();
   }   
 }  
 
  // create final big boss
 private void createBigBoss(){
    if (!isBoss) {
    doge[100] = new Doge(new GImage("9.gif"),true,0.3,this);
    doge[100].setLocation(1000+doge[100].getWidth(),550 - doge[100].getHeight() / 2);
    add(doge[100],1100,550 - doge[100].getHeight() / 2);
    new Thread(doge[100]).start();
    
   }
 }

}
