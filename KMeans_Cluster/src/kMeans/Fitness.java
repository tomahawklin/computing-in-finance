package kMeans;

import java.io.PrintStream;

public class Fitness {   
	
	// This class is used to estimate the fitness of clustering
	// The fitValue is the average of distance between 
	// every cluster's centroid and the points contained in this cluster
	
	Cluster[] clusters;
	Point[] points;
	double fitValue;
	
	public Fitness(Cluster[] clusters, Point[] points, PrintStream ps) {
		this.clusters = clusters;
		this.points = points;
		fitValue = 0;
		double sumDist = 0;
		for(Point p : points) {
			sumDist += Point.getDist(p, clusters[p.getLabel() - 1].centrd);
		}
		fitValue = sumDist / points.length;
		ps.println("The fitValue is " + fitValue);
	}
	

}
