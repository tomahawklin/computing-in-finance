package kMeans;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Cluster {
	public Centroid centrd;  // the cluster's centroid
	private int label;    // label ranges from 1 to k
	public List<Point> points;   // points that belong to this cluster

	public Cluster(int label) throws Exception {
		// Error check - lebel can't be less than or equal to zero
		if (label <= 0) 
			throw new Exception("Bad label");
		this.label = label;
		centrd = new Centroid(0,0);
		centrd.setCLabel(label);
		points = new ArrayList();
	}
	
	public void feedPoint(Point p) {
		points.add(p);
	}
	
	public void clearPoints() {
		points.clear();
	}
	
	public int getLabel() {
		return this.label;
	}
	
	public void setCentroid(double x, double y) {
		centrd.setX(x);
		centrd.setY(y);
	}
	
	public void printCluster(PrintStream ps) {
		ps.println("Cluster: " + label);
		ps.println("Centroid: " + centrd);
		ps.println("Points:");
		for(Point p : points) {
			ps.print(p + "\t");
		}
		ps.println();
	}
	
}
