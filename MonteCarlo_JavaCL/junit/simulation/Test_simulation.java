package simulation;

import org.junit.Test;
import static org.junit.Assert.*;

public class Test_simulation {

		
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
