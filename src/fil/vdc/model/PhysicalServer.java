package fil.vdc.model;

/**
 * @author Van Huynh Nguyen
 *
 */
public class PhysicalServer {
	final double CPU = 8; 
	final double RAM = 64;
	private String name;
	private double cpu;
	private double ram;
	private int state ; // state =0:  off, state=1:  on
	private SubstrateSwitch switchConnect;
	private double cost=0;
	public PhysicalServer(String name) {
		this.cpu = CPU;
		this.ram = RAM;
		this.name=name;
		this.cost =0;
	}

	//khong dung
	public PhysicalServer(SubstrateSwitch substrateSwitch) {
		this.cpu = CPU;
		this.ram = RAM;
		this.switchConnect = substrateSwitch;
		this.state=0;
	}

	public double getCpu() {
		return cpu;
	}

	public void setCpu(double cpu) {
		this.cpu = cpu;
		if(this.cpu == CPU)
		{
			this.state = 0;
			//System.out.println("trang thai =0");
		}
		else
			this.state = 1;
	}

	public double getRam() {
		return ram;
	}

	public void setRam(double ram) {
		this.ram = ram;
	}

	public SubstrateSwitch getSwitchConnect() {
		return switchConnect;
	}

	public void setSwitchConnect(SubstrateSwitch switchConnect) {
		this.switchConnect = switchConnect;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public double getCost() {
		return this.cost;
	}
	public void setCost(double bandwidth)
	{
		this.cost+=bandwidth;
	}

	public double getCPU() {
		return CPU;
	}

	public double getRAM() {
		return RAM;
	}


}
