package main;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ManyHello {
	private int[] dimensions = new int[3];
	private float[] xWind;
	private float[] yWind;
	private float[] uplift;
	private int nr;
	private CloudData data;
	private CloudDataParallel dataP;
	
	float time = 0;
	float times = 0;
	float timeP = 0;
	int wait = 5;
	//float timesP = 0;
	static long startTime = 0;
	private static void tick(){
		startTime = System.currentTimeMillis();
	}
	private static float tock(){
		return (System.currentTimeMillis() - startTime) / 1000.0f; 
	}
	public void run() {
		while (true) {
			// tick();
			times++;
			System.out.println(times);
			data = new CloudData();
			data.readData("input2.txt");
			data.count();
			data.writeData("output1.txt", new Vector());
			
			
		//}

		// while (true) {
		//times++;
		//System.out.println(times);
		dataP = new CloudDataParallel();
		dataP.readData("input2.txt");
		dataP.countParallel();
		dataP.writeData("output1.txt", new Vector());
		if (times > wait) {
		time += data.getTimer();
		timeP += dataP.getTimer();
		
		System.out.println("(Sequential) All took (average) " + time / (times - 5) + " seconds");
		System.out.println("(Parallel wind) All took (average) " + timeP / (times - 5) + " seconds");
		}
	}
			
			/*int[] output = new int[5242883];
			float[] outputf = new float[2];
			
			try{ 
				Scanner sc = new Scanner(new File("output1.txt"), "UTF-8");
				
				// input grid dimensions and simulation duration in timesteps
				output[0] = sc.nextInt();
				output[1] = sc.nextInt(); 
				output[2] = sc.nextInt();
				sc.nextLine();
				
				outputf[0] = Float.parseFloat(sc.next());
				outputf[1] = Float.parseFloat(sc.next());
				for(int t = 3; t < 5242883; t++) {
							output[t] = sc.nextInt();
				}
				
				sc.close(); 
			} 
			catch (IOException e){ 
				System.out.println("Unable to open input file "+"output1.txt");
				e.printStackTrace();
			}
			catch (java.util.InputMismatchException e){ 
				System.out.println("Malformed input file "+"output1.txt");
				e.printStackTrace();
			}
			
			int[] outputS = new int[5242883];
			float[] outputfS = new float[2];
			
			try{ 
				Scanner sc = new Scanner(new File("largesample_output.txt"), "UTF-8");
				
				// input grid dimensions and simulation duration in timesteps
				outputS[0] = sc.nextInt();
				outputS[1] = sc.nextInt(); 
				outputS[2] = sc.nextInt();
				sc.nextLine();
				
				outputfS[0] = Float.parseFloat(sc.next());
				outputfS[1] = Float.parseFloat(sc.next());
				for(int t = 3; t < 5242883; t++) {
							outputS[t] = sc.nextInt();
				}
				
				sc.close(); 
			} 
			catch (IOException e){ 
				System.out.println("Unable to open input file "+"output1.txt");
				e.printStackTrace();
			}
			catch (java.util.InputMismatchException e){ 
				System.out.println("Malformed input file "+"output1.txt");
				e.printStackTrace();
			}
			boolean equal = true;
			for(int t = 0; t < 5242883; t++) {
				if (output[t] != outputS[t]) {
					equal = false;
				}
			}
			System.out.println("De är lika: " + equal);*/
	}

	public static void main(String[] args) {
		/*
		 * HelloThread [] t = new HelloThread[21];
		 * 
		 * for(int i=0; i <= 20; ++i) { t[i] = new HelloThread(i); t[i].start(); }
		 */
		(new ManyHello()).run();
	}

}
