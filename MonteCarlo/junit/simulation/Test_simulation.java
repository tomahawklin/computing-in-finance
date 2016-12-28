package simulation;

import org.junit.Test;
import payOut.EuropeanCallPayOut;
import payOut.PayOut;
import randomVectorGenerator.AntiTheticRandomVectorGenerator;
import randomVectorGenerator.StdNormalRandomVectorGenerator;
import randomVectorGenerator.RandomVectorGenerator;
import simulation.Option.OptionBuilder;
import stockPath.BrownianStockPath;
import stockPath.StockPath;
import static org.junit.Assert.*;

public class Test_simulation {

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
