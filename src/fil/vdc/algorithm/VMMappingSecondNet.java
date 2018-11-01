package fil.vdc.algorithm;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fil.vdc.model.FatTree;
import fil.vdc.model.MaxBipartite;
import fil.vdc.model.PhysicalServer;
import fil.vdc.model.SecondNetMatching;
import fil.vdc.model.SubstrateSwitch;
import fil.vdc.model.Topology;
import fil.vdc.model.VDCRequest;
import fil.vdc.model.VirtualLink;
import fil.vdc.model.VirtualMachine;

public class VMMappingSecondNet {

	private VDCRequest VDCRequest;
	private Map<Integer, VirtualMachine> listVM;
	private boolean isSuccess;
	private Map<PhysicalServer, SubstrateSwitch> listLinksServer;
	private Map<SubstrateSwitch, LinkedList<PhysicalServer>> listPhyAvailableEdgeSwitch; // for
																							// near
																							// groups
	private Map<LinkedList<SubstrateSwitch>, LinkedList<PhysicalServer>> listMiddleSatisfied; // for
																								// middle
																								// groups
	private Map<LinkedList<SubstrateSwitch>, LinkedList<PhysicalServer>> listFarSatisfied; // for
																							// far
																							// groups
	private Map<LinkedList<SubstrateSwitch>, LinkedList<PhysicalServer>> listNearSatisfied;
	// list Physical server connected to physical machine
	private Map<SubstrateSwitch, LinkedList<PhysicalServer>> listPhysConEdge;
	private Map<LinkedList<SubstrateSwitch>, LinkedList<PhysicalServer>> selectedGroup;
	private boolean isSatisfiedCPUandRAM;
	private boolean isNearGroupSatisfied;
	private boolean isMiddleGroupSatisfied;
	private boolean isFarGroupSatisfied;
	private Map<VirtualMachine, PhysicalServer> mappingResults;
	private int numVM;
	private int numPhyTurnOnSecondNet;
	private Map<Integer, LinkedList<PhysicalServer>> listExceptPhy; // dung de
																	// luu cac
																	// group da
																	// chon
																	// nhung
																	// khong
																	// thanh
																	// cong
	private LinkedList<PhysicalServer> listPhyMaped;
	private LinkedList<VirtualLink> listVirLink;
	private double powerPhyConsumedSecondNet;

	public VMMappingSecondNet() {
		VDCRequest = new VDCRequest();
		isSuccess = false;
		listLinksServer = new HashMap<>();
		selectedGroup = new HashMap<>();
		listPhysConEdge = new HashMap<>();
		listPhyAvailableEdgeSwitch = new HashMap<>();
		isNearGroupSatisfied = false;
		isMiddleGroupSatisfied = false;
		isFarGroupSatisfied = false;
		listNearSatisfied = new HashMap<>();
		listMiddleSatisfied = new HashMap<>();
		listFarSatisfied = new HashMap<>();
		isSatisfiedCPUandRAM = true;
		mappingResults = new HashMap<>();
		numVM = 0;
		numPhyTurnOnSecondNet = 0;
		listExceptPhy = new HashMap<>();
		listPhyMaped = new LinkedList<>();
		listVirLink = new LinkedList<>();
		powerPhyConsumedSecondNet = 0;
	}

	public Topology runSecondNet(VDCRequest VDC, Topology topo, FatTree fatTree) {

		// Step 1 : Get vDC and Sort VM by Resource, ID
		this.VDCRequest = VDC;
		listVM = VDC.getListVirVM();
		listVirLink = VDC.getListVirLink();
		
		// ***************************************************************//

		// Step 2 : Iterator groups and choose the minimum adding consumed
		// energy one.
		numVM = VDC.getNumVM(); // number of VM belong to VDC
		listLinksServer = topo.getListLinksServer();
		Map<Integer, PhysicalServer> listPhysical = topo.getListPhyServers();
		// get number Physical server in each near group can host the minimum
		// VM'resource
		for (Entry<PhysicalServer, SubstrateSwitch> entry : listLinksServer.entrySet()) {
			// stupid, get physical server
			PhysicalServer phy = entry.getKey();
			// System.out.println("Physical server "+phy.getName()+" CPU
			// "+phy.getCpu()+" RAM "+phy.getRam());
			SubstrateSwitch edge = entry.getValue();
			if (listPhysConEdge.containsKey(edge)) {
				LinkedList<PhysicalServer> listPhy = listPhysConEdge.get(edge);
				listPhy.add(phy);
				listPhysConEdge.put(edge, listPhy);
			} else {
				LinkedList<PhysicalServer> listPhy = new LinkedList<>();
				listPhy.add(phy);
				listPhysConEdge.put(edge, listPhy);
			}
		}

		// we sort edge switch in increasing order by their ID
		// sortEdgeSwitch(); SecondNet does not sort Edge switch

		// get Resource max, which is required by VMs
		// double maxResource = getMaxResourceRequired(VDC); // future???

		/*
		 * #####################################################################
		 * #######
		 */

		// Step 2.1: iterator all near groups on FatTree topology and find the
		// minimum consumed energy, if don't, do step 2.2
		// iterator all physical machine connect to each edge
		for (Entry<SubstrateSwitch, LinkedList<PhysicalServer>> entry : listPhysConEdge.entrySet()) {
			SubstrateSwitch edge = entry.getKey();
			LinkedList<PhysicalServer> listPhS = entry.getValue();
			for (PhysicalServer phy : listPhS) {
				// if phy has CPU and RAM >0, add it to list
				if (phy.getCpu() > 0 && phy.getRam() > 0) {
					// already exit in list
					if (listPhyAvailableEdgeSwitch.containsKey(edge)) {
						LinkedList<PhysicalServer> list = listPhyAvailableEdgeSwitch.get(edge);
						list.add(phy);
						listPhyAvailableEdgeSwitch.put(edge, list);
					}
					// edge is newest
					else {
						LinkedList<PhysicalServer> list = new LinkedList<>();
						list.add(phy);
						listPhyAvailableEdgeSwitch.put(edge, list);
					}
				}
			}
		}
		if (!listPhyAvailableEdgeSwitch.isEmpty()) {
			// calculate resource
			Map<SubstrateSwitch, Double> listTemp = new HashMap<>();
			for (Entry<SubstrateSwitch, LinkedList<PhysicalServer>> entry : listPhyAvailableEdgeSwitch.entrySet()) {
				LinkedList<PhysicalServer> listPhy = entry.getValue();
				double resource = 0;
				for (PhysicalServer phy : listPhy)
					resource += phy.getCpu();
				listTemp.put(entry.getKey(), resource);

			}
			// sort list near group
			listPhyAvailableEdgeSwitch = sortGroup(listPhyAvailableEdgeSwitch, listTemp);

			Iterator<Map.Entry<SubstrateSwitch, LinkedList<PhysicalServer>>> iter = listPhyAvailableEdgeSwitch
					.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<SubstrateSwitch, LinkedList<PhysicalServer>> entry = iter.next();
				if (entry.getValue().size() < numVM)
					continue;
				else {
					// convert listVM from map -> linkedlist
					
					LinkedList<VirtualMachine> listVir = new LinkedList<>(listVM.values());
					
					Collections.sort(listVir, new Comparator<VirtualMachine>() {
						@Override
						public int compare(VirtualMachine o1, VirtualMachine o2) {
							// sort by ID
							return Integer.compare(Integer.parseInt(o2.getNameVM()), Integer.parseInt(o1.getNameVM()));
						}
					});
					
					LinkedList<PhysicalServer> listPhy =  entry.getValue();
					Collections.sort(listPhy, new Comparator<PhysicalServer>() {
						@Override
						public int compare(PhysicalServer o1, PhysicalServer o2) {
							// sort by ID
							return Integer.compare(Integer.parseInt(o1.getName()), Integer.parseInt(o2.getName()));
						}
					});

					MaxBipartite bipartite = new MaxBipartite(listVir, listPhy);
					if (isIn(listPhy, listExceptPhy))
						continue;
					// SecondNetMatching minCost = new SecondNetMatching();

					// minCost.MatchingSN(listVir, entry.getValue());
					bipartite.run();
					if (bipartite.isSuccess()) {
						listPhyMaped = listPhy;
						mappingResults = bipartite.getResult();
						isNearGroupSatisfied = true;
						break;
					}
					// bipartite graph
					// if bipartite Graph success, isNearGroupSatisfied = true;
				}

			}
		}

		// Step 2.2: iterator all middles groups on FatTree topology and find
		// the
		// minimum consumed energy, if don't, do step 2.3
		if (!isNearGroupSatisfied) {

			Map<LinkedList<SubstrateSwitch>, LinkedList<PhysicalServer>> list = new HashMap<>();
			Map<Integer, LinkedList<SubstrateSwitch>> listPod = fatTree.getListPod();

			// sort list Pod by resource
			Map<Integer, Double> tempList = new HashMap<>();
			for (Entry<Integer, LinkedList<SubstrateSwitch>> entry : listPod.entrySet()) {
				double resource = 0;
				for (SubstrateSwitch edge : entry.getValue())
					for (PhysicalServer phy : listPhysConEdge.get(edge))
						resource += phy.getCpu();
				tempList.put(entry.getKey(), resource);
			}

			listPod = sortMiddleGroup(listPod, tempList);

			for (Entry<Integer, LinkedList<SubstrateSwitch>> entry : listPod.entrySet()) {
				LinkedList<PhysicalServer> listPhyChoosed = new LinkedList<>();
				LinkedList<SubstrateSwitch> listEdge = entry.getValue();
				for (SubstrateSwitch edge : listEdge) {
					LinkedList<PhysicalServer> listPhy = listPhysConEdge.get(edge);
					for (PhysicalServer phy : listPhy)
						if (!listPhyChoosed.contains(phy))
							listPhyChoosed.add(phy);
				}
				if (listPhyChoosed.size() < numVM)
					continue;
				else {

					// bipartite graph
					// if bipartite Graph success, isMiddleGroupSatisfied =
					// true;
					LinkedList<VirtualMachine> listVir = new LinkedList<>(listVM.values());
					
					Collections.sort(listVir, new Comparator<VirtualMachine>() {
						@Override
						public int compare(VirtualMachine o1, VirtualMachine o2) {
							// sort by ID
							return Integer.compare(Integer.parseInt(o2.getNameVM()), Integer.parseInt(o1.getNameVM()));
						}
					});

					Collections.sort(listPhyChoosed, new Comparator<PhysicalServer>() {
						@Override
						public int compare(PhysicalServer o1, PhysicalServer o2) {
							// sort by ID
							return Integer.compare(Integer.parseInt(o1.getName()), Integer.parseInt(o2.getName()));
						}
					});
					if (isIn(listPhyChoosed, listExceptPhy))
						continue;
					MaxBipartite bipartite = new MaxBipartite(listVir, listPhyChoosed);
					bipartite.run();
					// SecondNetMatching minCost = new SecondNetMatching();
					// minCost.MatchingSN(listVir,listPhy);
					if (bipartite.isSuccess()) {
						listPhyMaped = listPhyChoosed;
						mappingResults = bipartite.getResult();
						isMiddleGroupSatisfied = true;
						break;
					}
				}

				if (isMiddleGroupSatisfied)
					break;
			}
		}
		// Step 2.3: iterator all far groups on FatTree topology and
		// find the
		// minimum consumed energy, if don't, do step 2.4
		if (!isMiddleGroupSatisfied && !isNearGroupSatisfied) {
			Map<LinkedList<SubstrateSwitch>, LinkedList<PhysicalServer>> list = new HashMap<>();
			Map<Integer, LinkedList<SubstrateSwitch>> listPod = fatTree.getListPod();
			int i = 1;
			LinkedList<SubstrateSwitch> listEdgeTemp = new LinkedList<>();

			// sort list Pod by resource
//			Map<Integer, Double> tempList = new HashMap<>();
//			for (Entry<Integer, LinkedList<SubstrateSwitch>> entry : listPod.entrySet()) {
//				double resource = 0;
//				for (SubstrateSwitch edge : entry.getValue())
//					for (PhysicalServer phy : listPhysConEdge.get(edge))
//						resource += phy.getCpu();
//				tempList.put(entry.getKey(), resource);
//			}
//
//			listPod = sortMiddleGroup(listPod, tempList);
			
			for (Entry<Integer, LinkedList<SubstrateSwitch>> entry : listPod.entrySet()) {

				if (i == 1) {
					listEdgeTemp = entry.getValue();
					i++;
					continue;
				} else {
					LinkedList<PhysicalServer> listPhyChoosed = new LinkedList<>();
					LinkedList<SubstrateSwitch> listEdge = new LinkedList<>(entry.getValue());
					for (SubstrateSwitch edge : listEdgeTemp)
						if (!listEdge.contains(edge))
							listEdge.add(edge);
					listEdgeTemp = entry.getValue();
					for (SubstrateSwitch edge : listEdge) {
						LinkedList<PhysicalServer> listPhy = listPhysConEdge.get(edge);
						for (PhysicalServer phy : listPhy) {
								if (!listPhyChoosed.contains(phy))
									listPhyChoosed.add(phy);
						}
					}
					if (listPhyChoosed.size() < numVM)
						continue;
					else {
						LinkedList<VirtualMachine> listVir = new LinkedList<>(listVM.values());
						
						Collections.sort(listVir, new Comparator<VirtualMachine>() {
							@Override
							public int compare(VirtualMachine o1, VirtualMachine o2) {
								// sort by ID
								return Integer.compare(Integer.parseInt(o2.getNameVM()), Integer.parseInt(o1.getNameVM()));
							}
						});

						Collections.sort(listPhyChoosed, new Comparator<PhysicalServer>() {
							@Override
							public int compare(PhysicalServer o1, PhysicalServer o2) {
								// sort by ID
								return Integer.compare(Integer.parseInt(o1.getName()), Integer.parseInt(o2.getName()));
							}
						});
						if (isIn(listPhyChoosed, listExceptPhy))
							continue;
						MaxBipartite bipartite = new MaxBipartite(listVir, listPhyChoosed);
						bipartite.run();
						// SecondNetMatching minCost = new
						// SecondNetMatching();
						// minCost.MatchingSN(listVir,listPhyChoosed);
						if (bipartite.isSuccess()) {
							listPhyMaped = listPhyChoosed;
							mappingResults = bipartite.getResult();
							isFarGroupSatisfied = true;
							break;
						}
					}
//					list.put(listEdge, listPhyChoosed);

				}
			}
			
		}

		if (isNearGroupSatisfied || isMiddleGroupSatisfied || isFarGroupSatisfied) {
			mapVM(mappingResults, topo);
			isSuccess = true;
		} else
			isSuccess = false;

		return topo;
	}

	public void mapVM(Map<VirtualMachine, PhysicalServer> results, Topology topo) {
		//Map<Integer, PhysicalServer> listPhyServers = topo.getListPhyServers();
		for (Entry<VirtualMachine, PhysicalServer> entry : results.entrySet()) {
			// System.out.println("Vir " + entry.getKey().getNameVM() + "
			// map onto Phy " + entry.getValue().getName());
			//int key = Integer.parseInt(entry.getValue().getName());
			PhysicalServer phy = entry.getValue();

			// update cost for Phy
//			for (VirtualLink virLink : listVirLink) {
//				if (virLink.getsVM().equals(entry.getKey()) || virLink.getdVM().equals(entry.getKey()))
//					phy.setCost(virLink.getBandwidthRequest());
//			}
			if (phy.getState() == 0)
				numPhyTurnOnSecondNet++;
			phy.setCpu(phy.getCpu() - entry.getKey().getCPU());
			phy.setRam(phy.getRam() - entry.getKey().getMemory());
			//listPhyServers.put(key, phy);
		}
		//topo.setListPhyServers(listPhyServers);
	}

	
	public int getNumPhyTurnOnSecondNet() {
		return numPhyTurnOnSecondNet;
	}

	public void setNumPhyTurnOnSecondNet(int numPhyTurnOnSecondNet) {
		this.numPhyTurnOnSecondNet = numPhyTurnOnSecondNet;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public Map<VirtualMachine, PhysicalServer> getMappingResults() {
		return mappingResults;
	}

	public LinkedList<PhysicalServer> getListPhyMaped() {
		return listPhyMaped;
	}

	public Map<Integer, LinkedList<PhysicalServer>> getListExceptPhy() {
		return listExceptPhy;
	}

	public void setListExceptPhy(Map<Integer, LinkedList<PhysicalServer>> listExceptPhy) {
		this.listExceptPhy = listExceptPhy;
	}

	// kiem tra xem listPhy tim dc co thuoc list loai bo khong
	public boolean isIn(LinkedList<PhysicalServer> listPhy, Map<Integer, LinkedList<PhysicalServer>> listPhyExcept) {
		boolean check = false;
		for (Entry<Integer, LinkedList<PhysicalServer>> entry : listPhyExcept.entrySet()) {
			boolean ck = true;
			LinkedList<PhysicalServer> phyExcept = entry.getValue();
			if (phyExcept.size() == listPhy.size()) {
				listPhy = sort(listPhy);
				phyExcept = sort(phyExcept);
				for (int i = 0; i < listPhy.size(); i++)
					if (!listPhy.get(i).equals(phyExcept.get(i)))
						ck = false;

			} else {
				ck = false;
				continue;
			}
			if (ck) {
				check = true;
				break;
			}
		}

		return check;
	}

	public LinkedList<PhysicalServer> sort(LinkedList<PhysicalServer> listPhY) {
		/* Sort PhyM in decreasing order by state, CPU, need to discuss */
		Collections.sort(listPhY, new Comparator<PhysicalServer>() {
			@Override
			public int compare(PhysicalServer o1, PhysicalServer o2) {
				if (o1.getState() < o2.getState()) {
					return 1;
				}
				if (o1.getState() > o2.getState()) {
					return -1;
				}
				if (o1.getState() == o2.getState()) {
					if (o1.getCpu() < o2.getCpu()) {
						return 1;
					}
					if (o1.getCpu() > o2.getCpu()) {
						return -1;
					}
					if (o1.getCpu() == o2.getCpu()) {
						if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName()))
							return -1;
						if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName()))
							return 1;
						return 0;
					}

				}
				return 0;
			}
		});
		return listPhY;
	}

	public double getPowerPhyConsumedSecondNet() {
		powerPhyConsumedSecondNet = this.numPhyTurnOnSecondNet * 400;
		return powerPhyConsumedSecondNet;
	}

	// giam dan resource
	public Map<SubstrateSwitch, LinkedList<PhysicalServer>> sortGroup(
			Map<SubstrateSwitch, LinkedList<PhysicalServer>> listPhy, final Map<SubstrateSwitch, Double> listTemp) {

		List<Map.Entry<SubstrateSwitch, LinkedList<PhysicalServer>>> list = new LinkedList<>(listPhy.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<SubstrateSwitch, LinkedList<PhysicalServer>>>() {
			@Override
			public int compare(Map.Entry<SubstrateSwitch, LinkedList<PhysicalServer>> o1,
					Map.Entry<SubstrateSwitch, LinkedList<PhysicalServer>> o2) {
				// sort by resource
				int result = Double.compare(listTemp.get(o2.getKey()), listTemp.get(o1.getKey()));
				return result;
			}
		});
		// update map
		HashMap<SubstrateSwitch, LinkedList<PhysicalServer>> temp = new LinkedHashMap<>();
		for (Map.Entry<SubstrateSwitch, LinkedList<PhysicalServer>> entry : list) {
			temp.put(entry.getKey(), entry.getValue());
		}
		listPhy = temp;
		return listPhy;
	}

	public Map<Integer, LinkedList<SubstrateSwitch>> sortMiddleGroup(Map<Integer, LinkedList<SubstrateSwitch>> listPod,
			final Map<Integer, Double> listTemp) {

		List<Map.Entry<Integer, LinkedList<SubstrateSwitch>>> list = new LinkedList<>(listPod.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<Integer, LinkedList<SubstrateSwitch>>>() {
			@Override
			public int compare(Map.Entry<Integer, LinkedList<SubstrateSwitch>> o1,
					Map.Entry<Integer, LinkedList<SubstrateSwitch>> o2) {
				// sort by resource
				int result = Double.compare(listTemp.get(o2.getKey()), listTemp.get(o1.getKey()));
				return result;
			}
		});
		// update map
		HashMap<Integer, LinkedList<SubstrateSwitch>> temp = new LinkedHashMap<>();
		for (Map.Entry<Integer, LinkedList<SubstrateSwitch>> entry : list) {
			temp.put(entry.getKey(), entry.getValue());
		}
		listPod = temp;
		return listPod;
	}

}
