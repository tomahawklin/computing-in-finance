package histogram;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.math.BigInteger;
import java.text.SimpleDateFormat;

import dataReader.QuotesReader;
/**
 * This class is used to output specific histograms
 * and to optimize the lookback parameter
 * 
 * @author lin
 *
 */
public class Optimizer {
	
	private int lookback;
	private List<Double> realValue;
	private List<Double> estiValue;
	private double mse;
	
	public static void main(String[] args) throws Exception {
		
		// Instantiate a quote reader for our data file
		QuotesReader reader = new QuotesReader("panicData.csv");
		
		// Construct a calendar to adjust date
		SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");		
		Calendar c = Calendar.getInstance();
		
		// We also want to find the optimal lookback which minimizes MSE
		List<Integer> optimal_lookback = new ArrayList<Integer>();
		// Output histograms from year 2000 to year 2008
		for (Integer i = 20000101; i < 20090101; i+= 10000) {
			// Create start date
			Date startDate = originalFormat.parse(i.toString());
			c.setTime(startDate); 
			c.add(Calendar.YEAR, 1);
			// Get end date
			Date endDate = c.getTime();
			// Read data in specific time period
			reader.readData(startDate, endDate);
			
			// Instantiate a histogram with x axis starting from 0 to 1 with 10 bins
			Histogram h = new Histogram(reader.getUpRatios(), reader.getN(), 10, 0, 1);
			// Output histograms into txt files, named with specific year
			h.makeHistogram(Integer.toString(i / 10000));
			System.out.println("u: " + h.getU() +" at year " + i / 10000);
			
			// Optimize part
			// For each year, we adjust lookback parameter and find
			// the optimal parameter with min MSE
			int flag = 0;
			double minMSE = Double.MAX_VALUE;
			// Start searching optimal lookback at 5 days
			// Increase by 30 days each iteration
			for (int j = 5; j < 366; j+=30) {
				// increase lookback days
				c.add(Calendar.DATE, j);
				endDate = c.getTime();
				// Reload data
				reader.readData(startDate, endDate);
				// Create a temporary histogram to calculate yhat
				Histogram temp = new Histogram(reader.getUpRatios(), reader.getN(), 10 ,0, 1);
				// y is the probability density in each interval for real curve
				List<Double> y = temp.getYProbs();
				// yhat is the probability density in each interval for fitting curve 
				List<Double> yhat = getYhat((int)h.getU(), reader.getN(), 10, 0 ,1);
				
				// Calculate MSE between y and yhat
				double MSE = getMSE(y, yhat);
				if (MSE < minMSE) {
					// Store the min MSE and corespond lookback days
					minMSE = MSE;
					flag = j;
				}
			}
			// Show in console
			System.out.println("Min MSE " + minMSE + " is obatained with lookback = " + flag);
			optimal_lookback.add(flag);
		}
		
		// Calculate the average value of optimal lookback paramter
		double sum = 0;
		for (int i:optimal_lookback) {
			sum += i;
		}
		System.out.println("The average optimal lookback is " + sum / optimal_lookback.size());
	}
	
	/**
	 * This function is used to calculate the MSE, used 
	 * to quantify the goodness of fitting 
	 * @param y The true values
	 * @param yhat The fitting values
	 * @return
	 * @throws Exception
	 */
	private static double getMSE (List<Double> y, List<Double> yhat) throws Exception {
		double mse = 0;
		if (y.size() != yhat.size())
			throw new Exception("Invalid input list!");
		for (int i = 0 ; i < y.size(); i++) {
			mse += (y.get(i) - yhat.get(i)) * (y.get(i) - yhat.get(i)) / y.size();
		}
		return mse;
	}
	
	/**
	 * This function returns predicted y values for the probability density
	 * in specific interval using the formula (1) in the paper assuming 
	 * U = D
	 * @param u
	 * @param n
	 * @param num_of_bins
	 * @param minVal
	 * @param maxVal
	 * @return
	 */
	private static List<Double> getYhat(int u, int n, int num_of_bins, double minVal, double maxVal) {
		List<Double> y = new ArrayList<Double>();
		for (int i = 0; i < num_of_bins; i++) {
			double width = (maxVal - minVal) / num_of_bins;
			int kl = (int) ((minVal + i * width) * n);
			int ku = (int) ((minVal + (i + 1) * width) * n);
			double y_i = 0;
			for (int k = kl; k < ku; k++) {
				double a = choose(u + k - 1, k);
				double b = choose(n + u - k - 1, n - k);
				double c = choose(n + u + u - 1, n);
				double p = a * b / c;
				y_i += p;
			}
			y.add(i,  y_i);			
		}
		return y;
	}
	
	/**
	 * This number calculate the binomial coefficient
	 * using the choose y in x formula 
	 * @param x
	 * @param y
	 * @return
	 */
	public static double choose(int x, int y) {
	    if (y < 0 || y > x) return 0;
	    if (y > x/2) {
	        // choose(n,k) == choose(n,n-k), 
	        // so this could save a little effort
	        y = x - y;
	    }

	    double denominator = 1.0, numerator = 1.0;
	    for (int i = 1; i <= y; i++) {
	        denominator *= i;
	        numerator *= (x + 1 - i);
	    }
	    return numerator / denominator;
	}
	
}
