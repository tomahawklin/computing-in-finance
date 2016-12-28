package randomVectorGenerator;

/***
 * This class is an Anti-Thetic decorator
 * It generates random vector in this pattern:
 * {...... ni, -ni, nj, -nj,.......}
 * @author lin
 *
 */

public class AntiTheticRandomVectorGenerator implements RandomVectorGenerator {
	
	private RandomVectorGenerator generator;
	private boolean flag = false; // Controls whether the random vector's pattern
	private double[] vector;
	
	public AntiTheticRandomVectorGenerator(RandomVectorGenerator generator) {
		this.generator = generator;
	}
	
	public double[] getVector() {
		if(flag) {
			// Repeat the previous vector but with opposite sign 
			for (int i = 0; i < vector.length; i++) {
				vector[i] = -1 * vector[i];
			}
			flag = false;
		}
		else {
			// Generate another random vector
			vector = generator.getVector();
			flag = true;
		}
		return vector;
	}

	
	
	
	
	
}
