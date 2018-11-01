package fil.vdc.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import sndlib.core.network.Link;

/**
 * @author Van Huynh Nguyen
 *
 */
public class SubstrateNetwork {
	private LinkedList<SubstrateSwitch> listNode;
	private LinkedList<SubstrateLink> listLink;
	private Map<SubstrateSwitch, LinkedHashSet<SubstrateSwitch>> map;
	// public SubstrateSwitch engressSwitch;
	// public SubstrateSwitch outgressSwitch;
	public LinkedList<SubstrateSwitch> listEngressSwitch;
	public LinkedList<SubstrateSwitch> listOutgressSwitch;

	public Map<Topology, SubstrateSwitch> SwitchToSwitch;
	public Map<Integer, SubstrateSwitch> listSubSwitch;
	private Result r;

	public SubstrateNetwork() {
		this.listNode = new LinkedList<>();
		this.listLink = new LinkedList<>();
		map = new HashMap<SubstrateSwitch, LinkedHashSet<SubstrateSwitch>>();
		this.SwitchToSwitch = new HashMap<>();
		this.listSubSwitch = new HashMap<>();

	}

	public SubstrateNetwork(LinkedList<SubstrateSwitch> listNodeInput, LinkedList<SubstrateLink> listLinkInput) {
		this.listNode = listNodeInput;
		this.listLink = listLinkInput;
		this.SwitchToSwitch = new HashMap<>();

		map = new HashMap<SubstrateSwitch, LinkedHashSet<SubstrateSwitch>>();
		for (SubstrateLink sLink : listLinkInput) {
			SubstrateSwitch srcNode = sLink.getStartSwitch();
			SubstrateSwitch dstNode = sLink.getEndSwitch();
			if (map.containsKey(srcNode)) {
				LinkedHashSet<SubstrateSwitch> listNeighbor = map.get(srcNode);
				listNeighbor.add(dstNode);
				map.put(srcNode, listNeighbor);
			} else {
				LinkedHashSet<SubstrateSwitch> listNeighbor = new LinkedHashSet<>();
				listNeighbor.add(dstNode);
				map.put(srcNode, listNeighbor);
			}
			if (map.containsKey(dstNode)) {
				LinkedHashSet<SubstrateSwitch> listNeighbor = map.get(dstNode);
				listNeighbor.add(srcNode);
				map.put(dstNode, listNeighbor);
			} else {
				LinkedHashSet<SubstrateSwitch> listNeighbor = new LinkedHashSet<>();
				listNeighbor.add(srcNode);
				map.put(dstNode, listNeighbor);
			}
		}
		// for (Entry<SubstrateSwitch, LinkedHashSet<SubstrateSwitch>> entry :
		// map.entrySet()) {
		// System.out.println("Switch " +
		// entry.getKey().getNameSubstrateSwitch());
		// for (SubstrateSwitch s : entry.getValue()) {
		// System.out.println(s.getNameSubstrateSwitch());
		// }
		// }

	}

	public LinkedList<SubstrateSwitch> getListNode() {
		return listNode;
	}

	public void setListNode(LinkedList<SubstrateSwitch> listNode) {
		this.listNode = listNode;
	}

	public LinkedList<SubstrateLink> getListLink() {
		return listLink;
	}

	public Map<Integer, SubstrateSwitch> getListSubSwitch() {
		return listSubSwitch;
	}

	public void setListSubSwitch(Map<Integer, SubstrateSwitch> listSubSwitch) {
		this.listSubSwitch = listSubSwitch;
	}

	public void setListLink(LinkedList<SubstrateLink> listLink) {
//		System.out.println("SET LIST LINK");
		this.listLink = listLink;
		//Set Link for TOPO CORE
		for (SubstrateLink sLink : listLink) {
			SubstrateSwitch srcNode = sLink.getStartSwitch();
			SubstrateSwitch dstNode = sLink.getEndSwitch();
			if (map.containsKey(srcNode)) {
				LinkedHashSet<SubstrateSwitch> listNeighbor = map.get(srcNode);
				listNeighbor.add(dstNode);
				map.put(srcNode, listNeighbor);
			} else {
				LinkedHashSet<SubstrateSwitch> listNeighbor = new LinkedHashSet<>();
				listNeighbor.add(dstNode);
				map.put(srcNode, listNeighbor);
			}
		}
	}

	public double getUtilizationByBW() {
		double BW = 0;
		double total = 0;
		for (SubstrateLink link : listLink) {
			BW += (1000 - link.getBandwidth());
			total += 1000;
		}
		return BW;
	}

	public double getTotal() {
		double total = 0;
		for (SubstrateLink link : listLink) {
			total += 1000;
		}
		return total;
	}

	public void clearNetwork() {
		for (SubstrateSwitch sNode : listNode)
			System.out.println("ERROR: No Code-Clear Substrate Swtich");
		for (SubstrateLink sLink : listLink)
			System.out.println("ERROR: No Codo-Clear Substrate Link");
	}

	public void sortSubstrateSwitch() {
		/* Sort SubstrateSwitch in decreasing order by state, CPU */
		Collections.sort(this.listNode, new Comparator<SubstrateSwitch>() {
			@Override
			public int compare(SubstrateSwitch o1, SubstrateSwitch o2) {

				if (o1.getType() > o2.getType())
					return -1;
				if (o1.getType() < o2.getType())
					return 1;
				if (o1.getType() == o2.getType()) {
					if (o1.getCpu() < o2.getCpu()) {
						return 1;
					}
					if (o1.getCpu() > o2.getCpu()) {
						return -1;
					}
				}
				return 0;
			}
		});
	}

	public void sortSubstrateSwitchByResource() {
		/* Sort SubstrateSwitch in decreasing order by state, CPU */
		Collections.sort(this.listNode, new Comparator<SubstrateSwitch>() {
			@Override
			public int compare(SubstrateSwitch o1, SubstrateSwitch o2) {

				if (o1.getCpu() < o2.getCpu()) {
					return 1;
				}
				if (o1.getCpu() > o2.getCpu()) {
					return -1;
				}

				return 0;
			}
		});
	}

	public LinkedList<SubstrateSwitch> adjacentNodes(SubstrateSwitch sNode) {
		LinkedHashSet<SubstrateSwitch> adjacent = map.get(sNode);
		// System.out.println("Adjectent " + adjacent.size());
		if (adjacent == null) {
			return new LinkedList<SubstrateSwitch>();
		}
		return new LinkedList<SubstrateSwitch>(adjacent);
	}

	public LinkedList<SubstrateSwitch> getListEngressSwitch() {
		return listEngressSwitch;
	}

	public void setListEngressSwitch(LinkedList<SubstrateSwitch> listEngressSwitch) {
		this.listEngressSwitch = listEngressSwitch;
	}

	public LinkedList<SubstrateSwitch> getListOutgressSwitch() {
		return listOutgressSwitch;
	}

	public void setListOutgressSwitch(LinkedList<SubstrateSwitch> listOutgressSwitch) {
		this.listOutgressSwitch = listOutgressSwitch;
	}

	public Map<Topology, SubstrateSwitch> getSwitchToSwitch() {
		return SwitchToSwitch;
	}

	public void setSwitchToSwitch(Map<Topology, SubstrateSwitch> switchToSwitch) {
		SwitchToSwitch = switchToSwitch;
	}

	public void printTopo() {
		r = new Result("InfoTopo");
		for (SubstrateSwitch subNode : this.listNode) {
			System.out.println(
					subNode.getNameSubstrateSwitch() + " is " + subNode.getType() + " resource: " + subNode.getCpu());
			r.info(subNode.getNameSubstrateSwitch() + " is " + subNode.getType() + " resource: " + subNode.getCpu());

		}

		for (SubstrateLink subLink : this.listLink) {
			System.out.println(subLink.getStartSwitch().getNameSubstrateSwitch() + "-"
					+ subLink.getEndSwitch().getNameSubstrateSwitch() + " " + subLink.getBandwidth());
			r.info(subLink.getStartSwitch().getNameSubstrateSwitch() + "-"
					+ subLink.getEndSwitch().getNameSubstrateSwitch() + " " + subLink.getBandwidth());
		}
	}

	// sort list core switch in order to increase by the distance from ingress
	// to next hop

	public Object clone() {
		SubstrateNetwork s = new SubstrateNetwork(this.listNode, this.listLink);
		return s;
	}

	public Map<SubstrateSwitch, LinkedHashSet<SubstrateSwitch>> getMap() {
		return map;
	}

	public void setMap(Map<SubstrateSwitch, LinkedHashSet<SubstrateSwitch>> map) {
		this.map = map;
	}

}
