package kMeans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import kMeans. KMeans;

public class Test_KMeans extends junit.framework.TestCase {

	public void test1() throws Exception {
		

		// This test iterates only two times and outputs a .txt file to help
		// examine the status before and after the iteration 
		
		File file = new File("test_KMeans.txt");
	    PrintStream ps = new PrintStream(new FileOutputStream(file));
		
		Point[] points = Point.uniSqPnt(5);
		KMeans km = new KMeans(5, points, ps);
		km.randStart(points);		
		
		ps.println("Initial points:");
		for (Point p : km.allPoints) {
			ps.println(p);
		}
		
		ps.println("Initial centroids:");
		for (Cluster c : km.clusters) {
			ps.println(c.centrd);
		}
			    
		km.feedPoint();
		km.moveCen();
		Fitness ft = new Fitness(km.clusters, km.allPoints, ps);
		km.feedPoint();
	}
}
