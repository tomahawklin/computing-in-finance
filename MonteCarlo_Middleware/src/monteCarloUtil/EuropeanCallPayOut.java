package monteCarloUtil;

import java.util.List;
import monteCarloUtil.StockPath;

/**
 * This class implements PayOut interface and
 * calculate an European call option's payout
 * @author lin
 *
 */

public class EuropeanCallPayOut implements PayOut {
	private double strikePrice;
	
	public double getPayout(StockPath stockPath) {
		double payOut;
		List<Double> prices = stockPath.getPrices();
		payOut = Math.max(0, prices.get(prices.size() - 1) - strikePrice);
		return payOut;
	}
	
	public EuropeanCallPayOut(double strikePrice) {
		this.strikePrice = strikePrice;
	}

}
