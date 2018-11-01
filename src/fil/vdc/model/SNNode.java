package fil.vdc.model;

public class SNNode {
	private VirtualMachine VM;
	private PhysicalServer PM;
	boolean isVM;
	public SNNode() {

	}
	
	public void setVMNode(VirtualMachine vm) {
		VM=vm;
	}
	public VirtualMachine getVMNode() {
		return VM;
	}
	
	public void setPMNode(PhysicalServer pm) {
		PM=pm;
	}
	public PhysicalServer getPMNode() {
		return PM;
	}
	
	public void setisVM(boolean a) {
		isVM=a;
	}
	public boolean getisVM() {
		return isVM;
	}
}
