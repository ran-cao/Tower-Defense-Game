/*
 * a resursive class of tree
 */

import acm.graphics.*;
import acm.util.*;
import java.awt.*;

public class Tree extends GCompound {
  // constants
  private static final int 
    RED_SHIFT = 22, // RGB color shifts at each level
    GREEN_SHIFT = 20,
    BLUE_SHIFT = 30,
    ANGLE_SHIFT = 30; // angle changes
  
  
  public Tree(double size, double angle, Color color, RandomGenerator rand) {
    if (size > 10) {
      GPen pen = new GPen(0,0);
      pen.movePolar(size/4, angle);
      double x = pen.getX();
      double y = pen.getY();
      
      // draw the trunk using a polygon
      drawTrunk(size, angle, color);
      
      // sizes of the left and right branches are slightly different
      double leftSize = size*3/4;
      double rightSize = size*2/3;
      
      // chosen randomly which one is larger
      if (rand.nextBoolean()) {
        leftSize = size*2/3;
        rightSize = size*3/4;
      }
      
      // color changes
      Color newColor = new Color(Math.min(255, color.getRed()+RED_SHIFT), 
                                 Math.min(255, color.getGreen()+GREEN_SHIFT), 
                                 Math.min(255, color.getBlue()+BLUE_SHIFT));
      
      // add the left and right branch
      Tree right = new Tree(rightSize, 
                            angle - 30*rand.nextDouble(0.9,1.1), 
                            newColor, rand);   
      add(right, x, y);
      
      Tree left = new Tree(leftSize, 
                           angle + 30*rand.nextDouble(0.9,1.1), 
                           newColor, rand);
      add(left, x, y);
      
    }
  } 
  
  // draw a trunk rooted at the origin with given size and angle
  private void drawTrunk(double size, double angle, Color color) {
    GPolygon trunk = new GPolygon();
    
    trunk.addVertex(0,0);
    trunk.addPolarEdge(size/60, angle + 90);
    trunk.addPolarEdge(size/4, angle);
    trunk.addPolarEdge(size/30, angle -90);
    trunk.addPolarEdge(size/4, angle + 180);
    trunk.addPolarEdge(size/60, angle + 90);
    
    add(trunk);
    trunk.setFilled(true);
    trunk.setColor(color);
  }
}