package fil.vdc.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SecondNetMatching {
	private Map<VirtualMachine,PhysicalServer> map;
	private boolean isSuccess;
	public SecondNetMatching() {
		map = new HashMap<VirtualMachine,PhysicalServer>();
		isSuccess = true;
	}
	
	
	
	

	
	
	/**
	 * this method is used to matching VM with PM use SecondNet Mapping
	 * @param listVM
	 * @param listPM
	 * @return Map<VirtualMachine,PhysicalServer>
	 */
	public void MatchingSN(LinkedList<VirtualMachine> listVM,LinkedList<PhysicalServer> listPM) {
		LinkedList<SNNode> listnodeVM= new LinkedList<SNNode>();
		LinkedList<SNNode> listnodePM= new LinkedList<SNNode>();
		LinkedList<SNEdge> listedge = new LinkedList<SNEdge>();
		LinkedList<SNEdge> flow = new LinkedList<SNEdge>();
		
		SNNode s= new SNNode();
		s.setisVM(false);
		PhysicalServer PMs= new PhysicalServer("source");
		s.setPMNode(PMs);
		
		SNNode t = new SNNode();
		t.setisVM(false);
		PhysicalServer PMt= new PhysicalServer("destination");
		t.setPMNode(PMt);
		
		
		for(int i=0;i<listVM.size();i++) {
			SNNode node=new SNNode();
			node.setVMNode(listVM.get(i));
			node.setisVM(true);
			listnodeVM.add(node);
		}
		for(int i=0;i<listPM.size();i++) {
			SNNode node=new SNNode();
			node.setPMNode(listPM.get(i));
			node.setisVM(false);
			listnodePM.add(node);
		}
		
		
		// create edge between source and VM Node
		for(int i=0;i<listnodeVM.size();i++) {
			SNEdge edge = new SNEdge(s,listnodeVM.get(i),0);
			listedge.add(edge);
		}
		
		// create edge between destination and PM Node
		for(int i=0;i<listnodePM.size();i++) {
			SNEdge edge = new SNEdge(listnodePM.get(i),t,0);
			listedge.add(edge);
		}
		
		//create edge between VMNode and PMNode
		for(int i=0;i<listnodeVM.size();i++) {
			for(int j=0;j<listnodePM.size();j++) {
				if(listnodeVM.get(i).getVMNode().getCPU() <= listnodePM.get(j).getPMNode().getCpu()
						&& 
						listnodeVM.get(i).getVMNode().getMemory() <= listnodePM.get(j).getPMNode().getRam()) {
					SNEdge edge= new SNEdge(listnodeVM.get(i),listnodePM.get(j),listnodePM.get(j).getPMNode().getCost()); // get bandwidth
					listedge.add(edge);
				}
			}
		}
		
		flow = createFlow(listedge,listnodeVM,listnodePM,s,t);

		updateListEdge(flow,listedge);

		LinkedList<SNEdge> negativecycle = new LinkedList<SNEdge>();
		negativecycle = findNegativeCycle(listedge,listnodeVM,listnodePM,s,t);

		while(negativecycle != null) {
			for(int i=0;i<negativecycle.size();i++) {
				SNEdge edge = negativecycle.get(i);
				if(edge.getWeight()<0) {
					SNEdge edge1= new SNEdge(edge.getEndNode(),edge.getStartNode(),-edge.getWeight());
					listedge.remove(edge);
					listedge.add(edge1);
				}
				else {
					flow.add(edge);
				}
			}
			updateListEdge(flow,listedge);
			negativecycle = findNegativeCycle(listedge,listnodeVM,listnodePM,s,t);
		}
		for(int i=0;i<flow.size();i++) {
			SNEdge edge=flow.get(i);
			if(edge.getStartNode().isVM== true && edge.getEndNode().isVM==false) {
				map.put(edge.getStartNode().getVMNode(),edge.getEndNode().getPMNode());
			}
		}


	}
	
	
	
	
	
	
	
	/**
	 * this method used to create a flow available in graph
	 * @param listedge
	 * @param listnodeVM
	 * @param listnodePM
	 * @param s the source node 
	 * @param t the destination node
	 * @return
	 */
	public LinkedList<SNEdge> createFlow(LinkedList<SNEdge> listedge,LinkedList<SNNode> listnodeVM, LinkedList<SNNode> listnodePM,SNNode s,SNNode t) {
		 LinkedList<SNEdge> flow = new LinkedList<SNEdge>();
		 Map <SNNode,LinkedList<SNNode>> neighbor = new HashMap<SNNode,LinkedList<SNNode>>();
		 double max_flow=0;
		 Map <SNNode,SNNode> parentnode = new HashMap<SNNode,SNNode>();
		 int require=listnodeVM.size();
		 
		 // create a map that store the node and its neighbor
		 for(int i=0;i<listedge.size();i++) {
			 SNNode start= listedge.get(i).getStartNode();
			 SNNode end = listedge.get(i).getEndNode();
			 if(neighbor.get(start)==null) {
				 LinkedList<SNNode> listneighbor= new LinkedList<SNNode>();
				 listneighbor.add(end);
				 neighbor.put(start, listneighbor);
			 }
			 else neighbor.get(start).add(end);
		 }
		 

		 
		 
		 
		 while(BFSFlow(listedge,neighbor,parentnode,s,t)) {
			 SNNode y = t;

			 max_flow++;
			 
			 for(y=t;parentnode.get(y)!=null;y=parentnode.get(y)) {
				 SNEdge edge1 = findEdge(listedge,parentnode.get(y),y);
				 

				 flow.add(edge1);
				 edge1.setCapacity(0);
			 }
		 }
		 
		 if(max_flow <require) {
			 isSuccess = false;
			 flow = new LinkedList<SNEdge>();
			 return flow;
		 }
		 else return flow;
	 }
	
	
	
	
	
	/**
	 * this method is used to find the augement path
	 * @param listedge
	 * @param neighbor
	 * @param parentnode
	 * @param s
	 * @param t
	 * @return
	 */
	 public boolean BFSFlow(LinkedList<SNEdge> listedge,Map <SNNode,LinkedList<SNNode>> neighbor, Map <SNNode,SNNode> parentnode,SNNode s,SNNode t) {
		 LinkedList<SNNode> queue = new LinkedList<SNNode>();
		 LinkedList<SNNode> visited = new LinkedList<SNNode>();
		 
		 queue.add(s);
		 visited.add(s);
		 while(!queue.isEmpty()) {
			 SNNode u = queue.poll();
			 visited.add(u);
			 
			 LinkedList<SNNode> listnbr = new  LinkedList<SNNode>();
					listnbr= neighbor.get(u);
			
			 if(listnbr!=null) {
			
				 for(int i=0;i<listnbr.size();i++) {
					 SNNode node = listnbr.get(i);
				 
					 SNEdge edge= findEdge(listedge,u,node);
				
					 if(!visited.contains(node) && edge.getCapacity()>0) {
					 
						 queue.add(node);
						 visited.add(node);
						 parentnode.put(node, u);
					 }
			 }

			 }
 		 }

		 if(visited.contains(t)) return true;
		 else return false;
	 }
	 
	 
	 /**
	  * this method is use to find the edge in the listedge which have the start and end node
	  * @param list
	  * @param start
	  * @param end
	  * @return
	  */
	 public SNEdge findEdge(LinkedList<SNEdge> list,SNNode start, SNNode end ) {

		 int u=0;
		 for(int i =0;i<list.size();i++) {
			 if(list.get(i).getStartNode()== start && list.get(i).getEndNode()==end) {

				 u=i;
			 }
		 }
		 return list.get(u);
		 //return edge;
	 }
	 
	 /**
	  * this method is used to update listedge, it will collect all edge in flow,
	  *  set the capacity = 0 and add the negative edge of edge in flow
	  * @param flow
	  * @param listedge
	  */
	 public void updateListEdge(LinkedList<SNEdge> flow,LinkedList<SNEdge> listedge) {
		 
		 for(int i=0;i<flow.size();i++) {
			 SNEdge edge = flow.get(i);

			 listedge.remove(edge);
			 SNEdge edge1= new SNEdge(edge.getEndNode(),edge.getStartNode(),-edge.getWeight());
			 listedge.add(edge1);
		 }
	 }
	 
	 /**
	  * this method is used to find the negative cycle in graph
	  * @param listedge
	  * @param listnodeVM
	  * @param listnodePM
	  * @param flow
	  * @param s
	  * @param t
	  * @return LinkedList<SNEdge> save all edge in the negative cycle
	  */
	 public LinkedList<SNEdge> findNegativeCycle(LinkedList<SNEdge> listedge,LinkedList<SNNode> listnodeVM,LinkedList<SNNode> listnodePM,
			SNNode s,SNNode t ) {
		 
		 LinkedList<SNEdge> negativecycle = new LinkedList<SNEdge>();
		 LinkedList<SNNode> negativenode = new LinkedList<SNNode>();
		 Map<SNNode,Double> distance = new HashMap<SNNode,Double>();
		 Map<SNNode,SNNode> parentmap = new HashMap<SNNode,SNNode>();
		 int nodenumber= listnodeVM.size()+listnodePM.size()+2;
		 

		 distance.put(t, 0.0);
		 distance.put(s, Double.MAX_VALUE);
		 
		 for(int i=0;i<listnodeVM.size();i++) {
			 distance.put(listnodeVM.get(i), Double.MAX_VALUE);
		 }
		 
		 for(int i=0;i<listnodePM.size();i++) {
			 distance.put(listnodePM.get(i), Double.MAX_VALUE);
		 }
		 
		 for(int i=0;i<nodenumber;i++) {
			 for(int j=0;j<listedge.size();j++) {
				 SNEdge edge = listedge.get(j);
				 SNNode start = edge.getStartNode();
				 SNNode end = edge.getEndNode();
				 double dend = distance.get(end);
				 if(distance.get(start) +  edge.getWeight() < dend && distance.get(start) != Double.MAX_VALUE) {

					 dend = distance.get(start)+edge.getWeight();

					 distance.remove(end);
					 distance.put(end, dend);
					 
					 if(parentmap.get(end)==null) {
						 parentmap.put(end, start);
					 }
					 else {
						 parentmap.remove(end);
						 parentmap.put(end, start);
					 }
				 }
			 }
		 }

		 int x=0;
		 int y=0;
		 for(int j=0;j<listedge.size();j++) {
			 
			 SNEdge edge = listedge.get(j);
			 SNNode start = edge.getStartNode();
			 SNNode end = edge.getEndNode();
			 double dend = distance.get(end);

			 if(distance.get(start) +  edge.getWeight() < dend && distance.get(start) != Double.MAX_VALUE) {

				 LinkedList<SNNode> mapnodedemo = new LinkedList<SNNode>();
				 int count =0;
				 do {
					 count++;
					 end = parentmap.get(end);
					 mapnodedemo.add(end);
				 } while( parentmap.get(end)!= start && count <5);

				 if(count<5) {
					 negativenode= mapnodedemo;
					 break;
				 }
			 }
		 }
		 

		 if(negativenode.isEmpty()) return null;
		 
		 for(int i=1;i<negativenode.size();i++) {
			 negativecycle.add(findEdge(listedge,negativenode.get(i),negativenode.get(i-1)));
		 }
		 negativecycle.add(findEdge(listedge,negativenode.get(0),negativenode.get(negativenode.size()-1)));
		 
//		 System.out.println(negativecycle.size());
//		 printListEdge(negativecycle);
		 return negativecycle;
	 }
	 
//	 public void printListEdge(LinkedList<SNEdge> listedge) {
//		 for(int i=0;i<listedge.size();i++) {
//			 SNEdge edge = listedge.get(i);
//			 SNNode start = edge.getStartNode();
//			 SNNode end = edge.getEndNode();
//			 double weight = edge.getWeight();
//			 double capacity = edge.getCapacity();
//			 System.out.print("edge: " + weight +" " + capacity);
//			 printNode(start);
//			 printNode(end);
//			 System.out.println("\n");
//		 }
//	 }
	 
//	 public void printNode(SNNode node) {
//		 if(node.isVM==true) {
//			 System.out.print(" VM:"+node.getVMNode().getNameVM());
//		 }
//		 else {
//			 System.out.print(" PM:"+ node.getPMNode().getName());
//		 }
//	 }







	public boolean isSuccess() {
		return isSuccess;
	}


	public Map<VirtualMachine, PhysicalServer> getResult() {
		return map;
	}
}
