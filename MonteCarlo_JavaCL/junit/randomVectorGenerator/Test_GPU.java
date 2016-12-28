package randomVectorGenerator;

import static org.junit.Assert.*;
import java.util.Random;
import org.junit.Test;

public class Test_GPU {
	
/**
 * In this function we test whether the GaussianVectorPairGenertor
 * can generator pair of Gaussian numbers we expect 
 * 
 * @throws Exception
 */
	@Test
	public void test_GPU() throws Exception {
		int n = 10;
		float[]
				input1 = new float[n],
				input2 = new float[n];
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			input1[i] = random.nextFloat();
			input2[i] = random.nextFloat();			
		}
		GaussianVectorPairGenerator generator = new GaussianVectorPairGenerator(input1, input2);
		float[]
				output1 = generator.getVector1(),
				output2 = generator.getVector2();
		for (int i = 0; i < n; i++) {
			// We test whether the error is small enough
			assertTrue(Math.abs(box_muller(input1[i], input2[i])[0] - output1[i]) < 0.000001f);
			assertTrue(Math.abs(box_muller(input1[i], input2[i])[1] - output2[i]) < 0.000001f);
		}
		
	}
	
	private static float[] box_muller(float u1, float u2) {
		float[] result = new float[2];
		result[0] = (float) (Math.sqrt( -2 * Math.log(u1)) * Math.cos(2 * 3.14159 * u2));
		result[1] = (float) (Math.sqrt( -2 * Math.log(u1)) * Math.sin(2 * 3.14159 * u2));
		return result;
	}

}
