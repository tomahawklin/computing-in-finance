package kMeans;

import org.junit.Test;

public class Test_Point extends junit.framework.TestCase {

	@Test
	public void testConstructor() {
		
		// Instantiate a point and make sure it does not belong to any cluster yet
		
		Point pt1 = new Point(1, 0);
		assertTrue(pt1.getX() == 1);
		assertTrue(pt1.getY() == 0);
		assertTrue(pt1.getLabel() == 0);
		assertTrue(pt1.getPrevLabel() == 0);
		
	}
	
	public void testUniformSquare() {
		
		// Test the coordinate of some initiated points
		
		Point[] points = Point.uniSqPnt(10);
		assertTrue(points[0].getX() == 0);
		assertTrue(points[0].getY() == 0);
		assertTrue(points.length == 100);
		assertTrue(points[99].getX() == 9);
		assertTrue(points[99].getX() == 9);
		
	}
	
	public void testDistance() {
		
		// Compute the distance between two points
		
	    Point pt1 = new Point(1, 0);	
		Point pt2 = new Point(4, 4);
		assertTrue(Point.getDist(pt1,pt2) == 5);
		Point pt3 = new Point(1, 0);
		assertTrue(Point.getDist(pt1, pt3) == 0);
		
	}
	
    public void tests() throws Exception {
		
		// Several tests
		
	    Point pt1 = new Point(1, 0);	
		Point pt2 = new Point(4, 4);
		pt1.setPoint(pt2);
		assertTrue(pt1.getX() == pt2.getX());
		assertTrue(pt1.getY() == pt2.getY());
		
		pt1.setLabel(10);
		pt1.setPrevLabel(10);
		assertTrue(pt1.getLabel() == 10);
		assertTrue(pt1.getPrevLabel() == 10);
		pt1.clearLb();
		assertTrue(pt1.getLabel() == 0);
		assertTrue(pt1.getPrevLabel() == 10);
		
	}
	
}
