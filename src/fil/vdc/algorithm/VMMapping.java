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
import fil.vdc.model.PhysicalServer;
import fil.vdc.model.SubstrateSwitch;
import fil.vdc.model.Topology;
import fil.vdc.model.VDCRequest;
import fil.vdc.model.VirtualMachine;

public class VMMapping {
	
	private Map<VDCRequest, Topology> resultTopo;
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
	private int numPhyTurnOnOur;
	private int numPhyTurnOnGreenHead;
	private Map<Integer, LinkedList<PhysicalServer>> listExceptPhy; // dung de luu cac group da chon nhung khong thanh cong
	private LinkedList<PhysicalServer> listPhyMaped;
	private double powerPhyConsumed;
	private double powerPhyConsumedGreenHead;
	private double numVMNearOur;
	private double numVMMiddleOur;
	private double numVMFarOur;
	private String type; // type of result vm mapping: map on near group type = "Near", map on middle group type ="Middle", 
	 // map on far group type= "Far"
	
	public VMMapping() {
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
		numPhyTurnOnOur = 0;
		numPhyTurnOnGreenHead = 0;
		listExceptPhy= new HashMap<>();
		listPhyMaped = new LinkedList<>();
		powerPhyConsumed = 0;
		powerPhyConsumedGreenHead =0;
		numVMNearOur=0;
		numVMMiddleOur =0;
		numVMFarOur =0;
		resultTopo = new HashMap<>();
	}

	public Topology run(VDCRequest VDC, Topology topo, FatTree fatTree) {

		// Step 1 : Get vDC and Sort VM by Resource, ID
		this.VDCRequest = VDC;
		listVM = VDC.getListVirVM();
		sortVM();
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
			PhysicalServer phy = listPhysical.get(Integer.parseInt(entry.getKey().getName()));
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
		sortEdgeSwitch();

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

			// If near group has number of available Phy < number of VM then
			// remove it
			Iterator<Map.Entry<SubstrateSwitch, LinkedList<PhysicalServer>>> iter = listPhyAvailableEdgeSwitch
					.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<SubstrateSwitch, LinkedList<PhysicalServer>> entry = iter.next();
				if (entry.getValue().size() < numVM || isIn(entry.getValue(), listExceptPhy)) {
					iter.remove();
				}
			}
		}

		if (!listPhyAvailableEdgeSwitch.isEmpty()) {
			isNearGroupSatisfied = true;
			for (Entry<SubstrateSwitch, LinkedList<PhysicalServer>> entry : listPhyAvailableEdgeSwitch.entrySet()) {
				LinkedList<SubstrateSwitch> listEdgeSwitch = new LinkedList<>();
				LinkedList<PhysicalServer> listPhysicalServer = new LinkedList<>();
				listEdgeSwitch.add(entry.getKey());
				listPhysicalServer = entry.getValue();
				listNearSatisfied.put(listEdgeSwitch, listPhysicalServer);
			}
			selectedGroup = chooseGroup(listNearSatisfied);
			if (selectedGroup.isEmpty())
				isNearGroupSatisfied = false;
		}

		// Step 2.2: iterator all middles groups on FatTree topology and find
		// the
		// minimum consumed energy, if don't, do step 2.3
		if (!isNearGroupSatisfied) {
			listMiddleSatisfied = getListMiddleSatisfied(fatTree);
			if (!listMiddleSatisfied.isEmpty()) {
				isMiddleGroupSatisfied = true;
				selectedGroup = chooseGroup(listMiddleSatisfied);
				if (selectedGroup.isEmpty())
					isMiddleGroupSatisfied = false;
			}
		}
		// Step 2.3: iterator all far groups on FatTree topology and
		// find the
		// minimum consumed energy, if don't, do step 2.4
		if (!isMiddleGroupSatisfied && !isNearGroupSatisfied) {
			listFarSatisfied = getListFarSatisfied(fatTree);

			if (!listFarSatisfied.isEmpty()) {
				isFarGroupSatisfied = true;
				selectedGroup = chooseGroup(listFarSatisfied);
				if (selectedGroup.isEmpty())
					isFarGroupSatisfied = false;
			}
		}

		// them bien map vao ????? stupid
		// System.out.println("Near " + isNearGroupSatisfied);
		// System.out.println("Middle " + isMiddleGroupSatisfied);
		// System.out.println("Far " + isFarGroupSatisfied);
		if (isNearGroupSatisfied) {
			// print group
			type = "Near";
			LinkedList<PhysicalServer> listServer = new LinkedList<>();
			for (Entry<LinkedList<SubstrateSwitch>, LinkedList<PhysicalServer>> entry : selectedGroup.entrySet()) {
				for (PhysicalServer phyMachine : entry.getValue())
				{
					if (listServer.contains(phyMachine))
						continue;
					else
						listServer.add(phyMachine);
				}

			}
			mapVM(listServer, topo);

		}
		if (isMiddleGroupSatisfied) {
			// print group
			type ="Middle";
			LinkedList<PhysicalServer> listServer = new LinkedList<>();
			for (Entry<LinkedList<SubstrateSwitch>, LinkedList<PhysicalServer>> entry : selectedGroup.entrySet()) {
				for (PhysicalServer phyMachine : entry.getValue())
					if (listServer.contains(phyMachine))
						continue;
					else
						listServer.add(phyMachine);

			}
			mapVM(listServer, topo);
		}
		if (isFarGroupSatisfied) {
			// print group
			type = "Far";
			LinkedList<PhysicalServer> listServer = new LinkedList<>();
			for (Entry<LinkedList<SubstrateSwitch>, LinkedList<PhysicalServer>> entry : selectedGroup.entrySet()) {
				for (PhysicalServer phyMachine : entry.getValue())
					if (listServer.contains(phyMachine))
						continue;
					else
						listServer.add(phyMachine);

			}
			mapVM(listServer, topo);
		}
		/*
		 * #####################################################################
		 * ####################
		 */

		// Step 2.4: if can not find any possible groups, map VM on individual
		// groups. This is the main idea of Greenhead

		if (!isNearGroupSatisfied && !isMiddleGroupSatisfied && !isFarGroupSatisfied) {
			// get all list physical server
			
			LinkedList<PhysicalServer> listAllServer = new LinkedList<>();
			for (Entry<SubstrateSwitch, LinkedList<PhysicalServer>> entry : listPhysConEdge.entrySet()) {
				LinkedList<PhysicalServer> list = entry.getValue();
				for (PhysicalServer phy : list) {
					if (listAllServer.contains(phy))
						continue;
					else {
						listAllServer.add(phy);
					}
				}
			}
			if(!isIn(listAllServer, listExceptPhy))
			{
				type = "Far";
			mapVM(listAllServer, topo);
			}
			// find Physical server
		}

		// *******************************************************************//
		// Step 3 : Map VM to physical server

		// return topo-updated for next VM
		return topo;
	}

	// run VM Mapping GreenHead
	public Topology runGreenHead(VDCRequest VDC, Topology topo, FatTree fatTree) {
		this.VDCRequest = VDC;
		listVM = VDC.getListVirVM();
		sortVM();
		// ***************************************************************//

		numVM = VDC.getNumVM(); // number of VMs belong to VDC
//		listLinksServer = topo.getListLinksServer();
		Map<Integer, PhysicalServer> listPhysical = topo.getListPhyServers();

		sortEdgeSwitch();
		LinkedList<PhysicalServer> listAllServer = new LinkedList<>();
		for (Entry<Integer, PhysicalServer> entry : listPhysical.entrySet())
			listAllServer.add(entry.getValue());
		mapVMGreenHead(listAllServer, topo);
		return topo;

	}

	public void mapVM(LinkedList<PhysicalServer> listServer, Topology topo) {
		listPhyMaped = listServer;
		listServer = sort(listServer);
		// for(PhysicalServer phy: listServer)
		// System.out.println("Phy "+phy.getName()+ " State "+phy.getState()+"
		// CPU "+phy.getCpu()+ " RAM "+phy.getRam());
		int count = 0;
		LinkedList<PhysicalServer> forgetPhy = new LinkedList<>();
		for (Entry<Integer, VirtualMachine> entry : listVM.entrySet())
			for (PhysicalServer phy : listServer) {
				if (phy.getCpu() >= entry.getValue().getCPU() && phy.getRam() >= entry.getValue().getMemory()) {
					if (forgetPhy.contains(phy))
						continue;
					else {
						forgetPhy.add(phy);
						if (phy.getState() == 0)
							numPhyTurnOnOur++;
						mappingResults.put(entry.getValue(), phy);
						count++;
						break;
					}
				}
			}
		if (count == numVM) {
			if(type != null)
				switch (type) {
				case "Near":
				{
					numVMNearOur = count;
					break;
				}
				case "Middle":
				{
					numVMMiddleOur = count;
					break;
				}
				
				case "Far":
				{
					numVMFarOur = count;
					break;
				}
				case "":
				{
					break;
				}
				default:
				{
					break;
				}
				}
			isSuccess = true;
			Map<Integer, PhysicalServer> listPhyServers = topo.getListPhyServers();
			for (Entry<VirtualMachine, PhysicalServer> entry : mappingResults.entrySet()) {
				// System.out.println("Vir " + entry.getKey().getNameVM() + "
				// map onto Phy " + entry.getValue().getName());

				int key = Integer.parseInt(entry.getValue().getName());
				PhysicalServer phy = listPhyServers.get(key);
				phy.setCpu(phy.getCpu() - entry.getKey().getCPU());
				phy.setRam(phy.getRam() - entry.getKey().getMemory());
				listPhyServers.put(key, phy);

			}
			topo.setListPhyServers(listPhyServers);
			// for (Entry<Integer, PhysicalServer> entry :
			// listPhyServers.entrySet())
			// System.out.println("Server " + entry.getValue().getName() + " CPU
			// " + entry.getValue().getCpu()
			// + " RAM " + entry.getValue().getRam() + " State " +
			// entry.getValue().getState());
		} else {
			isSuccess = false;
			mappingResults.clear();
		}
	}

	public void mapVMGreenHead(LinkedList<PhysicalServer> listServer, Topology topo) {
		
		listPhyMaped = listServer;
		listServer = sortGreenHead(listServer);
		int count = 0;
		LinkedList<PhysicalServer> forgetPhy = new LinkedList<>();
		for (Entry<Integer, VirtualMachine> entry : listVM.entrySet()) {
			for (PhysicalServer phy : listServer) {
				if (phy.getCpu() >= entry.getValue().getCPU() && phy.getRam() >= entry.getValue().getMemory()) {
					if (forgetPhy.contains(phy))
						continue;
					else {
						forgetPhy.add(phy);
						mappingResults.put(entry.getValue(), phy);
						count++;
						break;
					}
				}
			}
		}
		if (count == numVM) {
			isSuccess = true;
			Map<Integer, PhysicalServer> listPhyServers = topo.getListPhyServers();
			for (Entry<VirtualMachine, PhysicalServer> entry : mappingResults.entrySet()) {
				// System.out.println("Vir " + entry.getKey().getNameVM() + "
				// map onto Phy " + entry.getValue().getName());

				int key = Integer.parseInt(entry.getValue().getName());
				PhysicalServer phy = listPhyServers.get(key);
				phy.setCpu(phy.getCpu() - entry.getKey().getCPU());
				phy.setRam(phy.getRam() - entry.getKey().getMemory());
				//listPhyServers.put(key, phy);
			}
			topo.setListPhyServers(listPhyServers);;
		} else {
			isSuccess = false;
			mappingResults.clear();
		}
	}

	public VDCRequest getVDCRequest() {
		return VDCRequest;
	}

	public void setVDCRequest(VDCRequest VDCRequest) {
		this.VDCRequest = VDCRequest;
	}

	/**
	 * Sort VM in decreasing order by resource and id
	 */
	public void sortVM() {
		List<Map.Entry<Integer, VirtualMachine>> list = new LinkedList<>(listVM.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<Integer, VirtualMachine>>() {
			@Override
			public int compare(Map.Entry<Integer, VirtualMachine> o1, Map.Entry<Integer, VirtualMachine> o2) {
				// sort by CPU
				int result = Double.compare(o2.getValue().getCPU(), o1.getValue().getCPU());
				if (result == 0) {
					// sort by RAM
					result = Double.compare(o2.getValue().getMemory(), o1.getValue().getMemory());
					if (result == 0)
						// sort by ID
						result = Double.compare(o1.getKey(), o2.getKey());
				}
				return result;
			}
		});
		// update map
		HashMap<Integer, VirtualMachine> listsVirVM = new LinkedHashMap<>();
		for (Map.Entry<Integer, VirtualMachine> entry : list) {
			listsVirVM.put(entry.getKey(), entry.getValue());
		}
		listVM = listsVirVM;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public double getMaxResourceRequired(VDCRequest vdcRequest) {
		double resourceMax = 0;
		return resourceMax;
	}

	// choose near group with minimum consumed energy. There are two options
	// here
	// 1. the old way: iterator and map.
	// 2. the new way: using bipartite graph
	public Map<LinkedList<SubstrateSwitch>, LinkedList<PhysicalServer>> chooseGroup(
			Map<LinkedList<SubstrateSwitch>, LinkedList<PhysicalServer>> list) {
		Map<LinkedList<SubstrateSwitch>, LinkedList<PhysicalServer>> choosedGroup = new HashMap<>();

		LinkedList<SubstrateSwitch> listEdgeSwitchTemp = new LinkedList<>();
		LinkedList<PhysicalServer> listPhysicalServerTemp = new LinkedList<>();

		Map<LinkedList<SubstrateSwitch>, Integer> listEdgeTurnOnPhy = new LinkedHashMap<>();
		int num = Integer.MAX_VALUE;
		for (Entry<LinkedList<SubstrateSwitch>, LinkedList<PhysicalServer>> entry : list.entrySet()) {
			LinkedList<SubstrateSwitch> listEdgeSwitch = entry.getKey();
			LinkedList<PhysicalServer> listPhysicalServers = entry.getValue();
			int numPhYTurnOn = getNumberVMTurnOnOption1(listPhysicalServers);

			// if each result has same minimum physical need to turn on,
			// algorithm takes the highest edge switch id ?? do i need to
			// change???
			// if (numPhYTurnOn <= num) {
			// num = numPhYTurnOn;
			// listEdgeSwitchTemp = listEdgeSwitch;
			// listPhysicalServerTemp = listPhysicalServers;
			// }

			if (isSatisfiedCPUandRAM) {
				listEdgeTurnOnPhy.put(listEdgeSwitch, numPhYTurnOn);
				choosedGroup.put(listEdgeSwitch, listPhysicalServers);
			}
		}

		listEdgeTurnOnPhy = sortEdgeTurnOnPhy(listEdgeTurnOnPhy);
//		//debug
//		System.out.println("Num turn on");
//		for(Entry<LinkedList<SubstrateSwitch>, Integer> entry : listEdgeTurnOnPhy.entrySet())
//			System.out.print(entry.getValue()+" ");
//		System.out.println();
//		
//		
		Map<LinkedList<SubstrateSwitch>, LinkedList<PhysicalServer>> choosed = new HashMap<>();
		if (!listEdgeTurnOnPhy.isEmpty()) {
			Map.Entry<LinkedList<SubstrateSwitch>, Integer> entry = listEdgeTurnOnPhy.entrySet().iterator().next();
			LinkedList<SubstrateSwitch> edgeSwitch = entry.getKey();
			LinkedList<PhysicalServer> listPhy = choosedGroup.get(edgeSwitch);
			choosed.put(edgeSwitch, listPhy);
		}
		return choosed;
	}

	// sap xep danh sach cac group va so host phai bat trong moi group theo
	// chieu tang dan host bat va switch ID
	public Map<LinkedList<SubstrateSwitch>, Integer> sortEdgeTurnOnPhy(
			Map<LinkedList<SubstrateSwitch>, Integer> listEdgeTurnOnPhy) {
		List<Map.Entry<LinkedList<SubstrateSwitch>, Integer>> list = new LinkedList<>(listEdgeTurnOnPhy.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<LinkedList<SubstrateSwitch>, Integer>>() {
			@Override
			public int compare(Map.Entry<LinkedList<SubstrateSwitch>, Integer> o1,
					Map.Entry<LinkedList<SubstrateSwitch>, Integer> o2) {
				// sort by ID of edge switch
				int result = Double.compare(o1.getValue(), o2.getValue());
				if (result == 0) {
					int sumO1 = 0;
					int sumO2 = 0;
					for (SubstrateSwitch edge : o1.getKey())
						sumO1 += Integer.parseInt(edge.getNameSubstrateSwitch());
					for (SubstrateSwitch edge : o2.getKey())
						sumO2 += Integer.parseInt(edge.getNameSubstrateSwitch());
					 result = Double.compare(sumO1, sumO2);
				}
				return result;
			}
		});
		// update map
		HashMap<LinkedList<SubstrateSwitch>, Integer> temp = new LinkedHashMap<>();
		for (Map.Entry<LinkedList<SubstrateSwitch>, Integer> entry : list) {
			temp.put(entry.getKey(), entry.getValue());
		}
		listEdgeTurnOnPhy = temp;

		return listEdgeTurnOnPhy;
	}

	// get list pod groups satisfied
	public Map<LinkedList<SubstrateSwitch>, LinkedList<PhysicalServer>> getListMiddleSatisfied(FatTree fatTree) {
		Map<LinkedList<SubstrateSwitch>, LinkedList<PhysicalServer>> list = new HashMap<>();
		Map<Integer, LinkedList<SubstrateSwitch>> listPod = fatTree.getListPod();

		for (Entry<Integer, LinkedList<SubstrateSwitch>> entry : listPod.entrySet()) {
			LinkedList<PhysicalServer> listPhyChoosed = new LinkedList<>();
			LinkedList<SubstrateSwitch> listEdge = entry.getValue();
			for (SubstrateSwitch edge : listEdge) {
				LinkedList<PhysicalServer> listPhy = listPhysConEdge.get(edge);
				for (PhysicalServer phy : listPhy) {
					if (phy.getCpu() > 0 && phy.getRam() > 0)
						if (!listPhyChoosed.contains(phy))

							listPhyChoosed.add(phy);
				}
			}

			list.put(listEdge, listPhyChoosed);

		}
		Iterator<Map.Entry<LinkedList<SubstrateSwitch>, LinkedList<PhysicalServer>>> iter = list.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<LinkedList<SubstrateSwitch>, LinkedList<PhysicalServer>> entry = iter.next();
			if (entry.getValue().size() < numVM || isIn(entry.getValue(), listExceptPhy)) {
				iter.remove();
			}
		}
		return list;
	}
	// chon 2 pod lien tiep nhau: 1,2,3,...,n ->(1,2) (2,3) (3,4)
	public Map<LinkedList<SubstrateSwitch>, LinkedList<PhysicalServer>> getListFarSatisfied(FatTree fatTree) {
		Map<LinkedList<SubstrateSwitch>, LinkedList<PhysicalServer>> list = new HashMap<>();
		Map<Integer, LinkedList<SubstrateSwitch>> listPod = fatTree.getListPod();
		int i = 1;

		// ********************************************************************//

		LinkedList<SubstrateSwitch> listEdgeTemp = new LinkedList<>();

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
						if (phy.getCpu() > 0 && phy.getRam() > 0)
							if (!listPhyChoosed.contains(phy))
								listPhyChoosed.add(phy);
					}
				}
				list.put(listEdge, listPhyChoosed);

			}
		}
		

		Iterator<Map.Entry<LinkedList<SubstrateSwitch>, LinkedList<PhysicalServer>>> iter = list.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<LinkedList<SubstrateSwitch>, LinkedList<PhysicalServer>> entry = iter.next();
			if (entry.getValue().size() < numVM || isIn(entry.getValue(), listExceptPhy)) {
				iter.remove();
			}
		}
		return list;
	}

	// sort edge switch in increasing order by ID
	public void sortEdgeSwitch() {
		List<Map.Entry<SubstrateSwitch, LinkedList<PhysicalServer>>> list = new LinkedList<>(
				listPhysConEdge.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<SubstrateSwitch, LinkedList<PhysicalServer>>>() {
			@Override
			public int compare(Map.Entry<SubstrateSwitch, LinkedList<PhysicalServer>> o1,
					Map.Entry<SubstrateSwitch, LinkedList<PhysicalServer>> o2) {
				// sort by ID of edge switch
				int result = Double.compare(Double.valueOf(o1.getKey().getNameSubstrateSwitch()),
						Double.valueOf(o2.getKey().getNameSubstrateSwitch()));
				return result;
			}
		});
		// update map
		HashMap<SubstrateSwitch, LinkedList<PhysicalServer>> temp = new LinkedHashMap<>();
		for (Map.Entry<SubstrateSwitch, LinkedList<PhysicalServer>> entry : list) {
			temp.put(entry.getKey(), entry.getValue());
		}
		listPhysConEdge = temp;
	}

	public int getNumberVMTurnOnOption1(LinkedList<PhysicalServer> listPhY) {
		int num = 0;
		int numSucess = 0;
		listPhY = sort(listPhY);
		LinkedList<PhysicalServer> forgetPhy = new LinkedList<>();
		// stupid
		for (Entry<Integer, VirtualMachine> entry : listVM.entrySet())
			for (PhysicalServer phy : listPhY) {
				VirtualMachine vir = entry.getValue();
				if (phy.getCpu() >= vir.getCPU() && phy.getRam() >= vir.getMemory()) {
					if (forgetPhy.contains(phy))
						continue;
					else {
						if (phy.getState() == 0) {
							numSucess++;
							num++;
						} else
							numSucess++;

						forgetPhy.add(phy);
						break;
					}
				}
			}
		// System.out.println("num "+numSucess);
		if (numSucess == listVM.size())

			return num; // if success then return number of Physical servers
						// need to
						// turn on
		else

		{
			isSatisfiedCPUandRAM = false;
			return Integer.MAX_VALUE; // if fail return max integer, because we
										// only choose group with minimum number
										// of Physical servers need to turn on
		}
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
					if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName()))
						return -1;
					if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName()))
						return 1;
				}
//				if (o1.getState() == o2.getState()) {
//					if (o1.getCpu() < o2.getCpu()) {
//						return 1;
//					}
//					if (o1.getCpu() > o2.getCpu()) {
//						return -1;
//					}
//					if (o1.getCpu() == o2.getCpu()) {
//						if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName()))
//							return -1;
//						if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName()))
//							return 1;
//						return 0;
//					}
//
//				}
				return 0;
			}
		});
		return listPhY;
	}

	public LinkedList<PhysicalServer> sortGreenHead(LinkedList<PhysicalServer> listPhY) {
		/* Sort PhyM in decreasing order by ID, CPU, need to discuss */
		Collections.sort(listPhY, new Comparator<PhysicalServer>() {
			@Override
			public int compare(PhysicalServer o1, PhysicalServer o2) {

				if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName()))
					return -1;
				if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName()))
					return 1;

				return 0;
			}
		});
		return listPhY;
	}

	public Map<VirtualMachine, PhysicalServer> getMappingResults() {
		return mappingResults;
	}

	public void setMappingResults(Map<VirtualMachine, PhysicalServer> mappingResults) {
		this.mappingResults = mappingResults;
	}

	public int getNumPhyTurnOn() {
		return numPhyTurnOnOur;
	}

	public void setNumPhyTurnOn(int numPhyTurnOn) {
		this.numPhyTurnOnOur = numPhyTurnOn;
	}
	
	// kiem tra xem listPhy tim dc co thuoc list loai bo khong
	public boolean isIn(LinkedList<PhysicalServer> listPhy, Map<Integer,LinkedList<PhysicalServer>> listPhyExcept)
	{
//		System.out.println("List Except");
//		for(Entry<Integer, LinkedList<PhysicalServer>> entry: listPhyExcept.entrySet())
//		{
//			for(PhysicalServer phy: entry.getValue())
//				System.out.println(phy.getName()+" ");
//			System.out.println();
//		}
//		System.out.println("Lis Phy input");
//		for(PhysicalServer phy: listPhy)
//			System.out.println(phy.getName()+" ");
//		System.out.println();
		boolean check = false;
		for(Entry<Integer, LinkedList<PhysicalServer>> entry: listPhyExcept.entrySet())
		{
			boolean ck = true;
			LinkedList<PhysicalServer> phyExcept = entry.getValue();
			if(phyExcept.size() == listPhy.size())
			{
				listPhy= sort(listPhy);
				phyExcept = sort(phyExcept);
				for(int i=0; i< listPhy.size(); i++)
					if(!listPhy.get(i).equals(phyExcept.get(i)))
						ck=false;
				
			}
			else
			{
				ck= false;
				continue;
			}
			if(ck)
			{
				
				check= true;
				break;
			}
		}
		
		return check;
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

	public double getPowerConsumed() {
		powerPhyConsumed = this.numPhyTurnOnOur*400;
		return powerPhyConsumed;
	}

	public double getPowerConsumedGreenHead() {
		powerPhyConsumedGreenHead = this.numPhyTurnOnGreenHead*400;
		return powerPhyConsumedGreenHead;
	}
	public double getNumVMNear() {
		return numVMNearOur;
	}

	public double getNumVMMiddle() {
		return numVMMiddleOur;
	}

	public double getNumVMFar() {
		return numVMFarOur;
	}
	
	
	// adding new ideal
	public Topology runOurMapping(VDCRequest VDC, Topology topo, FatTree fatTree) {
		this.VDCRequest = VDC;
		listVM = VDC.getListVirVM();
//		sortVM();
		// ***************************************************************//

		numVM = VDC.getNumVM(); // number of VMs belong to VDC
		listLinksServer = topo.getListLinksServer();
		Map<Integer, PhysicalServer> listPhysical = topo.getListPhyServers();

//		sortEdgeSwitch();
		// for (Entry<PhysicalServer, SubstrateSwitch> entry :
		// listLinksServer.entrySet()) {
		// PhysicalServer phy =
		// listPhysical.get(Integer.parseInt(entry.getKey().getName()));
		// SubstrateSwitch edge = entry.getValue();
		// if (listPhysConEdge.containsKey(edge)) {
		// LinkedList<PhysicalServer> listPhy = listPhysConEdge.get(edge);
		// listPhy.add(phy);
		// listPhysConEdge.put(edge, listPhy);
		// } else {
		// LinkedList<PhysicalServer> listPhy = new LinkedList<>();
		// listPhy.add(phy);
		// listPhysConEdge.put(edge, listPhy);
		// }
		// }
		LinkedList<PhysicalServer> listAllServer = new LinkedList<>();
		// for (Entry<SubstrateSwitch, LinkedList<PhysicalServer>> entry :
		// listPhysConEdge.entrySet()) {
		// LinkedList<PhysicalServer> list = entry.getValue();
		// for (PhysicalServer phy : list) {
		// if (listAllServer.contains(phy))
		// continue;
		// else {
		// listAllServer.add(phy);
		// }
		// }
		// }
		for (Entry<Integer, PhysicalServer> entry : listPhysical.entrySet())
			listAllServer.add(entry.getValue());
		mapVMOur(listAllServer, topo);
		return topo;

	}

	
	public void mapVMOur(LinkedList<PhysicalServer> listServer, Topology topo) {
		listPhyMaped = listServer;
		listServer = sortOur(listServer);
		int count = 0;
		LinkedList<PhysicalServer> forgetPhy = new LinkedList<>();
		for (Entry<Integer, VirtualMachine> entry : listVM.entrySet())
			for (PhysicalServer phy : listServer) {
				if (phy.getCpu() >= entry.getValue().getCPU() && phy.getRam() >= entry.getValue().getMemory()) {
					if (forgetPhy.contains(phy))
						continue;
					else {
						forgetPhy.add(phy);
						if (phy.getState() == 0)
							numPhyTurnOnGreenHead++;
						mappingResults.put(entry.getValue(), phy);
						count++;
						break;
					}
				}
			}
		if (count == numVM) {
			isSuccess = true;
			Map<Integer, PhysicalServer> listPhyServers = topo.getListPhyServers();
			for (Entry<VirtualMachine, PhysicalServer> entry : mappingResults.entrySet()) {
				// System.out.println("Vir " + entry.getKey().getNameVM() + "
				// map onto Phy " + entry.getValue().getName());

				int key = Integer.parseInt(entry.getValue().getName());
				PhysicalServer phy = listPhyServers.get(key);
				phy.setCpu(phy.getCpu() - entry.getKey().getCPU());
				phy.setRam(phy.getRam() - entry.getKey().getMemory());
				listPhyServers.put(key, phy);

			}
			topo.setListPhyServers(listPhyServers);
			// for (Entry<Integer, PhysicalServer> entry :
			// listPhyServers.entrySet())
			// System.out.println("Server " + entry.getValue().getName() + " CPU
			// " + entry.getValue().getCpu()
			// + " RAM " + entry.getValue().getRam() + " State " +
			// entry.getValue().getState());
		} else {
			isSuccess = false;
			mappingResults.clear();
		}
	}

	
	public LinkedList<PhysicalServer> sortOur(LinkedList<PhysicalServer> listPhY) {
		/* Sort PhyM in decreasing order by ID, CPU, need to discuss */
		Collections.sort(listPhY, new Comparator<PhysicalServer>() {
			@Override
			public int compare(PhysicalServer o1, PhysicalServer o2) {

				if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName()))
					return -1;
				if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName()))
					return 1;
				if (Integer.parseInt(o1.getName()) == Integer.parseInt(o2.getName())) {
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
		return listPhY;
	}

}
