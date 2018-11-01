/*package fil.vdc.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import fil.vdc.algorithm.LinkMapping;
import fil.vdc.algorithm.VMMapping;
import fil.vdc.algorithm.VMMappingSecondNet;
import fil.vdc.model.FatTree;
import fil.vdc.model.GenVDCRequest;
import fil.vdc.model.LinkPhyEdge;
import fil.vdc.model.Log;
import fil.vdc.model.PhysicalServer;
import fil.vdc.model.Result;
import fil.vdc.model.SubstrateLink;
import fil.vdc.model.SubstrateSwitch;
import fil.vdc.model.Topology;
import fil.vdc.model.VDCRequest;
import fil.vdc.model.VirtualLink;
import fil.vdc.model.VirtualMachine;
import fil.vdc.model.modelHP;

public class Mapping2 {
	private final double LAMBDA = 8;
	private final double MU = 0.5;
	private final double A = 1.1128;
	private final double B = 205.106;
	private final double timeWindow = 24;
	private int K = 8;
	private int C;
	private FatTree fatTree;
	private FatTree fatTreeMigartion;
	private FatTree fatTreeGreenHead;
	private FatTree fatTreeSecondNet;
	private FatTree fatTreeJoinRemap;
	private FatTree fatTreeLeaveRemap;
	private FatTree fatTreeMixRemap;
	private Topology topo;
	private Topology topoMigration;
	private Topology topoGreenHead;
	private Topology topoSecondNet;
	private Topology topoJoinRemap;
	private Topology topoLeaveRemap;
	private Topology topoMixRemap;

	private LinkedList<VDCRequest> listVDCRequest;
	private GenVDCRequest genVDC;

	private VMMapping vmMapping;
	private LinkMapping linkMapping;

	private VMMapping vmMappingMigration;
	private LinkMapping linkMappingMigartion;

	private VMMapping vmMappingGreenHead;
	private LinkMapping linkMappingGreenHead;

	private VMMappingSecondNet vmMappingSecondNet;
	private LinkMapping linkMappingSecondNet;

	private VMMapping vmMappingJoinRemap;
	private LinkMapping linkMappingJoinRemap;

	private VMMapping vmMappingLeaveRemap;
	private LinkMapping linkMappingLeaveRemap;

	private VMMapping vmMappingMixRemap;
	private LinkMapping linkMappingMixRemap;

	private Map<Integer, Map<VirtualMachine, PhysicalServer>> mappingResults;

	private Map<Integer, Map<VirtualMachine, PhysicalServer>> mappingResultsMigration;

	private Map<Integer, Map<VirtualMachine, PhysicalServer>> mappingResultsGreenHead;
	private Map<Integer, Map<VirtualMachine, PhysicalServer>> mappingResultsSecondNet;
	private HashMap<Integer, Map<VirtualMachine, PhysicalServer>> mappingResultsJoinRemap;
	private HashMap<Integer, Map<VirtualMachine, PhysicalServer>> mappingResultsLeaveRemap;
	private HashMap<Integer, Map<VirtualMachine, PhysicalServer>> mappingResultsMixRemap;

	private Map<Integer, Map<LinkedList<SubstrateSwitch>, Double>> linkMappingResults;
	private Map<Integer, Map<VirtualLink, LinkedList<SubstrateSwitch>>> listPathMapped;
	private Map<Integer, Map<VirtualLink, LinkedList<LinkPhyEdge>>> listPhyEdgeMapped;
	private Map<Integer, Map<LinkedList<SubstrateSwitch>, Double>> linkMappingResultsMigration;

	private Map<Integer, Map<LinkedList<SubstrateSwitch>, Double>> linkMappingResultsGreenHead;
	private Map<Integer, Map<LinkedList<SubstrateSwitch>, Double>> linkMappingResultsSecondNet;
	private HashMap<Integer, Map<LinkedList<SubstrateSwitch>, Double>> linkMappingResultsJoinRemap;
	private HashMap<Integer, Map<LinkedList<SubstrateSwitch>, Double>> linkMappingResultsLeaveRemap;
	private HashMap<Integer, Map<LinkedList<SubstrateSwitch>, Double>> linkMappingResultsMixRemap;
	private Map<Integer, Map<LinkPhyEdge, Double>> linkPhyEdge;
	private Map<Integer, Map<LinkPhyEdge, Double>> linkPhyEdgeMigration;

	private Map<Integer, Map<LinkPhyEdge, Double>> linkPhyEdgeGreenHead;
	private Map<Integer, Map<LinkPhyEdge, Double>> linkPhyEdgeSecondNet;
	private HashMap<Integer, Map<LinkPhyEdge, Double>> linkPhyEdgeJoinRemap;
	private HashMap<Integer, Map<LinkPhyEdge, Double>> linkPhyEdgeLeaveRemap;
	private HashMap<Integer, Map<LinkPhyEdge, Double>> linkPhyEdgeMixRemap;

	private double numSuccess;
	private double numSuccessMigration;

	private double numSuccessGreenHead;
	private double numSuccessSecondNet;

	private double numSuccessJoinRemap;
	private double numSuccessLeaveRemap;
	private double numSuccessMixRemap;

	private LinkedList<Integer> idVDCIteratored;
	private LinkedList<Integer> idVDCIteratoredMigration;
	private LinkedList<Integer> idVDCIteratoredGreenHead;
	private LinkedList<Integer> idVDCIteratoredSecondNet;
	private LinkedList<Integer> idVDCIteratoredJoinRemap;
	private LinkedList<Integer> idVDCIteratoredLeaveRemap;
	private LinkedList<Integer> idVDCIteratoredMixRemap;

	private double powerOur;
	private double powerMigration;
	private double powerGreenHead;

	private double powerSecondNet;
	private double powerJoinRemap;
	private double powerLeaveRemap;
	private double powerMixRemap;

	private double powerOurNew;
	private double powerMigrationNew;
	private double powerGreenHeadNew;

	private double powerSecondNetNew;
	private double powerJoinRemapNew;
	private double powerLeaveRemapNew;
	private double powerMixRemapNew;

	private LinkedList<VDCRequest> listMapping;
	private LinkedList<VDCRequest> listMappingMig;
	private LinkedList<VDCRequest> listMappingSecondNet;
	private LinkedList<VDCRequest> listMappingGreenHead;
	private LinkedList<VDCRequest> listMappingJoin;
	private LinkedList<VDCRequest> listMappingLeave;
	private LinkedList<VDCRequest> listMappingMix;

	public Mapping2() {
		fatTree = new FatTree();
		fatTreeMigartion = new FatTree();
		fatTreeGreenHead = new FatTree();
		fatTreeSecondNet = new FatTree();
		fatTreeJoinRemap = new FatTree();
		fatTreeLeaveRemap = new FatTree();
		fatTreeMixRemap = new FatTree();

		topo = new Topology();
		topoMigration = new Topology();
		topoGreenHead = new Topology();
		topoSecondNet = new Topology();
		topoJoinRemap = new Topology();
		topoLeaveRemap = new Topology();
		topoMixRemap = new Topology();

		listVDCRequest = new LinkedList<>();
		genVDC = new GenVDCRequest();

		mappingResults = new HashMap<>();
		mappingResultsMigration = new HashMap<>();
		mappingResultsGreenHead = new HashMap<>();
		mappingResultsSecondNet = new HashMap<>();
		mappingResultsJoinRemap = new HashMap<>();
		mappingResultsLeaveRemap = new HashMap<>();
		mappingResultsMixRemap = new HashMap<>();

		numSuccess = 0;
		numSuccessMigration = 0;
		numSuccessGreenHead = 0;
		numSuccessSecondNet = 0;
		numSuccessJoinRemap = 0;
		numSuccessLeaveRemap = 0;
		numSuccessMixRemap = 0;

		idVDCIteratored = new LinkedList<>();
		idVDCIteratoredMigration = new LinkedList<>();
		idVDCIteratoredGreenHead = new LinkedList<>();
		idVDCIteratoredSecondNet = new LinkedList<>();
		idVDCIteratoredJoinRemap = new LinkedList<>();
		idVDCIteratoredLeaveRemap = new LinkedList<>();
		idVDCIteratoredMixRemap = new LinkedList<>();

		linkMappingResults = new HashMap<>();
		linkMappingResultsMigration = new HashMap<>();
		linkMappingResultsGreenHead = new HashMap<>();
		linkMappingResultsSecondNet = new HashMap<>();
		linkMappingResultsJoinRemap = new HashMap<>();
		linkMappingResultsLeaveRemap = new HashMap<>();
		linkMappingResultsMixRemap = new HashMap<>();

		listMapping = new LinkedList<>();
		listMappingMig = new LinkedList<>();
		listMappingGreenHead = new LinkedList<>();
		listMappingSecondNet = new LinkedList<>();
		listMappingJoin = new LinkedList<>();
		listMappingLeave = new LinkedList<>();
		listMappingMix = new LinkedList<>();

		listPathMapped = new HashMap<>();
		listPhyEdgeMapped = new HashMap<>();

		linkPhyEdge = new HashMap<>();
		linkPhyEdgeMigration = new HashMap<>();
		linkPhyEdgeGreenHead = new HashMap<>();
		linkPhyEdgeSecondNet = new HashMap<>();
		linkPhyEdgeJoinRemap = new HashMap<>();
		linkPhyEdgeLeaveRemap = new HashMap<>();
		linkPhyEdgeMixRemap = new HashMap<>();

		powerOur = 0;
		powerMigration = 0;
		powerGreenHead = 0;
		powerSecondNet = 0;
		powerJoinRemap = 0;
		powerLeaveRemap = 0;
		powerMixRemap = 0;

		powerOurNew = 0;
		powerMigrationNew = 0;
		powerGreenHeadNew = 0;
		powerSecondNetNew = 0;
		powerJoinRemapNew = 0;
		powerLeaveRemapNew = 0;
		powerMixRemapNew = 0;
	}

	public void run(int percent, int numberVMMax) {
		topo = fatTree.genFatTree(K);
		topoMigration = fatTreeMigartion.genFatTree(K);
		topoGreenHead = fatTreeGreenHead.genFatTree(K);
		topoSecondNet = fatTreeSecondNet.genFatTree(K);
		topoJoinRemap = fatTreeJoinRemap.genFatTree(K);
		topoLeaveRemap = fatTreeLeaveRemap.genFatTree(K);
		topoMixRemap = fatTreeMixRemap.genFatTree(K);

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
		for (int i = 0; i < listVDCRequest.size(); i++) {
			tmpListTime.add(listVDCRequest.get(i).getStartTime());
			tmpListTime.add(listVDCRequest.get(i).getEndTime());
		}

		LinkedList<Double> listTime = new LinkedList<>(tmpListTime);
		// sort list Time
		listTime = sortTime(listTime);

		Map<Double, LinkedList<VDCRequest>> tmplistVDCSort = new HashMap<>();
		LinkedList<VDCRequest> listVDC;
		for (int i = 0; i < listTime.size(); i++) {
			for (VDCRequest vdc : listVDCRequest) {
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

		double powerEdgeOur = 0;
		double powerEdgeGreenHead = 0;
		double powerEdgeSecondNet = 0;
		double powerEdgeJoinRemap = 0;
		double powerEdgeLeaveRemap = 0;
		double powerEdgeMixRemap = 0;
		double powerEdgeMigration = 0;

		double previousTimeOur = 0;
		double previousTimeGreenHead = 0;
		double previousTimeSecondNet = 0;
		double previousTimeJoinRemap = 0;
		double previousTimeLeaveRemap = 0;
		double previousTimeMixRemap = 0;
		double previousTimeMigration = 0;

		int numberVMOur, numberVMGH, numberVMSN, numberVMMig, numberVMJoin, numberVMLeave, numberVMMix;
		double timeArrive = 0, load = 0, numberVMArrive = 0, timeAcceptedOur = 0, utilOur = 0, numberVMAcceptedOur = 0,
				timeAcceptedGH = 0, utilGH = 0, numberVMAcceptedGH = 0, timeAcceptedSN = 0, utilSN = 0,
				numberVMAcceptedSN = 0, timeAcceptedMig = 0, utilMig = 0, numberVMAcceptedMig = 0, timeAcceptedJoin = 0,
				utilJoin = 0, numberVMAcceptedJoin = 0, timeAcceptedLeave = 0, utilLeave = 0, numberVMAcceptedLeave = 0,
				timeAcceptedMix = 0, utilMix = 0, numberVMAcceptedMix = 0;
		timeNext = 2;
		C = K * K * K;
		for (Entry<Double, LinkedList<VDCRequest>> entry : listVDCSort.entrySet()) {
			listVDC = entry.getValue();
			timeCurrent = entry.getKey();
			if (timeCurrent > timeWindow) {
				load = 100 * (timeArrive / timeWindow) / C;
				utilOur = 100 * (timeAcceptedOur / timeWindow) / C;
				utilGH = 100 * (timeAcceptedGH / timeWindow) / C;
				utilSN = 100 * (timeAcceptedSN / timeWindow) / C;
				utilMig = 100 * (timeAcceptedMig / timeWindow) / C;
				break;
			}

			// Calculate Power
			powerOur += powerEdgeOur * (timeCurrent - previousTimeOur);
			powerGreenHead += powerEdgeGreenHead * (timeCurrent - previousTimeGreenHead);
			powerSecondNet += powerEdgeSecondNet * (timeCurrent - previousTimeSecondNet);
			powerMigration += powerEdgeMigration * (timeCurrent - previousTimeMigration);

			// So VM dang duoc map toi thoi diem hien tai
			numberVMOur = getNumVmAlive(listMapping);
			numberVMGH = getNumVmAlive(listMappingGreenHead);
			numberVMSN = getNumVmAlive(listMappingSecondNet);
			numberVMMig = getNumVmAlive(listMappingMig);

			if (numberVMOur != 0)
				powerOurNew += (powerEdgeOur * (timeCurrent - previousTimeOur)) / numberVMOur;
			if (numberVMGH != 0)
				powerGreenHeadNew += ((powerEdgeGreenHead * (timeCurrent - previousTimeGreenHead))) / numberVMGH;
			if (numberVMSN != 0)
				powerSecondNetNew += ((powerEdgeSecondNet * (timeCurrent - previousTimeSecondNet))) / numberVMSN;
			if (numberVMMig != 0)
				powerMigrationNew += ((powerEdgeMigration * (timeCurrent - previousTimeMigration))) / numberVMMig;

			// sort VDC by priority (current, leave-> join and priority is
			// number of VM in vdc)
			sortListVDCByNoVM(listVDC, timeCurrent);

			boolean hasLeaveInMigrationTopo = false;
			for (VDCRequest vdc : listVDC) {
				// VDC leave
				if (vdc.getEndTime() == timeCurrent) {
					if (listMapping.contains(vdc)) {
						checkExpriedVDC(vdc);
						listMapping.remove(vdc);
					}
					if (listMappingMig.contains(vdc)) {
						checkExpriedVDCMigration(vdc);
						listMappingMig.remove(vdc);
						hasLeaveInMigrationTopo = true;
					}
					if (listMappingGreenHead.contains(vdc)) {
						checkExpriedVDCGreenHead(vdc);
						listMappingGreenHead.remove(vdc);
					}
					if (listMappingSecondNet.contains(vdc)) {
						checkExpriedVDCSecondNet(vdc);
						listMappingSecondNet.remove(vdc);
					}
				}
			}
			// Chi chay 1 lan migration
			if (hasLeaveInMigrationTopo)
				newMigration();

			for (VDCRequest vdc : listVDC) {
				// If vdc join
				if (vdc.getStartTime() == timeCurrent) {
					if (vdc.getEndTime() > timeWindow)
						timeArrive += ((timeWindow - vdc.getStartTime()) * vdc.getNumVM());
					else
						timeArrive += (vdc.getLifeTime() * vdc.getNumVM());
					numberVMArrive += vdc.getNumVM();

					if (runOur(vdc)) {
						if (vdc.getEndTime() > timeWindow)
							timeAcceptedOur += ((timeWindow - vdc.getStartTime()) * vdc.getNumVM());
						else
							timeAcceptedOur += (vdc.getLifeTime() * vdc.getNumVM());
						numberVMAcceptedOur += vdc.getNumVM();

					}
					if (runOurMigration(vdc)) {
						if (vdc.getEndTime() > timeWindow)
							timeAcceptedMig += ((timeWindow - vdc.getStartTime()) * vdc.getNumVM());
						else
							timeAcceptedMig += (vdc.getLifeTime() * vdc.getNumVM());
						numberVMAcceptedMig += vdc.getNumVM();
					}
					if (runGreenHead(vdc)) {
						if (vdc.getEndTime() > timeWindow)
							timeAcceptedGH += ((timeWindow - vdc.getStartTime()) * vdc.getNumVM());
						else
							timeAcceptedGH += (vdc.getLifeTime() * vdc.getNumVM());
						numberVMAcceptedGH += vdc.getNumVM();
					}
					if (runSecondNet(vdc)) {
						if (vdc.getEndTime() > timeWindow)
							timeAcceptedSN += ((timeWindow - vdc.getStartTime()) * vdc.getNumVM());
						else
							timeAcceptedSN += (vdc.getLifeTime() * vdc.getNumVM());
						numberVMAcceptedSN += vdc.getNumVM();
					}
				}
			}

			// tinh lai nang luong
			powerEdgeOur = getPowerNetworkDevice(topo) + getPowerServer(topo);
			powerEdgeGreenHead = getPowerNetworkDevice(topoGreenHead) + getPowerServer(topoGreenHead);
			powerEdgeSecondNet = getPowerNetworkDevice(topoSecondNet) + getPowerServer(topoSecondNet);
			powerEdgeMigration = getPowerNetworkDevice(topoMigration) + getPowerServer(topoMigration);

			previousTimeOur = timeCurrent;
			previousTimeGreenHead = timeCurrent;
			previousTimeSecondNet = timeCurrent;
			previousTimeMigration = timeCurrent;
		}

		// for Remap scenario
		timeNext = 2;
		timeArrive = 0;
		numberVMArrive = 0;
		for (Entry<Double, LinkedList<VDCRequest>> entry : listVDCSort.entrySet()) {
			listVDC = entry.getValue();
			timeCurrent = entry.getKey();
			if (timeCurrent > timeWindow) {
				load = 100 * (timeArrive / timeWindow) / C;
				utilJoin = 100 * (timeAcceptedJoin / timeWindow) / C;
				utilLeave = 100 * (timeAcceptedLeave / timeWindow) / C;
				utilMix = 100 * (timeAcceptedMix / timeWindow) / C;
				break;
			}

			// Calculate Power
			powerJoinRemap += powerEdgeJoinRemap * (timeCurrent - previousTimeJoinRemap);
			powerLeaveRemap += powerEdgeLeaveRemap * (timeCurrent - previousTimeLeaveRemap);
			powerMixRemap += powerEdgeMixRemap * (timeCurrent - previousTimeMixRemap);

			// So VM dang duoc map toi thoi diem hien tai
			numberVMJoin = getNumVmAlive(listMappingJoin);
			numberVMLeave = getNumVmAlive(listMappingLeave);
			numberVMMix = getNumVmAlive(listMappingMix);

			if (numberVMJoin != 0)
				powerJoinRemapNew += (powerEdgeJoinRemap * (timeCurrent - previousTimeJoinRemap)) / numberVMJoin;
			if (numberVMLeave != 0)
				powerLeaveRemapNew += ((powerEdgeLeaveRemap * (timeCurrent - previousTimeLeaveRemap))) / numberVMLeave;
			if (numberVMMix != 0)
				powerMixRemapNew += ((powerEdgeMixRemap * (timeCurrent - previousTimeMixRemap))) / numberVMMix;

			// sort VDC by priority (current, leave-> join and priority is
			// number of VM in vdc)
			sortListVDCByNoVM(listVDC, timeCurrent);

			// Remove nhung VDC leave truoc
			boolean hasLeaveInLeave = false;
			boolean hasLeaveInMix = false;
			for (VDCRequest vdc : listVDC) {
				if (vdc.getEndTime() == timeCurrent) {
					// check expired Join-remap
					if (listMappingJoin.contains(vdc)) {
						checkExpriedVDCJoinRemap(vdc);
						listMappingJoin.remove(vdc);
					}
					// check expire Leave -remap and remap
					if (listMappingLeave.contains(vdc)) {
						checkExpriedVDCLeaveRemap(vdc);
						listMappingLeave.remove(vdc);
						hasLeaveInLeave = true;
					}
					// check expire Mix-remap and remap
					if (listMappingMix.contains(vdc)) {
						checkExpriedVDCMixRemap(vdc);
						listMappingMix.remove(vdc);
						hasLeaveInMix = true;
					}
				}
			}
			// Remap khi có VDC leave trong topo RemapLeave
			if (hasLeaveInLeave) {
				Topology topologyLeaveRemapBackup = new Topology();
				FatTree fatTreeLeaveRemapBackup = new FatTree();
				topologyLeaveRemapBackup = fatTreeLeaveRemapBackup.genFatTree(K);
				HashMap<Integer, Map<VirtualMachine, PhysicalServer>> mappingResultsLeaveRemapBackup = new HashMap<>();
				HashMap<Integer, Map<LinkedList<SubstrateSwitch>, Double>> linkMappingResultsLeaveRemapBackup = new HashMap<>();
				HashMap<Integer, Map<LinkPhyEdge, Double>> linkPhyEdgeLeaveRemapBackup = new HashMap<>();
				// Try to remap all alive VDC + new-come VDC
				if (runRemap(listMappingLeave, topologyLeaveRemapBackup, fatTreeLeaveRemapBackup,
						mappingResultsLeaveRemapBackup, linkMappingResultsLeaveRemapBackup,
						linkPhyEdgeLeaveRemapBackup)) {
					// remap thanh cong
					topoLeaveRemap = topologyLeaveRemapBackup;
					fatTreeLeaveRemap = fatTreeLeaveRemapBackup;
					mappingResultsLeaveRemap = mappingResultsLeaveRemapBackup;
					linkMappingResultsLeaveRemap = linkMappingResultsLeaveRemapBackup;
					linkPhyEdgeLeaveRemap = linkPhyEdgeLeaveRemapBackup;
				}
			}

			if (hasLeaveInMix) {
				Topology topologyMixRemapBackup = new Topology();
				FatTree fatTreeMixRemapBackup = new FatTree();
				topologyMixRemapBackup = fatTreeMixRemapBackup.genFatTree(K);
				HashMap<Integer, Map<VirtualMachine, PhysicalServer>> mappingResultsMixRemapBackup = new HashMap<>();
				HashMap<Integer, Map<LinkedList<SubstrateSwitch>, Double>> linkMappingResultsMixRemapBackup = new HashMap<>();
				HashMap<Integer, Map<LinkPhyEdge, Double>> linkPhyEdgeMixRemapBackup = new HashMap<>();

				if (runRemap(listMappingMix, topologyMixRemapBackup, fatTreeMixRemapBackup,
						mappingResultsMixRemapBackup, linkMappingResultsMixRemapBackup, linkPhyEdgeMixRemapBackup)) {
					// remap thanh cong
					topoMixRemap = topologyMixRemapBackup;
					fatTreeMixRemap = fatTreeMixRemapBackup;
					mappingResultsMixRemap = mappingResultsMixRemapBackup;
					linkMappingResultsMixRemap = linkMappingResultsMixRemapBackup;
					linkPhyEdgeMixRemap = linkPhyEdgeMixRemapBackup;
				}
			}

			// Neu co VDC join thuc hien remap doi voi topo RemapJoin va
			// RemapMix
			for (VDCRequest vdc : listVDC) {
				if (vdc.getStartTime() == timeCurrent) {
					if (vdc.getEndTime() > timeWindow)
						timeArrive += ((timeWindow - vdc.getStartTime()) * vdc.getNumVM());
					else
						timeArrive += (vdc.getLifeTime() * vdc.getNumVM());
					numberVMArrive += vdc.getNumVM();

					Topology topologyJoinRemapBackup = new Topology();
					FatTree fatTreeJoinRemapBackup = new FatTree();
					topologyJoinRemapBackup = fatTreeJoinRemapBackup.genFatTree(K);
					HashMap<Integer, Map<VirtualMachine, PhysicalServer>> mappingResultsJoinRemapBackup = new HashMap<>();
					HashMap<Integer, Map<LinkedList<SubstrateSwitch>, Double>> linkMappingResultsJoinRemapBackup = new HashMap<>();
					HashMap<Integer, Map<LinkPhyEdge, Double>> linkPhyEdgeJoinRemapBackup = new HashMap<>();

					// join
					listMappingJoin.add(vdc);
					if (runRemap(listMappingJoin, topologyJoinRemapBackup, fatTreeJoinRemapBackup,
							mappingResultsJoinRemapBackup, linkMappingResultsJoinRemapBackup,
							linkPhyEdgeJoinRemapBackup)) {
						// remap thanh cong
						if (vdc.getEndTime() > timeWindow)
							timeAcceptedJoin += ((timeWindow - vdc.getStartTime()) * vdc.getNumVM());
						else
							timeAcceptedJoin += (vdc.getLifeTime() * vdc.getNumVM());
						numberVMAcceptedJoin += vdc.getNumVM();
						numSuccessJoinRemap++;
						topoJoinRemap = topologyJoinRemapBackup;
						fatTreeJoinRemap = fatTreeJoinRemapBackup;
						mappingResultsJoinRemap = mappingResultsJoinRemapBackup;
						linkMappingResultsJoinRemap = linkMappingResultsJoinRemapBackup;
						linkPhyEdgeJoinRemap = linkPhyEdgeJoinRemapBackup;
					} else
						listMappingJoin.remove(vdc);

					Topology topologyMixRemapBackup = new Topology();
					FatTree fatTreeMixRemapBackup = new FatTree();
					topologyMixRemapBackup = fatTreeMixRemapBackup.genFatTree(K);
					HashMap<Integer, Map<VirtualMachine, PhysicalServer>> mappingResultsMixRemapBackup = new HashMap<>();
					HashMap<Integer, Map<LinkedList<SubstrateSwitch>, Double>> linkMappingResultsMixRemapBackup = new HashMap<>();
					HashMap<Integer, Map<LinkPhyEdge, Double>> linkPhyEdgeMixRemapBackup = new HashMap<>();

					listMappingMix.add(vdc);
					if (runRemap(listMappingMix, topologyMixRemapBackup, fatTreeMixRemapBackup,
							mappingResultsMixRemapBackup, linkMappingResultsMixRemapBackup,
							linkPhyEdgeMixRemapBackup)) {
						// remap thanh cong
						if (vdc.getEndTime() > timeWindow)
							timeAcceptedMix += ((timeWindow - vdc.getStartTime()) * vdc.getNumVM());
						else
							timeAcceptedMix += (vdc.getLifeTime() * vdc.getNumVM());
						numberVMAcceptedMix += vdc.getNumVM();
						numSuccessMixRemap++;
						topoMixRemap = topologyMixRemapBackup;
						fatTreeMixRemap = fatTreeMixRemapBackup;
						mappingResultsMixRemap = mappingResultsMixRemapBackup;
						linkMappingResultsMixRemap = linkMappingResultsMixRemapBackup;
						linkPhyEdgeMixRemap = linkPhyEdgeMixRemapBackup;
					} else
						listMappingMix.remove(vdc);

					// Mapping for VDC join into topo leave-remap
					if (runLeaveMap(vdc)) {
						if (vdc.getEndTime() > timeWindow)
							timeAcceptedLeave += ((timeWindow - vdc.getStartTime()) * vdc.getNumVM());
						else
							timeAcceptedLeave += (vdc.getLifeTime() * vdc.getNumVM());
						numberVMAcceptedLeave += vdc.getNumVM();
					}
				}
			}
			powerEdgeJoinRemap = getPowerNetworkDevice(topoJoinRemap) + getPowerServer(topoJoinRemap);
			powerEdgeLeaveRemap = getPowerNetworkDevice(topoLeaveRemap) + getPowerServer(topoLeaveRemap);
			powerEdgeMixRemap = getPowerNetworkDevice(topoMixRemap) + getPowerServer(topoMixRemap);
			previousTimeJoinRemap = timeCurrent;
			previousTimeLeaveRemap = timeCurrent;
			previousTimeMixRemap = timeCurrent;

		}

		MainController.logLoad.info("" + load + "\t" + utilGH + "\t" + utilSN + "\t" + utilOur + "\t" + utilMig + "\t"
				+ utilJoin + "\t" + utilLeave + "\t" + utilMix);
		MainController.resultAcceptionRate.info("" + load + "\t" + utilGH + "\t" + numberVMAcceptedGH / numberVMArrive
				+ "\t" + utilSN + "\t" + numberVMAcceptedSN / numberVMArrive + "\t" + utilOur + "\t"
				+ numberVMAcceptedOur / numberVMArrive + "\t" + +utilMig + "\t" + numberVMAcceptedMig / numberVMArrive
				+ "\t" + utilJoin + "\t" + numberVMAcceptedJoin / numberVMArrive + "\t" + utilLeave + "\t"
				+ numberVMAcceptedLeave / numberVMArrive + "\t" + utilMix + "\t"
				+ numberVMAcceptedMix / numberVMArrive);

		MainController.resultPowerPerAccept
				.info("" + load + "\t" + utilGH + "\t" + getPowerGreenHead() * numberVMArrive / numberVMAcceptedGH
						+ "\t" + utilSN + "\t" + getPowerSecondNet() * numberVMArrive / numberVMAcceptedSN + "\t"
						+ utilOur + "\t" + getPowerOur() * numberVMArrive / numberVMAcceptedOur + "\t" + utilMig + "\t"
						+ getPowerMigration() * numberVMArrive / numberVMAcceptedMig + "\t" + utilJoin + "\t"
						+ getPowerJoinRemap() * numberVMArrive / numberVMAcceptedJoin + "\t" + utilLeave + "\t"
						+ getPowerLeaveRemap() * numberVMArrive / numberVMAcceptedLeave + "\t" + utilMix + "\t"
						+ getPowerMixRemap() * numberVMArrive / numberVMAcceptedMix);

		MainController.resultAcceptionRateVDC.info("" + load + "\t" +utilGH+"\t" + +getRatioSuccessGreenHead() + "\t"+ utilSN+ "\t"
				+ getRatioSuccessSecondNet() + "\t" + utilOur+ "\t"+ getRatioSuccess() + "\t" + getRatioSuccessMigration() + "\t"+ utilMig+ "\t"
				+ utilJoin+ "\t"	+ getRatioSuccessJoinRemap() + "\t"+ utilLeave+ "\t" + getRatioSuccessLeaveRemap() + "\t" + utilMix+ "\t"+ getRatioSuccessMixRemap());

		MainController.resultPowerPerAcceptVDC.info("" + load + "\t" + utilGH+ "\t"+ getPowerGreenHead() / getRatioSuccessGreenHead()
				+ "\t" + utilSN+ "\t"+ getPowerSecondNet() / getRatioSuccessSecondNet() + "\t"+ utilOur+ "\t" + getPowerOur() / getRatioSuccess()
				+ "\t"+ utilMig+ "\t" + getPowerMigration() / getRatioSuccessMigration() + "\t"+ utilJoin+ "\t"
				+ getPowerJoinRemap() / getRatioSuccessJoinRemap() + "\t"+ utilJoin+ "\t"
				+ getPowerLeaveRemap() / getRatioSuccessLeaveRemap() + "\t"+ utilLeave+ "\t"+ utilMix+ "\t"
				+ getPowerMixRemap() / getRatioSuccessMixRemap());

		MainController.resultPowerPerVDC.info("" + load + "\t" + utilGH+ "\t"+ getPowerGreenHeadNew() + "\t" + utilSN+ "\t"+ getPowerSecondNetNew()
				+ "\t" + utilOur+ "\t"+ getPowerOurNew() + "\t" + utilMig+ "\t"+ getPowerMigrationNew() + "\t"+ utilJoin+ "\t" + getPowerJoinRemapNew() + "\t"+ utilLeave+ "\t"
				+ getPowerLeaveRemapNew() + "\t" + utilMix+ "\t"+ getPowerMixRemapNew());
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
		// for(SubstrateSwitch sw : topo.getListSwitch()){
		// Map<SubstrateSwitch, Double> map = sw.getBandwidthPort();
		// StringBuilder str= new StringBuilder();
		// str.append(sw.getNameSubstrateSwitch()).append(": ");
		// for(SubstrateSwitch s: map.keySet()) {
		// str.append(s.getNameSubstrateSwitch()).append("\t").append(map.get(s)).append(";\t");
		// }
		// logFile.info(str.toString());
		// }
		// logFile.info("Substrate Link Between Switch");
		// for(SubstrateLink link: topo.getLinkBandwidth()) {
		// StringBuilder str= new StringBuilder();
		// str.append(link.getStartSwitch().getNameSubstrateSwitch()).append("-");
		// str.append(link.getEndSwitch().getNameSubstrateSwitch()).append("
		// BWfree = ");
		// str.append(link.getBandwidth());
		// logFile.info(str.toString());
		// }
		// logFile.info("Link PhysicalServer-SwitchEdge");
		// for(LinkPhyEdge link: topo.getListLinkPhyEdge()) {
		// StringBuilder str= new StringBuilder();
		// str.append(link.getPhysicalServer().getName()).append("-");
		// str.append(link.getEdgeSwitch().getNameSubstrateSwitch()).append("
		// BWfree = ");
		// str.append(link.getBandwidth());
		// logFile.info(str.toString());
		// }
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

	public boolean runOur(VDCRequest vdc) {
		MainController.logHEAE.info("VDC JOIN ID= " + vdc.getVdcID() + "; NoVM = " + vdc.getNumVM());
		MainController.logHEAE.info("TOPOLOGY CURRENT");
		printLogTopo(topo, MainController.logHEAE);
		boolean mapSuccess = false;
		vmMapping = new VMMapping();
		linkMapping = new LinkMapping();
		topo = vmMapping.run(vdc, topo, fatTree);
		if (vmMapping.isSuccess()) {
			topo = linkMapping.linkMappingOurAlgorithm(topo, vdc, vmMapping.getMappingResults());
			if (linkMapping.isSuccess()) {
				mapSuccess = true;
				numSuccess++;
				mappingResults.put(vdc.getVdcID(), vmMapping.getMappingResults());
				linkMappingResults.put(vdc.getVdcID(), linkMapping.getResultsLinkMapping()); // linkMapping
																								// Results
				linkPhyEdge.put(vdc.getVdcID(), linkMapping.getListBandwidthPhyEdge()); // link
																						// Phy-Edge
				listMapping.add(vdc);
				// printLogResultMapping(topo, MainController.logHEAE, vdc,
				// mappingResults.get(vdc.getVdcID()),
				// linkMapping.getListPathMapped());
				MainController.logHEAE.info("TOPOLOGY AFTER");
				printLogTopo(topo, MainController.logHEAE);
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
						topo = linkMappingNext.linkMappingOurAlgorithm(topo, vdc, vmMappingNext.getMappingResults());
						if (linkMappingNext.isSuccess()) {
							mapSuccess = true;
							numSuccess++;
							mappingResults.put(vdc.getVdcID(), vmMappingNext.getMappingResults());
							linkMappingResults.put(vdc.getVdcID(), linkMappingNext.getResultsLinkMapping()); // linkMapping
																												// Results
							linkPhyEdge.put(vdc.getVdcID(), linkMappingNext.getListBandwidthPhyEdge()); // link
																										// Phy-Edge
							listMapping.add(vdc);
							MainController.logHEAE.info("TOPOLOGY AFTER");
							printLogTopo(topo, MainController.logHEAE);
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
		return mapSuccess;
	}

	public boolean runLeaveMap(VDCRequest vdc) {
		boolean mapSuccess = false;
		vmMappingLeaveRemap = new VMMapping();
		linkMappingLeaveRemap = new LinkMapping();
		topoLeaveRemap = vmMappingLeaveRemap.run(vdc, topoLeaveRemap, fatTreeLeaveRemap);
		if (vmMappingLeaveRemap.isSuccess()) {
			topoLeaveRemap = linkMappingLeaveRemap.linkMappingOurAlgorithm(topoLeaveRemap, vdc,
					vmMappingLeaveRemap.getMappingResults());
			if (linkMappingLeaveRemap.isSuccess()) {
				mapSuccess = true;
				numSuccessLeaveRemap++;
				listMappingLeave.add(vdc);
				mappingResultsLeaveRemap.put(vdc.getVdcID(), vmMappingLeaveRemap.getMappingResults());
				linkMappingResultsLeaveRemap.put(vdc.getVdcID(), linkMappingLeaveRemap.getResultsLinkMapping()); // linkMapping
																													// Results
				linkPhyEdgeLeaveRemap.put(vdc.getVdcID(), linkMappingLeaveRemap.getListBandwidthPhyEdge()); // link
																											// Phy-Edge
			} else {
				topoLeaveRemap = removeVMMapping(vmMappingLeaveRemap.getMappingResults(), topoLeaveRemap);
				Map<Integer, LinkedList<PhysicalServer>> listPhyPre = vmMappingLeaveRemap.getListExceptPhy();
				listPhyPre.put(listPhyPre.size() + 1, vmMappingLeaveRemap.getListPhyMaped());
				boolean check = false;
				do {
					VMMapping vmMappingNext = new VMMapping();
					vmMappingNext.setListExceptPhy(listPhyPre);
					topoLeaveRemap = vmMappingNext.run(vdc, topoLeaveRemap, fatTreeLeaveRemap);
					LinkMapping linkMappingNext = new LinkMapping();
					if (vmMappingNext.isSuccess()) {
						topoLeaveRemap = linkMappingNext.linkMappingOurAlgorithm(topoLeaveRemap, vdc,
								vmMappingNext.getMappingResults());
						if (linkMappingNext.isSuccess()) {
							mapSuccess = true;
							numSuccessLeaveRemap++;
							listMappingLeave.add(vdc);
							mappingResultsLeaveRemap.put(vdc.getVdcID(), vmMappingNext.getMappingResults());
							linkMappingResultsLeaveRemap.put(vdc.getVdcID(), linkMappingNext.getResultsLinkMapping()); // linkMapping
																														// Results
							linkPhyEdgeLeaveRemap.put(vdc.getVdcID(), linkMappingNext.getListBandwidthPhyEdge()); // link
																													// Phy-Edge
							break;
						} else
							check = true;
					}
					if (vmMappingNext.getMappingResults().isEmpty()) {
						break;
					}
					topoLeaveRemap = removeVMMapping(vmMappingNext.getMappingResults(), topoLeaveRemap);
					listPhyPre = vmMappingNext.getListExceptPhy();
					listPhyPre.put(listPhyPre.size() + 1, vmMappingNext.getListPhyMaped());
				} while (check);
			}
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
				topo = linkMapping.linkMappingOurAlgorithm(topo, vdc, vmMapping.getMappingResults());
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
							topo = linkMappingNext.linkMappingOurAlgorithm(topo, vdc,
									vmMappingNext.getMappingResults());
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

	public boolean runGreenHead(VDCRequest vdc) {
		// MainController.logGreenHead.info("VDC JOIN ID= "+ vdc.getVdcID() + ";
		// NoVM = " + vdc.getNumVM());
		// MainController.logHEAE.info("TOPOLOGY CURRENT");
		// printLogTopo(topoGreenHead, MainController.logGreenHead);
		boolean mapSuccess = false;
		vmMappingGreenHead = new VMMapping();
		linkMappingGreenHead = new LinkMapping();
		topoGreenHead = vmMappingGreenHead.runGreenHead(vdc, topoGreenHead, fatTreeGreenHead);
		if (vmMappingGreenHead.isSuccess()) {
			topoGreenHead = linkMappingGreenHead.linkMappingGreenHead(topoGreenHead, vdc,
					vmMappingGreenHead.getMappingResults());
			if (linkMappingGreenHead.isSuccess()) {
				mapSuccess = true;
				numSuccessGreenHead++;
				mappingResultsGreenHead.put(vdc.getVdcID(), vmMappingGreenHead.getMappingResults());
				linkMappingResultsGreenHead.put(vdc.getVdcID(), linkMappingGreenHead.getResultsLinkMapping()); // linkMapping
																												// Results
				linkPhyEdgeGreenHead.put(vdc.getVdcID(), linkMappingGreenHead.getListBandwidthPhyEdge()); // link
																											// Phy-Edge
				listMappingGreenHead.add(vdc);
				// printLogResultMapping(topoGreenHead,
				// MainController.logGreenHead, vdc,
				// mappingResultsGreenHead.get(vdc.getVdcID()),
				// linkMappingGreenHead.getListPathMapped());
				// MainController.logGreenHead.info("TOPOLOGY AFTER");
				// printLogTopo(topoGreenHead, MainController.logGreenHead);
			} else {
				// first Option: Just Remove VM Mapping
				topoGreenHead = removeVMMapping(vmMappingGreenHead.getMappingResults(), topoGreenHead);
			}
		}
		return mapSuccess;
	}

	public boolean runSecondNet(VDCRequest vdc) {
		MainController.logSecondNet.info("VDC JOIN ID= " + vdc.getVdcID() + "; NoVM = " + vdc.getNumVM());
		MainController.logSecondNet.info("TOPOLOGY CURRENT");
		printLogTopo(topoSecondNet, MainController.logSecondNet);
		boolean mapSuccess = false;
		vmMappingSecondNet = new VMMappingSecondNet();
		linkMappingSecondNet = new LinkMapping();
		topoSecondNet = vmMappingSecondNet.runSecondNet(vdc, topoSecondNet, fatTreeSecondNet);
		if (vmMappingSecondNet.isSuccess()) {
			topoSecondNet = linkMappingSecondNet.linkMappingSecondNet(topoSecondNet, vdc,
					vmMappingSecondNet.getMappingResults());
			if (linkMappingSecondNet.isSuccess()) {
				mapSuccess = true;
				numSuccessSecondNet++;
				mappingResultsSecondNet.put(vdc.getVdcID(), vmMappingSecondNet.getMappingResults());
				linkMappingResultsSecondNet.put(vdc.getVdcID(), linkMappingSecondNet.getResultsLinkMapping()); // linkMapping
				// Results
				linkPhyEdgeSecondNet.put(vdc.getVdcID(), linkMappingSecondNet.getListBandwidthPhyEdge()); // link
																											// Phy-Edge
				listMappingSecondNet.add(vdc);
				// printLogResultMapping(topoSecondNet,
				// MainController.logSecondNet, vdc,
				// mappingResultsSecondNet.get(vdc.getVdcID()),
				// linkMappingSecondNet.getListPathMapped());
				MainController.logSecondNet.info("TOPOLOGY AFTER");
				printLogTopo(topoSecondNet, MainController.logSecondNet);
			} else {
				topoSecondNet = removeVMMapping(vmMappingSecondNet.getMappingResults(), topoSecondNet);
				Map<Integer, LinkedList<PhysicalServer>> listPhyPre = vmMappingSecondNet.getListExceptPhy();
				listPhyPre.put(listPhyPre.size() + 1, vmMappingSecondNet.getListPhyMaped());
				boolean check = false;
				do {
					VMMappingSecondNet vmMappingNext = new VMMappingSecondNet();
					vmMappingNext.setListExceptPhy(listPhyPre);
					topoSecondNet = vmMappingNext.runSecondNet(vdc, topoSecondNet, fatTreeSecondNet);
					LinkMapping linkMappingNext = new LinkMapping();
					if (vmMappingNext.isSuccess()) {
						topoSecondNet = linkMappingNext.linkMappingSecondNet(topoSecondNet, vdc,
								vmMappingNext.getMappingResults());
						if (linkMappingNext.isSuccess()) {
							mapSuccess = true;
							numSuccessSecondNet++;
							listMappingSecondNet.add(vdc);
							mappingResultsSecondNet.put(vdc.getVdcID(), vmMappingNext.getMappingResults());
							linkMappingResultsSecondNet.put(vdc.getVdcID(), linkMappingNext.getResultsLinkMapping()); // linkMapping
																														// //
																														// Results
							linkPhyEdgeSecondNet.put(vdc.getVdcID(), linkMappingNext.getListBandwidthPhyEdge()); // link
																													// Phy-Edge
							// printLogResultMapping(topoSecondNet,
							// MainController.logSecondNet, vdc,
							// mappingResultsSecondNet.get(vdc.getVdcID()),
							// linkMappingSecondNet.getListPathMapped());
							MainController.logSecondNet.info("TOPOLOGY AFTER");
							printLogTopo(topoSecondNet, MainController.logSecondNet);
							break;
						} else {
							check = true;
						}
					}

					if (vmMappingNext.getMappingResults().isEmpty()) {
						break;
					}
					topoSecondNet = removeVMMapping(vmMappingNext.getMappingResults(), topoSecondNet);
					listPhyPre = vmMappingNext.getListExceptPhy();
					listPhyPre.put(listPhyPre.size() + 1, vmMappingNext.getListPhyMaped());
				} while (check);
			}
		}
		return mapSuccess;
	}

	public boolean runOurMigration(VDCRequest vdc) {
		MainController.logMigration.info("VDC JOIN ID= " + vdc.getVdcID() + "; NoVM = " + vdc.getNumVM());
		MainController.logHEAE.info("TOPOLOGY CURRENT");
		// printLogTopo(topoMigration, MainController.logMigration);
		boolean mapSuccess = false;
		vmMappingMigration = new VMMapping();
		linkMappingMigartion = new LinkMapping();
		topoMigration = vmMappingMigration.run(vdc, topoMigration, fatTreeMigartion);
		if (vmMappingMigration.isSuccess()) {
			topoMigration = linkMappingMigartion.linkMappingOurAlgorithm(topoMigration, vdc,
					vmMappingMigration.getMappingResults());
			if (linkMappingMigartion.isSuccess()) {
				mapSuccess = true;
				numSuccessMigration++;
				mappingResultsMigration.put(vdc.getVdcID(), vmMappingMigration.getMappingResults());
				linkMappingResultsMigration.put(vdc.getVdcID(), linkMappingMigartion.getResultsLinkMapping()); // linkMapping
				// Results
				listPathMapped.put(vdc.getVdcID(), linkMappingMigartion.getListPathMapped());
				linkPhyEdgeMigration.put(vdc.getVdcID(), linkMappingMigartion.getListBandwidthPhyEdge()); // link
				// Phy-Edge
				listPhyEdgeMapped.put(vdc.getVdcID(), linkMappingMigartion.getListPhyEdgeMapped());
				listMappingMig.add(vdc);
				// printLogResultMapping(topoMigration,
				// MainController.logMigration, vdc,
				// mappingResultsMigration.get(vdc.getVdcID()),
				// linkMappingMigartion.getListPathMapped());
				MainController.logMigration.info("TOPOLOGY AFTER");
				printLogTopo(topoMigration, MainController.logMigration);

			} else {
				topoMigration = removeVMMapping(vmMappingMigration.getMappingResults(), topoMigration);
				Map<Integer, LinkedList<PhysicalServer>> listPhyPre = vmMappingMigration.getListExceptPhy();
				listPhyPre.put(listPhyPre.size() + 1, vmMappingMigration.getListPhyMaped());
				boolean check = false;
				do {
					VMMapping vmMappingNext = new VMMapping();
					vmMappingNext.setListExceptPhy(listPhyPre);
					topoMigration = vmMappingNext.run(vdc, topoMigration, fatTreeMigartion);
					LinkMapping linkMappingNext = new LinkMapping();
					if (vmMappingNext.isSuccess()) {
						topoMigration = linkMappingNext.linkMappingOurAlgorithm(topoMigration, vdc,
								vmMappingNext.getMappingResults());
						if (linkMappingNext.isSuccess()) {
							mapSuccess = true;
							numSuccessMigration++;
							mappingResultsMigration.put(vdc.getVdcID(), vmMappingNext.getMappingResults());
							linkMappingResultsMigration.put(vdc.getVdcID(), linkMappingNext.getResultsLinkMapping()); // linkMapping
							// Results
							listPathMapped.put(vdc.getVdcID(), linkMappingNext.getListPathMapped());
							linkPhyEdgeMigration.put(vdc.getVdcID(), linkMappingNext.getListBandwidthPhyEdge()); // link
							// Phy-Edge
							listPhyEdgeMapped.put(vdc.getVdcID(), linkMappingNext.getListPhyEdgeMapped());
							listMappingMig.add(vdc);
							// printLogResultMapping(topoMigration,
							// MainController.logMigration, vdc,
							// mappingResultsMigration.get(vdc.getVdcID()),
							// linkMappingMigartion.getListPathMapped());
							MainController.logMigration.info("TOPOLOGY AFTER");
							printLogTopo(topoMigration, MainController.logMigration);
							break;
						} else
							check = true;
					}

					if (vmMappingNext.getMappingResults().isEmpty()) {
						break;
					}
					topoMigration = removeVMMapping(vmMappingNext.getMappingResults(), topoMigration);
					listPhyPre = vmMappingNext.getListExceptPhy();
					listPhyPre.put(listPhyPre.size() + 1, vmMappingNext.getListPhyMaped());
				} while (check);
			}

		}
		return mapSuccess;
	}

	// check expired for each VDC and update physical server
	public void checkExpriedVDC(VDCRequest vdc) {

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

		MainController.logHEAE.info("VDC LEAVE ID= " + vdc.getVdcID());
		// MainController.logHEAE.info("TOPOLOGY AFTER VDC EXPRIED");
		// printLogTopo(topo, MainController.logHEAE);
	}

	public void checkExpriedVDCJoinRemap(VDCRequest vdc) {

		if (mappingResultsJoinRemap.containsKey(vdc.getVdcID())) {

			idVDCIteratoredJoinRemap.add(vdc.getVdcID());
			Map<VirtualMachine, PhysicalServer> result = mappingResultsJoinRemap.get(vdc.getVdcID());
			for (Entry<VirtualMachine, PhysicalServer> entry : result.entrySet()) {
				VirtualMachine vir = entry.getKey();
				PhysicalServer phy = entry.getValue();
				phy.setCpu(phy.getCpu() + vir.getCPU());
				phy.setRam(phy.getRam() + vir.getMemory());
			}
			mappingResultsJoinRemap.remove(vdc.getVdcID());
		}

		// return link bandwidth
		if (linkMappingResultsJoinRemap.containsKey(vdc.getVdcID())) {
			LinkedList<SubstrateLink> listLinkBandwidth = topoJoinRemap.getLinkBandwidth();
			Map<LinkedList<SubstrateSwitch>, Double> resultLink = linkMappingResultsJoinRemap.get(vdc.getVdcID());
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
			// topoJoinRemap.setLinkBandwidth(listLinkBandwidth);
			// topoJoinRemap.setListSwitch(listSwitch);
			linkMappingResultsJoinRemap.remove(vdc.getVdcID());
		}

		// return link bandwidth phy->edge
		if (linkPhyEdgeJoinRemap.containsKey(vdc.getVdcID())) {
			LinkedList<SubstrateSwitch> listPhySwitch = topoJoinRemap.getListPhySwitch();
			Map<LinkPhyEdge, Double> listLinkPhyEdge = linkPhyEdgeJoinRemap.get(vdc.getVdcID());
			for (LinkPhyEdge link : listLinkPhyEdge.keySet()) {
				PhysicalServer phy = link.getPhysicalServer();
				SubstrateSwitch edge = link.getEdgeSwitch();
				double bandwidth = listLinkPhyEdge.get(link);

				edge.setPort(getSwitchFromID(listPhySwitch, phy.getName()), 0 - bandwidth);
				link.setBandwidth(link.getBandwidth() + bandwidth);
				linkPhyEdgeJoinRemap.remove(vdc.getVdcID());
			}
		}
	}

	public void checkExpriedVDCLeaveRemap(VDCRequest vdc) {

		if (mappingResultsLeaveRemap.containsKey(vdc.getVdcID())) {

			idVDCIteratoredLeaveRemap.add(vdc.getVdcID());
			Map<VirtualMachine, PhysicalServer> result = mappingResultsLeaveRemap.get(vdc.getVdcID());
			for (Entry<VirtualMachine, PhysicalServer> entry : result.entrySet()) {
				VirtualMachine vir = entry.getKey();
				PhysicalServer phy = entry.getValue();
				phy.setCpu(phy.getCpu() + vir.getCPU());
				phy.setRam(phy.getRam() + vir.getMemory());
			}
			mappingResultsLeaveRemap.remove(vdc.getVdcID());
		}

		// return link bandwidth
		if (linkMappingResultsLeaveRemap.containsKey(vdc.getVdcID())) {
			LinkedList<SubstrateLink> listLinkBandwidth = topoLeaveRemap.getLinkBandwidth();
			Map<LinkedList<SubstrateSwitch>, Double> resultLink = linkMappingResultsLeaveRemap.get(vdc.getVdcID());
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
			// topoLeaveRemap.setLinkBandwidth(listLinkBandwidth);
			// topoLeaveRemap.setListSwitch(listSwitch);
			linkMappingResultsLeaveRemap.remove(vdc.getVdcID());
		}

		if (linkPhyEdgeLeaveRemap.containsKey(vdc.getVdcID())) {
			LinkedList<SubstrateSwitch> listPhySwitch = topoLeaveRemap.getListPhySwitch();
			Map<LinkPhyEdge, Double> listLinkPhyEdge = linkPhyEdgeLeaveRemap.get(vdc.getVdcID());
			for (LinkPhyEdge link : listLinkPhyEdge.keySet()) {
				PhysicalServer phy = link.getPhysicalServer();
				SubstrateSwitch edge = link.getEdgeSwitch();
				double bandwidth = listLinkPhyEdge.get(link);

				edge.setPort(getSwitchFromID(listPhySwitch, phy.getName()), -bandwidth);
				link.setBandwidth(link.getBandwidth() + bandwidth);
			}
			linkPhyEdgeLeaveRemap.remove(vdc.getVdcID());
		}
	}

	public void checkExpriedVDCMixRemap(VDCRequest vdc) {

		if (mappingResultsMixRemap.containsKey(vdc.getVdcID())) {

			idVDCIteratoredMixRemap.add(vdc.getVdcID());
			Map<VirtualMachine, PhysicalServer> result = mappingResultsMixRemap.get(vdc.getVdcID());
			for (Entry<VirtualMachine, PhysicalServer> entry : result.entrySet()) {
				VirtualMachine vir = entry.getKey();
				PhysicalServer phy = entry.getValue();
				phy.setCpu(phy.getCpu() + vir.getCPU());
				phy.setRam(phy.getRam() + vir.getMemory());
			}
			mappingResultsMixRemap.remove(vdc.getVdcID());
		}

		// return link bandwidth
		if (linkMappingResultsMixRemap.containsKey(vdc.getVdcID())) {
			LinkedList<SubstrateLink> listLinkBandwidth = topoMixRemap.getLinkBandwidth();
			Map<LinkedList<SubstrateSwitch>, Double> resultLink = linkMappingResultsMixRemap.get(vdc.getVdcID());
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
			// topoMixRemap.setLinkBandwidth(listLinkBandwidth);
			// topoMixRemap.setListSwitch(listSwitch);
			linkMappingResultsMixRemap.remove(vdc.getVdcID());
		}

		// return link bandwidth phy->edge
		if (linkPhyEdgeMixRemap.containsKey(vdc.getVdcID())) {
			LinkedList<SubstrateSwitch> listPhySwitch = topoMixRemap.getListPhySwitch();
			Map<LinkPhyEdge, Double> listLinkPhyEdge = linkPhyEdgeMixRemap.get(vdc.getVdcID());
			for (LinkPhyEdge link : listLinkPhyEdge.keySet()) {
				PhysicalServer phy = link.getPhysicalServer();
				SubstrateSwitch edge = link.getEdgeSwitch();
				double bandwidth = listLinkPhyEdge.get(link);

				edge.setPort(getSwitchFromID(listPhySwitch, phy.getName()), -bandwidth);
				link.setBandwidth(link.getBandwidth() + bandwidth);
			}
			linkPhyEdgeMixRemap.remove(vdc.getVdcID());
		}
	}

	public void checkExpriedVDCGreenHead(VDCRequest vdc) {
		if (mappingResultsGreenHead.containsKey(vdc.getVdcID())) {

			idVDCIteratoredGreenHead.add(vdc.getVdcID());
			Map<VirtualMachine, PhysicalServer> result = mappingResultsGreenHead.get(vdc.getVdcID());
			for (Entry<VirtualMachine, PhysicalServer> entry : result.entrySet()) {
				VirtualMachine vir = entry.getKey();
				PhysicalServer phy = entry.getValue();
				phy.setCpu(phy.getCpu() + vir.getCPU());
				phy.setRam(phy.getRam() + vir.getMemory());
			}
			mappingResultsGreenHead.remove(vdc.getVdcID());
		}

		// return link bandwidth
		if (linkMappingResultsGreenHead.containsKey(vdc.getVdcID())) {
			LinkedList<SubstrateLink> listLinkBandwidth = topoGreenHead.getLinkBandwidth();
			Map<LinkedList<SubstrateSwitch>, Double> resultLink = linkMappingResultsGreenHead.get(vdc.getVdcID());
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
			// topoGreenHead.setLinkBandwidth(listLinkBandwidth);
			// topoGreenHead.setListSwitch(listSwitch);
			linkMappingResultsGreenHead.remove(vdc.getVdcID());
		}

		// return link bandwidth phy->edge
		if (linkPhyEdgeGreenHead.containsKey(vdc.getVdcID())) {
			LinkedList<SubstrateSwitch> listPhySwitch = topoGreenHead.getListPhySwitch();
			Map<LinkPhyEdge, Double> listLinkPhyEdge = linkPhyEdgeGreenHead.get(vdc.getVdcID());
			for (LinkPhyEdge link : listLinkPhyEdge.keySet()) {
				PhysicalServer phy = link.getPhysicalServer();
				SubstrateSwitch edge = link.getEdgeSwitch();
				double bandwidth = listLinkPhyEdge.get(link);

				edge.setPort(getSwitchFromID(listPhySwitch, phy.getName()), -bandwidth);
				link.setBandwidth(link.getBandwidth() + bandwidth);
			}
			linkPhyEdgeGreenHead.remove(vdc.getVdcID());
		}
		// MainController.logGreenHead.info("VDC LEAVE ID= "+vdc.getVdcID());
		// MainController.logGreenHead.info("TOPOLOGY AFTER VDC EXPRIED");
		// printLogTopo(topoGreenHead, MainController.logGreenHead);
	}

	public void checkExpriedVDCSecondNet(VDCRequest vdc) {
		if (mappingResultsSecondNet.containsKey(vdc.getVdcID())) {

			idVDCIteratoredSecondNet.add(vdc.getVdcID());
			Map<VirtualMachine, PhysicalServer> result = mappingResultsSecondNet.get(vdc.getVdcID());
			for (Entry<VirtualMachine, PhysicalServer> entry : result.entrySet()) {
				VirtualMachine vir = entry.getKey();
				PhysicalServer phy = entry.getValue();
				phy.setCpu(phy.getCpu() + vir.getCPU());
				phy.setRam(phy.getRam() + vir.getMemory());
			}
			mappingResultsSecondNet.remove(vdc.getVdcID());
		}

		// return link bandwidth
		if (linkMappingResultsSecondNet.containsKey(vdc.getVdcID())) {
			LinkedList<SubstrateLink> listLinkBandwidth = topoSecondNet.getLinkBandwidth();
			LinkedList<SubstrateSwitch> listSwitch = topoSecondNet.getListSwitch();
			Map<LinkedList<SubstrateSwitch>, Double> resultLink = linkMappingResultsSecondNet.get(vdc.getVdcID());
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
			// topoSecondNet.setLinkBandwidth(listLinkBandwidth);
			// topoSecondNet.setListSwitch(listSwitch);
			linkMappingResultsSecondNet.remove(vdc.getVdcID());
		}

		// return link bandwidth phy->edge
		if (linkPhyEdgeSecondNet.containsKey(vdc.getVdcID())) {
			LinkedList<SubstrateSwitch> listPhySwitch = topoSecondNet.getListPhySwitch();
			Map<LinkPhyEdge, Double> listLinkPhyEdge = linkPhyEdgeSecondNet.get(vdc.getVdcID());
			for (LinkPhyEdge link : listLinkPhyEdge.keySet()) {
				PhysicalServer phy = link.getPhysicalServer();
				SubstrateSwitch edge = link.getEdgeSwitch();
				double bandwidth = listLinkPhyEdge.get(link);

				edge.setPort(getSwitchFromID(listPhySwitch, phy.getName()), 0 - bandwidth);
				link.setBandwidth(link.getBandwidth() + bandwidth);
			}
			linkPhyEdgeSecondNet.remove(vdc.getVdcID());
		}
		MainController.logSecondNet.info("VDC LEAVE ID= " + vdc.getVdcID());
		// MainController.logSecondNet.info("TOPOLOGY AFTER VDC EXPRIED");
		// printLogTopo(topoSecondNet, MainController.logSecondNet);
	}

	// check expired for each VDC and update physical server
	public void checkExpriedVDCMigration(VDCRequest vdc) {

		if (mappingResultsMigration.containsKey(vdc.getVdcID())) {

			idVDCIteratoredMigration.add(vdc.getVdcID());
			Map<VirtualMachine, PhysicalServer> result = mappingResultsMigration.get(vdc.getVdcID());
			for (Entry<VirtualMachine, PhysicalServer> entry : result.entrySet()) {
				VirtualMachine vir = entry.getKey();
				PhysicalServer phy = entry.getValue();
				phy.setCpu(phy.getCpu() + vir.getCPU());
				phy.setRam(phy.getRam() + vir.getMemory());
			}
			mappingResultsMigration.remove(vdc.getVdcID());
		}

		// return link bandwidth
		if (linkMappingResultsMigration.containsKey(vdc.getVdcID())) {
			LinkedList<SubstrateLink> listLinkBandwidth = topoMigration.getLinkBandwidth();
			LinkedList<SubstrateSwitch> listSwitch = topoMigration.getListSwitch();
			Map<LinkedList<SubstrateSwitch>, Double> resultLink = linkMappingResultsMigration.get(vdc.getVdcID());
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
			// topoMigration.setLinkBandwidth(listLinkBandwidth);
			// topoMigration.setListSwitch(listSwitch);
			linkMappingResultsMigration.remove(vdc.getVdcID());
		}

		// return link bandwidth phy->edge
		if (linkPhyEdgeMigration.containsKey(vdc.getVdcID())) {
			LinkedList<SubstrateSwitch> listPhySwitch = topoMigration.getListPhySwitch();
			Map<LinkPhyEdge, Double> listLinkPhyEdge = linkPhyEdgeMigration.get(vdc.getVdcID());
			for (LinkPhyEdge link : listLinkPhyEdge.keySet()) {
				PhysicalServer phy = link.getPhysicalServer();
				SubstrateSwitch edge = link.getEdgeSwitch();
				double bandwidth = listLinkPhyEdge.get(link);
				edge.setPort(getSwitchFromID(listPhySwitch, phy.getName()), -bandwidth);
				link.setBandwidth(link.getBandwidth() + bandwidth);
			}
			linkPhyEdgeMigration.remove(vdc.getVdcID());
		}

		if (listPathMapped.containsKey(vdc.getVdcID()))
			listPathMapped.remove(vdc.getVdcID());
		if (listPhyEdgeMapped.containsKey(vdc.getVdcID()))
			listPhyEdgeMapped.remove(vdc.getVdcID());
		// migration();
		MainController.logMigration.info("VDC LEAVE ID= " + vdc.getVdcID());
		// MainController.logMigration.info("TOPOLOGY AFTER VDC EXPRIED");
		// printLogTopo(topoMigration, MainController.logMigration);
	}

	public void newMigration() {

		// Migration Server
		Map<Integer, PhysicalServer> listPhyServers = topoMigration.getListPhyServers();
		// sort listPhyServers by order CPU free decrease
		listPhyServers = sortMapByComparatorPlus(listPhyServers);
		LinkedList<PhysicalServer> listPhy = new LinkedList<>(); // stupid

		for (Entry<Integer, PhysicalServer> entry : listPhyServers.entrySet()) {
			if (entry.getValue().getCpu() > 0 && entry.getValue().getCpu() < 8)
				listPhy.add(entry.getValue());
		}

		int countVM, countVL;
		double temCPU, tempRAM;
		VirtualMachine vmCur, vmRes;
		PhysicalServer srcServer, dstServer;
		VDCRequest req;
		LinkedList<VirtualLink> listVirLink = new LinkedList<>();
		LinkedList<VirtualMachine> listVMinSrc, listVMinDst;

		VirtualLink vLink;
		LinkedList<SubstrateSwitch> oldPath, newPath;
		// for revert
		Map<VirtualMachine, PhysicalServer> listServerChange = new HashMap<>();
		LinkedList<PhysicalServer> listServerDst = new LinkedList<>();
		Map<VirtualLink, LinkedList<SubstrateSwitch>> listOldPathChange = new HashMap<>();
		Map<VirtualLink, LinkedList<SubstrateSwitch>> listNewPathChange = new HashMap<>();
		Map<VirtualLink, LinkedList<LinkPhyEdge>> listNewLinkPhyEdge = new HashMap<>();
		Map<VirtualLink, LinkedList<LinkPhyEdge>> listOldLinkPhyEdge = new HashMap<>();

		Map<VirtualLink, LinkedList<SubstrateSwitch>> listOldPathChangeIRLink = new HashMap<>();
		Map<VirtualLink, LinkedList<SubstrateSwitch>> listNewPathChangeIRLink = new HashMap<>();
		Map<VirtualLink, LinkedList<LinkPhyEdge>> listNewLinkPEIRLink = new HashMap<>();
		Map<VirtualLink, LinkedList<LinkPhyEdge>> listOldLinkPEIRLink = new HashMap<>();

		Map<VirtualLink, LinkedList<SubstrateSwitch>> mapPath; // Map contain
																// virtual link
																// : its path
																// substrate
		LinkedList<LinkPhyEdge> listOldPhyEdge; // list contain Phy-Switch Edge
												// of a virtual link

		LinkedList<SubstrateLink> listLinkBandwidth = topoMigration.getLinkBandwidth();
		LinkedList<SubstrateSwitch> listSwitch = topoMigration.getListSwitch();
		LinkedList<SubstrateSwitch> listPhySwitch = topoMigration.getListPhySwitch();
		for (int i = 0; i < listPhy.size(); i++) {
			srcServer = listPhy.get(i);
			if (listServerDst.contains(srcServer))
				continue;
			// get List Virtual machine are hosted by sServer
			listVMinSrc = getListVirtualMachine(srcServer, mappingResultsMigration);
			countVM = 0;

			listServerChange.clear();
			listOldPathChange.clear();
			listNewPathChange.clear();
			listNewLinkPhyEdge.clear();
			listOldLinkPhyEdge.clear();
			for (int j = 0; j < listVMinSrc.size(); j++) {
				vmCur = listVMinSrc.get(j);
				boolean isMigrationVM = false;
				for (int d = listPhy.size() - 1; d >= 0; d--) {
					if (d == i)
						continue;
					dstServer = listPhy.get(d);
					if (dstServer.getCpu() == 0 || dstServer.getCpu() == 8)
						continue;
					listVMinDst = getListVirtualMachine(dstServer, mappingResultsMigration);
					req = getVDCRequest(vmCur.getVdcID());

					boolean checkVMinDST = false;
					for (VirtualMachine v : listVMinDst)
						if (v.getVdcID() == vmCur.getVdcID()) {
							checkVMinDST = true;
							break;
						}

					if (checkVMinDST)
						continue;
					if (checkLink(srcServer, dstServer, topoMigration)) {
						countVL = 0;
						temCPU = dstServer.getCpu() - vmCur.getCPU();
						tempRAM = dstServer.getRam() - vmCur.getMemory();
						// tim listVirLink chua vmCur
						listVirLink = getVirLink(req.getListVirLink(), vmCur);

						listOldPathChangeIRLink.clear();
						listNewPathChangeIRLink.clear();
						listNewLinkPEIRLink.clear();
						listOldLinkPEIRLink.clear();

						for (int k = 0; k < listVirLink.size(); k++) {
							vLink = listVirLink.get(k);
							if (vmCur.equals(vLink.getsVM()))
								vmRes = vLink.getdVM();
							else
								vmRes = vLink.getsVM();

							oldPath = new LinkedList<>();
							mapPath = listPathMapped.get(vmRes.getVdcID());
							for (Entry<VirtualLink, LinkedList<SubstrateSwitch>> entry : mapPath.entrySet()) {
								if (entry.getKey().equals(vLink)) {
									oldPath = entry.getValue();
									break;
								}
							}

							listOldPhyEdge = listPhyEdgeMapped.get(vmRes.getVdcID()).get(vLink);

							newPath = new LinkedList<>();
							LinkedList<LinkPhyEdge> linkPhyEdge = new LinkedList<>();

							// update after//
							PhysicalServer srcOfVMResServer = mappingResultsMigration.get(vmRes.getVdcID()).get(vmRes);
							if (findPossiblePath(oldPath, listOldPhyEdge, newPath, linkPhyEdge, vLink, srcOfVMResServer,
									dstServer)) {
								countVL++;
								// update topo
								// update bw link Phy-Sw edge
								listNewLinkPEIRLink.put(vLink, new LinkedList<>(linkPhyEdge));
								listOldLinkPEIRLink.put(vLink, new LinkedList<>(listOldPhyEdge));
								Map<LinkPhyEdge, Double> tempList = linkPhyEdgeMigration.get(vmCur.getVdcID());

								for (LinkPhyEdge newLink : linkPhyEdge) {
									newLink.setBandwidth(newLink.getBandwidth() - vLink.getBandwidthRequest());
									newLink.getEdgeSwitch().setPort(
											getSwitchFromID(listPhySwitch, newLink.getPhysicalServer().getName()),
											vLink.getBandwidthRequest());
									if (tempList.containsKey(newLink))
										tempList.put(newLink, tempList.get(newLink) + vLink.getBandwidthRequest());
									else
										tempList.put(newLink, vLink.getBandwidthRequest());

								}
								for (LinkPhyEdge oldLink : listOldPhyEdge) {
									oldLink.setBandwidth(oldLink.getBandwidth() + vLink.getBandwidthRequest());
									oldLink.getEdgeSwitch().setPort(
											getSwitchFromID(listPhySwitch, oldLink.getPhysicalServer().getName()),
											-vLink.getBandwidthRequest());
									tempList.put(oldLink, tempList.get(oldLink) - vLink.getBandwidthRequest());
								}
								listPhyEdgeMapped.get(vmRes.getVdcID()).put(vLink, linkPhyEdge);

								// update bw link in old path and new path
								mapPath.put(vLink, newPath); // update
																// listPathMapped
								listNewPathChangeIRLink.put(vLink, new LinkedList<>(newPath));
								listOldPathChangeIRLink.put(vLink, new LinkedList<>(oldPath));

								int noUpdate = 2 * (newPath.size() + oldPath.size() - 2);
								for (SubstrateLink link : listLinkBandwidth) {
									for (int cur = 0; cur < newPath.size() - 1; cur++) {
										SubstrateSwitch sw1 = newPath.get(cur);
										SubstrateSwitch sw2 = newPath.get(cur + 1);
										if (checkSubLink(link, sw1, sw2)) {
											link.setBandwidth(link.getBandwidth() - vLink.getBandwidthRequest());
											noUpdate--;
											link.getStartSwitch().setPort(link.getEndSwitch(),
													vLink.getBandwidthRequest());
											// link.getEndSwitch().setPort(link.getStartSwitch(),
											// vLink.getBandwidthRequest());
										}
									}
									for (int cur = 0; cur < oldPath.size() - 1; cur++) {
										SubstrateSwitch sw1 = oldPath.get(cur);
										SubstrateSwitch sw2 = oldPath.get(cur + 1);
										if (checkSubLink(link, sw1, sw2)) {
											link.setBandwidth(link.getBandwidth() + vLink.getBandwidthRequest());
											noUpdate--;
											link.getStartSwitch().setPort(link.getEndSwitch(),
													-vLink.getBandwidthRequest());
											// link.getEndSwitch().setPort(link.getStartSwitch(),
											// -vLink.getBandwidthRequest());
										}
									}
									if (noUpdate == 0)
										break;
								}
								// update link mapping result
								Map<LinkedList<SubstrateSwitch>, Double> tempMap = linkMappingResultsMigration
										.get(vmCur.getVdcID());
								if (tempMap.containsKey(newPath)) {
									tempMap.put(newPath, tempMap.get(newPath) + vLink.getBandwidthRequest());
								} else {
									tempMap.put(newPath, vLink.getBandwidthRequest());
								}
								tempMap.put(oldPath, tempMap.get(oldPath) - vLink.getBandwidthRequest());

							} else {
								break;
							}
						}

						if (countVL == listVirLink.size()) {
							// update dstServer,
							dstServer.setCpu(temCPU);
							dstServer.setRam(tempRAM);
							listServerChange.put(vmCur, dstServer);
							mappingResultsMigration.get(vmCur.getVdcID()).put(vmCur, dstServer);
							listOldPathChange.putAll(listOldPathChangeIRLink);
							listNewPathChange.putAll(listNewPathChangeIRLink);
							listOldLinkPhyEdge.putAll(listOldLinkPEIRLink);
							listNewLinkPhyEdge.putAll(listNewLinkPEIRLink);
							countVM++;
							isMigrationVM = true;
							break;
						} else {
							// revert physical - edge link
							revertPhyEdge(listNewLinkPEIRLink, 1);
							revertPhyEdge(listOldLinkPEIRLink, -1);
							revertPath(listNewPathChangeIRLink, 1);
							revertPath(listOldPathChangeIRLink, -1);

							for (Entry<VirtualLink, LinkedList<SubstrateSwitch>> entry : listOldPathChangeIRLink
									.entrySet()) {
								LinkedList<SubstrateSwitch> path = entry.getValue();
								VirtualLink vlink = entry.getKey();
								listPathMapped.get(vlink.getsVM().getVdcID()).put(vlink, path);
							}
							revertMappingResult(listNewPathChangeIRLink, listOldPathChangeIRLink);
						}
					}
				}
				//
				if (!isMigrationVM)
					break;
			}
			if (countVM == listVMinSrc.size()) {
				srcServer.setCpu(srcServer.getCPU());
				srcServer.setRam(srcServer.getRAM());
				listServerDst.addAll(listServerChange.values());
				// StringBuilder str = new StringBuilder();
				// str.append("MIGRAION FROM " + srcServer.getName()).append("
				// :");
				// for(Entry<VirtualMachine, PhysicalServer> entry:
				// listServerChange.entrySet()) {
				// str.append(entry.getKey().getNameVM()).append("->").append(entry.getValue().getName()).append(";");
				// }
				MainController.logMigration.info("MIGRAION FROM");

			} else {
				// revert physical - edge link
				revertPhyEdge(listNewLinkPhyEdge, 1);
				revertPhyEdge(listOldLinkPhyEdge, -1);
				revertPath(listNewPathChange, 1);
				revertPath(listOldPathChange, -1);

				for (Entry<VirtualLink, LinkedList<SubstrateSwitch>> entry : listOldPathChange.entrySet()) {
					LinkedList<SubstrateSwitch> path = entry.getValue();
					VirtualLink vlink = entry.getKey();
					listPathMapped.get(vlink.getsVM().getVdcID()).put(vlink, path);
				}
				revertMappingResult(listNewPathChange, listOldPathChange);
				for (Entry<VirtualMachine, PhysicalServer> entry : listServerChange.entrySet()) {
					PhysicalServer pS = entry.getValue();
					VirtualMachine vm = entry.getKey();
					pS.setCpu(pS.getCpu() + vm.getCPU());
					pS.setRam(pS.getRam() + vm.getMemory());
					mappingResultsMigration.get(vm.getVdcID()).put(vm, srcServer);
				}
			}
		}
		// if(listServerDst.size()>0) {
		// MainController.logMigration.info("TOPOLOGY AFTER MIGRATION");
		// printLogTopo(topoMigration, MainController.logMigration);
		// }
	}

	public boolean checkSubLink(SubstrateLink link, SubstrateSwitch sw1, SubstrateSwitch sw2) {
		if (link.getStartSwitch().equals(sw1) && link.getEndSwitch().equals(sw2))
			return true;
		if (link.getStartSwitch().equals(sw2) && link.getEndSwitch().equals(sw1))
			return true;
		return false;
	}

	public void revertPhyEdge(Map<VirtualLink, LinkedList<LinkPhyEdge>> mapLink, double isNew) {
		// isNew = 1 : link change la link moi
		// isNew = -1: link change la link cu

		LinkedList<SubstrateSwitch> listPhySwitch = topoMigration.getListPhySwitch();
		for (Entry<VirtualLink, LinkedList<LinkPhyEdge>> entry : mapLink.entrySet()) {
			LinkedList<LinkPhyEdge> listLink = entry.getValue();
			VirtualLink vLink = entry.getKey();
			Map<LinkPhyEdge, Double> tempList = linkPhyEdgeMigration.get(vLink.getsVM().getVdcID());
			LinkedList<LinkPhyEdge> listPhyEdge = listPhyEdgeMapped.get(vLink.getsVM().getVdcID()).get(vLink);
			for (LinkPhyEdge linkChange : listLink) {
				linkChange.setBandwidth(linkChange.getBandwidth() + isNew * vLink.getBandwidthRequest());
				linkChange.getEdgeSwitch().setPort(
						getSwitchFromID(listPhySwitch, linkChange.getPhysicalServer().getName()),
						-isNew * vLink.getBandwidthRequest());
				tempList.put(linkChange, tempList.get(linkChange) - isNew * vLink.getBandwidthRequest());
				if (isNew == 1)
					listPhyEdge.remove(linkChange);
				else
					listPhyEdge.add(linkChange);
			}
		}
	}

	public void revertPath(Map<VirtualLink, LinkedList<SubstrateSwitch>> mapPath, double isNew) {
		// isNew = 1 : link change la link moi
		// isNew = -1: link change la link cu
		LinkedList<SubstrateLink> listLinkBandwidth = topoMigration.getLinkBandwidth();
		for (Entry<VirtualLink, LinkedList<SubstrateSwitch>> entry : mapPath.entrySet()) {
			LinkedList<SubstrateSwitch> path = entry.getValue();
			VirtualLink vLink = entry.getKey();
			int noUpdate = 2 * (path.size() - 1);
			for (SubstrateLink link : listLinkBandwidth) {
				for (int cur = 0; cur < path.size() - 1; cur++) {
					SubstrateSwitch sw1 = path.get(cur);
					SubstrateSwitch sw2 = path.get(cur + 1);
					if (checkSubLink(link, sw1, sw2)) {
						link.setBandwidth(link.getBandwidth() + isNew * vLink.getBandwidthRequest());
						noUpdate--;
						link.getStartSwitch().setPort(link.getEndSwitch(), -isNew * vLink.getBandwidthRequest());
						// link.getEndSwitch().setPort(link.getStartSwitch(),
						// -isNew*vLink.getBandwidthRequest());
					}
				}
				if (noUpdate == 0)
					break;
			}
		}
	}

	public void revertMappingResult(Map<VirtualLink, LinkedList<SubstrateSwitch>> listNewPathChange,
			Map<VirtualLink, LinkedList<SubstrateSwitch>> listOldPathChange) {

		for (Entry<VirtualLink, LinkedList<SubstrateSwitch>> entry : listNewPathChange.entrySet()) {
			Map<LinkedList<SubstrateSwitch>, Double> tempMap = linkMappingResultsMigration
					.get(entry.getKey().getsVM().getVdcID());
			if (tempMap.containsKey(entry.getValue())) {
				tempMap.put(entry.getValue(), tempMap.get(entry.getValue()) - entry.getKey().getBandwidthRequest());
			}
		}
		for (Entry<VirtualLink, LinkedList<SubstrateSwitch>> entry : listOldPathChange.entrySet()) {
			Map<LinkedList<SubstrateSwitch>, Double> tempMap = linkMappingResultsMigration
					.get(entry.getKey().getsVM().getVdcID());
			if (tempMap.containsKey(entry.getValue())) {
				tempMap.put(entry.getValue(), tempMap.get(entry.getValue()) + entry.getKey().getBandwidthRequest());
			}
		}
	}

	public boolean findPossiblePath(LinkedList<SubstrateSwitch> oldPath, LinkedList<LinkPhyEdge> oldPhyEdge,
			LinkedList<SubstrateSwitch> newPath, LinkedList<LinkPhyEdge> newPhyEdge, VirtualLink vLink,
			PhysicalServer sPhy, PhysicalServer dPhy) {
		boolean isExistPath = false;

		LinkedList<LinkPhyEdge> listPhyEdge = topoMigration.getListLinkPhyEdge();

		// get Edge Switch, which connects with Physical Server
		Map<PhysicalServer, SubstrateSwitch> listLinkServers = topoMigration.getListLinksServer();
		SubstrateSwitch edgeSwitch1 = listLinkServers.get(sPhy);
		SubstrateSwitch edgeSwitch2 = listLinkServers.get(dPhy);

		LinkPhyEdge phy2Edge1 = null, phy2Edge2 = null;
		int countP2E = 0;
		for (int i = 0; i < listPhyEdge.size(); i++) {
			LinkPhyEdge link = listPhyEdge.get(i);
			if (link.getEdgeSwitch().equals(edgeSwitch1) && link.getPhysicalServer().equals(sPhy)) {
				phy2Edge1 = link;
				countP2E++;
			}
			if (link.getEdgeSwitch().equals(edgeSwitch2) && link.getPhysicalServer().equals(dPhy)) {
				phy2Edge2 = link;
				countP2E++;
			}
			if (countP2E == 2)
				break;
		}

		// check bandwidth Phy-Edge, if not satisfied break -> fail
		if (phy2Edge1.getBandwidth() >= vLink.getBandwidthRequest()
				&& phy2Edge2.getBandwidth() >= vLink.getBandwidthRequest()) {

			// get list agg switch connect to start edge switch, sort by state,
			// bandwidth
			Map<SubstrateSwitch, LinkedList<SubstrateSwitch>> listAggConnectEdge = topoMigration
					.getListAggConnectEdge();
			Map<SubstrateSwitch, LinkedList<SubstrateSwitch>> listCoreConnectAgg = topoMigration
					.getListCoreConnectAgg();
			LinkedList<SubstrateSwitch> listAggConnectStartEdge = new LinkedList<>();
			LinkedList<SubstrateSwitch> listAggConnectEndEdge = new LinkedList<>();
			// near groups
			if (edgeSwitch1.equals(edgeSwitch2)) {
				newPhyEdge.add(phy2Edge1);
				newPhyEdge.add(phy2Edge2);
				newPath.add(edgeSwitch1);
				isExistPath = true;
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
						if (isContainLink(edgeSwitch1, sw, oldPath)
								|| sw.getBandwidthPort().get(edgeSwitch1) + vLink.getBandwidthRequest() <= 1000) {
							if (isContainLink(edgeSwitch2, sw, oldPath)
									|| sw.getBandwidthPort().get(edgeSwitch2) + vLink.getBandwidthRequest() <= 1000) {
								newPath.add(edgeSwitch1);
								newPath.add(sw);
								newPath.add(edgeSwitch2);
								newPhyEdge.add(phy2Edge1);
								newPhyEdge.add(phy2Edge2);
								isExistPath = true;
								break;
							}
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
							if (checkBandwidthPath(edgeSwitch1, startAggSwitch, coreSwitch, endAggSwitch, edgeSwitch2,
									oldPath, vLink.getBandwidthRequest())) {
								newPath.add(edgeSwitch1);
								newPath.add(startAggSwitch);
								newPath.add(coreSwitch);
								newPath.add(endAggSwitch);
								newPath.add(edgeSwitch2);
								newPhyEdge.add(phy2Edge1);
								newPhyEdge.add(phy2Edge2);
								check = true;
								isExistPath = true;
								break;
							}
						}
						if (check)
							break;
					}
				}
			}
		}

		return isExistPath;
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
		for (VDCRequest req : listVDCRequest)
			if (req.getVdcID() == id) {
				vdc = req;
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

	public double getPowerNetworkDevice(Topology topo) {
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

	public double getPowerServer(Topology topo) {
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

	public LinkedList<VDCRequest> sortVDCTimeEnd(LinkedList<VDCRequest> listVDCEnd) {
		 Sort PhyM in decreasing order by ID, CPU, need to discuss 
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
		 Sort PhyM in decreasing order by ID, CPU, need to discuss 
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

	public double getPowerOur() {
		return powerOur;
	}

	public double getPowerGreenHead() {
		return powerGreenHead;
	}

	public double getPowerSecondNet() {
		return powerSecondNet;
	}

	public double getPowerMigration() {
		return powerMigration;
	}

	public double getPowerJoinRemap() {
		return powerJoinRemap;
	}

	public double getPowerLeaveRemap() {
		return powerLeaveRemap;
	}

	public double getPowerMixRemap() {
		return powerMixRemap;
	}

	public double getPowerOurNew() {
		return powerOurNew;
	}

	public double getPowerGreenHeadNew() {
		return powerGreenHeadNew;
	}

	public double getPowerSecondNetNew() {
		return powerSecondNetNew;
	}

	public double getPowerMigrationNew() {
		return powerMigrationNew;
	}

	public double getPowerJoinRemapNew() {
		return powerJoinRemapNew;
	}

	public double getPowerLeaveRemapNew() {
		return powerLeaveRemapNew;
	}

	public double getPowerMixRemapNew() {
		return powerMixRemapNew;
	}

	public double getRatioSuccess() {
		return (numSuccess * 1.0) / listVDCRequest.size();
	}

	public double getRatioSuccessGreenHead() {
		return (numSuccessGreenHead * 1.0) / listVDCRequest.size();
	}

	public double getRatioSuccessSecondNet() {
		return (numSuccessSecondNet * 1.0) / listVDCRequest.size();
	}

	public double getRatioSuccessMigration() {
		return (numSuccessMigration * 1.0) / listVDCRequest.size();
	}

	public double getRatioSuccessJoinRemap() {
		return (numSuccessJoinRemap * 1.0) / listVDCRequest.size();
	}

	public double getRatioSuccessLeaveRemap() {
		return (numSuccessLeaveRemap * 1.0) / listVDCRequest.size();
	}

	public double getRatioSuccessMixRemap() {
		return (numSuccessMixRemap * 1.0) / listVDCRequest.size();
	}

	public double getNumSuccess() {
		return numSuccess;
	}

	public double getNumSuccessGreenHead() {
		return numSuccessGreenHead;
	}

	public double getNumSuccessSecondNet() {
		return numSuccessSecondNet;
	}

	public double getNumSuccessMigration() {
		return numSuccessMigration;
	}

	public double getNumSuccessJoinRemap() {
		return numSuccessJoinRemap;
	}

	public double getNumSuccessLeaveRemap() {
		return numSuccessLeaveRemap;
	}

	public double getNumSuccessMixRemap() {
		return numSuccessMixRemap;
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
*/