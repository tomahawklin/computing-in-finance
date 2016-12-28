package randomVectorGenerator;

import java.util.Random;

public class UniformVectorGenerator {
	private float[] vector;
	private int dim;
	
	public UniformVectorGenerator(int dim) {
		this.dim = dim;
		vector = new float[dim];
		Random randomGenerator = new Random();
		for (int i = 0; i < dim; i++) {
			vector[i] = randomGenerator.nextFloat();
		}
	}
	
	public float[] getVector() {
		return vector;
	}

}
