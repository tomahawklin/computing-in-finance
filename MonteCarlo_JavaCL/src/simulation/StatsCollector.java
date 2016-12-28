package simulation;

import java.util.ArrayList;

/**
 *  This class collects double values into a list
 *  and reports the average and sample 
 *  standard deviation
 * @author lin
 *
 */

public class StatsCollector {
	
	private double avg;
	private double sumSquare;
	private double std;
	private ArrayList<Double> numbers;
	
	public StatsCollector() {
		this.numbers = new ArrayList<Double>();
		avg = 0;
		sumSquare = 0;
		std = 0;
	}
	
	public void add(double x) {
		int n = numbers.size() + 1;
		numbers.add(x);
		avg = (avg * (n - 1) + x) / n;
		sumSquare += x * x;
		// We calculate the sample sd, not population sd
		std = Math.sqrt((sumSquare / n  - avg * avg) / (n -1) * n);
	}
	
	public double getAvg() {	return avg; }
	
	public double getStd() {	return std; }
	
}
