package randomVectorGenerator;

import com.nativelibs4java.opencl.*;
import org.bridj.Pointer;
import static org.bridj.Pointer.allocateFloats;

/**
 * This class can generate pair of Gaussian numbers 
 * @author lin
 *
 */
public class GaussianVectorPairGenerator {
	float[] vector1;
	float[] vector2;
	int dim;
	/**
	 * 
	 * @param uniformVector1 uniform-distributed numbers between 0 and 1
	 * @param uniformVector2 uniform-distributed numbers between 0 and 1
	 * @throws Exception
	 */
	public GaussianVectorPairGenerator (float[] uniformVector1, float[] uniformVector2) throws Exception {
	    // Check different length 
		if (uniformVector1.length != uniformVector2.length) 
			throw new Exception("Different length of imput vectors!");
		dim = uniformVector1.length;
		 
		// Creating the platform which is our computer
		CLPlatform clPlatform = JavaCL.listPlatforms()[0];
		// Getting the CPU device
		CLDevice device = clPlatform.listGPUDevices(true)[0];
		// Make a context
		CLContext context = JavaCL.createContext(null, device);
		// Make a default FIFO queue
		CLQueue queue = context.createDefaultQueue();
		 
		// Read the program sources and compile them
		// Do Box Muller transformation in GPU
		String src = "__kernel void box_muller(__global const float*a, __global const float*b, __global float* out1, __global float* out2, int n)\n" +
			 "{\n" + 
			 "    int i = get_global_id(0);\n" +
			 "    if (i >= n)\n" + 
			 "        return;\n" +
			 "\n" +
			 "    out1[i] = sqrt(-2 * log(a[i])) * cos(b[i] * 2 * 3.14159);\n " +
			 "    out2[i] = sqrt(-2 * log(a[i])) * sin(b[i] * 2 * 3.14159);\n " +
			 "}"; 
		CLProgram program = context.createProgram(src);
		program.build();		
		CLKernel kernal = program.createKernel("box_muller");
		
		// Create and set pointers for input
		final int n = dim;
		final Pointer<Float>
				aPtr = allocateFloats(n),
				bPtr = allocateFloats(n);		
		for (int i = 0; i < n; i++) {
			aPtr.set(i, uniformVector1[i]);
			bPtr.set(i, uniformVector2[i]);
		}
		
		// Create OpenCL input buffers (using the native memory pointers aPtr and bPtr) :
		CLBuffer<Float>
				a = context.createFloatBuffer(CLMem.Usage.Input, aPtr),
				b = context.createFloatBuffer(CLMem.Usage.Input, bPtr);
		
		// Create OpenCL output buffers :
		CLBuffer<Float> 
				out1 = context.createFloatBuffer(CLMem.Usage.Output, n),
				out2 = context.createFloatBuffer(CLMem.Usage.Output, n);
		kernal.setArgs(a, b, out1, out2, n);
		CLEvent event = kernal.enqueueNDRange(queue, new int[]{n}, new int[]{1});

		// Create and set pointers for output
		final Pointer<Float> 
				out1Ptr = out1.read(queue, event),
				out2Ptr = out2.read(queue, event);		
		
		// Load data to vectors
		vector1 = new float[n];
		vector2 = new float[n];
		for (int i = 0; i < n; i++) {
			vector1[i] = out1Ptr.get(i);
			vector2[i] = out2Ptr.get(i);
		}
		
		// Release objects to optimize memory management 
		a.release();
		b.release();
		out1.release();
		out2.release();
		aPtr.release();
		bPtr.release();
		out1Ptr.release();
		out2Ptr.release();
			 
	}

	public float[] getVector1() {
		return vector1;
	}
	
	public float[] getVector2() {
		return vector2;
	}
	
	

}
