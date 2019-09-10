package main;

public class ManyHello {
	private CloudData data;
	private CloudDataParallel dataP;

	float time = 0;
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

			dataP = new CloudDataParallel();
			dataP.readData("input15_30_30.txt");
			dataP.countParallel();
			dataP.writeData("output15_30_30.txt", new Vector());
			if (times > wait) {
				time += data.getTimer();
				timeP += dataP.getTimer();

				System.out.println("(Sequential) All took (average) " + time / (times - 5) + " seconds");
				System.out.println("(Parallel wind) All took (average) " + timeP / (times - 5) + " seconds");
			}
		}

	}

	public static void main(String[] args) {
		(new ManyHello()).run();
	}

}
