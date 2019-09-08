package main;

import java.util.concurrent.RecursiveTask;

public class SumArrayVector2 extends RecursiveTask<Vector>  {
	  int lo; // arguments
	  int hi;
	  int dimx;
	  int dimy;
	  Vector[][][] adv;
	  static final int SEQUENTIAL_CUTOFF=60000;

	 // Vector ans = 0; // result 
	    
	  SumArrayVector2(Vector[][][] a, int l, int h, int x, int y) { 
	    lo=l; hi=h; adv=a; dimx = x; dimy = y;
	  }
	  
	  void locate(int pos, int[] ind) {
			ind[0] = (int) pos / (dimx * dimy); // t
			ind[1] = (pos % (dimx * dimy)) / dimy; // x
			ind[2] = pos % (dimy); // y
		}

	  protected Vector compute() {// return answer - instead of run
		if ((hi - lo) < SEQUENTIAL_CUTOFF) {
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
		} else {
			  SumArrayVector2 left = new SumArrayVector2(adv,lo,(hi+lo)/2, dimx, dimy);
			  SumArrayVector2 right= new SumArrayVector2(adv,(hi+lo)/2,hi,dimx, dimy);
			  
			  // order of next 4 lines
			  // essential – why?
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
