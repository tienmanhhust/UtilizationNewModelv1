package fil.vdc.algorithm;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fil.vdc.model.BFS;
import fil.vdc.model.BFSNew;
import fil.vdc.model.LinkPhyEdge;
import fil.vdc.model.PhysicalServer;
import fil.vdc.model.SubstrateLink;
import fil.vdc.model.SubstrateSwitch;
import fil.vdc.model.Topology;
import fil.vdc.model.VDCRequest;
import fil.vdc.model.VirtualLink;
import fil.vdc.model.VirtualMachine;
import fil.vdc.model.modelHP;

public class LinkMapping2 {
	private boolean isSuccess;
	private LinkedList<VirtualLink> listVirLink;
	private Map<LinkedList<SubstrateSwitch>, Double> resultsLinkMapping;
	private Map<VirtualLink, LinkedList<SubstrateSwitch>> listPathMapped;
	private Map<VirtualLink, LinkedList<LinkPhyEdge>> listPhyEdgeMapped;
	private Map<LinkPhyEdge, Double> listBandwidthPhyEdge;
	private double ratioLinkMappingSecondNet;
	private double ratioLinkMappingOur;
	private int numLinkSuccessOur;
	private int numLinkSuccessSecondNet;
	private int numLinkSuccessGreenHead;
	private double powerConsumed;

	public LinkMapping2() {
		isSuccess = false;
		listVirLink = new LinkedList<>();
		resultsLinkMapping = new HashMap<>();
		listPathMapped = new HashMap<>();
		listBandwidthPhyEdge = new HashMap<>();
		listPhyEdgeMapped = new HashMap<>();
		ratioLinkMappingOur =0;
		ratioLinkMappingSecondNet =0;
		numLinkSuccessOur =0;
		numLinkSuccessSecondNet =0;
		numLinkSuccessGreenHead=0;
		powerConsumed =0;
		
	}

	
	public Topology linkMappingOurAlgorithm(Topology topo, VDCRequest vdc,
			Map<VirtualMachine, PhysicalServer> resultsVMMapping) {
		// get all Vlink and sort by bandwidth request
		double powerTemp = getPower(topo);
		listVirLink = vdc.getListVirLink();
		sortVirLinkDecBandwidth();
		// iterator vLink list and convert to start edge switch and end edge
		// switch
		LinkedList<SubstrateLink> listLinkBandwidth = topo.getLinkBandwidth();
		LinkedList<LinkPhyEdge> listPhyEdge = topo.getListLinkPhyEdge();
		LinkedList<SubstrateSwitch> listPhySwitch = topo.getListPhySwitch();
		for (VirtualLink vLink : listVirLink) {
			// get source and destination Virtual Machine of Virtual Link
			VirtualMachine vm1 = vLink.getsVM();
			VirtualMachine vm2 = vLink.getdVM();

			// get Physical Servers, which host source and destination Virtual
			// Machine
			PhysicalServer phy1 = resultsVMMapping.get(vm1);
			PhysicalServer phy2 = resultsVMMapping.get(vm2);

			// get Edge Switch, which connects with Physical Server
			Map<PhysicalServer, SubstrateSwitch> listLinkServers = topo.getListLinksServer();
			SubstrateSwitch edgeSwitch1 = listLinkServers.get(phy1);
			SubstrateSwitch edgeSwitch2 = listLinkServers.get(phy2);

			// check bandwidth Phy-Edge, if not satisfied break -> fail
			if (!checkPhyEdge(topo, phy1, edgeSwitch1, phy2, edgeSwitch2, vLink.getBandwidthRequest(),listPhyEdge))
			{
				break;
//				continue;
			}
			// get list agg switch connect to start edge switch, sort by state,
			// bandwidth
			Map<SubstrateSwitch, LinkedList<SubstrateSwitch>> listAggConnectEdge = topo.getListAggConnectEdge();
			Map<SubstrateSwitch, LinkedList<SubstrateSwitch>> listCoreConnectAgg = topo.getListCoreConnectAgg();
			LinkedList<SubstrateSwitch> listAggConnectStartEdge = new LinkedList<>();
			LinkedList<SubstrateSwitch> listAggConnectEndEdge = new LinkedList<>();
			
			LinkPhyEdge phy2Edge1=null, phy2Edge2=null;
			int countP2E = 0;
			for(int i=0; i<listPhyEdge.size();i++) {
				LinkPhyEdge link = listPhyEdge.get(i);
				if(link.getEdgeSwitch().equals(edgeSwitch1)&&link.getPhysicalServer().equals(phy1)) {
					phy2Edge1 = link;
					countP2E++;
				}
				if(link.getEdgeSwitch().equals(edgeSwitch2)&&link.getPhysicalServer().equals(phy2)) {
					phy2Edge2 = link;
					countP2E++;
				}
				if(countP2E==2)
					break;
			}
			
			// near groups		
			if (edgeSwitch1.equals(edgeSwitch2)) {
				///////////////////////////////////////////////////////
				LinkedList<LinkPhyEdge> phyEdge = new LinkedList<>();
				phyEdge.add(phy2Edge1);
				phyEdge.add(phy2Edge2);
				listPhyEdgeMapped.put(vLink, phyEdge);
				phy2Edge1.setBandwidth(phy2Edge1.getBandwidth()-vLink.getBandwidthRequest());
				phy2Edge2.setBandwidth(phy2Edge2.getBandwidth()-vLink.getBandwidthRequest());
				edgeSwitch1.setPort(getSwitchFromID(listPhySwitch, phy1.getName()), vLink.getBandwidthRequest());
				edgeSwitch1.setPort(getSwitchFromID(listPhySwitch, phy2.getName()), vLink.getBandwidthRequest());
				if(listBandwidthPhyEdge.containsKey(phy2Edge1)){
					listBandwidthPhyEdge.put(phy2Edge1, listBandwidthPhyEdge.get(phy2Edge1) + vLink.getBandwidthRequest());
				} else {
					listBandwidthPhyEdge.put(phy2Edge1, vLink.getBandwidthRequest());
				}
				
				if(listBandwidthPhyEdge.containsKey(phy2Edge2)) {
					listBandwidthPhyEdge.put(phy2Edge2, listBandwidthPhyEdge.get(phy2Edge2) + vLink.getBandwidthRequest());
				}else {
					listBandwidthPhyEdge.put(phy2Edge2, vLink.getBandwidthRequest());
				}
				
				LinkedList<SubstrateSwitch> list = new LinkedList<>();
				list.add(edgeSwitch1);
				double temp=0;
				if(resultsLinkMapping.containsKey(list))
					temp= resultsLinkMapping.get(list);

				resultsLinkMapping.put(list, vLink.getBandwidthRequest()+temp);
				listPathMapped.put(vLink, list);
				numLinkSuccessOur++;
			} else {
				for (Entry<SubstrateSwitch, LinkedList<SubstrateSwitch>> entry : listAggConnectEdge.entrySet()) {
					if (entry.getKey().equals(edgeSwitch1))
						listAggConnectStartEdge = entry.getValue();
					if (entry.getKey().equals(edgeSwitch2))
						listAggConnectEndEdge = entry.getValue();
				}
				// sort list Agg
				listAggConnectStartEdge = sortListSwitch(listAggConnectStartEdge);
				listAggConnectEndEdge = sortListSwitch(listAggConnectEndEdge);

				// check middle groups
				if (listAggConnectStartEdge.equals(listAggConnectEndEdge)) {
					for (SubstrateSwitch sw : listAggConnectStartEdge) {
						LinkedList<SubstrateSwitch> path = new LinkedList<>();
						path.add(edgeSwitch1);
						path.add(sw);
						path.add(edgeSwitch2);
						if (vLink.getBandwidthRequest() > getBanwidthOfPath(path, listLinkBandwidth))
							continue;
						else {
							double temp=0;
							listPathMapped.put(vLink, path);
							if(resultsLinkMapping.containsKey(path))
								temp= resultsLinkMapping.get(path);
							resultsLinkMapping.put(path, vLink.getBandwidthRequest()+temp);
							listLinkBandwidth = MapLink(path, listLinkBandwidth, vLink.getBandwidthRequest());
							numLinkSuccessOur++;

							LinkedList<LinkPhyEdge> phyEdge = new LinkedList<>();
							phyEdge.add(phy2Edge1);
							phyEdge.add(phy2Edge2);
							listPhyEdgeMapped.put(vLink, phyEdge);
							phy2Edge1.setBandwidth(phy2Edge1.getBandwidth()-vLink.getBandwidthRequest());
							phy2Edge2.setBandwidth(phy2Edge2.getBandwidth()-vLink.getBandwidthRequest());
							edgeSwitch1.setPort(getSwitchFromID(listPhySwitch, phy1.getName()), vLink.getBandwidthRequest());
							edgeSwitch2.setPort(getSwitchFromID(listPhySwitch, phy2.getName()), vLink.getBandwidthRequest());
							if(listBandwidthPhyEdge.containsKey(phy2Edge1)){
								listBandwidthPhyEdge.put(phy2Edge1, listBandwidthPhyEdge.get(phy2Edge1) + vLink.getBandwidthRequest());
							} else {
								listBandwidthPhyEdge.put(phy2Edge1, vLink.getBandwidthRequest());
							}
							
							if(listBandwidthPhyEdge.containsKey(phy2Edge2)) {
								listBandwidthPhyEdge.put(phy2Edge2, listBandwidthPhyEdge.get(phy2Edge2) + vLink.getBandwidthRequest());
							}else {
								listBandwidthPhyEdge.put(phy2Edge2, vLink.getBandwidthRequest());
							}
							
							break;
						}
					}
				} else {
					// far groups
					boolean check = false;
					for (int i = 0; i < listAggConnectStartEdge.size(); i++) {
						// choose pair edge switchs
						SubstrateSwitch startAggSwitch = listAggConnectStartEdge.get(i);
						SubstrateSwitch endAggSwitch = listAggConnectEndEdge.get(i);

						// get list core switch
						LinkedList<SubstrateSwitch> listCoreSwitchConnectPair = new LinkedList<>();
						for (Entry<SubstrateSwitch, LinkedList<SubstrateSwitch>> entry : listCoreConnectAgg.entrySet())
							if (entry.getKey().equals(startAggSwitch))
								listCoreSwitchConnectPair = entry.getValue();
						// sort list Core switch
						listCoreSwitchConnectPair = sortListSwitch(listCoreSwitchConnectPair);
						// iterator core switch
						for (SubstrateSwitch coreSwitch : listCoreSwitchConnectPair) {
							LinkedList<SubstrateSwitch> path = new LinkedList<>();
							path.add(edgeSwitch1);
							path.add(startAggSwitch);
							path.add(coreSwitch);
							path.add(endAggSwitch);
							path.add(edgeSwitch2);
							if (vLink.getBandwidthRequest() > getBanwidthOfPath(path, listLinkBandwidth))
								continue;
							else {
								double temp = 0;
								listPathMapped.put(vLink, path);
								if(resultsLinkMapping.containsKey(path))
									temp= resultsLinkMapping.get(path);
								resultsLinkMapping.put(path, vLink.getBandwidthRequest()+temp);
								listLinkBandwidth = MapLink(path, listLinkBandwidth, vLink.getBandwidthRequest());
								numLinkSuccessOur++;
								
								LinkedList<LinkPhyEdge> phyEdge = new LinkedList<>();
								phyEdge.add(phy2Edge1);
								phyEdge.add(phy2Edge2);
								listPhyEdgeMapped.put(vLink, phyEdge);
								phy2Edge1.setBandwidth(phy2Edge1.getBandwidth()-vLink.getBandwidthRequest());
								phy2Edge2.setBandwidth(phy2Edge2.getBandwidth()-vLink.getBandwidthRequest());
								edgeSwitch1.setPort(getSwitchFromID(listPhySwitch, phy1.getName()), vLink.getBandwidthRequest());
								edgeSwitch2.setPort(getSwitchFromID(listPhySwitch, phy2.getName()), vLink.getBandwidthRequest());
								if(listBandwidthPhyEdge.containsKey(phy2Edge1)){
									listBandwidthPhyEdge.put(phy2Edge1, listBandwidthPhyEdge.get(phy2Edge1) + vLink.getBandwidthRequest());
								} else {
									listBandwidthPhyEdge.put(phy2Edge1, vLink.getBandwidthRequest());
								}
								
								if(listBandwidthPhyEdge.containsKey(phy2Edge2)) {
									listBandwidthPhyEdge.put(phy2Edge2, listBandwidthPhyEdge.get(phy2Edge2) + vLink.getBandwidthRequest());
								}else {
									listBandwidthPhyEdge.put(phy2Edge2, vLink.getBandwidthRequest());
								}
								check = true;
								break;
							}
						}
						if(check)
							break;
					}
				}
			}

		}

		
		
		
		
		topo.setLinkBandwidth(listLinkBandwidth);
		topo.setListLinkPhyEdge(listPhyEdge);
		if (numLinkSuccessOur == listVirLink.size()) {
			isSuccess = true;
			//topo = updatePortSwitch(topo, resultsLinkMapping);
			//topo= updatePortPhyEdge(topo);
			powerConsumed = getPower(topo)-powerTemp;
		} else
		{
			isSuccess = false;
			topo= reverseLinkMapping(topo, resultsLinkMapping);
			reversePhyLinkMapping(topo);
		}
		return topo;
	}	
	public void routingtoAccessNode(Topology topo, VDCRequest vdc, Map<VirtualMachine, PhysicalServer> resultsVMMapping){
		double powerTemp = getPower(topo);
		listVirLink = vdc.getListVirLink();
		sortVirLinkDecBandwidth();
		// iterator vLink list and convert to start edge switch and end edge
		// switch
		LinkedList<SubstrateLink> listLinkBandwidth = topo.getLinkBandwidth();
		LinkedList<LinkPhyEdge> listPhyEdge = topo.getListLinkPhyEdge();
		LinkedList<SubstrateSwitch> listPhySwitch = topo.getListPhySwitch();
		
		
		
	}
	
	public Topology linkMappingSecondNet(Topology topo, VDCRequest vdc,
			Map<VirtualMachine, PhysicalServer> resultsVMMapping) {
		
		double powerTemp = getPower(topo);
		listVirLink = vdc.getListVirLink();
		sortVirLinkDecBandwidth();
		LinkedList<SubstrateLink> listLinkBandwidth = topo.getLinkBandwidth();
		LinkedList<LinkPhyEdge> listPhyEdge= topo.getListLinkPhyEdge();
		LinkedList<SubstrateSwitch> listPhySwitch =  topo.getListPhySwitch();
		
		for (VirtualLink vLink : listVirLink) {
			// get source and destination Virtual Machine of Virtual Link
			VirtualMachine vm1 = vLink.getsVM();
			VirtualMachine vm2 = vLink.getdVM();

			// get Physical Servers, which host source and destination Virtual
			// Machine
			PhysicalServer phy1 = resultsVMMapping.get(vm1);
			PhysicalServer phy2 = resultsVMMapping.get(vm2);

			// get Edge Switch, which connects with Physical Server
			Map<PhysicalServer, SubstrateSwitch> listLinkServers = topo.getListLinksServer();
			SubstrateSwitch edgeSwitch1 = listLinkServers.get(phy1);
			SubstrateSwitch edgeSwitch2 = listLinkServers.get(phy2);

			// check bandwidth Phy-Edge, if not satisfied break -> fail
			if (!checkPhyEdge(topo, phy1, edgeSwitch1, phy2, edgeSwitch2, vLink.getBandwidthRequest(), listPhyEdge))
			{
				break;
			}

			LinkPhyEdge phy2Edge1=null, phy2Edge2=null;
			int countP2E = 0;
			for(int i=0; i<listPhyEdge.size();i++) {
				LinkPhyEdge link = listPhyEdge.get(i);
				if(link.getEdgeSwitch().equals(edgeSwitch1)&&link.getPhysicalServer().equals(phy1)) {
					phy2Edge1 = link;
					countP2E++;
				}
				if(link.getEdgeSwitch().equals(edgeSwitch2)&&link.getPhysicalServer().equals(phy2)) {
					phy2Edge2 = link;
					countP2E++;
				}
				if(countP2E==2)
					break;
			}
			
			if (edgeSwitch1.equals(edgeSwitch2)) {
				// 1: up, 0: down
				LinkedList<LinkPhyEdge> phyEdge = new LinkedList<>();
				phyEdge.add(phy2Edge1);
				phyEdge.add(phy2Edge2);
				listPhyEdgeMapped.put(vLink, phyEdge);
				phy2Edge1.setBandwidth(phy2Edge1.getBandwidth()-vLink.getBandwidthRequest());
				phy2Edge2.setBandwidth(phy2Edge2.getBandwidth()-vLink.getBandwidthRequest());
				
				if(listBandwidthPhyEdge.containsKey(phy2Edge1))
					listBandwidthPhyEdge.put(phy2Edge1, listBandwidthPhyEdge.get(phy2Edge1) + vLink.getBandwidthRequest());
				else
					listBandwidthPhyEdge.put(phy2Edge1, vLink.getBandwidthRequest());
				
				if(listBandwidthPhyEdge.containsKey(phy2Edge2))
					listBandwidthPhyEdge.put(phy2Edge2, listBandwidthPhyEdge.get(phy2Edge2) + vLink.getBandwidthRequest());
				else
					listBandwidthPhyEdge.put(phy2Edge2, vLink.getBandwidthRequest());
				edgeSwitch1.setPort(getSwitchFromID(listPhySwitch, phy1.getName()), vLink.getBandwidthRequest());
				edgeSwitch1.setPort(getSwitchFromID(listPhySwitch, phy2.getName()), vLink.getBandwidthRequest());
				LinkedList<SubstrateSwitch> list = new LinkedList<>();
				list.add(edgeSwitch1);
				listPathMapped.put(vLink, list);
				// check if path already exits
				double temp =0;
				if(resultsLinkMapping.containsKey(list))
					temp = resultsLinkMapping.get(list);
				resultsLinkMapping.put(list, vLink.getBandwidthRequest()+temp);
				numLinkSuccessSecondNet++;
				continue;

			}
			
			BFSNew bfs = new BFSNew(edgeSwitch1, edgeSwitch2);
			LinkedList<SubstrateSwitch> shortestPath = bfs.getShortestPath(topo, vLink.getBandwidthRequest(), getTypeTraffic(phy1, phy2, topo));
			
			if (vLink.getBandwidthRequest() > getBanwidthOfPath(shortestPath, listLinkBandwidth))
				break;
			if (shortestPath.size()>0) {
								
				double temp =0;
				if(resultsLinkMapping.containsKey(shortestPath))
					temp = resultsLinkMapping.get(shortestPath);
				resultsLinkMapping.put(shortestPath, vLink.getBandwidthRequest()+temp);
				listLinkBandwidth = MapLink(shortestPath, listLinkBandwidth, vLink.getBandwidthRequest());
				numLinkSuccessSecondNet++;
				listPathMapped.put(vLink, shortestPath);
				LinkedList<LinkPhyEdge> phyEdge = new LinkedList<>();
				phyEdge.add(phy2Edge1);
				phyEdge.add(phy2Edge2);
				listPhyEdgeMapped.put(vLink, phyEdge);
				phy2Edge1.setBandwidth(phy2Edge1.getBandwidth()-vLink.getBandwidthRequest());
				phy2Edge2.setBandwidth(phy2Edge2.getBandwidth()-vLink.getBandwidthRequest());
				edgeSwitch1.setPort(getSwitchFromID(listPhySwitch, phy1.getName()), vLink.getBandwidthRequest());
				edgeSwitch2.setPort(getSwitchFromID(listPhySwitch, phy2.getName()), vLink.getBandwidthRequest());				
				if(listBandwidthPhyEdge.containsKey(phy2Edge1))
					listBandwidthPhyEdge.put(phy2Edge1, listBandwidthPhyEdge.get(phy2Edge1) + vLink.getBandwidthRequest());
				else
					listBandwidthPhyEdge.put(phy2Edge1, vLink.getBandwidthRequest());
				
				if(listBandwidthPhyEdge.containsKey(phy2Edge2))
					listBandwidthPhyEdge.put(phy2Edge2, listBandwidthPhyEdge.get(phy2Edge2) + vLink.getBandwidthRequest());
				else
					listBandwidthPhyEdge.put(phy2Edge2, vLink.getBandwidthRequest());
			}
		}
		
		topo.setLinkBandwidth(listLinkBandwidth);
		topo.setListLinkPhyEdge(listPhyEdge);
		if (numLinkSuccessSecondNet == listVirLink.size()) {
			isSuccess = true;
			//topo= updatePortPhyEdge(topo);
			//topo = updatePortSwitch(topo, resultsLinkMapping);
			
			powerConsumed = getPower(topo)-powerTemp;
		} else
		{
			isSuccess = false;
			topo= reverseLinkMapping(topo, resultsLinkMapping);
			reversePhyLinkMapping(topo);
		}
		return topo;
	}

	public Topology linkMappingGreenHead(Topology topo, VDCRequest vdc,
			Map<VirtualMachine, PhysicalServer> resultsVMMapping) {
		
		double powerTemp = getPower(topo);
		listVirLink = vdc.getListVirLink();
		sortVirLinkDecBandwidth();
		LinkedList<SubstrateLink> listLinkBandwidth = topo.getLinkBandwidth();
		LinkedList<LinkPhyEdge> listPhyEdge= topo.getListLinkPhyEdge();
		LinkedList<SubstrateSwitch> listPhySwitch = topo.getListPhySwitch();
		
		for (VirtualLink vLink : listVirLink) {
			// get source and destination Virtual Machine of Virtual Link
			VirtualMachine vm1 = vLink.getsVM();
			VirtualMachine vm2 = vLink.getdVM();

			// get Physical Servers, which host source and destination Virtual
			// Machine
			PhysicalServer phy1 = resultsVMMapping.get(vm1);
			PhysicalServer phy2 = resultsVMMapping.get(vm2);

			// get Edge Switch, which connects with Physical Server
			Map<PhysicalServer, SubstrateSwitch> listLinkServers = topo.getListLinksServer();
			SubstrateSwitch edgeSwitch1 = listLinkServers.get(phy1);
			SubstrateSwitch edgeSwitch2 = listLinkServers.get(phy2);

			// check bandwidth Phy-Edge, if not satisfied break -> fail
			if (!checkPhyEdge(topo, phy1, edgeSwitch1, phy2, edgeSwitch2, vLink.getBandwidthRequest(), listPhyEdge))
			{
				break;
			}

			LinkPhyEdge phy2Edge1=null, phy2Edge2=null;
			int countP2E = 0;
			for(int i=0; i<listPhyEdge.size();i++) {
				LinkPhyEdge link = listPhyEdge.get(i);
				if(link.getEdgeSwitch().equals(edgeSwitch1)&&link.getPhysicalServer().equals(phy1)) {
					phy2Edge1 = link;
					countP2E++;
				}
				if(link.getEdgeSwitch().equals(edgeSwitch2)&&link.getPhysicalServer().equals(phy2)) {
					phy2Edge2 = link;
					countP2E++;
				}
				if(countP2E==2)
					break;
			}
			
			if (edgeSwitch1.equals(edgeSwitch2)) {
				// 1: up, 0: down
				LinkedList<LinkPhyEdge> phyEdge = new LinkedList<>();
				phyEdge.add(phy2Edge1);
				phyEdge.add(phy2Edge2);
				listPhyEdgeMapped.put(vLink, phyEdge);
				phy2Edge1.setBandwidth(phy2Edge1.getBandwidth()-vLink.getBandwidthRequest());
				phy2Edge2.setBandwidth(phy2Edge2.getBandwidth()-vLink.getBandwidthRequest());
				edgeSwitch1.setPort(getSwitchFromID(listPhySwitch, phy1.getName()), vLink.getBandwidthRequest());
				edgeSwitch1.setPort(getSwitchFromID(listPhySwitch, phy2.getName()), vLink.getBandwidthRequest());
				if(listBandwidthPhyEdge.containsKey(phy2Edge1))
					listBandwidthPhyEdge.put(phy2Edge1, listBandwidthPhyEdge.get(phy2Edge1) + vLink.getBandwidthRequest());
				else
					listBandwidthPhyEdge.put(phy2Edge1, vLink.getBandwidthRequest());
				
				if(listBandwidthPhyEdge.containsKey(phy2Edge2))
					listBandwidthPhyEdge.put(phy2Edge2, listBandwidthPhyEdge.get(phy2Edge2) + vLink.getBandwidthRequest());
				else
					listBandwidthPhyEdge.put(phy2Edge2, vLink.getBandwidthRequest());
				
				LinkedList<SubstrateSwitch> list = new LinkedList<>();
				list.add(edgeSwitch1);
				listPathMapped.put(vLink, list);
				// check if path already exits
				double temp =0;
				if(resultsLinkMapping.containsKey(list))
					temp = resultsLinkMapping.get(list);
				resultsLinkMapping.put(list, vLink.getBandwidthRequest()+temp);
				numLinkSuccessGreenHead++;
				continue;
			}
			BFSNew bfs = new BFSNew(edgeSwitch1, edgeSwitch2);
			LinkedList<SubstrateSwitch> shortestPath = bfs.getShortestPathGH(topo);
			if (vLink.getBandwidthRequest() > getBanwidthOfPath(shortestPath, listLinkBandwidth))
				break;
			if (shortestPath.size()>0) {
				double temp =0;
				if(resultsLinkMapping.containsKey(shortestPath))
					temp = resultsLinkMapping.get(shortestPath);
				resultsLinkMapping.put(shortestPath, vLink.getBandwidthRequest()+temp);
				listLinkBandwidth = MapLink(shortestPath, listLinkBandwidth, vLink.getBandwidthRequest());
				numLinkSuccessGreenHead++;
				
				listPathMapped.put(vLink, shortestPath);
				LinkedList<LinkPhyEdge> phyEdge = new LinkedList<>();
				phyEdge.add(phy2Edge1);
				phyEdge.add(phy2Edge2);
				listPhyEdgeMapped.put(vLink, phyEdge);
				phy2Edge1.setBandwidth(phy2Edge1.getBandwidth()-vLink.getBandwidthRequest());
				phy2Edge2.setBandwidth(phy2Edge2.getBandwidth()-vLink.getBandwidthRequest());
				edgeSwitch1.setPort(getSwitchFromID(listPhySwitch, phy1.getName()), vLink.getBandwidthRequest());
				edgeSwitch2.setPort(getSwitchFromID(listPhySwitch, phy2.getName()), vLink.getBandwidthRequest());
				if(listBandwidthPhyEdge.containsKey(phy2Edge1))
					listBandwidthPhyEdge.put(phy2Edge1, listBandwidthPhyEdge.get(phy2Edge1) + vLink.getBandwidthRequest());
				else
					listBandwidthPhyEdge.put(phy2Edge1, vLink.getBandwidthRequest());
				
				if(listBandwidthPhyEdge.containsKey(phy2Edge2))
					listBandwidthPhyEdge.put(phy2Edge2, listBandwidthPhyEdge.get(phy2Edge2) + vLink.getBandwidthRequest());
				else
					listBandwidthPhyEdge.put(phy2Edge2, vLink.getBandwidthRequest());
			}
		}
		
		topo.setLinkBandwidth(listLinkBandwidth);
		topo.setListLinkPhyEdge(listPhyEdge);
		if (numLinkSuccessGreenHead == listVirLink.size()) {
			isSuccess = true;
			//topo= updatePortPhyEdge(topo);
			//topo = updatePortSwitch(topo, resultsLinkMapping);
			powerConsumed = getPower(topo)-powerTemp;
		} else
		{
			isSuccess = false;
			topo= reverseLinkMapping(topo, resultsLinkMapping);
			reversePhyLinkMapping(topo);
		}
		return topo;
	}
	
	public int getTypeTraffic(PhysicalServer src, PhysicalServer dst, Topology topo) {
		int type = 0; //2: middle, 3: far
		int K = topo.getListAggSwitchInPod().size();
		int K2 = K*K/4;
		int idOfSrc = Integer.parseInt(src.getName())-1;
		int idOfDst = Integer.parseInt(dst.getName())-1;
		if ((idOfDst/K2) == (idOfSrc/K2)) {
			type = 2;
		} else
			type = 3;
		return type;
	}
	
	// sort List switch in increasing order by ID
	public LinkedList<SubstrateSwitch> sortListSwitch(LinkedList<SubstrateSwitch> list) {
		Collections.sort(list, new Comparator<SubstrateSwitch>() {
			@Override
			public int compare(SubstrateSwitch o1, SubstrateSwitch o2) {
				if (Integer.parseInt(o1.getNameSubstrateSwitch()) < Integer.parseInt(o2.getNameSubstrateSwitch())) {
					return -1;
				}
				if (Integer.parseInt(o1.getNameSubstrateSwitch()) > Integer.parseInt(o2.getNameSubstrateSwitch())) {
					return 1;
				}
				return 0;
			}
		});
		return list;
	}

	// ??????
	public Topology mincostFlowLinkMapping(Topology topo) {
		return topo;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void sortVirLinkDecBandwidth() {
		/* Sort List Virtual Link in decreasing order by bandwidth request */
		Collections.sort(listVirLink, new Comparator<VirtualLink>() {
			@Override
			public int compare(VirtualLink o1, VirtualLink o2) {
				if (o1.getBandwidthRequest() < o2.getBandwidthRequest()) {
					return 1;
				}
				if (o1.getBandwidthRequest() > o2.getBandwidthRequest()) {
					return -1;
				}
				return 0;
			}
		});
	}

	public Map<Integer, LinkedList<SubstrateSwitch>> getAllPath(SubstrateSwitch s1, SubstrateSwitch s2, Topology topo) {
		BFS bfs = new BFS(s1, s2);
		bfs.run(topo);
		return bfs.getMypath();
	}

	// sort Path in increasing order by number of hop
	public Map<Integer, LinkedList<SubstrateSwitch>> sortPathByHop(Map<Integer, LinkedList<SubstrateSwitch>> paths) {
		List<Map.Entry<Integer, LinkedList<SubstrateSwitch>>> list = new LinkedList<>(paths.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<Integer, LinkedList<SubstrateSwitch>>>() {
			@Override
			public int compare(Map.Entry<Integer, LinkedList<SubstrateSwitch>> o1,
					Map.Entry<Integer, LinkedList<SubstrateSwitch>> o2) {
				int result = Integer.compare(o1.getValue().size(), o2.getValue().size());
				return result;
			}
		});
		// update map
		HashMap<Integer, LinkedList<SubstrateSwitch>> temp = new LinkedHashMap<>();
		for (Map.Entry<Integer, LinkedList<SubstrateSwitch>> entry : list) {
			temp.put(entry.getKey(), entry.getValue());
		}
		paths = temp;
		return paths;
	}

	public double getBanwidthOfPath(LinkedList<SubstrateSwitch> path, LinkedList<SubstrateLink> listLinkBandwidth) {
		double bandwidth = Integer.MAX_VALUE;
		for (int i = 0; i < path.size() - 1; i++) {
			SubstrateSwitch switch1 = path.get(i);
			SubstrateSwitch switch2 = path.get(i + 1);
			for (int j = 0; j < listLinkBandwidth.size(); j++) {
				SubstrateLink link = listLinkBandwidth.get(j);
				if (link.getStartSwitch().equals(switch1) && link.getEndSwitch().equals(switch2)) {
					if (link.getBandwidth() < bandwidth)
						bandwidth = link.getBandwidth();
					break;
				}
			}

		}
		return bandwidth;
	}
	
	public Topology updatePortSwitch(Topology topo, Map<LinkedList<SubstrateSwitch>, Double> resultsLinkMapping)
	{
		for (Entry<LinkedList<SubstrateSwitch>, Double> entry : resultsLinkMapping.entrySet()) {
			LinkedList<SubstrateSwitch> path = entry.getKey();
			double bandwidth = entry.getValue();
			if(path.size()<=1)
				continue;
			for (int i = 0; i < path.size() - 1; i++) {				
				SubstrateSwitch switch1 = path.get(i);
				SubstrateSwitch switch2 = path.get(i + 1);				
				// update sw port state
				switch1.setPort(switch2, bandwidth);
				switch2.setPort(switch1, bandwidth);				
			}
		}		
		return topo;
	}

	public Topology reverseLinkMapping(Topology topo, Map<LinkedList<SubstrateSwitch>, Double> resultsLinkMapping) {

		LinkedList<SubstrateLink> listLinkBandwidth = topo.getLinkBandwidth();
		LinkedList<SubstrateSwitch> listSwitch = topo.getListSwitch();
		for (Entry<LinkedList<SubstrateSwitch>, Double> entry : resultsLinkMapping.entrySet()) {
			LinkedList<SubstrateSwitch> path = entry.getKey();
			double bandwidth = entry.getValue();
			if(path.size()<=1)
				continue;
			for (int i = 0; i < path.size() - 1; i++) {
				
				SubstrateSwitch switch1 = path.get(i);
				SubstrateSwitch switch2 = path.get(i + 1);
				for (int j = 0; j < listLinkBandwidth.size(); j++) {
					SubstrateLink link = listLinkBandwidth.get(j);
					double bw = link.getBandwidth();
					// update bandwidth, two-direction
					//Vm1-> Vm2
					if (link.getStartSwitch().equals(switch1) && link.getEndSwitch().equals(switch2)) {
						
						link.setBandwidth(bw + bandwidth);
						listLinkBandwidth.set(j, link);
						// break;
					}
					//Vm2-> Vm1
					if (link.getStartSwitch().equals(switch2) && link.getEndSwitch().equals(switch1)) {
						link.setBandwidth(bw + bandwidth);
						listLinkBandwidth.set(j, link);
						// break;
					}
				}
				switch1.setPort(switch2, -bandwidth);
				switch2.setPort(switch1, -bandwidth);
			}
		}
		topo.setLinkBandwidth(listLinkBandwidth);
		topo.setListSwitch(listSwitch);
		return topo;
	}

	public LinkedList<SubstrateLink> MapLink(LinkedList<SubstrateSwitch> path, LinkedList<SubstrateLink> listLinkBandwidthTemp, double bandwidth)
	{
		for (int i = 0; i < path.size() - 1; i++) {
		
			SubstrateSwitch switch1 = path.get(i);
			SubstrateSwitch switch2 = path.get(i + 1);
			for (int j = 0; j < listLinkBandwidthTemp.size(); j++) {
				SubstrateLink link = listLinkBandwidthTemp.get(j);
				// update bandwidth, two-direction
				if (link.getStartSwitch().equals(switch1) && link.getEndSwitch().equals(switch2)) {
					link.setBandwidth(link.getBandwidth() - bandwidth);
					listLinkBandwidthTemp.set(j, link);
					// break;
				}
				//Vm1-> Vm2 == Vm2-Vm1
				if (link.getStartSwitch().equals(switch2) && link.getEndSwitch().equals(switch1)) {
					link.setBandwidth(link.getBandwidth() - bandwidth);
					listLinkBandwidthTemp.set(j, link);
					// break;
				}
			}
			switch1.setPort(switch2, bandwidth);
			switch2.setPort(switch1, bandwidth);
			
		}
		return listLinkBandwidthTemp;
	}

	public Topology updatePortPhyEdge(Topology topo)
	{

		LinkedList<SubstrateSwitch> phySwitch = topo.getListPhySwitch();
		for (LinkPhyEdge link : listBandwidthPhyEdge.keySet()) {
			PhysicalServer phy = link.getPhysicalServer();
			SubstrateSwitch edge = link.getEdgeSwitch();
			double bandwidth = listBandwidthPhyEdge.get(link);
			edge.setPort(getSwitchFromID(phySwitch, phy.getName()), bandwidth);
		}
		return topo;
	}
	
	public void reversePhyLinkMapping(Topology topo) {
		LinkedList<SubstrateSwitch> phySwitch = topo.getListPhySwitch();		
		for (LinkPhyEdge link : listBandwidthPhyEdge.keySet()) {
			link.setBandwidth(link.getBandwidth()+listBandwidthPhyEdge.get(link));
			link.getEdgeSwitch().setPort(getSwitchFromID(phySwitch, link.getPhysicalServer().getName()), -listBandwidthPhyEdge.get(link));
		}
	}
	
	public SubstrateSwitch getSwitchFromID(LinkedList<SubstrateSwitch> listSwitch, String id)
	{
		SubstrateSwitch s= new SubstrateSwitch();
		for(SubstrateSwitch sw: listSwitch)
			if(sw.getNameSubstrateSwitch().equals(id))
			{
				s= sw;
				break;
			}
		return s;
	}

	// assume edge-phy <=> phy-edge
	public boolean checkPhyEdge(Topology topo, PhysicalServer phy1, SubstrateSwitch edge1, PhysicalServer phy2,
			SubstrateSwitch edge2, double bandwidth, LinkedList<LinkPhyEdge> listPhyEdgeTemp) {
		boolean check = false;
		boolean Satisfied = false;
		for (LinkPhyEdge link : listPhyEdgeTemp) {
			if ((link.getPhysicalServer().equals(phy1) && link.getEdgeSwitch().equals(edge1))) {
				if (link.getBandwidth() >= bandwidth)
					Satisfied = true;
			}
			if ((link.getPhysicalServer().equals(phy2) && link.getEdgeSwitch().equals(edge2))) {
				if (link.getBandwidth() >= bandwidth)
					check = true;
			}

		}
		
		return (Satisfied&&check);
	}

	public Map<LinkedList<SubstrateSwitch>, Double> getResultsLinkMapping() {
		return resultsLinkMapping;
	}

	public Map<LinkPhyEdge, Double> getListBandwidthPhyEdge() {
		return listBandwidthPhyEdge;
	}

	public double getRatioLinkMappingSecondNet() {
		return ratioLinkMappingSecondNet;
	}

	public double getRatioLinkMappingOur() {
		return ratioLinkMappingOur;
	}

	public double getPower(Topology topo)
	{
		double power = 0;
		modelHP HP = new modelHP();	
		LinkedList<SubstrateSwitch> listSwitch = topo.getListPhySwitch();
//		for(SubstrateLink link: topo.getLinkBandwidth())
//		{
//			double bw = link.getBandwidth();
//			SubstrateSwitch s = link.getStartSwitch();
//			if(listSwitch.containsKey(s.getNameSubstrateSwitch()))
//			{
//				SubstrateSwitch sw = listSwitch.get(s.getNameSubstrateSwitch());
//				sw.setPort(link.getEndSwitch(), 1000-bw);
//				listSwitch.put(s.getNameSubstrateSwitch(), sw);
//			}
//			else				
//			{
//				s.setPort(link.getEndSwitch(), 1000-bw);
//				listSwitch.put(s.getNameSubstrateSwitch(), s);
//			}
//			
//		}
		for(SubstrateSwitch entry: listSwitch)
		{
			power+= HP.getPowerOfSwitch(entry);
		}
			
		return power;
	}

	public double getPowerConsumed() {
		return powerConsumed;
	}


	public Map<VirtualLink, LinkedList<SubstrateSwitch>> getListPathMapped() {
		return listPathMapped;
	}


	public void setListPathMapped(Map<VirtualLink, LinkedList<SubstrateSwitch>> listPathMapped) {
		this.listPathMapped = listPathMapped;
	}


	public Map<VirtualLink, LinkedList<LinkPhyEdge>> getListPhyEdgeMapped() {
		return listPhyEdgeMapped;
	}


	public void setListPhyEdgeMapped(Map<VirtualLink, LinkedList<LinkPhyEdge>> listPhyEdgeMapped) {
		this.listPhyEdgeMapped = listPhyEdgeMapped;
	}
}
