package fil.vdc.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Van Huynh Nguyen
 *
 */
public class GenSubstrateNetwork {
	private int numNode;
	private double alpha;
	private double beta;

	public GenSubstrateNetwork(int numNodeInput, double alphaInput, double betaInput) {
		this.numNode = numNodeInput;
		this.alpha = alphaInput;
		this.beta = betaInput;
	}

	public SubstrateNetwork gen() {

		LinkedList<SubstrateSwitch> listSNode = new LinkedList<>();
		Map<Integer, SubstrateSwitch> listNode = new HashMap<>();
		LinkedList<SubstrateLink> listSLink = new LinkedList<>();
		for (int i = 1; i <= numNode; i++) {
			SubstrateSwitch sNode = new SubstrateSwitch(String.valueOf(i), 100, false);
			listSNode.add(sNode);
			listNode.put(i, sNode);
		}
		WaxmanGenerator wax = new WaxmanGenerator();
		listSLink = wax.WaxmanSub(numNode, alpha, beta, listNode);
		SubstrateNetwork subNetwork = new SubstrateNetwork(listSNode, listSLink);

		for (SubstrateSwitch sw : subNetwork.getListNode()) {
			Map<SubstrateSwitch, Double> bwport = sw.getBandwidthPort();
			LinkedList<SubstrateSwitch> neighbor = subNetwork.adjacentNodes(sw);
			for (SubstrateSwitch s : neighbor) {
				bwport.put(s, (double) 0);
			}
		}
		subNetwork.setListLink(listSLink);
		subNetwork.setListNode(listSNode);

		return subNetwork;
	}

	public SubstrateNetwork genAbileneTopo() {
		double bw = 1000;
		LinkedList<SubstrateSwitch> listSw = new LinkedList<>();
		LinkedList<SubstrateLink> listLink = new LinkedList<>();
		Map<Integer, SubstrateSwitch> list = new HashMap<>();
		for (int i = 1; i <= 12; i++) {
			SubstrateSwitch s = new SubstrateSwitch(String.valueOf(i), 100, false);
			listSw.add(s);
			list.put(i, s);
		}
		SubstrateLink l1 = new SubstrateLink(list.get(1), list.get(2), bw);
		SubstrateLink l2 = new SubstrateLink(list.get(1), list.get(12), bw);
		SubstrateLink l3 = new SubstrateLink(list.get(2), list.get(3), bw);
		SubstrateLink l4 = new SubstrateLink(list.get(3), list.get(5), bw);
		SubstrateLink l5 = new SubstrateLink(list.get(3), list.get(10), bw);
		SubstrateLink l6 = new SubstrateLink(list.get(5), list.get(4), bw);
		SubstrateLink l7 = new SubstrateLink(list.get(4), list.get(6), bw);
		SubstrateLink l8 = new SubstrateLink(list.get(5), list.get(9), bw);
		SubstrateLink l9 = new SubstrateLink(list.get(6), list.get(7), bw);
		SubstrateLink l10 = new SubstrateLink(list.get(9), list.get(8), bw);
		SubstrateLink l11 = new SubstrateLink(list.get(9), list.get(10), bw);
		SubstrateLink l12 = new SubstrateLink(list.get(9), list.get(7), bw);
		SubstrateLink l13 = new SubstrateLink(list.get(12), list.get(11), bw);
		SubstrateLink l14 = new SubstrateLink(list.get(2), list.get(12), bw);
		SubstrateLink l15 = new SubstrateLink(list.get(11), list.get(10), bw);

		listLink.add(l15);
		listLink.add(l14);
		listLink.add(l13);
		listLink.add(l12);
		listLink.add(l11);
		listLink.add(l10);
		listLink.add(l9);
		listLink.add(l8);
		listLink.add(l7);
		listLink.add(l6);
		listLink.add(l5);
		listLink.add(l4);
		listLink.add(l3);
		listLink.add(l2);
		listLink.add(l1);

		SubstrateNetwork subNetwork = new SubstrateNetwork(listSw, listLink);
		
		for (SubstrateSwitch sw : subNetwork.getListNode()) {
			Map<SubstrateSwitch, Double> bwport = sw.getBandwidthPort();
			LinkedList<SubstrateSwitch> neighbor = subNetwork.adjacentNodes(sw);
			for (SubstrateSwitch s : neighbor) {
				bwport.put(s, (double) 0);
			}
		}
		
		subNetwork.setListSubSwitch(list);
		subNetwork.setListNode(listSw);
		subNetwork.setListLink(listLink);

		return subNetwork;

	}

	public int getNumNode() {
		return numNode;
	}

	public void setNumNode(int numNode) {
		this.numNode = numNode;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public double getBeta() {
		return beta;
	}

	public void setBeta(double beta) {
		this.beta = beta;
	}

	public SubstrateNetwork genTest() {
		SubstrateNetwork subNetwork = new SubstrateNetwork();
		LinkedList<SubstrateSwitch> listSNode = new LinkedList<>();
		LinkedList<SubstrateLink> listSLink = new LinkedList<>();
		for (int i = 1; i <= 4; i++) {
			SubstrateSwitch sNode = new SubstrateSwitch(String.valueOf(i), 100, false);
			listSNode.add(sNode);
		}
		for (int i = 0; i < 3; i++) {
			SubstrateLink sLink1 = new SubstrateLink(listSNode.get(i), listSNode.get(i + 1), 1000);
			SubstrateLink sLink2 = new SubstrateLink(listSNode.get(i + 1), listSNode.get(i), 1000);
			listSLink.add(sLink1);
			listSLink.add(sLink2);
		}
		SubstrateLink sLink1 = new SubstrateLink(listSNode.get(0), listSNode.get(3), 1000);
		SubstrateLink sLink2 = new SubstrateLink(listSNode.get(3), listSNode.get(0), 1000);
		listSLink.add(sLink1);
		listSLink.add(sLink2);
		subNetwork.setListLink(listSLink);
		subNetwork.setListNode(listSNode);
		return subNetwork;
	}

}
