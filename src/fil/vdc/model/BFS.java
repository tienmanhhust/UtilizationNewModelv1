package fil.vdc.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class helps to find all paths exit between source node and destination
 * node
 * 
 * @author Van Huynh Nguyen
 * 
 * 
 */
public class BFS {

	private SubstrateSwitch startNode;
	private SubstrateSwitch endNode;
	private Map<Integer,LinkedList<SubstrateSwitch>> mypath;
	private int i=1;
	/**
	 * The initial class
	 */
	
	public BFS(SubstrateSwitch start, SubstrateSwitch end) {
		startNode = start;
		endNode = end;
		mypath = new HashMap<>();
	}

	/**
	 * Initial some parameters and run main function
	 * 
	 * @param topo
	 *            This is the topology object, which contains all informations
	 *            of Substrate Network
	 */
	public void run(Topology topo) {
	
//		mypath.clear();
		LinkedList<SubstrateSwitch> visited = new LinkedList<>();
		visited.add(startNode);
		breadthFirst(topo, visited);
	}

	/**
	 * This is the main function, which is used to find all paths exit between
	 * startNode and endNode
	 * 
	 * @param topo
	 *            This is the topology object, which contains all informations
	 *            of Substrate Network
	 * @param visited
	 *            This is the list of nodes, which are visited by algorithm
	 */
	public void breadthFirst(Topology topo, LinkedList<SubstrateSwitch> visited) {
		LinkedList<SubstrateSwitch> nodes = topo.adjacentNodes(visited.getLast());
		for (SubstrateSwitch node : nodes) {

			if (visited.contains(node)) {
				continue;
			}
			// if found the end node, update path to mypath list
			if (node.equals(endNode)) {
				visited.add(node);
				printPath(visited);
				visited.removeLast();
				break;
			}
		}

		for (SubstrateSwitch node : nodes) {

			if (visited.contains(node) || node.equals(endNode)) {
				continue;
			}
			visited.addLast(node);
			breadthFirst(topo, visited);
			visited.removeLast();
		}

	}

	
	public LinkedList<SubstrateSwitch> getShortestPath()
	{
		int numMin = Integer.MAX_VALUE;
		LinkedList<SubstrateSwitch> tempPath=  new LinkedList<>();
		for(Entry<Integer,LinkedList<SubstrateSwitch>> entry: mypath.entrySet())
		{
			if(entry.getValue().size()<numMin)
			{
				numMin = entry.getValue().size();
				tempPath = entry.getValue();
			}

		}
		//??????????
//		System.out.println("Min size "+numMin);

		return tempPath;
	}
	public void printPath(LinkedList<SubstrateSwitch> visited) {

		// for Substrateswitch khong duoc??? why
		LinkedList<SubstrateSwitch> list = new LinkedList<>();
		for(int i=0; i< visited.size(); i++)
		{
			list.add(visited.get(i));
//			System.out.print(visited.get(i).getNameSubstrateSwitch()+" ");
		}
//		System.out.println();
		mypath.put(i, list);
//		System.out.println("Times "+i);
		i++;
	}

	
	public Map<Integer,LinkedList<SubstrateSwitch>> getMypath() {
		return mypath;
	}
	
}