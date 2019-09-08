package main;

import java.util.concurrent.RecursiveTask;

public class Classify extends RecursiveTask<int[]> {
	int lo; // arguments
	int hi;
	int dimx;
	int dimy;
	Vector[][][] adv;
	float[][][] conv;
	int[] classification;
	static final int SEQUENTIAL_CUTOFF = 60000;

	// Vector ans = 0; // result

	Classify(Vector[][][] a, float[][][] c, int l, int h, int x, int y) {
		lo = l;
		hi = h;
		adv = a;
		conv = c;
		dimx = x;
		dimy = y;
		classification = new int[hi - lo + 1];
	}

	void locate(int pos, int[] ind) {
		ind[0] = (int) pos / (dimx * dimy); // t
		ind[1] = (pos % (dimx * dimy)) / dimy; // x
		ind[2] = pos % (dimy); // y
	}

	protected int[] compute() {// return answer - instead of run

		if ((hi - lo) < SEQUENTIAL_CUTOFF) {
			
			int[] tempArrLo = new int[3];
			locate(lo, tempArrLo);
			int[] tempArrHi = new int[3];
			locate(hi, tempArrHi);

			// Float[][][] magVec = new Float[dimt][dimx][dimy];
			float f;
			int elementNo = 0;
			for (int t = tempArrLo[0]; t <= tempArrHi[0]; t++) {
				for (int x = tempArrLo[1]; x <= tempArrHi[1]; x++) {
					for (int y = tempArrLo[2]; y <= tempArrHi[2]; y++) {
						// xT += adv[t][x][y].x;
						// yT += adv[t][x][y].y;
						int noOf = 0;
						float xAvg = 0;
						float yAvg = 0;
						for (int i = 0; i < 3; i++) {
							for (int j = 0; j < 3; j++) {
								if (x - 1 + i >= 0 && y - 1 + j >= 0 && x - 1 + i < dimx && y - 1 + j < dimy) {
									xAvg += adv[t][x - 1 + i][y - 1 + j].x;
									yAvg += adv[t][x - 1 + i][y - 1 + j].y;
									noOf++;
								}
							}
						}
						f = (float) Math.sqrt(Math.pow(xAvg / noOf, 2) + Math.pow(yAvg / noOf, 2));
						//System.out.println("F: " + f);
						if (Math.abs(conv[t][x][y]) > f) {
							classification[elementNo] = 0;
						} else if (Math.abs(conv[t][x][y]) <= f && f > 0.2) {
							classification[elementNo] = 1;
						} else {
							classification[elementNo] = 2;
						}
						//System.out.println(classification[elementNo]);
						elementNo++;
					}
				}
			}
			// xT=xT/dim();
			// yT=yT/dim();
			return classification;

		} else {
			Classify left = new Classify(adv, conv, lo, ((hi + lo) / 2), dimx, dimy);
			Classify right = new Classify(adv, conv, (hi + lo) / 2 + 1, hi, dimx, dimy);

			// order of next 4 lines
			// essential – why?
			left.fork();
			int[] rightAns = right.compute();
			int[] leftAns = left.join();
			
			//System.out.println(rightAns.length);
			
			System.arraycopy(leftAns, 0, classification, 0, leftAns.length);
			System.arraycopy(rightAns, 0, classification, leftAns.length, rightAns.length);
			return classification;
		}
	}
}
