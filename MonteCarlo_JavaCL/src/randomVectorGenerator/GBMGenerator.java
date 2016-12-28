package randomVectorGenerator;

import static org.bridj.Pointer.allocateFloats;

import org.bridj.Pointer;

import com.nativelibs4java.opencl.CLBuffer;
import com.nativelibs4java.opencl.CLContext;
import com.nativelibs4java.opencl.CLDevice;
import com.nativelibs4java.opencl.CLEvent;
import com.nativelibs4java.opencl.CLKernel;
import com.nativelibs4java.opencl.CLMem;
import com.nativelibs4java.opencl.CLPlatform;
import com.nativelibs4java.opencl.CLProgram;
import com.nativelibs4java.opencl.CLQueue;
import com.nativelibs4java.opencl.JavaCL;
/**
 * This class calculate geometric brownian motion in GPU
 * @author lin
 *
 */
public class GBMGenerator {
	
	private double[] strikePrices;
	/**
	 * 
	 * @param gaussianVector1 Gaussian-distributed numbers
	 * @param gaussianVector2 Gaussian-distributed numbers
	 * @param miu mean of geometric brownian motion
	 * @param vol standard error of geometric brownian motion
	 * @param s0 initial price
	 * @throws Exception
	 */
	public GBMGenerator(float[] gaussianVector1, float[] gaussianVector2, float miu, float vol, float s0) throws Exception {
		if (gaussianVector1.length != gaussianVector2.length) 
				throw new Exception("Different length of imput vectors!");
			final int n = gaussianVector1.length;
			// Creating the platform which is our computer
			CLPlatform clPlatform = JavaCL.listPlatforms()[0];
			// Getting the CPU device
			CLDevice device = clPlatform.listGPUDevices(true)[0];
			// Make a context
			CLContext context = JavaCL.createContext(null, device);
			// Make a default FIFO queue
			CLQueue queue = context.createDefaultQueue();
			 
			// Read the program sources and compile them
			// We calculate s(252) directly in GPU
			String src = "__kernel void gbm(__global const float*a, __global float*miu, __global float*vol,  __global float*out, int n)\n" +
				 "{\n" + 
				 "    int i = get_global_id(0);\n" +
				 "    if (i >= n)\n" + 
				 "        return;\n" +
				 "\n" +
				 "    out[i] = exp((miu[0] - vol[0] * vol[0] / 2) * 252 + vol[0] * a[i] * 15.87);\n " +
				 "}"; 
			CLProgram program = context.createProgram(src);
			program.build();			
			CLKernel kernal = program.createKernel("gbm");
			
			// Create and set pointers for input
			final Pointer<Float> aPtr = allocateFloats(2 * n);
			final Pointer<Float> miuPtr = allocateFloats(1);
			final Pointer<Float> volPtr = allocateFloats(1);
			miuPtr.set(0, miu);
			volPtr.set(0, vol);
			
			// Merge two Gaussian vector into one
			for (int i = 1; i < n; i++) {
				aPtr.set(i, gaussianVector1[i]);
			}
			for (int i = n; i < 2 * n; i++) {
				aPtr.set(i, gaussianVector2[i - n]);
			}
					
			// Create OpenCL input buffers (using the native memory pointers aPtr and bPtr) :
			CLBuffer<Float> a = context.createFloatBuffer(CLMem.Usage.Input, aPtr);
			CLBuffer<Float> miuBuffer = context.createFloatBuffer(CLMem.Usage.Input, miuPtr);
			CLBuffer<Float> volBuffer = context.createFloatBuffer(CLMem.Usage.Input, volPtr);
			
			// Create OpenCL output buffers :
			CLBuffer<Float> out = context.createFloatBuffer(CLMem.Usage.Output, 2 * n);
			kernal.setArgs(a, miuBuffer, volBuffer, out, 2 * n);
			CLEvent event = kernal.enqueueNDRange(queue, new int[]{2 * n}, new int[]{1});
			
			// Read price data from GPU 
			strikePrices = new double[2 * n];
			final Pointer<Float> outPtr = out.read(queue, event);
			for (int i = 0; i < 2 * n; i++) {
				strikePrices[i] = s0 * outPtr.get(i);
			}

			// release objects to optimize memory management
			a.release();
			miuBuffer.release();
			volBuffer.release();
			out.release();
			aPtr.release();
			miuPtr.release();
			volPtr.release();
			outPtr.release();		
	}
	
	public double[] getStrikePrices() {
		//System.out.println(strikePrice);
		return strikePrices;
	}

}
