package main;
/**
 * Main is the class used to run and test both the sequential 
 * and parallel program.
 * <P>
 * It uses the class {@link CloudData} to perform and time sequential operations while 
 * {@link CloudDataParallel} is used to run the operations with parallel algorithms. 
 * <P>
 * Block 2 should be surrounded with a block comment to run the program sequential and block 1 surrounded to run it parallel.
 * <P>
 * CloudData and CloudDataParallel are never active at the same time, they are
 * tested separately to avoid corrupt measurements and the first five runs are 
 * not measured in both cases.
 * 
 * @author      Philip Nylén
 * @version     1.5
 * @since       1.0
 */
public class Main {
	private CloudData data;
	private CloudDataParallel dataP;

	float time = 1000;
	float times = 0;
	float timeP = 0;
	int wait = 5;
	//Run is the method that runs the program
	public void run(String[] args) {

		while (true) {
			times++;
			System.out.println(times - 5);
			
			//Block 1 - sequential
			/*data = new CloudData();
			data.readData(args[0]);
			data.count();
			data.writeData(args[1], new Vector());
			if (times > wait) {
				if (time > data.getTimer()) {
					time = data.getTimer();
				}
				System.out.println("(Sequential) Best time: " + time + " seconds");
			}*/
			
			//Block 2 - parallel
			dataP = new CloudDataParallel();
			dataP.readData(args[0]);
			dataP.countParallel();
			dataP.writeData(args[1], new Vector());
			if (times > wait) {
				timeP += dataP.getTimer();
				System.out.println("(Parallel wind) All took (average) " + timeP / (times - 5) + " seconds");
			}
		}

	}

	public static void main(String[] args) {	
		(new Main()).run(args);
	}

}
