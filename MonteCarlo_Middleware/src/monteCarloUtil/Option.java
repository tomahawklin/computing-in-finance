package monteCarloUtil;


/**
 * This class characterize an option
 * We construct an option using an
 * option builder, which allows us
 * to instantiate an object with 
 * custom parameters 
 * @author lin
 *
 */
public class Option {
	private String name;
	private double initialPrice;
	private String payOutType;
	private double interestRate;
	private double volatility;
	private double strikePrice;
	private int period;
	
	/**
	 * We use an option builder class
	 * to construct an option object
	 * @author lin
	 *
	 */
	public static class OptionBuilder {
		private String name;
		private double initialPrice;
		private String payOutType;
		private double interestRate;
		private double volatility;
		private double strikePrice;
		private int period;
		
		/**
		 * The following three fields are requisite
		 * characteristics for an option
		 * @param name Option's target stock name
		 * @param initialPrice Current Price of the target stock
		 * @param payOutType Such as AsianCall or EuropeanCall, etc
		 */
		
		public OptionBuilder(String name, double initialPrice, String payOutType) {
			this.name = name;
			this.initialPrice = initialPrice;
			this.payOutType = payOutType;
		}
		
		public OptionBuilder interestRate(double interestRate) {
			this.interestRate = interestRate;
			return this;
		}
		
		public OptionBuilder volatility(double volatility) {
			this.volatility = volatility;
			return this;
		}
		
		public OptionBuilder period(int period) {
			this.period = period;
			return this;
		}
		
		public OptionBuilder strikePrice(double strikePrice) {
			this.strikePrice = strikePrice;
			return this;
		}
		
		public Option build() {
			return new Option(this);			
		}
	}
	
	// The construction function of Option will only be
	// called in an option builder's build() function
	private Option(OptionBuilder builder) {
		this.name = builder.name;
		this.initialPrice = builder.initialPrice;
		this.payOutType = builder.payOutType;
		this.interestRate = builder.interestRate;
		this.volatility = builder.volatility;
		this.strikePrice = builder.strikePrice;
		this.period = builder.period;	
	}
	
	public double getInitialPrice() {
		return initialPrice;
	}
	
	public double getInterestRate() {
		return interestRate;
	}
	
	public double getVolatility() {
		return volatility;
	}
	
	public double getStrikePrice() {
		return strikePrice;
	}
	
	public int getPeriod() {
		return period;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPayOutType() {
		return payOutType;
	}
	
}
