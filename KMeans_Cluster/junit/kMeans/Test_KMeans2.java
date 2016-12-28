package kMeans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.junit.Test;

public class Test_KMeans2 extends junit.framework.TestCase {

	@Test
	public void test() throws Exception {
		
		// This test iterates only two times and outputs a .txt file to help 
		// examine the status before and after the iteration 
		
		File file = new File("test_KMeans2.txt");
	    PrintStream ps = new PrintStream(new FileOutputStream(file));
	    Point[] points = Point.uniSqPnt(5);
		KMeans2 km2 = new KMeans2(5, points, ps);
		km2.randStart(points);
		
		ps.println("Initial points:");
		for (Point p : km2.allPoints) {
			ps.println(p);
		}
		
		ps.println("Initial centroids:");
		for (Cluster c : km2.clusters) {
			ps.println(c.centrd);
		}
		
		km2.feedPoint();
		km2.moveCen();
		Fitness ft = new Fitness(km2.clusters, km2.allPoints, ps);
		km2.feedPoint();
		
		ps.close();
		
	}

}
