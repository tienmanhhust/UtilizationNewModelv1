package fil.vdc.model;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * @author huynhhust
 *
 */
public class WaxmanGenerator {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public LinkedList<VirtualLink> Waxman(int N, Double alpha, Double beta, Map<Integer, VirtualMachine> listVM) {
		LinkedList<VirtualLink> listVirLink = new LinkedList<>();
		LinkedList<String> link = new LinkedList();
		Map<Integer, Integer> idOfNode = new HashMap<Integer, Integer>();
		Double xmax = 100.0;
		Double xmin = 0.0;
		Double ymax = 100.0;
		Double ymin = 0.0;
		Random r = new Random();
		Point2D[] nodeXYPositionTable = new Point2D.Double[N + 1];
		for (int nodeId = 1; nodeId < N + 1; nodeId++) {
			idOfNode.put(nodeId, nodeId);
			nodeXYPositionTable[nodeId] = new Point2D.Double(xmin + (xmax - xmin) * r.nextDouble(),
					ymin + (ymax - ymin) * r.nextDouble());
		}
		double dist_max = -Double.MAX_VALUE;
		for (int originNodeId = 1; originNodeId < N + 1; originNodeId++) {
			for (int destinationNodeId = originNodeId + 1; destinationNodeId < N + 1; destinationNodeId++) {
				double dist = nodeXYPositionTable[originNodeId].distance(nodeXYPositionTable[destinationNodeId]);

				if (dist > dist_max)
					dist_max = dist;
			}
		}
		for (int originNodeId = 1; originNodeId < N; originNodeId++)
			for (int destinationNodeId = originNodeId + 1; destinationNodeId < N + 1; destinationNodeId++) {
				if (originNodeId == destinationNodeId) {
					continue;
				}
				double dist = nodeXYPositionTable[originNodeId].distance(nodeXYPositionTable[destinationNodeId]);
				double p = alpha * Math.exp(-dist / (beta * dist_max));
				// r.nextDouble(dist);
				if ((link.contains(String.valueOf(originNodeId) + " " + String.valueOf(destinationNodeId)))
						|| link.contains(String.valueOf(destinationNodeId) + " " + String.valueOf(originNodeId)))
					continue;
				if (r.nextDouble() < p) {
					Random rand = new Random();
					int bandwidth = rand.nextInt(90) + 10;
					// int bandwidth = 50;
					VirtualLink vir = new VirtualLink(listVM.get(originNodeId), listVM.get(destinationNodeId),
							bandwidth);
					listVirLink.add(vir);
					link.add(String.valueOf(originNodeId) + " " + String.valueOf(destinationNodeId));

					link.add(String.valueOf(destinationNodeId) + " " + String.valueOf(originNodeId));
					Integer idOfSourceNode = idOfNode.get(originNodeId);
					Integer idOfDestNode = idOfNode.get(destinationNodeId);

					for (int nodeId = 1; nodeId < N + 1; nodeId++) {
						Integer id = idOfNode.get(nodeId);
						if (id == idOfDestNode) {
							idOfNode.remove(nodeId);
							idOfNode.put(nodeId, idOfSourceNode);
						}

					}
				}

			}
		LinkedList<Integer> idOfNet = new LinkedList<Integer>();
		Integer id = idOfNode.get(1);
		idOfNet.add(id);
		for (int nodeId = 2; nodeId < N + 1; nodeId++) {
			if (id != idOfNode.get(nodeId)) {
				id = idOfNode.get(nodeId);
				idOfNet.add(id);
			}

		}
		if (idOfNet.size() > 1) {
			for (int i = 0; i < idOfNet.size() - 1; i++) {
				Integer n = idOfNet.get(i);
				Integer m = idOfNet.get(i + 1);
				Integer source = 0;
				Integer dest = 0;
				Iterator<Entry<Integer, Integer>> iter = idOfNode.entrySet().iterator();
				while (iter.hasNext()) {
					Entry<Integer, Integer> entry = iter.next();
					if (entry.getValue() == n)
						source = entry.getKey();
					if (entry.getValue() == m)
						dest = entry.getKey();
				}
				if (source != 0 && dest != 0) {
					Random rand = new Random();
					int bandwidth = rand.nextInt(100) + 10;
					// int bandwidth = 50;
					VirtualLink vir = new VirtualLink(listVM.get(source), listVM.get(dest), bandwidth);
					listVirLink.add(vir);

				}
			}
		}
		return listVirLink;
	}

	public LinkedList<SubstrateLink> WaxmanSub(int N, Double alpha, Double beta,
			Map<Integer, SubstrateSwitch> listSub) {
		LinkedList<SubstrateLink> listSubLink = new LinkedList<>();
		LinkedList<String> link = new LinkedList();
		Map<Integer, Integer> idOfNode = new HashMap<Integer, Integer>();
		Double xmax = 100.0;
		Double xmin = 0.0;
		Double ymax = 100.0;
		Double ymin = 0.0;
		Random r = new Random();
		Point2D[] nodeXYPositionTable = new Point2D.Double[N + 1];
		for (int nodeId = 1; nodeId < N + 1; nodeId++) {
			idOfNode.put(nodeId, nodeId);
			nodeXYPositionTable[nodeId] = new Point2D.Double(xmin + (xmax - xmin) * r.nextDouble(),
					ymin + (ymax - ymin) * r.nextDouble());
		}
		double dist_max = -Double.MAX_VALUE;
		for (int originNodeId = 1; originNodeId < N + 1; originNodeId++) {
			for (int destinationNodeId = originNodeId + 1; destinationNodeId < N + 1; destinationNodeId++) {
				double dist = nodeXYPositionTable[originNodeId].distance(nodeXYPositionTable[destinationNodeId]);

				if (dist > dist_max)
					dist_max = dist;
			}
		}
		for (int originNodeId = 1; originNodeId < N; originNodeId++)
			for (int destinationNodeId = originNodeId + 1; destinationNodeId < N + 1; destinationNodeId++) {
				if (originNodeId == destinationNodeId) {
					continue;
				}
				double dist = nodeXYPositionTable[originNodeId].distance(nodeXYPositionTable[destinationNodeId]);
				double p = alpha * Math.exp(-dist / (beta * dist_max));
				// r.nextDouble(dist);
				if ((link.contains(String.valueOf(originNodeId) + " " + String.valueOf(destinationNodeId)))
						|| link.contains(String.valueOf(destinationNodeId) + " " + String.valueOf(originNodeId)))
					continue;
				if (r.nextDouble() < p) {
					Random rand = new Random();
					int bandwidth = rand.nextInt(90) + 10;
					// int bandwidth = 50;
					// VirtualLink vir = new
					// VirtualLink(listVM.get(originNodeId),
					// listVM.get(destinationNodeId), bandwidth);
			
					SubstrateLink subLink = new SubstrateLink(listSub.get(originNodeId), listSub.get(destinationNodeId),
							1000);

					listSubLink.add(subLink);
					link.add(String.valueOf(originNodeId) + " " + String.valueOf(destinationNodeId));

					link.add(String.valueOf(destinationNodeId) + " " + String.valueOf(originNodeId));
					Integer idOfSourceNode = idOfNode.get(originNodeId);
					Integer idOfDestNode = idOfNode.get(destinationNodeId);

					for (int nodeId = 1; nodeId < N + 1; nodeId++) {
						Integer id = idOfNode.get(nodeId);
						if (id == idOfDestNode) {
							idOfNode.remove(nodeId);
							idOfNode.put(nodeId, idOfSourceNode);
						}

					}
				}

			}
		LinkedList<Integer> idOfNet = new LinkedList<Integer>();
		Integer id = idOfNode.get(1);
		idOfNet.add(id);
		for (int nodeId = 2; nodeId < N + 1; nodeId++) {
			if (id != idOfNode.get(nodeId)) {
				id = idOfNode.get(nodeId);
				idOfNet.add(id);
			}

		}
		if (idOfNet.size() > 1) {
			for (int i = 0; i < idOfNet.size() - 1; i++) {
				Integer n = idOfNet.get(i);
				Integer m = idOfNet.get(i + 1);
				Integer source = 0;
				Integer dest = 0;
				Iterator<Entry<Integer, Integer>> iter = idOfNode.entrySet().iterator();
				while (iter.hasNext()) {
					Entry<Integer, Integer> entry = iter.next();
					if (entry.getValue() == n)
						source = entry.getKey();
					if (entry.getValue() == m)
						dest = entry.getKey();
				}
				if (source != 0 && dest != 0) {
					Random rand = new Random();
					int bandwidth = rand.nextInt(90) + 10;
					// int bandwidth = 50;

					SubstrateLink subLink = new SubstrateLink(listSub.get(source), listSub.get(dest), 1000);
					listSubLink.add(subLink);

				}
			}
		}
		return listSubLink;
	}

}