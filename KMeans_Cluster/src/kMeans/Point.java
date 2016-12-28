package kMeans;

public class Point {
	
	// A point's label ranges from 0 to k. 0 means this
	// point has not been assigned to any cluster yet
	
	private int label;   
	private int prevLabel;   
	private double x;
	private double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
		label = 0; 
		prevLabel = 0;
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public int getLabel() {
		return this.label;
	}
	
	public void setLabel(int label) throws Exception {
		// Error check - label can't be less than zero
		if (label < 0)
			throw new Exception("Bad label");
		this.label = label;
	}
	
	public int getPrevLabel() {
		return this.prevLabel;
	}
	
	public void setPrevLabel(int label) throws Exception {
		// Error check - PrevLabel can't be less than zero
		if (label < 0)
			throw new Exception("Bad Prevlabel");
		this.prevLabel = label;
	}
	
	public void setPoint(Point p) {
		setX(p.getX());
		setY(p.getY());		
	}
	
	// Produce n * n points uniformly on a plane in a square
	static public Point[] uniSqPnt(int n) {		
		Point[] points = new Point[n * n];
		int cnt = 0;
		for(int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				points[cnt] = new Point(i, j);
				cnt++;
			}
		}
		return points;		
	}
	
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	
	static double getDist(Point p1, Point p2) {	
		return Math.sqrt((p1.getX() - p2.getX()) * (p1.getX() - p2.getX()) + (p1.getY() - p2.getY()) * (p1.getY() - p2.getY()));
	} 
	
	public void clearLb() {
		this.label = 0;
	}
}
