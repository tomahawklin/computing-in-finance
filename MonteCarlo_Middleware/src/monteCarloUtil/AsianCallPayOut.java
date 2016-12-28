package monteCarloUtil;

import java.util.List;
import monteCarloUtil.StockPath;

/**
 * This class implements PayOut interface 
 * and calculate an Asian call option's payout
 * @author lin
 *
 */

public class AsianCallPayOut implements PayOut {
	
	private double strikePrice;
	
	public double getPayout(StockPath stockPath) {
		double payOut;
		double sum = 0;
		List<Double> prices = stockPath.getPrices();
		for ( double p : prices) {
			sum += p;
		}
		double avg = sum / prices.size();
		payOut = Math.max(0, avg - strikePrice);
		return payOut;
	}
	
	public AsianCallPayOut(double strikePrice) {
		this.strikePrice = strikePrice;
	}
	
}
