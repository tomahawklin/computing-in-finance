package simulation;

import payOut.AsianCallPayOut;
import payOut.EuropeanCallPayOut;
import payOut.PayOut;
import randomVectorGenerator.AntiTheticRandomVectorGenerator;
import randomVectorGenerator.StdNormalRandomVectorGenerator;
import randomVectorGenerator.RandomVectorGenerator;
import simulation.Option.OptionBuilder;
import stockPath.BrownianStockPath;
import stockPath.StockPath;

/**
 * This Class is used to run simulation
 * It only contains a static function
 * and the main method for the project
 * @author lin
 *
 */

public class Simulator {
	/**
	 * 
	 * @param option The option we want to price
	 * @param payOut The PayOut type for the option
	 * @param statsCollector The Stats Collector 
	 * @param stopProb The probability for stopping criteria
	 * @param stopError The Error for stopping criteria
	 * @return the discounted payout, i.e., the current price
	 */
	public static double simulate(Option option, PayOut payOut, StatsCollector statsCollector, double stopProb, double stopError) {
		
		// First we use the formula give by the professor 
		// to estimate the quantile of gaussian distribution
		double t = Math.sqrt(Math.log(1 / ((1 - stopProb) * (1 - stopProb) / 4)));
		double xp = t - (2.515517 + 0.802853 * t + 0.010328 * t * t) / (1 + 1.432788 * t + 0.189269 * t * t + 0.001308 * t * t * t);
		
		// The simulation part
		boolean notConverge = true;
		int iterCount = 0;
		RandomVectorGenerator normGenerator = new StdNormalRandomVectorGenerator(option.getPeriod());
		RandomVectorGenerator antiGenerator = new AntiTheticRandomVectorGenerator(normGenerator);

		while(notConverge) {
			// Generate random vector and stock path using Anti-Thetic decorator 
			StockPath path = new BrownianStockPath(option, antiGenerator);			
			// Calculate the discounted payout
			double currentPrice = payOut.getPayout(path) * Math.exp(-1 * option.getInterestRate() * option.getPeriod());
			statsCollector.add(currentPrice);
			// Judge whether the simulation meets stopping criteria
			if (xp * statsCollector.getStd() / Math.sqrt(iterCount) < stopError && statsCollector.getStd() > 0) {
				System.out.println("Converge after " + iterCount + " times of simulation.");
				notConverge = false;
			}
			iterCount++;
		}
		return statsCollector.getAvg();
	}
	
	public static void main( String[] args ) {
		long start = System.nanoTime();
		// Solving problem 1
		String name = "IBM";
		double initialPrice = 152.35;
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
		PayOut payOut = new EuropeanCallPayOut(strikePrice);
		double price = Simulator.simulate(option, payOut, statsCollector, 0.96, 0.1);
		System.out.println("The first option's price should be " + price);
	
		// Solving problem 2
		String payOutType2 = "AsianCall";
		double strikePrice2 = 164;
		
		// Build the option with an option builder
		OptionBuilder ob2 = new OptionBuilder(name, initialPrice, payOutType2);
		Option option2 = ob2.interestRate(interestRate).period(period).strikePrice(strikePrice2).volatility(volatility).build();
		
		// Set Stats Collector and PayOut to run simulation
		StatsCollector statsCollector2 = new StatsCollector();
		PayOut payOut2 = new AsianCallPayOut(strikePrice2);
		double price2 = Simulator.simulate(option2, payOut2, statsCollector2, 0.96, 0.1);
		System.out.println("The second option's price should be " + price2);
		
		// Calculate the elapsed time to help optimize run time
		double elapsedTimeInSec = (System.nanoTime() - start) * 1.0e-9;
		System.out.println("Elapsed time in seconds : " + elapsedTimeInSec);
	}

}
