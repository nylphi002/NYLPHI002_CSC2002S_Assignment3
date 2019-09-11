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
	
	Vector [][][] advection;
	float [][][] convection;
	int [][][] classification;
	int dimx, dimy, dimt;
	float xT = 0;
	float yT = 0;
	
	int dim(){
		return dimt*dimx*dimy;
	}
	
	void locate(int pos, int [] ind)
	{
		ind[0] = (int) pos / (dimx*dimy); // t
		ind[1] = (pos % (dimx*dimy)) / dimy; // x
		ind[2] = pos % (dimy); // y
	}
	
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
		tick();
		//Float[][][] magVec = new Float[dimt][dimx][dimy];
		float f;
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
					//magVec[t][x][y] = (float) Math.sqrt(Math.pow(xAvg/noOf, 2) + Math.pow(yAvg/noOf, 2));
					f = (float) Math.sqrt(Math.pow(xAvg/noOf, 2) + Math.pow(yAvg/noOf, 2));
					if (Math.abs(convection[t][x][y]) > f) {
						classification[t][x][y] = 0;
					} else if (Math.abs(convection[t][x][y]) <= f && f > 0.2) {
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
		System.out.println("(Sequential) Count took "+ time3 +" seconds");
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
