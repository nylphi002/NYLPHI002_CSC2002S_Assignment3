package main;

import java.util.concurrent.RecursiveTask;
/**
 * SumArrayVector2 is the class that calculates the prevailing wind by using the input data from the weather simulation.
 * <P>
 * This is done using a forkjoinpool to divide the calculations over several threads and in to smaller portions.
 * <P>
 * SumArrayVector2 is called from {@link CloudDataParallel} and returns the total x and y vector from all grid and time points
 * 
 * @author 		Philip Nylén
 * @version 	1.5
 * @since		1.0
 */
public class SumArrayVector2 extends RecursiveTask<Vector>  {
	  int lo;
	  int hi;
	  int dimx;
	  int dimy;
	  Vector[][][] adv;
	  static final int SEQUENTIAL_CUTOFF=91000;
	    
	  SumArrayVector2(Vector[][][] a, int l, int h, int x, int y) { 
	    lo=l; hi=h; adv=a; dimx = x; dimy = y;
	  }
	// convert linear position into 3D location in simulation grid
	  void locate(int pos, int[] ind) {
			ind[0] = (int) pos / (dimx * dimy);
			ind[1] = (pos % (dimx * dimy)) / dimy;
			ind[2] = pos % (dimy);
		}

	  protected Vector compute() {
		//If the sequential part is small enough the first if-expression will be executed in a sequential manner
		if ((hi - lo) < SEQUENTIAL_CUTOFF) {
			//Locate where the first and last element of this objects part is placed in the original three dimensional array
			int[] tempArrLo = new int[3];
			locate(lo, tempArrLo);
			int[] tempArrHi = new int[3];
			locate(hi, tempArrHi);

			Vector ans = new Vector();
			ans.x = 0;
			ans.y = 0;
			for (int t = tempArrLo[0]; t <= tempArrHi[0]; t++) {
				for (int x = tempArrLo[1]; x <= tempArrHi[1]; x++) {
					for (int y = tempArrLo[2]; y <= tempArrHi[2]; y++) {
						ans.x += adv[t][x][y].x;
						ans.y += adv[t][x][y].y;
					}
				}
			}
			return ans;
		//If the sequential part is too big the operation below will be executed
		} else {
			  SumArrayVector2 left = new SumArrayVector2(adv,lo,(hi+lo)/2, dimx, dimy);
			  SumArrayVector2 right= new SumArrayVector2(adv,(hi+lo)/2,hi,dimx, dimy);
			  //The new arrays will be separated to different threads
			  left.fork();
			  Vector rightAns = right.compute();
			  Vector leftAns  = left.join();
			  Vector vectorSum = new Vector();
			  vectorSum.x = leftAns.x + rightAns.x;
			  vectorSum.y = leftAns.y + rightAns.y;
			  return vectorSum;     
		  }
	 }
}
