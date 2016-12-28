package middleware;

import javax.jms.JMSException;

import org.junit.Test;

import monteCarloUtil.AntiTheticRandomVectorGenerator;
import monteCarloUtil.RandomVectorGenerator;
import monteCarloUtil.StdNormalRandomVectorGenerator;

import static org.junit.Assert.*;

public class Test_Middleware {
	
	@Test
	public void test_SplitText() {
		// We want to test whether we can get correct information from split text message
		String t = "InterestRate:0.1, sigma:0.2, strike:3, s0:1, type:Eruopean, duration:150";
		String text[] = t.split(",");
		for (String word:text) {
			System.out.println(word);
			String textSplit[] = word.split(":");
			System.out.println(textSplit[1]);
		}		
	}
	
	@Test
	public void test_BadOptionType() throws Exception {
		// Test sending bad option type
		// Should close the session due to bad option type
		MonteCarloServerPtP server = new MonteCarloServerPtP("IBM", 100);
		MonteCarloClientPtP client = new MonteCarloClientPtP("IBM");
		server.sendSimuReq("0.0001", "0.01", "164", "152.35","AsianBid", "252");
		client.run();
		server.sendSimuReq("0.0001", "0.01", "164", "152.35","AsianBid", "252");
		client.run();
	}
	
	@Test
	public void test_AntiThetic() {
		// This Test ensures that the Anti-Thetic RandVectorGenerator
		// generates the vector we expect
		RandomVectorGenerator rvg1 = new StdNormalRandomVectorGenerator(5);
		RandomVectorGenerator rvg2 = new AntiTheticRandomVectorGenerator(rvg1);
		double[] vec1 = rvg1.getVector();
		double[] temp = rvg2.getVector();
		double[] vec2 = new double[5];
		for (int i = 0; i < 5; i++) {
			vec2[i] = temp[i];
		}
		// We expect the vector2 is same as vector1
		for (int i = 0; i < 5; i++) {
			assertTrue(vec2[i] == vec1[i]);
		}
		temp = rvg2.getVector();
		double[] vec3 = new double[5];
		// We expect the next vector3 is opposite to vector2
		for (int i = 0; i < 5; i++) {
			vec3[i] = temp[i];
		}
		for (int i = 0; i < 5; i++) {
			assertTrue(vec2[i] == -1 * vec3[i]);
		}		
	}
	
	@Test
	public void test_StatsCollector() {
		// This test ensures the Stats Collector
		// reports the right value
		StatsCollector statsCollector = new StatsCollector();
		for (int i = 0; i < 3 ; i++) {
			statsCollector.add(i);			
		}
		// We know that the mean and standard 
		// deviation for {0,1,2} are both 1
		assertTrue(statsCollector.getAvg() == 1);
		assertTrue(statsCollector.getStd() == 1);		
	}

}
