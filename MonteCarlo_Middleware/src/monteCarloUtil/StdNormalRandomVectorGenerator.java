package monteCarloUtil;

import java.util.Random;

import monteCarloUtil.RandomVectorGenerator;

/**
 * This class generates random vector
 * with a standard normal distribution
 * @author lin
 *
 */

public class StdNormalRandomVectorGenerator implements RandomVectorGenerator {
	
	private double[] vector;
	private int dim;
	
	/**
	 * 
	 * @param dim The dimension of the random vector
	 */
	
	public StdNormalRandomVectorGenerator(int dim) {
		this.dim = dim;
		vector = new double[dim];		
	}
	
	public double[] getVector() {
		this.nextRandomVector();
		return vector;
	}
	
	public void nextRandomVector() {
		Random randomGenerator = new Random();
		for (int i = 0; i < dim; i++) {
			vector[i] = randomGenerator.nextGaussian();
		}
	}

}
