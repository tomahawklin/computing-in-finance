package histogram;

import static org.junit.Assert.*;

import org.junit.Test;

public class Test_Optimizer {

	@Test
	/**
	 * This test ensures the choose function get 
	 * produce the right binomial coefficient
	 */
	public void test_binomial() {
		assertTrue(Optimizer.choose(20, 8) == 125970);
		assertTrue(Optimizer.choose(20, 1) == 20);
		assertTrue(Optimizer.choose(20, -1) == 0);
		assertTrue(Optimizer.choose(20, 21) == 0);
	}

}
