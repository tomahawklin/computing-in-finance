package stockPath;

import java.util.ArrayList;
import java.util.List;
import randomVectorGenerator.RandomVectorGenerator;
import simulation.Option;

/**
 * This class implements StockPath interface  
 * and generate geometric brownian stock path
 * @author lin
 *
 */
public class BrownianStockPath implements StockPath {
	
	private RandomVectorGenerator generator;
	private Option option;
	private List<Double> path;
	
	public BrownianStockPath (Option option, RandomVectorGenerator generator) {
		
		this.generator = generator;
		this.option = option;
		this.path = new ArrayList();
		double strikePrice = option.getInitialPrice();
		path.add(strikePrice);
		double[] randoms = this.generator.getVector();
		double vol = option.getVolatility();
		double ir = option.getInterestRate();
		for (int i = 0; i < randoms.length; i++) {
			strikePrice *= Math.exp((ir - vol * vol / 2) + vol * randoms[i]);
			path.add(strikePrice);
		}		
	}
	
	public List<Double> getPrices() {
		return path;
	}
}
