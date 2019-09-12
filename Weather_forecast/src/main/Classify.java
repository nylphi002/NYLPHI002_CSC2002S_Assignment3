package main;

import java.util.concurrent.RecursiveTask;

/**
 * Classify is the recursive class that classifies which type of clouds are created
 * by the input data from the weather simulation.
 * <P>
 * This is done using a forkjoinpool to divide the calculations over several threads and in to smaller portions.
 * <P>
 * Classify is called from {@link CloudDataParallel}, takes in necessary input and returns an array of integers that represent which cloud types that are created
 * 
 * 
 * @author 		Philip Nylén
 * @version 	1.5
 * @since		1.0
 */

public class Classify extends RecursiveTask<int[]> {
	int lo;
	int hi;
	int dimx;
	int dimy;
	Vector[][][] adv;
	float[][][] conv;
	int[] classification;
	static final int SEQUENTIAL_CUTOFF = 91000;

	Classify(Vector[][][] a, float[][][] c, int l, int h, int x, int y) {
		lo = l;
		hi = h;
		adv = a;
		conv = c;
		dimx = x;
		dimy = y;
		classification = new int[hi - lo + 1];
	}
	// convert linear position into 3D location in simulation grid
	void locate(int pos, int[] ind) {
		ind[0] = (int) pos / (dimx * dimy);
		ind[1] = (pos % (dimx * dimy)) / dimy;
		ind[2] = pos % (dimy);
	}
	//The method compute classifies the cloud types based on the input data 
	protected int[] compute() {
		//If the sequential part is small enough the first if-expression will be executed in a sequential manner
		if ((hi - lo) < SEQUENTIAL_CUTOFF) {
			//Locate where the first and last element of this objects part is placed in the original three dimensional array
			int[] tempArrLo = new int[3];
			locate(lo, tempArrLo);
			int[] tempArrHi = new int[3];
			locate(hi, tempArrHi);
			float f;
			int elementNo = 0;
			
			for (int t = tempArrLo[0]; t <= tempArrHi[0]; t++) {
				for (int x = tempArrLo[1]; x <= tempArrHi[1]; x++) {
					for (int y = tempArrLo[2]; y <= tempArrHi[2]; y++) {
						int noOf = 0;
						float xAvg = 0;
						float yAvg = 0;
						for (int i = 0; i < 3; i++) {
							for (int j = 0; j < 3; j++) {
								//Checks if a specific grid point exists
								if (x - 1 + i >= 0 && y - 1 + j >= 0 && x - 1 + i < dimx && y - 1 + j < dimy) {
									xAvg += adv[t][x - 1 + i][y - 1 + j].x;
									yAvg += adv[t][x - 1 + i][y - 1 + j].y;
									noOf++;
								}
							}
						}
						f = (float) Math.sqrt(Math.pow(xAvg / noOf, 2) + Math.pow(yAvg / noOf, 2));
						//The wind magnitude is compared to the absolut uplift to determine the cloud type
						if (Math.abs(conv[t][x][y]) > f) {
							classification[elementNo] = 0;
						} else if (Math.abs(conv[t][x][y]) <= f && f > 0.2) {
							classification[elementNo] = 1;
						} else {
							classification[elementNo] = 2;
						}
						elementNo++;
					}
				}
			}
			return classification;
		//If the sequential part is too big the operation below will be executed
		} else {
			Classify left = new Classify(adv, conv, lo, ((hi + lo) / 2), dimx, dimy);
			Classify right = new Classify(adv, conv, (hi + lo) / 2 + 1, hi, dimx, dimy);
			//The new arrays will be separated to different threads
			left.fork();
			int[] rightAns = right.compute();
			int[] leftAns = left.join();
			//The smaller arrays are copied to the double-as-large array in this stage
			System.arraycopy(leftAns, 0, classification, 0, leftAns.length);
			System.arraycopy(rightAns, 0, classification, leftAns.length, rightAns.length);
			return classification;
		}
	}
}
