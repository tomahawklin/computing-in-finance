package simulation;

import randomVectorGenerator.GBMGenerator;
import randomVectorGenerator.GaussianVectorPairGenerator;
import randomVectorGenerator.UniformVectorGenerator;
import simulation.Option.OptionBuilder;

/**
 * This Class is used to run simulation to price an European Call Option
 * It only contains a static function and the main method for the project
 * @author lin
 *
 */

public class Simulator {
	/**
	 * 
	 * @param option The option we want to price
	 * @param statsCollector The Stats Collector 
	 * @param stopProb The probability for stopping criteria
	 * @param stopError The Error for stopping criteria
	 * @return discounted payout, i.e., the price of our option
	 * @throws Exception 
	 */
	public static double simulate(Option option, StatsCollector statsCollector, double stopProb, double stopError) throws Exception {
		
		// First we use the formula give by the professor 
		// to estimate the quantile of gaussian distribution
		double t = Math.sqrt(Math.log(1 / ((1 - stopProb) * (1 - stopProb) / 4)));
		double xp = t - (2.515517 + 0.802853 * t + 0.010328 * t * t) / (1 + 1.432788 * t + 0.189269 * t * t + 0.001308 * t * t * t);
		
		// The simulation part
		boolean notConverge = true;
		int iterCount = 0;
		
		while (notConverge) {
			// We set batch size as 1 million
			int batchSize = 1000000;
			
			// Create two vectors of uniform-distributed numbers between 0 and 1
			UniformVectorGenerator unGenerator1 = new UniformVectorGenerator(batchSize);
			UniformVectorGenerator unGenerator2 = new UniformVectorGenerator(batchSize);
			
			// Create a  pair of vector of Gaussian-distributed numbers using Box Muller transformation
			GaussianVectorPairGenerator gsGenerator = new GaussianVectorPairGenerator(unGenerator1.getVector(), unGenerator2.getVector());		
			
			// Create Geometric Brownian Motion
			GBMGenerator generator = new GBMGenerator(gsGenerator.getVector1(), gsGenerator.getVector2(), (float) option.getInterestRate(), (float) option.getVolatility(), (float) option.getInitialPrice());
						
			
			for (int i = 0; i < 2 * batchSize; i++) {
				// Calculate the discounted payout, i.e., the option's price
				double payOut = Math.max(0, generator.getStrikePrices()[i] - option.getStrikePrice());
				double price = payOut * Math.exp(-1 * option.getInterestRate() * option.getPeriod());
				statsCollector.add(price);
				System.out.println(price);
				
				// Judge whether the simulation meets stopping criteria
				if (xp * statsCollector.getStd() / Math.sqrt(iterCount) < stopError && statsCollector.getStd() > 0) {
					System.out.println("Converge after " + iterCount + " times of simulation.");
					notConverge = false;
					break;
				}
				iterCount++;
			}			
		}
		return statsCollector.getAvg();
	}
	
	public static void main( String[] args ) throws Exception {
		long start = System.nanoTime();
		// Solving problem 1
		String name = "IBM";
		float initialPrice = (float) 152.35;
		double volatility = 0.01;
		double interestRate = 0.0001;
		int period = 252;
		double strikePrice = 165;
		String payOutType = "EuropeanCall";
		
		// Build the option with an option builder
		OptionBuilder ob = new OptionBuilder(name, initialPrice, payOutType);
		Option option = ob.interestRate(interestRate).period(period).strikePrice(strikePrice).volatility(volatility).build();
		
		// Set Stats Collector and PayOut to run simulation
		StatsCollector statsCollector = new StatsCollector();
		
		double price = Simulator.simulate(option, statsCollector, 0.96, 0.1);
		//double price = Math.max(0, generator.getStrikePrice() - strikePrice);
		System.out.println("The first option's price should be " + price);
		
		// Calculate the elapsed time to help optimize run time
		double elapsedTimeInSec = (System.nanoTime() - start) * 1.0e-9;
		System.out.println("Elapsed time in seconds : " + elapsedTimeInSec);
	}

}
