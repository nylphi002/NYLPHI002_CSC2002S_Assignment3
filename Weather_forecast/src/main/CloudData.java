package main;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.io.FileWriter;
import java.io.PrintWriter;

public class CloudData {
static long startTime = 0;
private float timer;
	
	private static void tick(){
		startTime = System.currentTimeMillis();
	}
	private static float tock(){
		return (System.currentTimeMillis() - startTime) / 1000.0f; 
	}
	
	/*static final ForkJoinPool fjPool2 = new ForkJoinPool();
	static int[][][] classify(Vector[][][] advection, float[][][] convection, int dimT, int dimx, int dimy){
		  return fjPool2.invoke(new Classify(advection, convection,0,dimT, dimx, dimy));
	}*/
	
	Vector [][][] advection; // in-plane regular grid of wind vectors, that evolve over time
	float [][][] convection; // vertical air movement strength, that evolves over time
	int [][][] classification; // cloud type per grid point, evolving over time
	int dimx, dimy, dimt; // data dimensions
	float xT = 0;
	float yT = 0;
	// overall number of elements in the timeline grids
	int dim(){
		return dimt*dimx*dimy;
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
			
			classification = new int[dimt][dimx][dimy];
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
	
	void count() {
		timer = 0;
		System.gc();
		/*tick();
		for(int t = 0; t < dimt; t++) {
			for(int x = 0; x < dimx; x++) {
				for(int y = 0; y < dimy; y++){
					xT += advection[t][x][y].x; 
					yT += advection[t][x][y].y; 
				} 
			}
		}
		xT=xT/dim();
		yT=yT/dim();
		float time3 = tock();*/
		
		//timer = 0;
		//tick();
		//classification = classify(advection,convection,dim() - 1,dimx,dimy);
		//time3 = tock();
		//float time1 = tock();
		
		//startTime = 0;
		//tick();
		/*Float[][][] magVec = new Float[dimt][dimx][dimy];
		int count = 0;
		for (int t = 0; t < dimt; t++) {
			for (int x = 0; x < dimx; x++) {
				for (int y = 0; y < dimy; y++) {
					int noOf = 0;
					float xAvg = 0;
					float yAvg = 0;
					int[] tempArr = new int[3];
					locate(count, tempArr);

					for (int i = 0; i < 3; i++) {
						for (int j = 0; j < 3; j++) {
							if (tempArr[1] - 1 + i >= 0 && tempArr[2] - 1 + j >= 0 && tempArr[1] - 1 + i < dimx && tempArr[2] - 1 + j < dimy) {
								xAvg += advection[tempArr[0]][tempArr[1] - 1 + i][tempArr[2] - 1 + j].x;
								yAvg += advection[tempArr[0]][tempArr[1] - 1 + i][tempArr[2] - 1 + j].y;
								noOf ++;
							}
						}
					}
					xAvg = xAvg/noOf;
					yAvg = yAvg/noOf;
					magVec[t][x][y] = (float) Math.sqrt(Math.pow(xAvg, 2) + Math.pow(yAvg, 2));
					count++;
				}
			}
		}*/
		
		/*Float[][][] magVec = new Float[dimt][dimx][dimy];
		for (int t = 0; t < dimt; t++) {
			for (int x = 0; x < dimx; x++) {
				for (int y = 0; y < dimy; y++) {
					int noOf = 0;
					float xAvg = 0;
					float yAvg = 0;
					for (int i = 0; i < 3; i++) {
						for (int j = 0; j < 3; j++) {
							if (x - 1 + i >= 0 && y - 1 + j >= 0 && x - 1 + i < dimx && y - 1 + j < dimy) {
								xAvg += advection[t][x - 1 + i][y - 1 + j].x;
								yAvg += advection[t][x - 1 + i][y - 1 + j].y;
								noOf ++;
							}
						}
					}
					magVec[t][x][y] = (float) Math.sqrt(Math.pow(xAvg/noOf, 2) + Math.pow(yAvg/noOf, 2));
				}
			}
		}
		//float time2 = tock();
		
		
		//startTime = 0;
		//tick();
		for(int t = 0; t < dimt; t++) {
			for(int x = 0; x < dimx; x++) {
				for(int y = 0; y < dimy; y++){
					if (Math.abs(convection[t][x][y]) > magVec[t][x][y]) {
						classification[t][x][y] = 0;
					} else if (Math.abs(convection[t][x][y]) <= magVec[t][x][y] && magVec[t][x][y] > 0.2) {
						classification[t][x][y] = 1;
					} else {
						classification[t][x][y] = 2;
					}
				}
			}
		}*/
		tick();
		Float[][][] magVec = new Float[dimt][dimx][dimy];
		for (int t = 0; t < dimt; t++) {
			for (int x = 0; x < dimx; x++) {
				for (int y = 0; y < dimy; y++) {
					xT += advection[t][x][y].x; 
					yT += advection[t][x][y].y;
					int noOf = 0;
					float xAvg = 0;
					float yAvg = 0;
					for (int i = 0; i < 3; i++) {
						for (int j = 0; j < 3; j++) {
							if (x - 1 + i >= 0 && y - 1 + j >= 0 && x - 1 + i < dimx && y - 1 + j < dimy) {
								xAvg += advection[t][x - 1 + i][y - 1 + j].x;
								yAvg += advection[t][x - 1 + i][y - 1 + j].y;
								noOf ++;
							}
						}
					}
					magVec[t][x][y] = (float) Math.sqrt(Math.pow(xAvg/noOf, 2) + Math.pow(yAvg/noOf, 2));
					if (Math.abs(convection[t][x][y]) > magVec[t][x][y]) {
						classification[t][x][y] = 0;
					} else if (Math.abs(convection[t][x][y]) <= magVec[t][x][y] && magVec[t][x][y] > 0.2) {
						classification[t][x][y] = 1;
					} else {
						classification[t][x][y] = 2;
					}
				}
			}
		}
		xT=xT/dim();
		yT=yT/dim();
		float time3 = tock();
		timer = time3;
		//System.out.println("(Sequential) Calculate global average took "+ time1 +" seconds");
		//System.out.println("(Sequential) Calculate local average took "+ time2 +" seconds");
		System.out.println("(Sequential) Count took "+ time3 +" seconds");
		//System.out.println("(Sequential) Count KLAR");
	}
	
	public float getTimer() {
		return timer;
	}
	
	// write classification output to file
	void writeData(String fileName, Vector wind){
		 try{ 
			 FileWriter fileWriter = new FileWriter(fileName);
			 PrintWriter printWriter = new PrintWriter(fileWriter);
			 printWriter.printf("%d %d %d\n", dimt, dimx, dimy);
			 printWriter.printf(Locale.US,"%f %f\n", xT, yT);
			 
			 for(int t = 0; t < dimt; t++){
				 for(int x = 0; x < dimx; x++){
					for(int y = 0; y < dimy; y++){
						printWriter.printf("%d ", classification[t][x][y]);
					}
				 }
				 printWriter.printf("\n");
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
