package middleware;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import monteCarloUtil.Option;
import monteCarloUtil.Option.OptionBuilder;
import monteCarloUtil.BrownianStockPath;
import monteCarloUtil.StockPath;
import monteCarloUtil.AsianCallPayOut;
import monteCarloUtil.EuropeanCallPayOut;
import monteCarloUtil.PayOut;
import monteCarloUtil.AntiTheticRandomVectorGenerator;
import monteCarloUtil.RandomVectorGenerator;
import monteCarloUtil.StdNormalRandomVectorGenerator;

/**
 * This class is used to receive simulation request,
 * do MonteCarlo simulation and send simulation result
 * 
 * In this homework we only have one server and one
 * client interacting. We design them in PtP pattern
 * @author lin
 *
 */

public class MonteCarloClientPtP {
	// URL of JMS server
	private static String brokerURL = "tcp://localhost:61616";
	// Option name to prevent multiple managers from mixed up
	private String optionName;
	// JMS connection factory
	private static ConnectionFactory factory;
	// JMS connection
	private Connection connection;
	// JMS session
	private Session session;
	// JMS message producer
	private MessageProducer producer;
	
	/**
	 *    
	 * @param optionName Option name to prevent multiple managers from mixed up
	 * @throws Exception
	 */
	public MonteCarloClientPtP(String optionName) throws Exception {
		factory = new ActiveMQConnectionFactory(brokerURL);
		connection = factory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		producer = session.createProducer(null);
		this.optionName = optionName;
	}
	
	/**
	 * This function receives text message from queue
	 * @throws Exception
	 */
	public void run() throws Exception {
		Destination destination = session.createQueue(optionName + " simulation request");
		MessageConsumer messageConsumer = session.createConsumer(destination);
		messageConsumer.setMessageListener(new MessageListener() {
			public void onMessage(Message message) {
				if (message instanceof TextMessage) {
					try{
						// The format of text message is like "InterestRate:0.1, sigma:0.2, strike:3, s0:1, type:Eruopean, duration:150"
						// We get the information by first splitting the text by ","
						// and then get the information after ":"
						String text[] = ((TextMessage)message).getText().split(",");
						double interestRate = Double.parseDouble(text[0].split(":")[1]);
						double sigma = Double.parseDouble(text[1].split(":")[1]);
						double strike = Double.parseDouble(text[2].split(":")[1]);
						double s0 = Double.parseDouble(text[3].split(":")[1]);
						String type = text[4].split(":")[1];
						int duration = Integer.parseInt(text[5].split(":")[1]);
						
						// Build the option with an option builder
						OptionBuilder ob = new OptionBuilder(optionName, s0, type);
						Option option = ob.interestRate(interestRate).period(duration).strikePrice(strike).volatility(sigma).build();
						
						// Generate random vector and stock path using Anti-Thetic decorator
						RandomVectorGenerator normGenerator = new StdNormalRandomVectorGenerator(option.getPeriod());
						RandomVectorGenerator antiGenerator = new AntiTheticRandomVectorGenerator(normGenerator); 
						StockPath path = new BrownianStockPath(option, antiGenerator);		
					
						// Calculate option's current price based on its type
						String result = "";
						switch(type) {
						case "AsianCall":
							PayOut aPayOut = new AsianCallPayOut(strike);
							result = String.valueOf(aPayOut.getPayout(path) * Math.exp(-1 * option.getInterestRate() * option.getPeriod()));							
							break;
						case "EuropeanCall":
							PayOut ePayOut = new EuropeanCallPayOut(strike);
							result = String.valueOf(ePayOut.getPayout(path) * Math.exp(-1 * option.getInterestRate() * option.getPeriod()));
							break;
						default:
							// When the option's type is bad, close the connection
							System.out.println("Bad option type! Try again.");
							break;		
						}
						
						// Convert to String type and send via sendResult function
						sendResult(result);
						message.acknowledge();
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	/**
	 * 
	 * @param result Option's current price sent to specified destination
	 * @throws JMSException
	 */
	private void sendResult(String result) throws JMSException {
		Destination topic = session.createQueue(optionName + " simulation result");
		TextMessage message = session.createTextMessage(result);
		producer.send(topic, message);
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public void close() throws JMSException {
	    if (connection != null) {
	        connection.close();
	    }
	}
	   
	public static void main( String[] args ) throws Exception {
		MonteCarloClientPtP client = new MonteCarloClientPtP("IBM");
		client.run();
	}

}
