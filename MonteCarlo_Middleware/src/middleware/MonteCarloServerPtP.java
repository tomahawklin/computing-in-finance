package middleware;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * This class is used to send simulation request,
 * receive simulation request and judge convergence
 * 
 * In this homework we only have one server and one
 * client interacting. We design them in PtP pattern
 * @author lin
 *
 */

public class MonteCarloServerPtP {
	// URL of JMS server
	protected static String brokerURL = "tcp://localhost:61616";
	// Option name to prevent multiple managers from mixed up
	private String optionName;
	// Numbers of requests in batch
	private int batchSize;
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
	 * @param optionName 
	 * @param batchSize
	 * @throws JMSException
	 */
	public MonteCarloServerPtP(String optionName, int batchSize) throws JMSException {
		this.optionName = optionName;
		this.batchSize = batchSize;
		factory = new ActiveMQConnectionFactory(brokerURL);
		connection = factory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);		
	    producer = session.createProducer(null);
	}
		
	/**
	 * 
	 * @param interestRate Option's interest rate
	 * @param sigma Option's volatility
	 * @param strike Option's strike price at the end
	 * @param s0 Option's initial price
	 * @param type Option's type such as AsianCall
	 * @param duration Option's period
	 * @throws Exception
	 */
	public void sendSimuReq(String interestRate, String sigma, String strike, String s0, String type, String duration) throws Exception {
	      Destination topic = session.createQueue(optionName + " simulation request");
	      TextMessage msg = session.createTextMessage("InterestRate:" + interestRate + ", sigma:" + sigma + ", strike:" + strike + ", s0:" + s0 + ", type:" + type + ", duration:" + duration);
	      producer.send(topic, msg);
	}
	/**
	 * 
	 * @param statsCollector Stats collector to collect simulation results from message
	 * @throws JMSException
	 */
	public void collectSimuRes(StatsCollector statsCollector) throws JMSException {
		Destination destination = session.createQueue(optionName + " simulation result");
		MessageConsumer messageConsumer = session.createConsumer(destination);
	    messageConsumer.setMessageListener(new MessageListener() {
	    	public void onMessage(Message message) {
	    		if (message instanceof TextMessage) {
	    			try {
	    				statsCollector.add(Double.parseDouble(((TextMessage)message).getText()));
	    				message.acknowledge();
	    			} catch (JMSException e) {
	    				e.printStackTrace();
	    			}
	    		}
	    	}
	    });
	}
	/**
	 * This function runs the simulation in batch
	 * @throws Exception
	 */
	public void run() throws Exception {
		// First we use the formula give by the professor 
	    // to estimate the quantile of gaussian distribution
	    final double stopProb = 0.96;
	    final double stopError = 0.1;
	    double t = Math.sqrt(Math.log(1 / ((1 - stopProb) * (1 - stopProb) / 4)));
	    double xp = t - (2.515517 + 0.802853 * t + 0.010328 * t * t) / (1 + 1.432788 * t + 0.189269 * t * t + 0.001308 * t * t * t);
        
	    // Setup stats collector and loop to run simulation
		StatsCollector statsCollector = new StatsCollector();
	    boolean notConverge = true;
	    while (notConverge) {
	    	    for (int i = 0; i < batchSize; i++) {
	    	    	    sendSimuReq("0.0001", "0.01", "165", "152.35","EuropeanCall", "252"); // Calculate the price of first option
	    	    	    //sendSimuReq("0.0001", "0.01", "164", "152.35","AsianCall", "252");  // Calculate the price of second option
	    	    }
	    	    for (int i = 0; i < batchSize; i++) {
	    	    	collectSimuRes(statsCollector);	
	    	    }
	    	    // Judge whether the simulation meets stopping criteria
	    	    if (xp * statsCollector.getStd() / Math.sqrt(statsCollector.getN()) < stopError && statsCollector.getStd() > 0) {
	    	        System.out.println("Converge after " + statsCollector.getN() + " times of simulation.");
	    	        notConverge = false;
	    	    }
	    	    System.out.println("Iterations: " + statsCollector.getN() + "; Price: " + statsCollector.getAvg() + "; Std: " + statsCollector.getStd());
	    }
	    double price = statsCollector.getAvg();
	    System.out.println("The option's price should be " + price);
	    
	}
	
	public void close() throws JMSException {
	    if (connection != null) {
	        connection.close();
	    }
	}
	
	public static void main( String[] args ) throws Exception {
		long start = System.nanoTime();
		// Initialize a Monte Carlo simulation manager with a batch size of 100
		MonteCarloServerPtP server = new MonteCarloServerPtP("IBM", 100);
		server.run();
		// Calculate the elapsed time to help optimize run time
	    double elapsedTimeInSec = (System.nanoTime() - start) * 1.0e-9;
		System.out.println("Elapsed time in seconds : " + elapsedTimeInSec);			    
	}
}
