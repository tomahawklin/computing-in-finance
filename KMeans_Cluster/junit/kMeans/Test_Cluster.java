package kMeans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.junit.Test;

public class Test_Cluster  extends junit.framework.TestCase {

	@Test
	public void testBadLabel() throws Exception {
		
		// Test constructor a cluster with a bad label. This should throw an exception
		
		Cluster c = new Cluster(0);
		
	}
    
	public void testConstructor() throws Exception {
		
		Cluster c = new Cluster(1);
		assertTrue(c.points.size() == 0);
		assertTrue(c.getLabel() == 1);
		assertTrue(c.centrd.getCLabel() == 1);
		assertTrue(c.centrd.getX() == 0);
		assertTrue(c.centrd.getY() == 0);
		c.setCentroid(10, 10);
		assertTrue(c.centrd.getX() == 10);
		assertTrue(c.centrd.getY() == 10);
		
	}
	
	public void testFeedPoint() throws Exception {
		
		Cluster c = new Cluster(1);
		assertTrue(c.points.size() == 0);
		assertTrue(c.getLabel() == 1);
		assertTrue(c.centrd.getX() == 0);
		assertTrue(c.centrd.getY() == 0);
		c.feedPoint(new Point(1,0));
		assertTrue(c.points.size() == 1);
		c.clearPoints();
		assertTrue(c.points.size() ==0);
		
	}
	
	public void testNullCluster() throws Exception {
		Cluster c = new Cluster(1);
		File file = new File("Test_NullCluster.txt");
	    PrintStream ps = new PrintStream(new FileOutputStream(file));
		c.printCluster(ps);
	}
}
