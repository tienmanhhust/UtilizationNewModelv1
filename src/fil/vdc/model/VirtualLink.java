package fil.vdc.model;

/**
 * Builds virtual link
 * 
 * @author Van Huynh Nguyen
 *
 */
public class VirtualLink {
	private VirtualMachine sVM;
	private VirtualMachine dVM;
	private double bandwidthRequest;

	/**
	 * Constructs virtual link
	 */
	public VirtualLink() {
		this.sVM = new VirtualMachine();
		this.dVM = new VirtualMachine();
		this.bandwidthRequest = 0;

	}

	/**
	 * Construct virtual link
	 * 
	 * @param s
	 *            Source switch
	 * @param d
	 *            Destination switch
	 * @param bandwidth
	 *            Bandwidth between source and destination switch
	 */
	public VirtualLink(VirtualMachine s, VirtualMachine d, double bandwidth) {
		this.sVM = s;
		this.dVM = d;
		this.bandwidthRequest = bandwidth;
	}

	public VirtualMachine getsVM() {
		return sVM;
	}

	public void setsVM(VirtualMachine sVM) {
		this.sVM = sVM;
	}

	public VirtualMachine getdVM() {
		return dVM;
	}

	public void setdVM(VirtualMachine dVM) {
		this.dVM = dVM;
	}

	public double getBandwidthRequest() {
		return bandwidthRequest;
	}

	public void setBandwidthRequest(double bandwidthRequest) {
		this.bandwidthRequest = bandwidthRequest;
	}

}
