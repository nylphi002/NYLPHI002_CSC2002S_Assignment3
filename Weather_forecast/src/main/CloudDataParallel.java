package main;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.io.FileWriter;
import java.io.PrintWriter;

public class CloudDataParallel {
static long startTime = 0;
private float timer;
	private static void tick(){
		startTime = System.currentTimeMillis();
	}
	private static float tock(){
		return (System.currentTimeMillis() - startTime) / 1000.0f; 
	}
	
	static final ForkJoinPool fjPool = new ForkJoinPool();
	static int[] classify(Vector[][][] advection, float[][][] convection, int dim, int dimx, int dimy){
		  return fjPool.invoke(new Classify(advection, convection,0,dim, dimx, dimy));
	}
	static final ForkJoinPool fjPool2 = new ForkJoinPool();
	static Vector countPrevailing(Vector[][][] advection, float[][][] convection, int dim, int dimx, int dimy){
		  return fjPool2.invoke(new SumArrayVector2(advection,0,dim, dimx, dimy));
	}
	
	Vector [][][] advection; // in-plane regular grid of wind vectors, that evolve over time
	float [][][] convection; // vertical air movement strength, that evolves over time
	int [] classification; // cloud type per element
	int dimx, dimy, dimt; // data dimensions
	Vector sumVec = new Vector();
	
	// overall number of elements in the timeline grids
	int dim(){
		return dimt*dimx*dimy;
	}
	
	public float getTimer() {
		return timer;
	}
	
	// convert linear position into 3D location in simulation grid
	void locate(int pos, int [] ind)
	{
		ind[0] = (int) pos / (dimx*dimy); // t
		ind[1] = (pos % (dimx*dimy)) / dimy; // x
		ind[2] = pos % (dimy); // y
	}
	
	// read cloud simulation data from file
	void readData(String fileName) { 
		try{ 
			Scanner sc = new Scanner(new File(fileName), "UTF-8");
			
			// input grid dimensions and simulation duration in timesteps
			dimt = sc.nextInt();
			dimx = sc.nextInt(); 
			dimy = sc.nextInt();
			sc.nextLine();
			
			// initialize and load advection (wind direction and strength) and convection
			advection = new Vector[dimt][dimx][dimy];
			convection = new float[dimt][dimx][dimy];
			for(int t = 0; t < dimt; t++)
				for(int x = 0; x < dimx; x++)
					for(int y = 0; y < dimy; y++) {
						advection[t][x][y] = new Vector();
						advection[t][x][y].x = Float.parseFloat(sc.next());
						advection[t][x][y].y = Float.parseFloat(sc.next());
						convection[t][x][y] = Float.parseFloat(sc.next());
					}
			
			
			classification = new int[dim()];
			sc.close(); 
		} 
		catch (IOException e){ 
			System.out.println("Unable to open input file "+fileName);
			e.printStackTrace();
		}
		catch (java.util.InputMismatchException e){ 
			System.out.println("Malformed input file "+fileName);
			e.printStackTrace();
		}
	}
	
	void countParallel() {
		System.gc();
		timer = 0;
		tick();
		classification = classify(advection,convection,dim() - 1,dimx,dimy);
		sumVec = countPrevailing(advection,convection,dim() - 1,dimx,dimy);
		sumVec.x = sumVec.x/dim();
		sumVec.y = sumVec.y/dim();
		float time1 = tock();
		timer = time1;
		System.out.println("(Parallel) Calculate took "+ time1 +" seconds");
	}
	
	// write classification output to file
	void writeData(String fileName, Vector wind){
		 try{ 
			 FileWriter fileWriter = new FileWriter(fileName);
			 PrintWriter printWriter = new PrintWriter(fileWriter);
			 printWriter.printf("%d %d %d\n", dimt, dimx, dimy);
			 printWriter.printf(Locale.US,"%f %f\n", sumVec.x, sumVec.y);
			 
			 for(int t = 0; t < dim(); t++) {
					printWriter.printf("%d ", classification[t]);
					if (t % (dimy - 1) * (dimx - 1) == (dimy - 1) * (dimx - 1) - 1) {
						printWriter.printf("\n");
					}
			}
				 
			 printWriter.close();
		 }
		 catch (IOException e){
			 System.out.println("Unable to open output file "+fileName);
				e.printStackTrace();
		 }
		 System.out.println("KLAR");
	}
}