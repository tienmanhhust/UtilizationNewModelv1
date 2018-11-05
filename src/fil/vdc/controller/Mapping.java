package fil.vdc.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.security.auth.callback.LanguageCallback;

import java.util.Set;

import fil.vdc.algorithm.LinkMapping;
import fil.vdc.algorithm.VMMapping;
import fil.vdc.algorithm.VMMappingSecondNet;
import fil.vdc.model.BFSNew;
import fil.vdc.model.FatTree;
import fil.vdc.model.GenSubstrateNetwork;
import fil.vdc.model.GenVDCRequest;
import fil.vdc.model.LinkPhyEdge;
import fil.vdc.model.Log;
import fil.vdc.model.PhysicalServer;
import fil.vdc.model.Result;
import fil.vdc.model.SubstrateLink;
import fil.vdc.model.SubstrateNetwork;
import fil.vdc.model.SubstrateSwitch;
import fil.vdc.model.Topology;
import fil.vdc.model.VDCRequest;
import fil.vdc.model.VirtualLink;
import fil.vdc.model.VirtualMachine;
import fil.vdc.model.modelHP;
import sndlib.core.network.Link;

public class Mapping {
	private final double LAMBDA = 8;
	private final double MU = 0.5;
	private final double A = 1.1128;
	private final double B = 205.106;
	private final double timeWindow = 24;
	private int K = 10;
	private int C;

	private FatTree fatTree;
	private FatTree fatTree1;
	private FatTree fatTree2;
	private FatTree fatTree3;

	private Topology topo;
	private Topology topo1;
	private Topology topo2;
	private Topology topo3;
	////
	private FatTree fatTree00;
	private FatTree fatTree01;
	private FatTree fatTree02;
	private FatTree fatTree03;

	private Topology topo00;
	private Topology topo01;
	private Topology topo02;
	private Topology topo03;
	////
	private Topology topoNew;
	private FatTree fatTreeNew;

	private Topology topoNewBig;
	private FatTree fatTreeNewBig;

	private Map<VDCRequest, Topology> resultTopo;
	private Map<VDCRequest, Topology> resultTopoNew;

	private Map<VDCRequest, Topology> resultTopo0;
	private Map<VDCRequest, Topology> resultTopoNewBig;

	private Map<Integer, Topology> listTopo;
	private Map<Integer, Topology> listTopo0;

	private Map<Topology, SubstrateSwitch> listCoreToSubTopo;
	private Map<Topology, SubstrateSwitch> listCoreToSubTopoNew;

	private Map<VDCRequest, Integer> listVDCRequest;
	private GenVDCRequest genVDC;

	private VMMapping vmMapping;
	private LinkMapping linkMapping;

	private VMMapping vmMapping0;
	private LinkMapping linkMapping0;

	private VMMapping vmMappingNew;
	private LinkMapping linkMappingNew;

	private VMMapping vmMappingNewBig;
	private LinkMapping linkMappingNewBig;

	private Map<Integer, Map<VirtualMachine, PhysicalServer>> mappingResults;
	private Map<Integer, Map<VirtualMachine, PhysicalServer>> mappingResultsNew;

	private Map<Integer, Map<VirtualMachine, PhysicalServer>> mappingResults0;
	private Map<Integer, Map<VirtualMachine, PhysicalServer>> mappingResultsNewBig;

	private Map<Integer, Map<LinkedList<SubstrateSwitch>, Double>> linkMappingResults;
	private Map<Integer, Map<LinkedList<SubstrateSwitch>, Double>> linkMappingResultsNew;

	private Map<Integer, Map<LinkedList<SubstrateSwitch>, Double>> linkMappingResults0;
	private Map<Integer, Map<LinkedList<SubstrateSwitch>, Double>> linkMappingResultsNewBig;

	private Map<Integer, Map<LinkedList<SubstrateSwitch>, Double>> linkMappingResultsCore;
	private Map<Integer, Map<LinkedList<SubstrateSwitch>, Double>> linkMappingResultsCoreNew;

	private Map<SubstrateSwitch, Topology> listSubSwitchToCore;

	private Map<Integer, Map<VirtualLink, LinkedList<SubstrateSwitch>>> listPathMapped;
	private Map<Integer, Map<VirtualLink, LinkedList<LinkPhyEdge>>> listPhyEdgeMapped;
	private Map<VDCRequest, Topology> tempMapping;
	private Map<VDCRequest, Topology> tempMappingNew;
	private Map<VDCRequest, Topology> tempMapping0;
	private Map<VDCRequest, Topology> tempMappingNewBig;
	
	
	private Map<Topology, FatTree> listFatTree;
	private Map<Topology, FatTree> listFatTreeBig;
	private Map<Topology, FatTree> listFatTree0;
	private Map<Topology, FatTree> listFatTreeNew;

	private Map<Integer, Map<LinkPhyEdge, Double>> linkPhyEdge;
	private Map<Integer, Map<LinkPhyEdge, Double>> linkPhyEdgeNew;

	private Map<Integer, Map<LinkPhyEdge, Double>> linkPhyEdge0;
	private Map<Integer, Map<LinkPhyEdge, Double>> linkPhyEdgeNewBig;

	private double numSuccess;
	private double numSuccessNew;
	private double numSuccess0;
	private double numSuccessNewBig;

	private LinkedList<Integer> idVDCIteratored;
	private LinkedList<Integer> idVDCIteratoredNew;

	private LinkedList<Integer> idVDCIteratored0;
	private LinkedList<Integer> idVDCIteratoredNewBig;

	private double powerOur;
	private double powerOurNewSum;
	private double powerOurK4;
	private double powerOurK6_1;
	private double powerOurK6_2;
	private double powerOurK8;

	private double powerOurNew;
	private double powerOurNewNew;

	private double powerOur0;
	private double powerOurNewBig;

	private LinkedList<VDCRequest> listMapping;
	private LinkedList<VDCRequest> listMappingNew;
	private LinkedList<VDCRequest> listMapping0;
	private LinkedList<VDCRequest> listMappingNewBig;
	private SubstrateNetwork coreTopo;
	private SubstrateNetwork coreTopoNew;
	private double[] acceptCentralized;
	private double[] acceptDistributed;
	private double[] numberCentralized;
	private double[] numberDistributed;

	public Mapping() {
		listTopo = new HashMap<>();
		listTopo0 = new HashMap<>();

		listCoreToSubTopo = new HashMap<>();
		fatTree = new FatTree();
		fatTree00 = new FatTree();

		topo = new Topology();
		topoNew = new Topology();

		topo00 = new Topology();
		topo01 = new Topology();
		topo02 = new Topology();
		topo03 = new Topology();
		topoNewBig = new Topology();

		fatTree1 = new FatTree();
		fatTree2 = new FatTree();
		fatTree3 = new FatTree();
		fatTreeNew = new FatTree();

		fatTree01 = new FatTree();
		fatTree02 = new FatTree();
		fatTree03 = new FatTree();
		fatTreeNewBig = new FatTree();

		topo1 = new Topology();
		topo2 = new Topology();
		topo3 = new Topology();

		coreTopo = new SubstrateNetwork();
		coreTopoNew = new SubstrateNetwork();

		listVDCRequest = new HashMap<>();
		genVDC = new GenVDCRequest();
		mappingResults = new HashMap<>();
		mappingResultsNew = new HashMap<>();
		mappingResults0 = new HashMap<>();
		mappingResultsNewBig = new HashMap<>();

		numSuccess = 0;
		numSuccess0 = 0;
		numSuccessNew = 0;
		numSuccessNewBig = 0;
		idVDCIteratored = new LinkedList<>();
		idVDCIteratoredNew = new LinkedList<>();
		idVDCIteratored0 = new LinkedList<>();
		idVDCIteratoredNewBig = new LinkedList<>();

		listMapping = new LinkedList<>();
		listMappingNew = new LinkedList<>();
		listMapping0 = new LinkedList<>();
		listMappingNewBig = new LinkedList<>();
		linkPhyEdge = new HashMap<>();
		linkPhyEdge0 = new HashMap<>();
		linkPhyEdgeNewBig = new HashMap<>();

		powerOur0 = 0;
		powerOurNewBig = 0;

		powerOur = 0;
		powerOurNewSum = 0;
		powerOurNew = 0;
		powerOurNewNew = 0;
		powerOurK4 = 0;
		powerOurK6_1 = 0;
		powerOurK6_2 = 0;
		powerOurK8 = 0;
		linkMappingResults = new HashMap<>();
		linkMappingResultsNew = new HashMap<>();

		linkMappingResults0 = new HashMap<>();
		linkMappingResultsNewBig = new HashMap<>();

		listPathMapped = new HashMap<>();
		listPhyEdgeMapped = new HashMap<>();
		tempMapping = new HashMap<>();
		tempMappingNew = new HashMap<>();
		tempMapping0 = new HashMap<>();
		tempMappingNewBig = new HashMap<>();
		listFatTree = new HashMap<>();
		listFatTree0 = new HashMap<>();
		listFatTreeBig = new HashMap<>();

		resultTopo = new HashMap<>();
		resultTopoNew = new HashMap<>();
		resultTopo0 = new HashMap<>();
		resultTopoNewBig = new HashMap<>();
		linkMappingResultsCore = new HashMap<>();
		linkMappingResultsCoreNew = new HashMap<>();
		listCoreToSubTopoNew = new HashMap<>();
		listSubSwitchToCore = new HashMap<>();

		linkPhyEdgeNew = new HashMap<>();
		linkPhyEdgeNewBig = new HashMap<>();
		acceptCentralized = new double[20];
		acceptDistributed = new double[20];
		numberCentralized = new double[20];
		numberDistributed = new double[20];
	}

	public void run(int percent, int numberVMMax) {

		// For Distributed
		// Generate environment simulation
		topo = fatTree.genFatTree(8);
		topo1 = fatTree1.genFatTree(6);
		topo2 = fatTree2.genFatTree(6);
		topo3 = fatTree3.genFatTree(4);
		topoNew = fatTreeNew.genFatTree(10);

		topo00 = fatTree.genFatTree(8);
		topo01 = fatTree1.genFatTree(6);
		topo02 = fatTree2.genFatTree(6);
		topo03 = fatTree3.genFatTree(4);
		topoNewBig = fatTreeNew.genFatTree(10);
		listFatTree.put(topo, fatTree);
		listFatTree.put(topo1, fatTree1);
		listFatTree.put(topo2, fatTree2);
		listFatTree.put(topo3, fatTree3);

		listFatTree0.put(topo03, fatTree03);
		listFatTree0.put(topo03, fatTree02);
		listFatTree0.put(topo01, fatTree01);
		listFatTree0.put(topo00, fatTree00);

		GenSubstrateNetwork genSub = new GenSubstrateNetwork(8, 0.5, 0.5);
		GenSubstrateNetwork genSubNew = new GenSubstrateNetwork(8, 0.5, 0.5);
		coreTopo = genSub.genAbileneTopo();
		coreTopoNew = genSubNew.genAbileneTopo();

		LinkedList<SubstrateSwitch> listEngressSwitch = new LinkedList<>();
		LinkedList<SubstrateSwitch> listOutgressSwitch = new LinkedList<>();

		LinkedList<SubstrateSwitch> listEngressSwitchNew = new LinkedList<>();
		LinkedList<SubstrateSwitch> listOutgressSwitchNew = new LinkedList<>();

		listEngressSwitch.add(coreTopo.getListSubSwitch().get(9));
		listOutgressSwitch.add(coreTopo.getListSubSwitch().get(9));
		listEngressSwitch.add(coreTopo.getListSubSwitch().get(10));
		listOutgressSwitch.add(coreTopo.getListSubSwitch().get(10));
		listEngressSwitch.add(coreTopo.getListSubSwitch().get(5));
		listOutgressSwitch.add(coreTopo.getListSubSwitch().get(5));
		listEngressSwitch.add(coreTopo.getListSubSwitch().get(7));
		listOutgressSwitch.add(coreTopo.getListSubSwitch().get(7));

		coreTopo.setListEngressSwitch(listEngressSwitch);
		coreTopo.setListOutgressSwitch(listOutgressSwitch);

		listEngressSwitchNew.add(coreTopoNew.getListSubSwitch().get(9));
		listOutgressSwitchNew.add(coreTopoNew.getListSubSwitch().get(9));
		listEngressSwitchNew.add(coreTopoNew.getListSubSwitch().get(10));
		listOutgressSwitchNew.add(coreTopoNew.getListSubSwitch().get(10));
		listEngressSwitchNew.add(coreTopoNew.getListSubSwitch().get(5));
		listOutgressSwitchNew.add(coreTopoNew.getListSubSwitch().get(5));
		listEngressSwitchNew.add(coreTopoNew.getListSubSwitch().get(7));
		listOutgressSwitchNew.add(coreTopoNew.getListSubSwitch().get(7));

		coreTopoNew.setListEngressSwitch(listEngressSwitchNew);
		coreTopoNew.setListOutgressSwitch(listOutgressSwitchNew);

		listTopo.put(10, topo);
		listTopo.put(5, topo1);
		listTopo.put(7, topo2);
		listTopo.put(9, topo3);

		// coreTopoNew.setListLink(coreTopoNew.getListLink());
		// coreTopoNew.setListNode(coreTopoNew.getListNode());
		// coreTopoNew.setMap(coreTopoNew.getMap());

		listCoreToSubTopo.put(topo, coreTopo.getListSubSwitch().get(10));
		listCoreToSubTopo.put(topo1, coreTopo.getListSubSwitch().get(5));
		listCoreToSubTopo.put(topo2, coreTopo.getListSubSwitch().get(7));
		listCoreToSubTopo.put(topo3, coreTopo.getListSubSwitch().get(9));

		listSubSwitchToCore.put(coreTopo.getListSubSwitch().get(10), topo);
		listSubSwitchToCore.put(coreTopo.getListSubSwitch().get(5), topo1);
		listSubSwitchToCore.put(coreTopo.getListSubSwitch().get(7), topo2);
		listSubSwitchToCore.put(coreTopo.getListSubSwitch().get(9), topo3);

		listCoreToSubTopoNew.put(topoNew, coreTopoNew.getListSubSwitch().get(1));

		coreTopo.setSwitchToSwitch(listCoreToSubTopo);

		coreTopoNew.setSwitchToSwitch(listCoreToSubTopoNew);

		listVDCRequest = genVDC.gen((double) percent, MU, K, numberVMMax);

		// chia time de phuc vu
		double timeCurrent = 0;
		double timeNext = 0;

		// boolean find;
		// for(VDCRequest vdc : listVDCRequest) {
		// find=false;
		// while(!find) {
		// if(vdc.getStartTime() > timeCurrent && vdc.getStartTime()<=timeNext)
		// {
		// vdc.setStartTime(timeNext);
		// find = true;
		// } else {
		// timeCurrent = timeNext;
		// timeNext +=timeWindow;
		// }
		// }
		// }

		Set<Double> tmpListTime = new HashSet<>();
		for (Entry<VDCRequest, Integer> entry : listVDCRequest.entrySet()) {
			tmpListTime.add(entry.getKey().getStartTime());
			tmpListTime.add(entry.getKey().getEndTime());
		}

		LinkedList<Double> listTime = new LinkedList<>(tmpListTime);
		// sort list Time
		listTime = sortTime(listTime);

		Map<Double, LinkedList<VDCRequest>> tmplistVDCSort = new HashMap<>();
		LinkedList<VDCRequest> listVDC;
		for (int i = 0; i < listTime.size(); i++) {
			for (Entry<VDCRequest, Integer> entry : listVDCRequest.entrySet()) {
				VDCRequest vdc = entry.getKey();
				timeCurrent = vdc.getStartTime();
				if (vdc.getStartTime() == listTime.get(i)) {
					if (!tmplistVDCSort.containsKey(timeCurrent)) {
						listVDC = new LinkedList<>();
						tmplistVDCSort.put(timeCurrent, listVDC);
					}
					listVDC = tmplistVDCSort.get(timeCurrent);
					listVDC.add(vdc);
				}
				timeCurrent = vdc.getEndTime();
				if (vdc.getEndTime() == listTime.get(i)) {
					if (!tmplistVDCSort.containsKey(timeCurrent)) {
						listVDC = new LinkedList<>();
						tmplistVDCSort.put(timeCurrent, listVDC);
					}
					listVDC = tmplistVDCSort.get(timeCurrent);
					listVDC.add(vdc);
				}
			}
		}

		Map<Double, LinkedList<VDCRequest>> listVDCSort = new HashMap<>();

		listVDCSort = sortMapTime(tmplistVDCSort);
		// System.out.println("Size: " + listVDCSorti .size());
		double powerEdgeOur = 0;
		double powerEdgeOur0 = 0;
		double powerEdgeOurNew = 0;
		double powerEdgeOurNewBig = 0;
		double powerEdgeOurK4 = 0;
		double powerEdgeOurK6_1 = 0;
		double powerEdgeOurK6_2 = 0;
		double powerEdgeOurK8 = 0;

		double previousTimeOur = 0;
		double previousTimeOurNew = 0;
		double previousTimeOur0 = 0;
		double previousTimeOurNewBig = 0;
		int numberVMOur, numberVMOurNew, numberVMOur0, numberVMOurNewBig;

		double timeArrive = 0, load = 0, numberVMArrive = 0, timeAcceptedOur = 0, utilOur = 0, numberVMAcceptedOur = 0,
				timeAcceptedGH = 0, utilGH = 0, numberVMAcceptedGH = 0, timeAcceptedSN = 0, utilSN = 0,
				numberVMAcceptedSN = 0, timeAcceptedMig = 0, utilMig = 0, numberVMAcceptedMig = 0, timeAcceptedJoin = 0,
				utilJoin = 0, numberVMAcceptedJoin = 0, timeAcceptedLeave = 0, utilLeave = 0, numberVMAcceptedLeave = 0,
				timeAcceptedMix = 0, utilMix = 0, numberVMAcceptedMix = 0, timeAcceptedOurNew = 0, timeAcceptedOur0 = 0,
				timeAcceptedOurNewBig = 0, timeArriveNew = 0, numberVMArriveNew = 0, utilOurNew = 0, utilOurNewBig = 0,
				utilOur0 = 0, numberVMAcceptedOurNew = 0, numberVMAcceptedOur0 = 0, numberVMAcceptedOurNewBig = 0;
		timeNext = 2;
		C = 3 * (K * K * K);

		for (Entry<Double, LinkedList<VDCRequest>> entry : listVDCSort.entrySet()) {
			listVDC = entry.getValue();
			timeCurrent = entry.getKey();
			if (timeCurrent > timeWindow) {
				load = 100 * (timeArrive / timeWindow) / C;
				utilOur = 100 * (timeAcceptedOur / timeWindow) / C;
				utilOur0 = 100 * (timeAcceptedOur0 / timeWindow) / C;
				utilOurNew = 100 * (timeAcceptedOurNew / timeWindow) / C;
				utilOurNewBig = 100 * (timeAcceptedOurNewBig / timeWindow) / C;
				break;
			}

			// Calculate Power
			powerOur += powerEdgeOur * (timeCurrent - previousTimeOur);
			powerOur0 += powerEdgeOur0 * (timeCurrent - previousTimeOur0);
			powerOurK4 += powerEdgeOurK4 * (timeCurrent - previousTimeOur);
			powerOurK6_1 += powerEdgeOurK6_1 * (timeCurrent - previousTimeOur);
			powerOurK6_2 += powerEdgeOurK6_2 * (timeCurrent - previousTimeOur);
			powerOurK8 += powerEdgeOurK8 * (timeCurrent - previousTimeOur);

			powerOurNewSum += powerEdgeOurNew * (timeCurrent - previousTimeOurNew);
			powerOurNewBig += powerEdgeOurNewBig * (timeCurrent - previousTimeOurNewBig);
			// So VM dang duoc map toi thoi diem hien tai
			numberVMOur = getNumVmAlive(listMapping);
			numberVMOurNew = getNumVmAlive(listMappingNew);
			numberVMOur0 = getNumVmAlive(listMapping0);
			numberVMOurNewBig = getNumVmAlive(listMappingNewBig);

			if (numberVMOur != 0) {
				powerOurNew += (powerEdgeOur * (timeCurrent - previousTimeOur)) / listMapping.size();

			}
			if (numberVMOurNew != 0) {
				powerOurNewNew += (powerEdgeOurNew * (timeCurrent - previousTimeOur)) / listMappingNew.size();
			}
			if (numberVMOur0 != 0) {
				powerOur0 += (powerEdgeOur * (timeCurrent - previousTimeOur)) / listMapping0.size();

			}
			if (numberVMOurNewBig != 0) {
				powerOurNewBig += (powerEdgeOurNewBig * (timeCurrent - previousTimeOur)) / listMappingNewBig.size();
			}
			// sort VDC by priority (current, leave-> join and priority is
			// number of VM in vdc)
			sortListVDCByNoVM(listVDC, timeCurrent);

			for (VDCRequest vdc : listVDC) {
				// VDC leave
				if (vdc.getEndTime() == timeCurrent) {
					if (listMapping.contains(vdc)) {
						checkExpriedVDC(vdc);
						listMapping.remove(vdc);
					}
					if (listMappingNew.contains(vdc)) {
						checkExpriedVDCNew(vdc);
						listMappingNew.remove(vdc);
					}
					if (listMappingNewBig.contains(vdc)) {
						checkExpriedVDCNewBig(vdc);
						listMappingNewBig.remove(vdc);
					}
					if (listMapping0.contains(vdc)) {
						checkExpriedVDC0(vdc);
						listMappingNewBig.remove(vdc);
					}
				}

			}
			for (VDCRequest vdc : listVDC) {
				// If vdc join
				if (vdc.getStartTime() == timeCurrent) {


					if (vdc.getEndTime() > timeWindow)
						timeArrive += ((timeWindow - vdc.getStartTime()) * vdc.getNumVM());
					else
						timeArrive += (vdc.getLifeTime() * vdc.getNumVM());
					numberVMArrive += vdc.getNumVM();

					if (runOurPlus(vdc)) {
						if (vdc.getEndTime() > timeWindow)
							timeAcceptedOur += ((timeWindow - vdc.getStartTime()) * vdc.getNumVM());
						else
							timeAcceptedOur += (vdc.getLifeTime() * vdc.getNumVM());
						numberVMAcceptedOur += vdc.getNumVM();

					}

					if (runOurPlusNew(vdc)) {
						if (vdc.getEndTime() > timeWindow)
							timeAcceptedOurNew += ((timeWindow - vdc.getStartTime()) * vdc.getNumVM());
						else
							timeAcceptedOurNew += (vdc.getLifeTime() * vdc.getNumVM());
						numberVMAcceptedOurNew += vdc.getNumVM();
					}
					if (runOurPlus0(vdc)) {
						if (vdc.getEndTime() > timeWindow)
							timeAcceptedOur0 += ((timeWindow - vdc.getStartTime()) * vdc.getNumVM());
						else
							timeAcceptedOur0 += (vdc.getLifeTime() * vdc.getNumVM());
						numberVMAcceptedOur0 += vdc.getNumVM();
					}
					if (runOurNewBig(vdc,topoNewBig,fatTreeNewBig)) {
						if (vdc.getEndTime() > timeWindow)
							timeAcceptedOurNewBig += ((timeWindow - vdc.getStartTime()) * vdc.getNumVM());
						else
							timeAcceptedOurNewBig += (vdc.getLifeTime() * vdc.getNumVM());
						numberVMAcceptedOurNewBig += vdc.getNumVM();
					}
				}
				//

			}

			// tinh lai nang luong
			powerEdgeOur = getPowerNetworkDevice(listTopo) + getPowerServer(listTopo);
			powerEdgeOurNew = getPowerNetworkDeviceNew(topoNew) + getPowerServerNew(topoNew);

			powerEdgeOurK4 = getPowerNetworkDeviceNew(topo3) + getPowerServerNew(topo3);
			powerEdgeOurK6_1 = getPowerNetworkDeviceNew(topo2) + getPowerServerNew(topo2);
			powerEdgeOurK6_2 = getPowerNetworkDeviceNew(topo1) + getPowerServerNew(topo1);
			powerEdgeOurK8 = getPowerNetworkDeviceNew(topo) + getPowerServerNew(topo);

			previousTimeOur = timeCurrent;
			previousTimeOurNew = timeCurrent;

		}
		// System.out.println("Number VM " + numberVMAcceptedOur + " " +
		// numberVMArrive);
		MainController.resultAcceptionRate.info(
				"" + load + "\t" + utilOur + "\t" + utilOurNew + "\t" + numberVMAcceptedOur / numberVMArrive + "\t"
						+ numberVMAcceptedOurNew / numberVMArrive + "/t" + getNumSuccess() * 1.0 / listVDCRequest.size()
						+ "/t" + getNumSuccessNew() * 1.0 / listVDCRequest.size());
		MainController.resultPowerPerVDC
				.info("" + load + "\t" + utilOur + "\t" + getPowerOurNew() + "\t" + getPowerOurNewNew());
		MainController.resultPowerConsumption.info(
				"" + load + "\t" + utilOur + "\t" + getPowerOur() + "\t" + getPowerOurNewSum() + "\t" + getPowerOurK4()
						+ "\t" + getPowerOurK6_1() + "\t" + getPowerOurK6_2() + "\t" + getPowerOurK8());
	}

	public Map<SubstrateSwitch, Integer> sortListCoreSwitchToDC(SubstrateSwitch sSwitch) {
		Map<SubstrateSwitch, Integer> listSwitchByDistance = new HashMap<>();

		for (SubstrateSwitch s : coreTopo.getListEngressSwitch()) {
			BFSNew bfs = new BFSNew(sSwitch, s);
			if (s.equals(sSwitch)) {
				listSwitchByDistance.put(s, 0);
			} else {
				LinkedList<SubstrateSwitch> shortestPath = bfs.getShortestPathCore(coreTopo);
				listSwitchByDistance.put(s, shortestPath.size());
			}
		}
		listSwitchByDistance = sortMapByDistance(listSwitchByDistance);
		return listSwitchByDistance;
	}

	public boolean runOurPlus(VDCRequest vdc) {
		boolean isSuccess = false;
		listTopo = sortMapByCap(listTopo);
		int indexOfSwitchIngres = listVDCRequest.get(vdc);
		SubstrateSwitch coreSwitch = coreTopo.getListSubSwitch().get(indexOfSwitchIngres);
		Map<SubstrateSwitch, Integer> listSwitchDC = sortListCoreSwitchToDC(coreSwitch);

		// for (Entry<Integer, Topology> entry : listTopo.entrySet()) {
		// for (int i = 0; i < listTopo.size(); i++) {
		for (Entry<SubstrateSwitch, Integer> entry : listSwitchDC.entrySet()) {
			Topology topo = listSubSwitchToCore.get(entry.getKey());
			if (runOur(vdc, topo, listFatTree.get(topo), entry.getKey())) {
				resultTopo.put(vdc, topo);
				isSuccess = true;
				break;
			} else {
				// System.out.println("Mapping UnSuccess " + vdc.getVdcID());
			}
		}
		return isSuccess;
	}

	public boolean runOurPlusNew(VDCRequest vdc) {
		boolean isSuccess = false;
		boolean isPrint = true;
		int indexIngress = listVDCRequest.get(vdc);
		SubstrateSwitch ingressSwitch = coreTopoNew.getListSubSwitch().get(indexIngress);
		if (runOurNew(vdc, topoNew, fatTreeNew, isPrint, ingressSwitch)) {
			resultTopoNew.put(vdc, topoNew);
			isSuccess = true;
		} else {
			isSuccess = false;
		}

		return isSuccess;

	}
	
	public boolean runOurPlus0(VDCRequest vdc) {
		boolean isSuccess = false;
		boolean isPrint = true;
		int indexIngress = listVDCRequest.get(vdc);
//		SubstrateSwitch ingressSwitch = coreTopoNew.getListSubSwitch().get(indexIngress);
		for(Entry<Topology,FatTree> entry : listFatTree0.entrySet()){
			
			if (runOur0(vdc, entry.getKey(), entry.getValue())) {
				resultTopo0.put(vdc, topo);
				isSuccess = true;
				break;
			} else {
				isSuccess = false;
			}

		
		}
		return isSuccess;
		


	}

	public void printLogTopo(Topology topo, Result logFile) {
		if (!MainController.isEnableLog)
			return;
		logFile.info("Physical Server");
		for (Entry<Integer, PhysicalServer> entry : topo.getListPhyServers().entrySet()) {
			PhysicalServer phy = entry.getValue();
			logFile.info("" + phy.getName() + "\tCPU FREE: " + phy.getCpu());
		}

		logFile.info("Physical Switch");

	}

	public void printLogResultMapping(Topology topo, Result logFile, VDCRequest vdc,
			Map<VirtualMachine, PhysicalServer> vmMappingResults,
			Map<VirtualLink, LinkedList<SubstrateSwitch>> linkMappingResult) {
		if (!MainController.isEnableLog)
			return;
		logFile.info("MAPPING SUCCESS FOR VDC " + vdc.getVdcID());
		StringBuilder str = new StringBuilder();
		str.append("LIST VM-SERVER: ");
		for (Entry<VirtualMachine, PhysicalServer> entry : vmMappingResults.entrySet()) {
			str.append(entry.getKey().getNameVM()).append("-").append(entry.getValue().getName()).append("; ");
		}
		logFile.info(str.toString());
		logFile.info("LIST LINK: ");
		for (VirtualLink vLink : linkMappingResult.keySet()) {
			StringBuilder strlink = new StringBuilder();
			strlink.append(vLink.getsVM().getNameVM()).append("-").append(vLink.getdVM().getNameVM()).append(": ");
			LinkedList<SubstrateSwitch> list = linkMappingResult.get(vLink);
			for (SubstrateSwitch sw : list) {
				strlink.append(sw.getNameSubstrateSwitch()).append("->");
			}
			strlink.append(" BW REQUEST = ").append(vLink.getBandwidthRequest());
			logFile.info(strlink.toString());
		}
	}

	public void printLogResultMappingMigration(Topology topo, Result logFile,
			Map<VirtualMachine, PhysicalServer> vmMappingResults,
			Map<VirtualLink, LinkedList<SubstrateSwitch>> linkMappingResult) {
		if (!MainController.isEnableLog)
			return;
		StringBuilder str = new StringBuilder();
		str.append("LIST VM-SERVER: ");
		for (Entry<VirtualMachine, PhysicalServer> entry : vmMappingResults.entrySet()) {
			str.append(entry.getKey().getNameVM()).append("-").append(entry.getValue().getName()).append("; ");
		}
		logFile.info(str.toString());
		logFile.info("LIST LINK: ");
		for (VirtualLink vLink : linkMappingResult.keySet()) {
			StringBuilder strlink = new StringBuilder();
			strlink.append(vLink.getsVM().getNameVM()).append("-").append(vLink.getdVM().getNameVM()).append(": ");
			LinkedList<SubstrateSwitch> list = linkMappingResult.get(vLink);
			for (SubstrateSwitch sw : list) {
				strlink.append(sw.getNameSubstrateSwitch()).append("->");
			}
			strlink.append("BW REQUEST = ").append(vLink.getBandwidthRequest());
			logFile.info(strlink.toString());
		}

	}

	public boolean runOurNew(VDCRequest vdc, Topology topo, FatTree fatTree, boolean isPrint,
			SubstrateSwitch ingressSwitch) {

		boolean mapSuccess = false;
		boolean isNodeMappingSuccess = false;
		vmMappingNew = new VMMapping();
		linkMappingNew = new LinkMapping();
		linkMappingNew.setPrint(isPrint);
		linkMappingNew.setIngressSwitch(ingressSwitch);
		topo = vmMappingNew.run(vdc, topo, fatTree);
		if (vmMappingNew.isSuccess()) {
			isNodeMappingSuccess = true;
			// System.out.println("NodMapping SUccess");
			topo = linkMappingNew.linkMappingOurAlgorithm(topo, vdc, vmMappingNew.getMappingResults(), coreTopoNew);
			if (linkMappingNew.isSuccess()) {
				// System.out.println("Link Mapping SUccess");
				mapSuccess = true;
				numSuccessNew++;
				mappingResultsNew.put(vdc.getVdcID(), vmMappingNew.getMappingResults());
				tempMappingNew.put(vdc, topo);
				linkMappingResultsNew.put(vdc.getVdcID(), linkMappingNew.getResultsLinkMapping()); // linkMapping
																									// Results
				linkMappingResultsCoreNew.put(vdc.getVdcID(), linkMappingNew.getResultsLinkMappingCore());

				linkPhyEdgeNew.put(vdc.getVdcID(), linkMappingNew.getListBandwidthPhyEdge()); // link
																								// Phy-Edge
				listMappingNew.add(vdc);
				// printLogResultMapping(topo, MainController.logHEAE, vdc,
				// mappingResults.get(vdc.getVdcID()),
				// linkMapping.getListPathMapped());

			} else {

				topo = removeVMMapping(vmMappingNew.getMappingResults(), topo);
				Map<Integer, LinkedList<PhysicalServer>> listPhyPre = vmMappingNew.getListExceptPhy();
				listPhyPre.put(listPhyPre.size() + 1, vmMappingNew.getListPhyMaped());
				boolean check = false;
				do {
					VMMapping vmMappingNext = new VMMapping();
					vmMappingNext.setListExceptPhy(listPhyPre);
					topo = vmMappingNext.run(vdc, topo, fatTree);
					LinkMapping linkMappingNext = new LinkMapping();
					linkMappingNext.setIngressSwitch(ingressSwitch);
					if (vmMappingNext.isSuccess()) {
						isNodeMappingSuccess = true;
						topo = linkMappingNext.linkMappingOurAlgorithm(topo, vdc, vmMappingNext.getMappingResults(),
								coreTopoNew);
						if (linkMappingNext.isSuccess()) {
							mapSuccess = true;
							numSuccessNew++;
							mappingResultsNew.put(vdc.getVdcID(), vmMappingNext.getMappingResults());
							linkMappingResultsNew.put(vdc.getVdcID(), linkMappingNext.getResultsLinkMapping()); // linkMapping
							linkMappingResultsCoreNew.put(vdc.getVdcID(), linkMappingNew.getResultsLinkMappingCore());
							tempMappingNew.put(vdc, topo);
							// Results
							linkPhyEdgeNew.put(vdc.getVdcID(), linkMappingNext.getListBandwidthPhyEdge()); // link
																											// Phy-Edge
							listMappingNew.add(vdc);

							break;
						} else {
							check = true;

						}
					}

					if (vmMappingNext.getMappingResults().isEmpty()) {
						// if (isNodeMappingSuccess) {
						// MainController.logMapping.info("L");
						// } else {
						// MainController.logMapping.info("N");
						// }
						break;
					}
					topo = removeVMMapping(vmMappingNext.getMappingResults(), topo);
					listPhyPre = vmMappingNext.getListExceptPhy();
					listPhyPre.put(listPhyPre.size() + 1, vmMappingNext.getListPhyMaped());
				} while (check);
			}

		} else {
			// MainController.logMapping.info("N");
		}
		return mapSuccess;
	}

	public boolean runOur(VDCRequest vdc, Topology topo, FatTree fatTree, SubstrateSwitch ingressSwitc) {

		boolean mapSuccess = false;
		vmMapping = new VMMapping();
		linkMapping = new LinkMapping();
		linkMapping.setIngressSwitch(ingressSwitc);
		boolean isNodeMapping = false;
		// System.out.println("IngressSwitch " +
		// ingressSwitc.getNameSubstrateSwitch());
		topo = vmMapping.run(vdc, topo, fatTree);
		if (vmMapping.isSuccess()) {
			isNodeMapping = true;
			// System.out.println("VM Mapping SUccess ");
			topo = linkMapping.linkMappingOurAlgorithm(topo, vdc, vmMapping.getMappingResults(), coreTopo);
			if (linkMapping.isSuccess()) {
				// SysteNumber VMm.out.println("VM Mapping ");
				mapSuccess = true;
				numSuccess++;
				mappingResults.put(vdc.getVdcID(), vmMapping.getMappingResults());
				tempMapping.put(vdc, topo);
				linkMappingResults.put(vdc.getVdcID(), linkMapping.getResultsLinkMapping()); // linkMapping
																								// Results
				linkMappingResultsCore.put(vdc.getVdcID(), linkMapping.getResultsLinkMappingCore());
				linkPhyEdge.put(vdc.getVdcID(), linkMapping.getListBandwidthPhyEdge()); // link
																						// Phy-Edge
				listMapping.add(vdc);
				// printLogResultMapping(topo, MainController.logHEAE, vdc,
				// mappingResults.get(vdc.getVdcID()),
				// linkMapping.getListPathMapped());

			} else {

				topo = removeVMMapping(vmMapping.getMappingResults(), topo);
				Map<Integer, LinkedList<PhysicalServer>> listPhyPre = vmMapping.getListExceptPhy();
				listPhyPre.put(listPhyPre.size() + 1, vmMapping.getListPhyMaped());
				boolean check = false;
				do {
					VMMapping vmMappingNext = new VMMapping();
					vmMappingNext.setListExceptPhy(listPhyPre);
					topo = vmMappingNext.run(vdc, topo, fatTree);
					LinkMapping linkMappingNext = new LinkMapping();
					linkMappingNext.setIngressSwitch(ingressSwitc);
					if (vmMappingNext.isSuccess()) {
						isNodeMapping = true;
						topo = linkMappingNext.linkMappingOurAlgorithm(topo, vdc, vmMappingNext.getMappingResults(),
								coreTopo);
						if (linkMappingNext.isSuccess()) {
							mapSuccess = true;
							numSuccess++;
							mappingResults.put(vdc.getVdcID(), vmMappingNext.getMappingResults());
							linkMappingResults.put(vdc.getVdcID(), linkMappingNext.getResultsLinkMapping()); // linkMapping
							linkMappingResultsCore.put(vdc.getVdcID(), linkMapping.getResultsLinkMappingCore());
							tempMapping.put(vdc, topo);
							// Results
							linkPhyEdge.put(vdc.getVdcID(), linkMappingNext.getListBandwidthPhyEdge()); // link
																										// Phy-Edge
							listMapping.add(vdc);

							break;
						} else
							check = true;
					}

					if (vmMappingNext.getMappingResults().isEmpty()) {
						if (isNodeMapping) {
							// MainController.logMappingDistributed.info("L");
						} else {
							// MainController.logMappingDistributed.info("N");
						}
						break;
					}
					topo = removeVMMapping(vmMappingNext.getMappingResults(), topo);
					listPhyPre = vmMappingNext.getListExceptPhy();
					listPhyPre.put(listPhyPre.size() + 1, vmMappingNext.getListPhyMaped());
				} while (check);
			}

		} else {
			// MainController.logMappingDistributed.info("N");
		}
		return mapSuccess;
	}
	
	public boolean runOurNewBig(VDCRequest vdc, Topology topo, FatTree fatTree) {

		boolean mapSuccess = false;
		vmMappingNewBig = new VMMapping();
		linkMappingNewBig = new LinkMapping();
		linkMappingNewBig.setIngressSwitch(null);
		boolean isNodeMapping = false;
		// System.out.println("IngressSwitch " +
		// ingressSwitc.getNameSubstrateSwitch());
		topo = vmMappingNewBig.run(vdc, topo, fatTree);
		if (vmMappingNew.isSuccess()) {
			isNodeMapping = true;
			// System.out.println("VM Mapping SUccess ");
			topo = linkMappingNewBig.linkMappingOurAlgorithmOld(topo, vdc, vmMappingNewBig.getMappingResults());
			if (linkMappingNewBig.isSuccess()) {
				// SysteNumber VMm.out.println("VM Mapping ");
				mapSuccess = true;
				numSuccessNewBig++;
				mappingResultsNewBig.put(vdc.getVdcID(), vmMappingNewBig.getMappingResults());
				tempMappingNewBig.put(vdc, topo);
				linkMappingResultsNewBig.put(vdc.getVdcID(), linkMappingNewBig.getResultsLinkMapping()); // linkMapping
																								// Results
//				linkMappingResultsCoreNEw.put(vdc.getVdcID(), linkMappingNewBig.getResultsLinkMappingCore());
				linkPhyEdgeNewBig.put(vdc.getVdcID(), linkMappingNewBig.getListBandwidthPhyEdge()); // link
																						// Phy-Edge
				listMappingNewBig.add(vdc);
				// printLogResultMapping(topo, MainController.logHEAE, vdc,
				// mappingResults.get(vdc.getVdcID()),
				// linkMapping.getListPathMapped());

			} else {

				topo = removeVMMapping(vmMappingNewBig.getMappingResults(), topo);
				Map<Integer, LinkedList<PhysicalServer>> listPhyPre = vmMappingNewBig.getListExceptPhy();
				listPhyPre.put(listPhyPre.size() + 1, vmMappingNewBig.getListPhyMaped());
				boolean check = false;
				do {
					VMMapping vmMappingNext = new VMMapping();
					vmMappingNext.setListExceptPhy(listPhyPre);
					topo = vmMappingNext.run(vdc, topo, fatTree);
					LinkMapping linkMappingNext = new LinkMapping();
					linkMappingNext.setIngressSwitch(null);
					if (vmMappingNext.isSuccess()) {
						isNodeMapping = true;
						topo = linkMappingNext.linkMappingOurAlgorithmOld(topo, vdc, vmMappingNext.getMappingResults());
						if (linkMappingNext.isSuccess()) {
							mapSuccess = true;
							numSuccessNewBig++;
							mappingResultsNewBig.put(vdc.getVdcID(), vmMappingNext.getMappingResults());
							linkMappingResultsNewBig.put(vdc.getVdcID(), linkMappingNext.getResultsLinkMapping()); // linkMapping
//							linkMappingResultsCoreNewBig.put(vdc.getVdcID(), linkMapping.getResultsLinkMappingCore());
							tempMappingNewBig.put(vdc, topo);
							// Results
							linkPhyEdgeNewBig.put(vdc.getVdcID(), linkMappingNext.getListBandwidthPhyEdge()); // link
																										// Phy-Edge
							listMappingNewBig.add(vdc);

							break;
						} else
							check = true;
					}

					if (vmMappingNext.getMappingResults().isEmpty()) {
						if (isNodeMapping) {
							// MainController.logMappingDistributed.info("L");
						} else {
							// MainController.logMappingDistributed.info("N");
						}
						break;
					}
					topo = removeVMMapping(vmMappingNext.getMappingResults(), topo);
					listPhyPre = vmMappingNext.getListExceptPhy();
					listPhyPre.put(listPhyPre.size() + 1, vmMappingNext.getListPhyMaped());
				} while (check);
			}

		} else {
			// MainController.logMappingDistributed.info("N");
		}
		return mapSuccess;
	}
	public boolean runOur0(VDCRequest vdc, Topology topo, FatTree fatTree) {

		boolean mapSuccess = false;
		vmMapping0 = new VMMapping();
		linkMapping0 = new LinkMapping();
		linkMapping0.setIngressSwitch(null);
		boolean isNodeMapping = false;
		// System.out.println("IngressSwitch " +
		// ingressSwitc.getNameSubstrateSwitch());
		topo = vmMapping0.run(vdc, topo, fatTree);
		if (vmMapping0.isSuccess()) {
			isNodeMapping = true;
			// System.out.println("VM Mapping SUccess ");
			topo = linkMappingNewBig.linkMappingOurAlgorithmOld(topo, vdc, vmMappingNewBig.getMappingResults());
			if (linkMappingNewBig.isSuccess()) {
				// SysteNumber VMm.out.println("VM Mapping ");
				mapSuccess = true;
				numSuccessNewBig++;
				mappingResultsNewBig.put(vdc.getVdcID(), vmMappingNewBig.getMappingResults());
				tempMappingNewBig.put(vdc, topo);
				linkMappingResultsNewBig.put(vdc.getVdcID(), linkMappingNewBig.getResultsLinkMapping()); // linkMapping
																								// Results
//				linkMappingResultsCoreNEw.put(vdc.getVdcID(), linkMappingNewBig.getResultsLinkMappingCore());
				linkPhyEdgeNewBig.put(vdc.getVdcID(), linkMappingNewBig.getListBandwidthPhyEdge()); // link
																						// Phy-Edge
				listMappingNewBig.add(vdc);
				// printLogResultMapping(topo, MainController.logHEAE, vdc,
				// mappingResults.get(vdc.getVdcID()),
				// linkMapping.getListPathMapped());

			} else {

				topo = removeVMMapping(vmMappingNewBig.getMappingResults(), topo);
				Map<Integer, LinkedList<PhysicalServer>> listPhyPre = vmMappingNewBig.getListExceptPhy();
				listPhyPre.put(listPhyPre.size() + 1, vmMappingNewBig.getListPhyMaped());
				boolean check = false;
				do {
					VMMapping vmMappingNext = new VMMapping();
					vmMappingNext.setListExceptPhy(listPhyPre);
					topo = vmMappingNext.run(vdc, topo, fatTree);
					LinkMapping linkMappingNext = new LinkMapping();
					linkMappingNext.setIngressSwitch(null);
					if (vmMappingNext.isSuccess()) {
						isNodeMapping = true;
						topo = linkMappingNext.linkMappingOurAlgorithmOld(topo, vdc, vmMappingNext.getMappingResults());
						if (linkMappingNext.isSuccess()) {
							mapSuccess = true;
							numSuccessNewBig++;
							mappingResultsNewBig.put(vdc.getVdcID(), vmMappingNext.getMappingResults());
							linkMappingResultsNewBig.put(vdc.getVdcID(), linkMappingNext.getResultsLinkMapping()); // linkMapping
//							linkMappingResultsCoreNewBig.put(vdc.getVdcID(), linkMapping.getResultsLinkMappingCore());
							tempMappingNewBig.put(vdc, topo);
							// Results
							linkPhyEdgeNewBig.put(vdc.getVdcID(), linkMappingNext.getListBandwidthPhyEdge()); // link
																										// Phy-Edge
							listMappingNewBig.add(vdc);

							break;
						} else
							check = true;
					}

					if (vmMappingNext.getMappingResults().isEmpty()) {
						if (isNodeMapping) {
							// MainController.logMappingDistributed.info("L");
						} else {
							// MainController.logMappingDistributed.info("N");
						}
						break;
					}
					topo = removeVMMapping(vmMappingNext.getMappingResults(), topo);
					listPhyPre = vmMappingNext.getListExceptPhy();
					listPhyPre.put(listPhyPre.size() + 1, vmMappingNext.getListPhyMaped());
				} while (check);
			}

		} else {
			// MainController.logMappingDistributed.info("N");
		}
		return mapSuccess;
	}

	public boolean runRemap(LinkedList<VDCRequest> listVdc, Topology topo, FatTree fatTree,
			HashMap<Integer, Map<VirtualMachine, PhysicalServer>> mappingResults,
			HashMap<Integer, Map<LinkedList<SubstrateSwitch>, Double>> linkMappingResults,
			HashMap<Integer, Map<LinkPhyEdge, Double>> linkPhyEdge) {

		int num = 0;
		boolean isSuccess = false;
		listVdc = sortListVDCByCPU(listVdc);
		for (VDCRequest vdc : listVdc) {
			VMMapping vmMapping = new VMMapping();
			LinkMapping linkMapping = new LinkMapping();
			topo = vmMapping.run(vdc, topo, fatTree);
			if (vmMapping.isSuccess()) {
				topo = linkMapping.linkMappingOurAlgorithm(topo, vdc, vmMapping.getMappingResults(), coreTopo);
				if (linkMapping.isSuccess()) {
					num++;
					mappingResults.put(vdc.getVdcID(), vmMapping.getMappingResults());
					linkMappingResults.put(vdc.getVdcID(), linkMapping.getResultsLinkMapping()); // linkMapping
					// Results
					linkPhyEdge.put(vdc.getVdcID(), linkMapping.getListBandwidthPhyEdge()); // link
					// Phy-Edge
				} else {
					topo = removeVMMapping(vmMapping.getMappingResults(), topo);
					Map<Integer, LinkedList<PhysicalServer>> listPhyPre = vmMapping.getListExceptPhy();
					listPhyPre.put(listPhyPre.size() + 1, vmMapping.getListPhyMaped());
					boolean check = false;
					do {
						VMMapping vmMappingNext = new VMMapping();
						vmMappingNext.setListExceptPhy(listPhyPre);
						topo = vmMappingNext.run(vdc, topo, fatTree);
						LinkMapping linkMappingNext = new LinkMapping();
						if (vmMappingNext.isSuccess()) {
							topo = linkMappingNext.linkMappingOurAlgorithm(topo, vdc, vmMappingNext.getMappingResults(),
									coreTopo);
							if (linkMappingNext.isSuccess()) {
								num++;
								mappingResults.put(vdc.getVdcID(), vmMappingNext.getMappingResults());
								linkMappingResults.put(vdc.getVdcID(), linkMappingNext.getResultsLinkMapping()); // linkMapping
								// Results
								linkPhyEdge.put(vdc.getVdcID(), linkMappingNext.getListBandwidthPhyEdge()); // link
								// Phy-Edge
								break;
							} else
								check = true;
						}

						if (vmMappingNext.getMappingResults().isEmpty()) {
							break;
						}
						topo = removeVMMapping(vmMappingNext.getMappingResults(), topo);
						listPhyPre = vmMappingNext.getListExceptPhy();
						listPhyPre.put(listPhyPre.size() + 1, vmMappingNext.getListPhyMaped());
					} while (check);
				}
			}
		}
		// System.out.println("NUm "+num+" Size "+listVdc.size());
		if (num == listVdc.size())

			isSuccess = true;
		return isSuccess;
	}

	// check expired for each VDC and update physical server
	public void checkExpriedVDC(VDCRequest vdc) {
		Topology topo = tempMapping.get(vdc);
		if (mappingResults.containsKey(vdc.getVdcID())) {
			idVDCIteratored.add(vdc.getVdcID());
			Map<VirtualMachine, PhysicalServer> result = mappingResults.get(vdc.getVdcID());
			for (Entry<VirtualMachine, PhysicalServer> entry : result.entrySet()) {
				VirtualMachine vir = entry.getKey();
				PhysicalServer phy = entry.getValue();
				phy.setCpu(phy.getCpu() + vir.getCPU());
				phy.setRam(phy.getRam() + vir.getMemory());
			}
			mappingResults.remove(vdc.getVdcID());
		}

		// return link bandwidth
		if (linkMappingResults.containsKey(vdc.getVdcID())) {
			LinkedList<SubstrateLink> listLinkBandwidth = topo.getLinkBandwidth();
			Map<LinkedList<SubstrateSwitch>, Double> resultLink = linkMappingResults.get(vdc.getVdcID());
			for (Entry<LinkedList<SubstrateSwitch>, Double> entry : resultLink.entrySet()) {
				LinkedList<SubstrateSwitch> path = entry.getKey();
				double bandwidth = entry.getValue();
				for (int i = 0; i < path.size() - 1; i++) {
					SubstrateSwitch switch1 = path.get(i);
					SubstrateSwitch switch2 = path.get(i + 1);
					switch1.setPort(switch2, -bandwidth);
					switch2.setPort(switch1, -bandwidth);
					int c = 0;
					for (int j = 0; j < listLinkBandwidth.size(); j++) {
						SubstrateLink link = listLinkBandwidth.get(j);
						// update bandwidth, two-direction
						if (link.getStartSwitch().equals(switch1) && link.getEndSwitch().equals(switch2)) {
							link.setBandwidth(link.getBandwidth() + bandwidth);
							listLinkBandwidth.set(j, link);
							c++;
							if (c == 2)
								break;
						}
						if (link.getStartSwitch().equals(switch2) && link.getEndSwitch().equals(switch1)) {
							link.setBandwidth(link.getBandwidth() + bandwidth);
							listLinkBandwidth.set(j, link);
							c++;
							if (c == 2)
								break;
						}

					}
				}
			}
			// topo.setLinkBandwidth(listLinkBandwidth);
			// topo.setListSwitch(listSwitch);
			linkMappingResults.remove(vdc.getVdcID());
		}

		// return link bandwidth phy->edge
		if (linkPhyEdge.containsKey(vdc.getVdcID())) {
			LinkedList<SubstrateSwitch> listPhySwitch = topo.getListPhySwitch();
			Map<LinkPhyEdge, Double> listLinkPhyEdge = linkPhyEdge.get(vdc.getVdcID());
			for (LinkPhyEdge link : listLinkPhyEdge.keySet()) {
				PhysicalServer phy = link.getPhysicalServer();
				SubstrateSwitch edge = link.getEdgeSwitch();
				double bandwidth = listLinkPhyEdge.get(link);
				edge.setPort(getSwitchFromID(listPhySwitch, phy.getName()), 0 - bandwidth);
				link.setBandwidth(link.getBandwidth() + bandwidth);
			}
			linkPhyEdge.remove(vdc.getVdcID());
		}
		checkExpriedVDCCore(vdc);

		// MainController.logHEAE.info("TOPOLOGY AFTER VDC EXPRIED");
		// printLogTopo(topo, MainController.logHEAE);
	}

	public void checkExpriedVDCNewBig(VDCRequest vdc) {
		Topology topo = tempMappingNewBig.get(vdc);
		if (mappingResultsNewBig.containsKey(vdc.getVdcID())) {
			idVDCIteratoredNewBig.add(vdc.getVdcID());
			Map<VirtualMachine, PhysicalServer> result = mappingResultsNewBig.get(vdc.getVdcID());
			for (Entry<VirtualMachine, PhysicalServer> entry : result.entrySet()) {
				VirtualMachine vir = entry.getKey();
				PhysicalServer phy = entry.getValue();
				phy.setCpu(phy.getCpu() + vir.getCPU());
				phy.setRam(phy.getRam() + vir.getMemory());
			}
			mappingResultsNewBig.remove(vdc.getVdcID());
		}

		// return link bandwidth
		if (linkMappingResultsNewBig.containsKey(vdc.getVdcID())) {
			LinkedList<SubstrateLink> listLinkBandwidth = topo.getLinkBandwidth();
			Map<LinkedList<SubstrateSwitch>, Double> resultLink = linkMappingResultsNewBig.get(vdc.getVdcID());
			for (Entry<LinkedList<SubstrateSwitch>, Double> entry : resultLink.entrySet()) {
				LinkedList<SubstrateSwitch> path = entry.getKey();
				double bandwidth = entry.getValue();
				for (int i = 0; i < path.size() - 1; i++) {
					SubstrateSwitch switch1 = path.get(i);
					SubstrateSwitch switch2 = path.get(i + 1);
					switch1.setPort(switch2, -bandwidth);
					switch2.setPort(switch1, -bandwidth);
					int c = 0;
					for (int j = 0; j < listLinkBandwidth.size(); j++) {
						SubstrateLink link = listLinkBandwidth.get(j);
						// update bandwidth, two-direction
						if (link.getStartSwitch().equals(switch1) && link.getEndSwitch().equals(switch2)) {
							link.setBandwidth(link.getBandwidth() + bandwidth);
							listLinkBandwidth.set(j, link);
							c++;
							if (c == 2)
								break;
						}
						if (link.getStartSwitch().equals(switch2) && link.getEndSwitch().equals(switch1)) {
							link.setBandwidth(link.getBandwidth() + bandwidth);
							listLinkBandwidth.set(j, link);
							c++;
							if (c == 2)
								break;
						}

					}
				}
			}
			// topo.setLinkBandwidth(listLinkBandwidth);
			// topo.setListSwitch(listSwitch);
			linkMappingResultsNewBig.remove(vdc.getVdcID());
		}

		// return link bandwidth phy->edge
		if (linkPhyEdgeNewBig.containsKey(vdc.getVdcID())) {
			LinkedList<SubstrateSwitch> listPhySwitch = topo.getListPhySwitch();
			Map<LinkPhyEdge, Double> listLinkPhyEdge = linkPhyEdgeNewBig.get(vdc.getVdcID());
			for (LinkPhyEdge link : listLinkPhyEdge.keySet()) {
				PhysicalServer phy = link.getPhysicalServer();
				SubstrateSwitch edge = link.getEdgeSwitch();
				double bandwidth = listLinkPhyEdge.get(link);
				edge.setPort(getSwitchFromID(listPhySwitch, phy.getName()), 0 - bandwidth);
				link.setBandwidth(link.getBandwidth() + bandwidth);
			}
			linkPhyEdgeNewBig.remove(vdc.getVdcID());
		}
//		checkExpriedVDCCoreNew(vdc);

	}
	public void checkExpriedVDCNew(VDCRequest vdc) {
		Topology topo = tempMappingNew.get(vdc);
		if (mappingResultsNew.containsKey(vdc.getVdcID())) {
			idVDCIteratoredNew.add(vdc.getVdcID());
			Map<VirtualMachine, PhysicalServer> result = mappingResultsNew.get(vdc.getVdcID());
			for (Entry<VirtualMachine, PhysicalServer> entry : result.entrySet()) {
				VirtualMachine vir = entry.getKey();
				PhysicalServer phy = entry.getValue();
				phy.setCpu(phy.getCpu() + vir.getCPU());
				phy.setRam(phy.getRam() + vir.getMemory());
			}
			mappingResultsNew.remove(vdc.getVdcID());
		}

		// return link bandwidth
		if (linkMappingResultsNew.containsKey(vdc.getVdcID())) {
			LinkedList<SubstrateLink> listLinkBandwidth = topo.getLinkBandwidth();
			Map<LinkedList<SubstrateSwitch>, Double> resultLink = linkMappingResultsNew.get(vdc.getVdcID());
			for (Entry<LinkedList<SubstrateSwitch>, Double> entry : resultLink.entrySet()) {
				LinkedList<SubstrateSwitch> path = entry.getKey();
				double bandwidth = entry.getValue();
				for (int i = 0; i < path.size() - 1; i++) {
					SubstrateSwitch switch1 = path.get(i);
					SubstrateSwitch switch2 = path.get(i + 1);
					switch1.setPort(switch2, -bandwidth);
					switch2.setPort(switch1, -bandwidth);
					int c = 0;
					for (int j = 0; j < listLinkBandwidth.size(); j++) {
						SubstrateLink link = listLinkBandwidth.get(j);
						// update bandwidth, two-direction
						if (link.getStartSwitch().equals(switch1) && link.getEndSwitch().equals(switch2)) {
							link.setBandwidth(link.getBandwidth() + bandwidth);
							listLinkBandwidth.set(j, link);
							c++;
							if (c == 2)
								break;
						}
						if (link.getStartSwitch().equals(switch2) && link.getEndSwitch().equals(switch1)) {
							link.setBandwidth(link.getBandwidth() + bandwidth);
							listLinkBandwidth.set(j, link);
							c++;
							if (c == 2)
								break;
						}

					}
				}
			}
			// topo.setLinkBandwidth(listLinkBandwidth);
			// topo.setListSwitch(listSwitch);
			linkMappingResultsNew.remove(vdc.getVdcID());
		}

		// return link bandwidth phy->edge
		if (linkPhyEdgeNew.containsKey(vdc.getVdcID())) {
			LinkedList<SubstrateSwitch> listPhySwitch = topo.getListPhySwitch();
			Map<LinkPhyEdge, Double> listLinkPhyEdge = linkPhyEdgeNew.get(vdc.getVdcID());
			for (LinkPhyEdge link : listLinkPhyEdge.keySet()) {
				PhysicalServer phy = link.getPhysicalServer();
				SubstrateSwitch edge = link.getEdgeSwitch();
				double bandwidth = listLinkPhyEdge.get(link);
				edge.setPort(getSwitchFromID(listPhySwitch, phy.getName()), 0 - bandwidth);
				link.setBandwidth(link.getBandwidth() + bandwidth);
			}
			linkPhyEdgeNew.remove(vdc.getVdcID());
		}
		checkExpriedVDCCoreNew(vdc);

		// MainController.logHEAE.info("TOPOLOGY AFTER VDC EXPRIED");
		// printLogTopo(topo, MainController.logHEAE);
	}
	
	public void checkExpriedVDC0(VDCRequest vdc) {
		
		Topology topo = tempMapping0.get(vdc);
		if (mappingResults0.containsKey(vdc.getVdcID())) {
			idVDCIteratored0.add(vdc.getVdcID());
			Map<VirtualMachine, PhysicalServer> result = mappingResults0.get(vdc.getVdcID());
			for (Entry<VirtualMachine, PhysicalServer> entry : result.entrySet()) {
				VirtualMachine vir = entry.getKey();
				PhysicalServer phy = entry.getValue();
				phy.setCpu(phy.getCpu() + vir.getCPU());
				phy.setRam(phy.getRam() + vir.getMemory());
			}
			mappingResults0.remove(vdc.getVdcID());
		}

		// return link bandwidth
		if (linkMappingResults0.containsKey(vdc.getVdcID())) {
			LinkedList<SubstrateLink> listLinkBandwidth = topo.getLinkBandwidth();
			Map<LinkedList<SubstrateSwitch>, Double> resultLink = linkMappingResults0.get(vdc.getVdcID());
			for (Entry<LinkedList<SubstrateSwitch>, Double> entry : resultLink.entrySet()) {
				LinkedList<SubstrateSwitch> path = entry.getKey();
				double bandwidth = entry.getValue();
				for (int i = 0; i < path.size() - 1; i++) {
					SubstrateSwitch switch1 = path.get(i);
					SubstrateSwitch switch2 = path.get(i + 1);
					switch1.setPort(switch2, -bandwidth);
					switch2.setPort(switch1, -bandwidth);
					int c = 0;
					for (int j = 0; j < listLinkBandwidth.size(); j++) {
						SubstrateLink link = listLinkBandwidth.get(j);
						// update bandwidth, two-direction
						if (link.getStartSwitch().equals(switch1) && link.getEndSwitch().equals(switch2)) {
							link.setBandwidth(link.getBandwidth() + bandwidth);
							listLinkBandwidth.set(j, link);
							c++;
							if (c == 2)
								break;
						}
						if (link.getStartSwitch().equals(switch2) && link.getEndSwitch().equals(switch1)) {
							link.setBandwidth(link.getBandwidth() + bandwidth);
							listLinkBandwidth.set(j, link);
							c++;
							if (c == 2)
								break;
						}

					}
				}
			}
			// topo.setLinkBandwidth(listLinkBandwidth);
			// topo.setListSwitch(listSwitch);
			linkMappingResults0.remove(vdc.getVdcID());
		}

		// return link bandwidth phy->edge
		if (linkPhyEdge0.containsKey(vdc.getVdcID())) {
			LinkedList<SubstrateSwitch> listPhySwitch = topo.getListPhySwitch();
			Map<LinkPhyEdge, Double> listLinkPhyEdge = linkPhyEdge0.get(vdc.getVdcID());
			for (LinkPhyEdge link : listLinkPhyEdge.keySet()) {
				PhysicalServer phy = link.getPhysicalServer();
				SubstrateSwitch edge = link.getEdgeSwitch();
				double bandwidth = listLinkPhyEdge.get(link);
				edge.setPort(getSwitchFromID(listPhySwitch, phy.getName()), 0 - bandwidth);
				link.setBandwidth(link.getBandwidth() + bandwidth);
			}
			linkPhyEdge0.remove(vdc.getVdcID());
		}
//		checkExpriedVDCCoreNew(vdc);

		// MainController.logHEAE.info("TOPOLOGY AFTER VDC EXPRIED");
		// printLogTopo(topo, MainController.logHEAE);
	}
	public void checkExpriedVDCCore(VDCRequest vdc) {

		// return link bandwidth
		if (linkMappingResultsCore.containsKey(vdc.getVdcID())) {
			LinkedList<SubstrateLink> listLinkBandwidth = coreTopo.getListLink();
			Map<LinkedList<SubstrateSwitch>, Double> resultLink = linkMappingResultsCore.get(vdc.getVdcID());
			for (Entry<LinkedList<SubstrateSwitch>, Double> entry : resultLink.entrySet()) {
				LinkedList<SubstrateSwitch> path = entry.getKey();
				double bandwidth = entry.getValue();
				for (int i = 0; i < path.size() - 1; i++) {
					SubstrateSwitch switch1 = path.get(i);
					SubstrateSwitch switch2 = path.get(i + 1);
					switch1.setPort(switch2, -bandwidth);
					switch2.setPort(switch1, -bandwidth);
					int c = 0;
					for (int j = 0; j < listLinkBandwidth.size(); j++) {
						SubstrateLink link = listLinkBandwidth.get(j);
						// update bandwidth, two-direction
						if (link.getStartSwitch().equals(switch1) && link.getEndSwitch().equals(switch2)) {
							link.setBandwidth(link.getBandwidth() + bandwidth);
							listLinkBandwidth.set(j, link);
							c++;
							if (c == 2)
								break;
						}
						if (link.getStartSwitch().equals(switch2) && link.getEndSwitch().equals(switch1)) {
							link.setBandwidth(link.getBandwidth() + bandwidth);
							listLinkBandwidth.set(j, link);
							c++;
							if (c == 2)
								break;
						}

					}
				}
			}
			// topo.setLinkBandwidth(listLinkBandwidth);
			// topo.setListSwitch(listSwitch);
			linkMappingResultsCore.remove(vdc.getVdcID());
		}

		// return link bandwidth phy->edge

		// MainController.logHEAE.info("TOPOLOGY AFTER VDC EXPRIED");
		// printLogTopo(topo, MainController.logHEAE);
	}

	public void checkExpriedVDCCoreNew(VDCRequest vdc) {

		// return link bandwidth
		if (linkMappingResultsCoreNew.containsKey(vdc.getVdcID())) {
			LinkedList<SubstrateLink> listLinkBandwidth = coreTopoNew.getListLink();
			Map<LinkedList<SubstrateSwitch>, Double> resultLink = linkMappingResultsCoreNew.get(vdc.getVdcID());
			for (Entry<LinkedList<SubstrateSwitch>, Double> entry : resultLink.entrySet()) {
				LinkedList<SubstrateSwitch> path = entry.getKey();
				double bandwidth = entry.getValue();
				for (int i = 0; i < path.size() - 1; i++) {
					SubstrateSwitch switch1 = path.get(i);
					SubstrateSwitch switch2 = path.get(i + 1);
					switch1.setPort(switch2, -bandwidth);
					switch2.setPort(switch1, -bandwidth);
					int c = 0;
					for (int j = 0; j < listLinkBandwidth.size(); j++) {
						SubstrateLink link = listLinkBandwidth.get(j);
						// update bandwidth, two-direction
						if (link.getStartSwitch().equals(switch1) && link.getEndSwitch().equals(switch2)) {
							link.setBandwidth(link.getBandwidth() + bandwidth);
							listLinkBandwidth.set(j, link);
							c++;
							if (c == 2)
								break;
						}
						if (link.getStartSwitch().equals(switch2) && link.getEndSwitch().equals(switch1)) {
							link.setBandwidth(link.getBandwidth() + bandwidth);
							listLinkBandwidth.set(j, link);
							c++;
							if (c == 2)
								break;
						}

					}
				}
			}
			// topo.setLinkBandwidth(listLinkBandwidth);
			// topo.setListSwitch(listSwitch);
			linkMappingResultsCoreNew.remove(vdc.getVdcID());
		}

		// return link bandwidth phy->edge

		// MainController.logHEAE.info("TOPOLOGY AFTER VDC EXPRIED");
		// printLogTopo(topo, MainController.logHEAE);
	}

	public boolean checkSubLink(SubstrateLink link, SubstrateSwitch sw1, SubstrateSwitch sw2) {
		if (link.getStartSwitch().equals(sw1) && link.getEndSwitch().equals(sw2))
			return true;
		if (link.getStartSwitch().equals(sw2) && link.getEndSwitch().equals(sw1))
			return true;
		return false;
	}

	public boolean isContainLink(SubstrateSwitch sw1, SubstrateSwitch sw2, LinkedList<SubstrateSwitch> oldPath) {
		boolean isContain = false;
		for (int i = 0; i < oldPath.size() - 1; i++) {
			SubstrateSwitch switch1 = oldPath.get(i);
			SubstrateSwitch switch2 = oldPath.get(i + 1);
			if ((switch1.equals(sw1) && switch2.equals(sw2)) || (switch1.equals(sw2) && switch2.equals(sw1))) {
				isContain = true;
				break;
			}
		}
		return isContain;
	}

	public boolean checkBandwidthPath(SubstrateSwitch edge1, SubstrateSwitch agg1, SubstrateSwitch core,
			SubstrateSwitch agg2, SubstrateSwitch edge2, LinkedList<SubstrateSwitch> oldPath, double bw) {
		if (isContainLink(edge1, agg1, oldPath) || edge1.getBandwidthPort().get(agg1) + bw <= 1000) {
			if (isContainLink(agg1, core, oldPath) || agg1.getBandwidthPort().get(core) + bw <= 1000) {
				if (isContainLink(core, agg2, oldPath) || core.getBandwidthPort().get(agg2) + bw <= 1000) {
					if (isContainLink(edge2, agg2, oldPath) || edge2.getBandwidthPort().get(agg2) + bw <= 1000) {
						return true;
					} else
						return false;
				} else
					return false;
			} else
				return false;
		} else
			return false;
	}

	public double getBanwidthOfPathMigration(LinkedList<SubstrateSwitch> path,
			LinkedList<SubstrateLink> listLinkBandwidth, LinkedList<SubstrateSwitch> oldPath, double bandwidthRequest) {
		double bandwidth = Integer.MAX_VALUE;
		for (int i = 0; i < path.size() - 1; i++) {
			SubstrateSwitch switch1 = path.get(i);
			SubstrateSwitch switch2 = path.get(i + 1);
			for (int j = 0; j < listLinkBandwidth.size(); j++) {
				SubstrateLink link = listLinkBandwidth.get(j);
				double temp = 0;
				if (isContainLink(link, oldPath))
					temp = bandwidthRequest;
				if (link.getStartSwitch().equals(switch1) && link.getEndSwitch().equals(switch2)) {
					if (link.getBandwidth() + temp < bandwidth)
						bandwidth = link.getBandwidth() + temp;
					break;
				}
			}
		}
		return bandwidth;
	}

	public boolean isContainLink(SubstrateLink link, LinkedList<SubstrateSwitch> oldPath) {
		boolean isContain = false;
		SubstrateSwitch s = link.getStartSwitch();
		SubstrateSwitch d = link.getEndSwitch();
		for (int i = 0; i < oldPath.size() - 1; i++) {
			SubstrateSwitch switch1 = oldPath.get(i);
			SubstrateSwitch switch2 = oldPath.get(i + 1);
			if ((switch1.equals(s) && switch2.equals(d)) || (switch1.equals(d) && switch2.equals(s))) {
				isContain = true;
				break;
			}
		}
		return isContain;
	}

	public LinkedList<VirtualLink> getVirLink(LinkedList<VirtualLink> listVirLink, VirtualMachine vir) {
		LinkedList<VirtualLink> lstLink = new LinkedList<>();
		for (VirtualLink vLink : listVirLink) {
			VirtualMachine s = vLink.getsVM();
			VirtualMachine d = vLink.getdVM();
			if (s.equals(vir) || d.equals(vir))
				lstLink.add(vLink);
		}
		// sort by bw request decrease
		Collections.sort(lstLink, new Comparator<VirtualLink>() {
			@Override
			public int compare(VirtualLink o1, VirtualLink o2) {
				return Double.compare(o2.getBandwidthRequest(), o1.getBandwidthRequest());
			}
		});

		return lstLink;
	}

	public VDCRequest getVDCRequest(int id) {
		VDCRequest vdc = new VDCRequest();
		for (Entry<VDCRequest, Integer> req : listVDCRequest.entrySet())
			if (req.getKey().getVdcID() == id) {
				vdc = req.getKey();
				break;
			}
		return vdc;
	}

	// kiem tra bang thong giua hai server
	public boolean checkLink(PhysicalServer s, PhysicalServer d, Topology topo) {
		boolean check = true;
		Map<PhysicalServer, SubstrateSwitch> listLinkServers = topo.getListLinksServer();
		LinkedList<LinkPhyEdge> listPhyEdge = topo.getListLinkPhyEdge();
		LinkedList<SubstrateLink> listLinkBandwidth = topo.getLinkBandwidth();
		SubstrateSwitch edgeSwitch1 = listLinkServers.get(s);
		SubstrateSwitch edgeSwitch2 = listLinkServers.get(d);

		// check bandwidth Phy-Edge, if not satisfied break -> fail
		if (!checkPhyEdge(topo, s, edgeSwitch1, d, edgeSwitch2, 0, listPhyEdge)) {
			check = false;
		} else {
			Map<SubstrateSwitch, LinkedList<SubstrateSwitch>> listAggConnectEdge = topo.getListAggConnectEdge();
			Map<SubstrateSwitch, LinkedList<SubstrateSwitch>> listCoreConnectAgg = topo.getListCoreConnectAgg();
			LinkedList<SubstrateSwitch> listAggConnectStartEdge = new LinkedList<>();
			LinkedList<SubstrateSwitch> listAggConnectEndEdge = new LinkedList<>();
			if (edgeSwitch1.equals(edgeSwitch2)) {
				check = true;
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
						if (getBanwidthOfPath(path, listLinkBandwidth) == 0)
							continue;
						else {
							check = true;
							break;
						}
					}
				} else {
					// far groups
					boolean isInterupt = false;
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
							if (getBanwidthOfPath(path, listLinkBandwidth) == 0)
								check = false;
							else {
								check = true;
								isInterupt = true;
								break;
							}
						}
						if (isInterupt)
							break;

					}
				}
			}
		}
		return check;
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

	public boolean checkPhyEdge(Topology topo, PhysicalServer phy1, SubstrateSwitch edge1, PhysicalServer phy2,
			SubstrateSwitch edge2, double bandwidth, LinkedList<LinkPhyEdge> listPhyEdgeTemp) {
		boolean Satisfied = false;
		for (LinkPhyEdge link : listPhyEdgeTemp) {
			if ((link.getPhysicalServer().equals(phy1) && link.getEdgeSwitch().equals(edge1))) {
				if (link.getBandwidth() >= bandwidth) {
					Satisfied = true;
					break;
				}
			}
		}
		for (LinkPhyEdge link : listPhyEdgeTemp) {
			if ((link.getPhysicalServer().equals(phy2) && link.getEdgeSwitch().equals(edge2))) {
				if (link.getBandwidth() >= bandwidth) {
					Satisfied = true;
					break;
				} else
					Satisfied = false;
			}
		}

		return Satisfied;
	}

	public LinkedList<VirtualMachine> getListVirtualMachine(PhysicalServer phy,
			Map<Integer, Map<VirtualMachine, PhysicalServer>> mappingResult) {
		LinkedList<VirtualMachine> list = new LinkedList<>();
		for (Entry<Integer, Map<VirtualMachine, PhysicalServer>> entry : mappingResult.entrySet()) {
			Map<VirtualMachine, PhysicalServer> map = entry.getValue();
			for (Entry<VirtualMachine, PhysicalServer> e : map.entrySet()) {
				if (e.getValue().equals(phy))
					list.add(e.getKey());
			}
		}
		return list;
	}

	public SubstrateSwitch getSwitchFromID(LinkedList<SubstrateSwitch> listSwitch, String id) {
		SubstrateSwitch s = new SubstrateSwitch();
		for (SubstrateSwitch sw : listSwitch)
			if (sw.getNameSubstrateSwitch().equals(id)) {
				s = sw;
				break;
			}
		return s;
	}

	public Topology removeVMMapping(Map<VirtualMachine, PhysicalServer> results, Topology topo) {
		try {

			if (!results.isEmpty())
				for (Entry<VirtualMachine, PhysicalServer> entry : results.entrySet()) {
					VirtualMachine vir = entry.getKey();
					PhysicalServer phy = entry.getValue();
					phy.setCpu(phy.getCpu() + vir.getCPU());
					phy.setRam(phy.getRam() + vir.getMemory());
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return topo;
	}

	public double getPowerNetworkDevice(Map<Integer, Topology> listTopo) {
		double power = 0;
		modelHP HP = new modelHP();

		// for (SubstrateLink link : topo.getLinkBandwidth()) {
		// double bw = link.getBandwidth();
		// SubstrateSwitch s = link.getStartSwitch();
		// if (listSwitch.containsKey(s.getNameSubstrateSwitch())) {
		// SubstrateSwitch sw = listSwitch.get(s.getNameSubstrateSwitch());
		// sw.setPort(link.getEndSwitch(), 1000 - bw);
		// listSwitch.put(s.getNameSubstrateSwitch(), sw);
		// } else
		//
		// {
		// s.setPort(link.getEndSwitch(), 1000 - bw);
		// listSwitch.put(s.getNameSubstrateSwitch(), s);
		// }
		//
		// }
		for (Entry<Integer, Topology> entry1 : listTopo.entrySet()) {
			Topology topo = entry1.getValue();
			LinkedList<SubstrateSwitch> listSwitch = topo.getListPhySwitch();
			for (SubstrateSwitch entry : listSwitch) {
				power += HP.getPowerOfSwitch(entry);
			}
		}

		return power;
	}

	public double getPowerNetworkDeviceNew(Topology topo) {
		double power = 0;
		modelHP HP = new modelHP();

		LinkedList<SubstrateSwitch> listSwitch = topo.getListPhySwitch();
		// for (SubstrateLink link : topo.getLinkBandwidth()) {
		// double bw = link.getBandwidth();
		// SubstrateSwitch s = link.getStartSwitch();
		// if (listSwitch.containsKey(s.getNameSubstrateSwitch())) {
		// SubstrateSwitch sw = listSwitch.get(s.getNameSubstrateSwitch());
		// sw.setPort(link.getEndSwitch(), 1000 - bw);
		// listSwitch.put(s.getNameSubstrateSwitch(), sw);
		// } else
		//
		// {
		// s.setPort(link.getEndSwitch(), 1000 - bw);
		// listSwitch.put(s.getNameSubstrateSwitch(), s);
		// }
		//
		// }
		for (SubstrateSwitch entry : listSwitch) {
			power += HP.getPowerOfSwitch(entry);
		}

		return power;
	}

	public double getPowerServerNew(Topology topo) {
		double power = 0;
		Map<Integer, PhysicalServer> listPhyServers = topo.getListPhyServers();
		for (Entry<Integer, PhysicalServer> entry : listPhyServers.entrySet()) {
			if (entry.getValue().getState() == 1) {
				double cpuUlt = entry.getValue().getCpu() / entry.getValue().getCPU();
				power += (A * cpuUlt + B);
			}

		}
		return power;
	}

	public double getPowerServer(Map<Integer, Topology> listTopo) {
		double power = 0;

		for (Entry<Integer, Topology> entry1 : listTopo.entrySet()) {
			Topology topo = entry1.getValue();
			Map<Integer, PhysicalServer> listPhyServers = topo.getListPhyServers();
			for (Entry<Integer, PhysicalServer> entry : listPhyServers.entrySet()) {
				if (entry.getValue().getState() == 1) {
					double cpuUlt = entry.getValue().getCpu() / entry.getValue().getCPU();
					power += (A * cpuUlt + B);
				}
			}
		}
		return power;
	}

	public LinkedList<VDCRequest> sortVDCTimeEnd(LinkedList<VDCRequest> listVDCEnd) {
		/* Sort PhyM in decreasing order by ID, CPU, need to discuss */
		Collections.sort(listVDCEnd, new Comparator<VDCRequest>() {
			@Override
			public int compare(VDCRequest o1, VDCRequest o2) {

				if (o1.getEndTime() > o2.getEndTime())
					return 1;
				if (o1.getEndTime() < o2.getEndTime())
					return -1;
				return 0;
			}
		});
		return listVDCEnd;
	}

	public LinkedList<Double> sortTime(LinkedList<Double> listTime) {
		/* Sort PhyM in decreasing order by ID, CPU, need to discuss */
		Collections.sort(listTime, new Comparator<Double>() {
			@Override
			public int compare(Double o1, Double o2) {

				if (o1 > o2)
					return 1;
				if (o1 < o2)
					return -1;
				return 0;
			}
		});
		return listTime;
	}

	public Map<Double, LinkedList<VDCRequest>> sortMapTime(Map<Double, LinkedList<VDCRequest>> unsortMap) {

		// Convert Map to List
		List<Map.Entry<Double, LinkedList<VDCRequest>>> list = new LinkedList<Map.Entry<Double, LinkedList<VDCRequest>>>(
				unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<Double, LinkedList<VDCRequest>>>() {
			public int compare(Map.Entry<Double, LinkedList<VDCRequest>> o1,
					Map.Entry<Double, LinkedList<VDCRequest>> o2) {
				return Double.compare(o1.getKey(), o2.getKey());
			}
		});

		// Convert sorted map back to a Map
		Map<Double, LinkedList<VDCRequest>> sortedMap = new LinkedHashMap<Double, LinkedList<VDCRequest>>();
		for (Iterator<Map.Entry<Double, LinkedList<VDCRequest>>> it = list.iterator(); it.hasNext();) {
			Map.Entry<Double, LinkedList<VDCRequest>> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	public Map<Integer, PhysicalServer> sortMapByComparator(Map<Integer, PhysicalServer> unsortMap) {

		// Convert Map to List
		List<Map.Entry<Integer, PhysicalServer>> list = new LinkedList<Map.Entry<Integer, PhysicalServer>>(
				unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<Integer, PhysicalServer>>() {
			public int compare(Map.Entry<Integer, PhysicalServer> o1, Map.Entry<Integer, PhysicalServer> o2) {
				return Double.compare(o1.getValue().getCpu(), o2.getValue().getCpu());
			}
		});

		// Convert sorted map back to a Map
		Map<Integer, PhysicalServer> sortedMap = new LinkedHashMap<Integer, PhysicalServer>();
		for (Iterator<Map.Entry<Integer, PhysicalServer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<Integer, PhysicalServer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	public Map<SubstrateSwitch, Integer> sortMapByDistance(Map<SubstrateSwitch, Integer> unsortMap) {

		// Convert Map to List
		List<Map.Entry<SubstrateSwitch, Integer>> list = new LinkedList<Map.Entry<SubstrateSwitch, Integer>>(
				unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<SubstrateSwitch, Integer>>() {
			public int compare(Map.Entry<SubstrateSwitch, Integer> o1, Map.Entry<SubstrateSwitch, Integer> o2) {
				return Double.compare(o1.getValue(), o2.getValue());
			}
		});

		// Convert sorted map back to a Map
		Map<SubstrateSwitch, Integer> sortedMap = new LinkedHashMap<SubstrateSwitch, Integer>();
		for (Iterator<Map.Entry<SubstrateSwitch, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<SubstrateSwitch, Integer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	public Map<Integer, PhysicalServer> sortMapByComparatorPlus(Map<Integer, PhysicalServer> unsortMap) {

		// Convert Map to List
		List<Map.Entry<Integer, PhysicalServer>> list = new LinkedList<Map.Entry<Integer, PhysicalServer>>(
				unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<Integer, PhysicalServer>>() {
			public int compare(Map.Entry<Integer, PhysicalServer> o1, Map.Entry<Integer, PhysicalServer> o2) {
				return Double.compare(o2.getValue().getCpu(), o1.getValue().getCpu());
			}
		});

		// Convert sorted map back to a Map
		Map<Integer, PhysicalServer> sortedMap = new LinkedHashMap<Integer, PhysicalServer>();
		for (Iterator<Map.Entry<Integer, PhysicalServer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<Integer, PhysicalServer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	public Map<Integer, Topology> sortMapByCap(Map<Integer, Topology> unsortMap) {

		// Convert Map to List
		List<Map.Entry<Integer, Topology>> list = new LinkedList<Map.Entry<Integer, Topology>>(unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<Integer, Topology>>() {
			public int compare(Map.Entry<Integer, Topology> o1, Map.Entry<Integer, Topology> o2) {
				return Double.compare(o2.getValue().getCPURes(), o1.getValue().getCPURes());
			}
		});

		// Convert sorted map back to a Map
		Map<Integer, Topology> sortedMap = new LinkedHashMap<Integer, Topology>();
		for (Iterator<Map.Entry<Integer, Topology>> it = list.iterator(); it.hasNext();) {
			Map.Entry<Integer, Topology> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	public double getPowerOur() {
		return powerOur;
	}

	public double getPowerOurNew() {
		return powerOurNew;
	}

	public double getPowerOurNewNew() {
		return powerOurNewNew;
	}

	public double getPowerOurK4() {
		return powerOurK4;
	}

	public double getPowerOurK6_1() {
		return powerOurK6_1;
	}

	public double getPowerOurK6_2() {
		return powerOurK6_2;
	}

	public double getPowerOurK8() {
		return powerOurK8;
	}

	public double getPowerOurNewSum() {
		return powerOurNewSum;
	}

	public double getRatioSuccess() {
		return (numSuccess * 1.0) / listVDCRequest.size();
	}

	public double getNumSuccess() {
		return numSuccess;
	}

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

	public LinkedList<VDCRequest> sortListVDCByCPU(LinkedList<VDCRequest> list) {
		Collections.sort(list, new Comparator<VDCRequest>() {
			@Override
			public int compare(VDCRequest o1, VDCRequest o2) {
				if (o1.getTotalCPU() < o2.getTotalCPU()) {
					return 1;
				}
				if (o1.getTotalCPU() > o2.getTotalCPU()) {
					return -1;
				}
				return 0;
			}
		});
		return list;
	}

	public LinkedList<VDCRequest> sortListVDCByNoVM(LinkedList<VDCRequest> list, double timeCurrent) {
		Collections.sort(list, new Comparator<VDCRequest>() {
			@Override
			public int compare(VDCRequest o1, VDCRequest o2) {
				if (o1.getEndTime() == timeCurrent && o2.getEndTime() != timeCurrent)
					return 1;
				else if (o1.getEndTime() != timeCurrent && o2.getEndTime() == timeCurrent)
					return -1;
				else {
					if (o1.getNumVM() < o2.getNumVM())
						return 1;
					else if (o1.getNumVM() > o2.getNumVM())
						return -1;
					else
						return 0;
				}
			}
		});
		return list;
	}

	public String checkFatTree(int k, String s, String d) {

		String check = null;
		int sID = Integer.valueOf(s);
		int dID = Integer.valueOf(d);
		if (sID == dID)
			check = "none";
		else {
			for (int i = 1; i < k; i++) {
				// System.out.println("a s " + i);
				if (sID > ((i - 1) * k * k / 4) && sID <= (i * k * k / 4) && dID > ((i - 1) * k * k / 4)
						&& dID <= (i * k * k / 4)) {
					// System.out.println("cung mot POD");
					if (sID > (i - 1) * k / 2 && sID <= (i * k / 2) && dID > ((i - 1) * k / 2) && dID <= (i * k / 2)) {
						check = "near";
						// System.out.println("ss ");
						break;
					} else
						check = "middle";
					break;
				} else
					check = "far";
			}
		}

		return check;

	}

	public Map<VDCRequest, Topology> getResultTopo() {
		return resultTopo;
	}

	public void setResultTopo(Map<VDCRequest, Topology> resultTopo) {
		this.resultTopo = resultTopo;
	}

	public double getNumSuccessNew() {
		return numSuccessNew;
	}

	public boolean isSaved(LinkedList<SubstrateSwitch> oldPath, LinkedList<SubstrateSwitch> newPath, double bandwidth,
			PhysicalServer sServer) {
		boolean isSaved = false;
		double pBeforeMigOld = 0;
		double pAfterMigOld = 0;
		double pBeforeMigNew = 0;
		double pAfterMigNew = 0;
		modelHP HP = new modelHP();
		for (SubstrateSwitch s : oldPath) {
			pBeforeMigOld += HP.getPowerOfSwitch(s);
		}
		for (SubstrateSwitch s : newPath) {
			pBeforeMigNew += HP.getPowerOfSwitch(s);
		}
		// gia su hai chieu la nhu nhau
		//
		for (int i = 0; i < oldPath.size() - 1; i++) {
			if (i == 0) {
				pAfterMigOld += HP.calculateForOldPath(oldPath.get(i), oldPath.get(i), oldPath.get(i + 1), bandwidth);
			} else {
				pAfterMigOld += HP.calculateForOldPath(oldPath.get(i - 1), oldPath.get(i), oldPath.get(i + 1),
						bandwidth);
			}
		}
		for (int i = 0; i < newPath.size() - 1; i++) {
			if (i == 0) {
				pAfterMigNew += HP.calculateForNewPath(newPath.get(i), newPath.get(i), newPath.get(i + 1), bandwidth);
			} else {
				pAfterMigNew += HP.calculateForNewPath(newPath.get(i - 1), newPath.get(i), newPath.get(i + 1),
						bandwidth);

			}
		}
		double cpuUlt = sServer.getCpu() / sServer.getCPU();
		double powerMigartion = 0; // under construction
		double powerServerSaved = A * cpuUlt + B;
		double totalpowerSaved = powerServerSaved + pBeforeMigOld - pAfterMigOld;
		double totalpowerConsumed = pAfterMigNew - pBeforeMigNew + powerMigartion;
		if (totalpowerSaved > totalpowerConsumed)
			isSaved = true;
		return isSaved;
	}

	public int getNumVmAlive(LinkedList<VDCRequest> listVDC) {
		int totalVM = 0;
		for (VDCRequest vdc : listVDC)
			totalVM += vdc.getNumVM();
		return totalVM;
	}

}
