import acm.graphics.*;

/**
 * GStar.java <p>
 * 
 * a class for n-point star polygons  <p>
 */
public class GStar extends GPolygon {
  /** 
   * a constructor
   * @param    radius  the distance from center to star point.
   * @param    n       the numbr of points
   */
  public GStar(double radius, int n) {
    GPen pen = new GPen(0, 0); // use a pen to trace the vertices
    
    // use a loop to add the vertices
    for (int i = 0; i < n; i++) {
      // add a point on the outer circle
      pen.setLocation(0, 0);
      pen.movePolar(radius, 90+i*360/n);
      addVertex(pen.getX(), pen.getY());
      
      // add a point on the inner circle
      pen.setLocation(0, 0);
      pen.movePolar(radius/2, 90+i*360/n+180/n);
      addVertex(pen.getX(), pen.getY());
    }
    markAsComplete(); // complete the construction
  }
  
  /** 
   * a constructor
   * @param    radius  the distance from center to star point.
   * @param    n       the numbr of points
   * @param    x       the x-coordinate of the center
   * @param    y       the y-coordinate of the center
   */
  public GStar(double radius, int n, double x, double y) {
    this(radius, n); // create a star
    setLocation(x, y); // set the location of the star
  }
}