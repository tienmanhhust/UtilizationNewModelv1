package fil.vdc.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Build the new Virtual Data Center (VDC) Request
 * 
 * @author Van Huynh Nguyen
 *
 */
public class VDCRequest {
	private int vdcID;
	private int numVM;
	private Map<Integer, VirtualMachine> listVirVM;
	private LinkedList<VirtualLink> listVirLink;
	// private LinkedList<VirtualSwitch> listVirSwitch;
	private double startTime;
	private double lifeTime;
	private double bwrequest;

	/**
	 * Constructs new VDC Request
	 */
	public VDCRequest() {
		this.listVirVM = new HashMap<>();
		this.listVirLink = new LinkedList<>();
		// this.listVirSwitch = new LinkedList<>();
		this.numVM = 0;
		this.vdcID = 0;
		this.bwrequest = 0;
	}

	/**
	 * Constructs new VDC Request
	 * 
	 * @param vm
	 *            The virtual machine
	 * @param listVLink
	 *            List virtual links
	 * @param listVSwitch
	 *            List virtual switchs
	 */
	public VDCRequest(Map<Integer, VirtualMachine> listVM, LinkedList<VirtualLink> listVLink, double startTime,
			double lifeTime, int vdcID) {
		this.listVirVM = listVM;
		this.listVirLink = listVLink;
		// this.listVirSwitch = listVSwitch;
		this.startTime = startTime;
		this.lifeTime = lifeTime;
		this.vdcID = vdcID;
	}

	public int getNumVM() {
		return numVM;
	}

	public void setNumVM(int numVM) {
		this.numVM = numVM;
	}

	public Map<Integer, VirtualMachine> getListVirVM() {
		return listVirVM;
	}

	public void setListVirVM(Map<Integer, VirtualMachine> listVirVM) {
		this.listVirVM = listVirVM;
	}

	public LinkedList<VirtualLink> getListVirLink() {
		return listVirLink;
	}

	public void setListVirLink(LinkedList<VirtualLink> listVirLink) {
		this.listVirLink = listVirLink;
	}

	public double getStartTime() {
		return startTime;
	}

	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}

	public double getEndTime() {
		return this.startTime + this.lifeTime;
	}
	public double getLifeTime() {
		return this.lifeTime;
	}
	
	public int getVdcID() {
		return vdcID;
	}

	public void setVdcID(int vdcID) {
		this.vdcID = vdcID;
	}

	public double getBwrequest() {
		return bwrequest;
	}

	public void setBwrequest(double bwrequest) {
		this.bwrequest = bwrequest;
	}

	public double getTotalCPU() {
		double total = 0;
		for (Entry<Integer, VirtualMachine> entry : listVirVM.entrySet())
			total += entry.getValue().getCPU();

		return total;
	}
}
