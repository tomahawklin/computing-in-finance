package kMeans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class KMeans {
	private int k;
	Cluster[] clusters;
	Point[] allPoints;
	private int iteration;
	boolean isFinish = false;
	PrintStream ps;
	
	public KMeans(int k, Point[] pts, PrintStream ps) {
		this.k = k;  // There are k clusters
		clusters = new Cluster[k];
		allPoints = pts;   // All the data points used in KMeans 
		iteration = 0;   // How many times has the algorithm iterated  
		this.ps = ps;   
	}
	
	public void feedPoint() throws Exception {
		for (Cluster c : clusters) {
			// The Clusters need to clear all the points they have before they are fed with points
			c.clearPoints();    
		}
		// Calculate how many points move to another cluster as the criterion of loop
		double sumChange = 0;
		for(Point p : allPoints) {
			int newLabel = 0;
			double min = Double.MAX_VALUE;
			for(int i = 0; i < k; i++) {
				double distance = Point.getDist(p, clusters[i].centrd);
				if( distance < min) {
					min = distance;
					newLabel = i + 1;
				}
			}
			if (newLabel != p.getLabel()) {
				sumChange += 1;
				p.setLabel(newLabel);
			}
			clusters[newLabel - 1].feedPoint(p);
		}
		System.out.println(sumChange + " point(s) move to another cluster.");
		iteration += 1;
		ps.println("*****Iteration: " + iteration + "*****");
		for ( Cluster c : clusters) {
			c.printCluster(ps);
		}
		if (sumChange == 0) {
			isFinish = true;
			ps.println("Iteration complete!");
		}
	}
	
	public void moveCen() throws Exception {
		for (Cluster c : clusters) {
			double sumX = 0;
			double sumY = 0;
			if (c.points.size() == 0)
				throw new Exception("Null cluster");
			for (Point p : c.points) {
				sumX += p.getX();
				sumY += p.getY();
			}
			Point newCen = new Point(sumX / c.points.size(), sumY / c.points.size());
			c.centrd.setPoint(newCen);			
		}
	}
	
	// Produce a series of random centroids
	public void randStart(Point[] dps) throws Exception {
		Random r = new Random();
		HashSet hs = new HashSet();
		while(hs.size() < k) {
			int tmp = r.nextInt(dps.length);
			hs.add(tmp);
		}
		int i = 0;
		for(Object h : hs) {
			clusters[i] = new Cluster(i + 1);
			clusters[i].centrd.setPoint(dps[(int)h]);
			i++;
		}		
	}
	
	public static void main(String[] args) throws Exception {
		
		// Produce clusters using KMeans Method
		
		File file = new File("result_KMeans.txt");
	    PrintStream ps = new PrintStream(new FileOutputStream(file));
		
		Point[] points = Point.uniSqPnt(100);
		KMeans km = new KMeans(500, points, ps);
		km.randStart(points);  
			    
		while(!km.isFinish) {
			km.feedPoint();
			km.moveCen();
			Fitness ft = new Fitness(km.clusters, km.allPoints, ps);
		}
		ps.close();
		
		// Produce clusters using KMeans2 Method
		
		File file2 = new File("result_KMeans2.txt");
	    PrintStream ps2 = new PrintStream(new FileOutputStream(file2));
		KMeans2 km2 = new KMeans2(500, points, ps2);
		km2.randStart(points);
		
		while(!km2.isFinish) {
			km2.feedPoint();
			km2.moveCen();
			Fitness ft = new Fitness(km2.clusters, km2.allPoints, ps2);
		}
		ps2.close();		
	}
}
