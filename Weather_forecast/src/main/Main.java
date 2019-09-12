package main;
/**
 * Main is the class which is used to run and test both the sequential 
 * and parallel program.
 * <P>
 * It uses the class CloudData {@link CloudData} to perform and time sequential operations while 
 * CloudDataParallel {@link CloudDataParallel} is used to run the operations with parallel algorithms.
 * <P>
 * CloudData and CloudDataParallel are never active at the same time, they are
 * tested separately to avoid corrupt measurements and the first five runs are 
 * not measured.
 * 
 * @author      Philip Nylén
 * @version     %I%, %G%
 * @since       1.0
 */
public class Main {
	private CloudData data;
	private CloudDataParallel dataP;

	float time = 1000;
	float times = 0;
	float timeP = 0;
	int wait = 5;

	public void run() {

		while (true) {
			times++;
			System.out.println(times - 5);
			data = new CloudData();
			data.readData("input15_30_30.txt");
			data.count();
			data.writeData("output15_30_30.txt", new Vector());

			/*dataP = new CloudDataParallel();
			dataP.readData("input15_30_30.txt");
			dataP.countParallel();
			dataP.writeData("output15_30_30.txt", new Vector());*/
			if (times > wait) {
				if (time > data.getTimer()) {
					time = data.getTimer();
				}
				System.out.println("(Sequential) Best time: " + time + " seconds");
				//timeP += dataP.getTimer();

				//System.out.println("(Sequential) All took (average) " + time / (times - 5) + " seconds");
				
				//System.out.println("(Parallel wind) All took (average) " + timeP / (times - 5) + " seconds");
			}
		}

	}

	public static void main(String[] args) {
		(new Main()).run();
	}

}
