package fil.vdc.model;

import java.util.LinkedList;
import java.util.*;
public class Flow {
	private LinkedList<SubstrateLink> flow; 
	//define a flow in a graph with its link and its used bandwidth
	public Flow() {
		flow= new LinkedList<SubstrateLink>();
	}
	public LinkedList<SubstrateLink> getFlow() {
		return flow;
	}
	public void addLink(SubstrateLink link) {
		if(flow.contains(link)) {
			flow.remove(link);
			flow.add(link);
		}
		else flow.add(link);
	}
	public void removeLink(SubstrateLink link) {
		flow.remove(link);
	}
	public double getBandwidth(SubstrateSwitch s1,SubstrateSwitch s2) {
		double bandwidth = 0;
		for(int i=0 ;i < flow.size();i++) {
			SubstrateLink slink = flow.get(i);
			if(slink.getStartSwitch()==s1 && slink.getEndSwitch()==s2) {
				bandwidth=slink.getBandwidth();
			}
		}
		return bandwidth;
	}
	
}
