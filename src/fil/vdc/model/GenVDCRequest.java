package fil.vdc.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

/**
 * @author Van Huynh Nguyen
 *
 */
public class GenVDCRequest {
	private final int CPU = 2; // 2 cores each VM
	private final int RAM = 16; // 16GB RAMs each VM
	private final double ALPHA = 0.5;
	private final double BETA = 0.5;
	private final double startTimeSimulation = 0; // hour
	private final double endTimeSimulation = 24; // 60 hour
	private Map<VDCRequest, Integer> listVDC;
	private double lambda;

	private int count;

	public GenVDCRequest() {
		listVDC = new HashMap<>();
		this.count = 1;
	}

	public Map<VDCRequest, Integer> gen(double percent, double mu, int k, int numberVMMax) {
		// this.percent =numVM*0.1;
		// Double t= new Double(this.percent*k*k*k);
		// int total = t.intValue();
		// Random rand= new Random();
		// this.numVM = numVM;
		// lambda = numVM;
		// mu = total/(this.numVM*lambda);
		// while(mu>4)
		// {
		// lambda++;
		// mu = total/(this.numVM*lambda);
		// }
		// if(mu==0)
		// {
		// mu=1;
		// }
		Integer[] arraySwitch = {5,7,9,10};

		lambda = (1.0 * percent * 2 * mu * 3 * (k * k * k)) / (100.0 * (2 + numberVMMax));
		// System.out.println("lambda " + lambda);
		double nextArrival = 0; // time of next arrival
		while (nextArrival < endTimeSimulation) {
			Random random = new Random();
			int nameSwitch = random.nextInt(4);
			// createVDC with startTime, life time
			listVDC.put(createVDC(nextArrival, StdRandom.exp(mu), count, numberVMMax), arraySwitch[nameSwitch]);
			nextArrival += StdRandom.exp(lambda);
			count++;
		}
		return listVDC;

	}

	public VDCRequest createVDC(double timeBegin, double lifeTime, int vdcId, int numberVMMax) {
		Map<Integer, VirtualMachine> listVM = new HashMap<>();
		LinkedList<VirtualLink> listVLink = new LinkedList<>();
		Random rand = new Random();
		WaxmanGenerator waxMan = new WaxmanGenerator();
		int numVM = rand.nextInt(numberVMMax - 3) + 4; // num VM : uniform 4 -
														// 40 VMs

		for (int i = 1; i <= numVM; i++) {
			VirtualMachine vm = new VirtualMachine(String.valueOf(i), CPU, RAM, vdcId);
			listVM.put(i, vm);
		}

		listVLink = waxMan.Waxman(numVM, ALPHA, BETA, listVM);
		VDCRequest vdc = new VDCRequest(listVM, listVLink, timeBegin, lifeTime, vdcId);
		vdc.setNumVM(numVM);
		int bwrequest = rand.nextInt(20) + 5;
		vdc.setBwrequest(bwrequest);
		return vdc;
	}
}
