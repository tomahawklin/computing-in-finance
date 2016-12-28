package kMeans;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Random;

public class KMeans2 {
	private int k;
	Cluster[] clusters;
	Point[] allPoints;
	private int iteration;
	boolean isFinish = false;
	PrintStream ps;
	
	public KMeans2(int k, Point[] pts, PrintStream ps) {
		this.k = k;
		clusters = new Cluster[k];
		allPoints = pts;
		iteration = 0;
		this.ps = ps;
	}
	
	public void moveCen() throws Exception {
		for (Cluster c : clusters) {
			if (c.points.size() == 0)
				throw new Exception("Null cluster");
			double sumX = 0;
			double sumY = 0;
			for (Point p : c.points) {
				sumX += p.getX();
				sumY += p.getY();
			}
			Point newCen = new Point(sumX / c.points.size(), sumY / c.points.size());
			c.centrd.setPoint(newCen);			
		}
	}
	
	public void feedPoint() throws Exception {
		for (Point p : allPoints) {
			// The Clusters need to clear all the points they have before they are fed with points
			p.clearLb();    
		}
		// Calculate how many points move to another cluster as the criterion of loop
		double sumChange = 0;
		for(Cluster c : clusters) {
			c.clearPoints();
			while (c.points.size() < (allPoints.length / k)) {
				int pointIndex = 0;
				double min = Double.MAX_VALUE;
				for(int i = 0; i < allPoints.length; i++) {
					if (allPoints[i].getLabel() != 0) {
						continue;
					}
					double distance = Point.getDist(allPoints[i], c.centrd);
					if( distance < min) {
						min = distance;
						pointIndex = i;
					}
				}
				if (c.getLabel() != allPoints[pointIndex].getPrevLabel()) {
					sumChange += 1;					
				}
				allPoints[pointIndex].setLabel(c.getLabel());
				allPoints[pointIndex].setPrevLabel(c.getLabel());
				c.feedPoint(allPoints[pointIndex]);	
			}
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
}
