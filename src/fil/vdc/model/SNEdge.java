package fil.vdc.model;

public class SNEdge {
	private SNNode startnode;
	private SNNode endnode;
	private double weight;
	private double capacity;
	public SNEdge(SNNode start,SNNode end,double weight) {
		this.startnode= start;
		this.endnode= end;
		this.weight=weight;
		this.capacity=1;
	}
	public void setStartNode(SNNode node) {
		startnode= node;
	}
	public void setEndNode(SNNode node) {
		endnode = node;
	}
	public void setWeight(double w) {
		weight= w;
	}
	public SNNode getStartNode() {
		return startnode;
	}
	public SNNode getEndNode() {
		return endnode;
	}
	public double getWeight() {
		return weight;
	}
	public void setCapacity(double c) {
		capacity=c;
	}
	public double getCapacity() {
		return capacity;
	}
}
