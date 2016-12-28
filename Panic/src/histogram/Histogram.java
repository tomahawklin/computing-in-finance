package histogram;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class generates pseudo histogram by printing
 * values on x and y axis into separate txt files
 * 
 * @author lin
 *
 */
public class Histogram {
	private List<Double> values;
	private int num_of_stocks;
	private int num_of_bins;
	private double width;
	private double minVal;
	private double maxVal;
	private List<Double> xVals;
	private List<Double> yVals;
	
	/**
	 * In the construction function we create
	 * bins and feed points into them to get 
	 * x values and y values
	 * 
	 * @param values A list of original values to be plotted in histogram
	 * @param n Number of stocks in a period
	 * @param num_of_bins Number of bins correspond to x axis
	 * @param minVal The min value of x axis
	 * @param maxVal The max value of x axis
	 * @throws Exception
	 */
	public Histogram(List<Double> values, int n, int num_of_bins, double minVal, double maxVal) throws Exception {
		this.values = values;
		this.num_of_stocks = n;
		this.num_of_bins = num_of_bins;
		this.minVal = minVal;
		this.maxVal = maxVal;
		this.width = (this.maxVal - this.minVal) / this.num_of_bins;
		
		// Initialize lists to store x values and y values 
		xVals = new ArrayList<Double>(this.num_of_bins + 1);
		yVals = new ArrayList<Double>(this.num_of_bins);
		
		for (double i = 0; i < num_of_bins; i++) {
			double x = minVal + i * width;
			xVals.add(x);
			yVals.add((double)0);
		}
		xVals.add(maxVal);
		
		//System.out.println("##");
		// Feed points to x and y lists
		for (double value:values) {
			feedPoints(value);
			//System.out.println(value);
		}
		
	}
	/**
	 * This function feed points to x and y value lists 
	 * If we have two intervals such as [0.1,0.2] and
	 * [0.2,0.3], we will assign 0.2 to the second 
	 * interval. We also assign the max value to the 
	 * last interval.
	 * 
	 * @param value Original value to be plotted in histogram
	 * @throws Exception
	 */
	private void feedPoints(double value) throws Exception {
		if (value > maxVal || value < minVal)
			throw new Exception("Invalid input value!");
		int index = value < maxVal? (int)Math.floor((value - minVal) / width) : num_of_bins - 1;		
		yVals.set(index, yVals.get(index) + 1);
	}
	
	/**
	 * This function calculate the parameter U using the formula
	 * in the paper by assuming that U = D
	 * @return
	 */
	public double getU() {
		double mean = 0;
		double variance = 0;
		for (double value:values) {
			mean += value;
			variance += value * value;
		}
		mean = mean / values.size();
		variance = variance / values.size();
		variance = variance - mean * mean;
		variance = variance * values.size() / (values.size() - 1);
		double u = (0.25 - variance) * 0.5  / (variance - 0.25 / num_of_stocks);
		return u;		
	}
	
	/**
	 * This function outputs x and y values into txt files
	 * @param fileName The filename of output file 
	 * @throws IOException
	 */
	public void makeHistogram (String fileName) throws IOException {
		BufferedWriter xWriter = new BufferedWriter( new FileWriter(fileName + "_X.txt"));
		BufferedWriter yWriter = new BufferedWriter( new FileWriter(fileName + "_Y.txt"));
	
		for (int i = 0; i < num_of_bins; i++) {
			xWriter.write(xVals.get(i) + ",");
			yWriter.write(yVals.get(i) + ",");			
		}
		xWriter.write(xVals.get(num_of_bins) + "");
		xWriter.close();
		yWriter.close();
	}
	
	public List<Double> getXVals() {
		return xVals;
	}
	
	public List<Double> getYVals() {
		return yVals;
	}
	
	public List<Double> getYProbs() {
		List<Double> yProbs = new ArrayList<Double>();
		for (int i = 0; i < num_of_bins; i++) {
			yProbs.add(yVals.get(i) / values.size());
		}
		return yProbs;
	}
}
