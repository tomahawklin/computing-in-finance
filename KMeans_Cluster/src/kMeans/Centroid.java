package kMeans;

import java.util.Random;

public class Centroid extends Point {
	
	// As centroid is a special point, its label and 
	// prevLabel are always 0. While it has a centLabel.
	
	private int CentLabel;  // Label ranges from 1 to k

	public Centroid(double x, double y) {
		super(x, y);	
	}
	
	public int getCLabel() {
		return this.CentLabel;
	}
	
	public void setCLabel(int label) {
		this.CentLabel = label;
	}
	
}
